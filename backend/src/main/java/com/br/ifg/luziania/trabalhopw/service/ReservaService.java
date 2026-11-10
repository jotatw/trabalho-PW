package com.br.ifg.luziania.trabalhopw.service;

import com.br.ifg.luziania.trabalhopw.dto.CriarReservaDTO;
import com.br.ifg.luziania.trabalhopw.dto.ReservaDTO;
import com.br.ifg.luziania.trabalhopw.model.Reserva;
import com.br.ifg.luziania.trabalhopw.model.CategoriaVeiculo;
import com.br.ifg.luziania.trabalhopw.repository.ClienteRepository;
import com.br.ifg.luziania.trabalhopw.repository.CategoriaVeiculoRepository;
import com.br.ifg.luziania.trabalhopw.repository.ReservaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import java.time.OffsetDateTime;
import java.util.List;

@ApplicationScoped
public class ReservaService {
    @Inject ReservaRepository reservas;
    @Inject ClienteRepository clientes;
    @Inject CategoriaVeiculoRepository categorias;
    @Inject EntityManager entityManager;

    @Transactional
    public List<ReservaDTO> listar() { return reservas.listAll().stream().map(this::dto).toList(); }

    @Transactional
    public ReservaDTO criar(CriarReservaDTO dados) {
        if (!dados.getDataFim().isAfter(dados.getDataInicio()))
            throw new IllegalArgumentException("A data final deve ser posterior à inicial.");
        if (!dados.getDataInicio().isAfter(OffsetDateTime.now()))
            throw new IllegalArgumentException("A reserva deve começar no futuro.");
        var cliente = clientes.findById(dados.getClienteId());
        var categoria = categorias.findById(dados.getCategoriaId());
        if (cliente == null || !Boolean.TRUE.equals(cliente.getAtivo()))
            throw new IllegalArgumentException("Cliente não encontrado ou inativo.");
        if (categoria == null || !Boolean.TRUE.equals(categoria.getAtivo()))
            throw new IllegalArgumentException("Categoria não encontrada ou inativa.");
        Reserva reserva = new Reserva();
        reserva.setCliente(cliente);
        reserva.setCategoria(categoria);
        reserva.setDataInicio(dados.getDataInicio());
        reserva.setDataFim(dados.getDataFim());
        reserva.setObservacoes(dados.getObservacoes());
        reserva.setStatus("PENDENTE");
        reservas.persist(reserva);
        return dto(reserva);
    }

    @Transactional
    public ReservaDTO alterarStatus(Long id, String status, boolean admin) {
        Reserva reserva = reservas.findById(id);
        if (reserva == null) throw new jakarta.ws.rs.NotFoundException();
        if (status == null) throw new IllegalArgumentException("Transição de reserva inválida.");
        status = status.trim().toUpperCase();
        if (!List.of("CONFIRMADA", "CANCELADA").contains(status))
            throw new IllegalArgumentException("Transição de reserva inválida.");
        if (!"PENDENTE".equals(reserva.getStatus()))
            throw new IllegalArgumentException("A reserva não está pendente.");
        if ("CONFIRMADA".equals(status) && !reserva.getDataFim().isAfter(OffsetDateTime.now()))
            throw new IllegalArgumentException("Não é possível confirmar uma reserva cujo período já terminou.");
        if ("CANCELADA".equals(status) && !admin)
            throw new jakarta.ws.rs.ForbiddenException();
        if ("CONFIRMADA".equals(status)) verificarDisponibilidade(reserva);
        reserva.setStatus(status);
        return dto(reserva);
    }

    /** Serializa confirmações da mesma categoria e conta reservas/locações que cruzam o período. */
    private void verificarDisponibilidade(Reserva reserva) {
        entityManager.find(CategoriaVeiculo.class, reserva.getCategoria().getId(), LockModeType.PESSIMISTIC_WRITE);
        long frotaAtiva = entityManager.createQuery(
                        "select count(v) from Veiculo v where v.categoria.id = :categoria " +
                                "and v.ativo = true and v.status not in ('MANUTENCAO', 'INATIVO')", Long.class)
                .setParameter("categoria", reserva.getCategoria().getId()).getSingleResult();
        long reservasConfirmadas = entityManager.createQuery(
                        "select count(r) from Reserva r where r.categoria.id = :categoria " +
                                "and r.status = 'CONFIRMADA' and r.dataInicio < :fim and r.dataFim > :inicio " +
                                "and not exists (select l.id from Locacao l where l.reserva = r and l.status = 'ATIVA')", Long.class)
                .setParameter("categoria", reserva.getCategoria().getId())
                .setParameter("inicio", reserva.getDataInicio()).setParameter("fim", reserva.getDataFim())
                .getSingleResult();
        long locacoesAtivas = entityManager.createQuery(
                        "select count(l) from Locacao l where l.veiculo.categoria.id = :categoria " +
                                "and l.status = 'ATIVA' and l.inicio < :fim and l.previsaoDevolucao > :inicio", Long.class)
                .setParameter("categoria", reserva.getCategoria().getId())
                .setParameter("inicio", reserva.getDataInicio()).setParameter("fim", reserva.getDataFim())
                .getSingleResult();
        if (frotaAtiva == 0 || reservasConfirmadas + locacoesAtivas >= frotaAtiva)
            throw new IllegalArgumentException("Não há veículos disponíveis nessa categoria para o período solicitado.");
    }

    private ReservaDTO dto(Reserva r) {
        return new ReservaDTO(r.getId(), r.getCliente().getId(), r.getCliente().getNome(),
                r.getCategoria().getId(), r.getCategoria().getNome(), r.getDataInicio(),
                r.getDataFim(), r.getStatus(), r.getObservacoes());
    }
}
