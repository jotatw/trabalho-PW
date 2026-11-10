package com.br.ifg.luziania.trabalhopw.service;

import com.br.ifg.luziania.trabalhopw.dto.*;
import com.br.ifg.luziania.trabalhopw.model.*;
import com.br.ifg.luziania.trabalhopw.repository.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

@ApplicationScoped
public class LocacaoService {
    @Inject LocacaoRepository locacoes;
    @Inject ReservaRepository reservas;
    @Inject VeiculoRepository veiculos;
    @Inject EntityManager entityManager;

    @Transactional
    public List<LocacaoDTO> listar() {
        return locacoes.listAll().stream().map(this::dto).toList();
    }

    @Transactional
    public LocacaoDTO iniciar(IniciarLocacaoDTO dados) {
        Reserva reserva = entityManager.find(Reserva.class, dados.getReservaId(), LockModeType.PESSIMISTIC_WRITE);
        if (reserva == null) throw new NotFoundException("Reserva não encontrada.");
        if (!"CONFIRMADA".equals(reserva.getStatus()))
            throw new IllegalArgumentException("A locação exige uma reserva confirmada.");
        OffsetDateTime agora = OffsetDateTime.now();
        if (agora.isBefore(reserva.getDataInicio()) || !agora.isBefore(reserva.getDataFim()))
            throw new IllegalArgumentException("A retirada está fora do período da reserva.");
        if (locacoes.count("reserva.id", reserva.getId()) > 0)
            throw new IllegalArgumentException("Esta reserva já possui uma locação.");
        if (!Boolean.TRUE.equals(reserva.getCliente().getAtivo()))
            throw new IllegalArgumentException("O cliente desta reserva está inativo.");

        Veiculo veiculo = entityManager.find(Veiculo.class, dados.getVeiculoId(), LockModeType.PESSIMISTIC_WRITE);
        if (veiculo == null) throw new NotFoundException("Veículo não encontrado.");
        if (!Boolean.TRUE.equals(veiculo.getAtivo()) || !"DISPONIVEL".equals(veiculo.getStatus()))
            throw new IllegalArgumentException("O veículo não está disponível.");
        if (!veiculo.getCategoria().getId().equals(reserva.getCategoria().getId()))
            throw new IllegalArgumentException("O veículo não pertence à categoria reservada.");
        long conflito = locacoes.count("veiculo.id = ?1 and status = 'ATIVA' and inicio < ?3 and previsaoDevolucao > ?2",
                veiculo.getId(), agora, reserva.getDataFim());
        if (conflito > 0) throw new IllegalArgumentException("O veículo já está ocupado nesse período.");

        Locacao locacao = new Locacao();
        locacao.setReserva(reserva);
        locacao.setCliente(reserva.getCliente());
        locacao.setVeiculo(veiculo);
        locacao.setInicio(agora);
        locacao.setPrevisaoDevolucao(reserva.getDataFim());
        locacao.setValorDiaria(reserva.getCategoria().getValorDiaria());
        locacao.setStatus("ATIVA");
        locacoes.persist(locacao);
        veiculo.setStatus("ALUGADO");
        return dto(locacao);
    }

    @Transactional
    public LocacaoDTO devolver(Long id, DevolverLocacaoDTO dados) {
        Locacao locacao = entityManager.find(Locacao.class, id, LockModeType.PESSIMISTIC_WRITE);
        if (locacao == null) throw new NotFoundException("Locação não encontrada.");
        if (!"ATIVA".equals(locacao.getStatus()))
            throw new IllegalArgumentException("A locação já foi encerrada.");
        Veiculo veiculo = entityManager.find(Veiculo.class, locacao.getVeiculo().getId(), LockModeType.PESSIMISTIC_WRITE);
        if (dados.getQuilometragem() < veiculo.getQuilometragem())
            throw new IllegalArgumentException("A quilometragem de devolução não pode ser menor que a atual.");

        OffsetDateTime agora = OffsetDateTime.now();
        long segundos = Math.max(1, Duration.between(locacao.getInicio(), agora).getSeconds());
        long diarias = Math.max(1, (segundos + 86_399) / 86_400);
        BigDecimal total = locacao.getValorDiaria().multiply(BigDecimal.valueOf(diarias)).setScale(2, RoundingMode.HALF_UP);
        locacao.setDevolucao(agora);
        locacao.setValorTotal(total);
        locacao.setStatus("CONCLUIDA");
        veiculo.setQuilometragem(dados.getQuilometragem());
        veiculo.setStatus("DISPONIVEL");
        if (locacao.getReserva() != null) locacao.getReserva().setStatus("CONCLUIDA");
        return dto(locacao);
    }

    private LocacaoDTO dto(Locacao l) {
        return new LocacaoDTO(l.getId(), l.getReserva() == null ? null : l.getReserva().getId(),
                l.getCliente().getNome(), l.getVeiculo().getMarca() + " " + l.getVeiculo().getModelo(),
                l.getVeiculo().getPlaca(), l.getInicio(), l.getPrevisaoDevolucao(), l.getDevolucao(),
                l.getValorDiaria(), l.getValorTotal(), l.getStatus());
    }
}
