package com.br.ifg.luziania.trabalhopw.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public class PerfilEntradaDTO {
 @NotBlank @Size(max=30) private String nome;
 @Size(max=150) private String descricao;
 public String getNome(){return nome;} public String getDescricao(){return descricao;}
}
