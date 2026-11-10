package com.br.ifg.luziania.trabalhopw.dto;
import java.time.OffsetDateTime;
public record ReservaDTO(Long id, Long clienteId, String clienteNome, Long categoriaId, String categoriaNome, OffsetDateTime dataInicio, OffsetDateTime dataFim, String status, String observacoes) { }
