package com.br.ifg.luziania.trabalhopw.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;

public class CriarReservaDTO {
    @NotNull private Long clienteId;
    @NotNull private Long categoriaId;
    @NotNull private OffsetDateTime dataInicio;
    @NotNull private OffsetDateTime dataFim;
    @Size(max = 500) private String observacoes;
    public Long getClienteId() { return clienteId; }
    public Long getCategoriaId() { return categoriaId; }
    public OffsetDateTime getDataInicio() { return dataInicio; }
    public OffsetDateTime getDataFim() { return dataFim; }
    public String getObservacoes() { return observacoes; }
}
