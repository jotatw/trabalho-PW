package com.br.ifg.luziania.trabalhopw.dto;
import jakarta.validation.constraints.*;
public class VeiculoEntradaDTO {
 @NotNull @Positive private Long categoriaId; @NotBlank @Size(max=50) private String marca; @NotBlank @Size(max=80) private String modelo;
 @NotNull @Min(1900) @Max(2100) private Short ano; @NotBlank @Pattern(regexp="[A-Za-z0-9-]{7}") private String placa;
 @NotBlank @Size(max=30) private String cor; @NotNull @PositiveOrZero private Integer quilometragem;
 public Long getCategoriaId(){return categoriaId;} public String getMarca(){return marca;} public String getModelo(){return modelo;} public Short getAno(){return ano;} public String getPlaca(){return placa;} public String getCor(){return cor;} public Integer getQuilometragem(){return quilometragem;}
}
