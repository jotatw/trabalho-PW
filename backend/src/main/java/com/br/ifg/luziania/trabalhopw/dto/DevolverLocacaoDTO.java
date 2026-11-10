package com.br.ifg.luziania.trabalhopw.dto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
public class DevolverLocacaoDTO {
    @NotNull @PositiveOrZero private Integer quilometragem;
    public Integer getQuilometragem() { return quilometragem; }
}
