package com.br.ifg.luziania.trabalhopw.dto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
public class IniciarLocacaoDTO {
    @NotNull @Positive private Long reservaId;
    @NotNull @Positive private Long veiculoId;
    public Long getReservaId() { return reservaId; }
    public Long getVeiculoId() { return veiculoId; }
}
