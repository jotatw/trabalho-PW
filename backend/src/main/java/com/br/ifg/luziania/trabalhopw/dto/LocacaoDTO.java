package com.br.ifg.luziania.trabalhopw.dto;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
public record LocacaoDTO(Long id, Long reservaId, String cliente, String veiculo, String placa,
                         OffsetDateTime inicio, OffsetDateTime previsaoDevolucao,
                         OffsetDateTime devolucao, BigDecimal valorDiaria,
                         BigDecimal valorTotal, String status) { }
