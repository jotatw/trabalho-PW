package com.br.ifg.luziania.trabalhopw.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
public class AtualizarUsuarioDTO {
 @NotBlank @Size(max=100) private String nome;
 @NotBlank @Email @Size(max=150) private String email;
 @Size(max=72) private String senha;
 @NotNull @Positive private Long perfilId;
 public String getNome(){return nome;} public String getEmail(){return email;} public String getSenha(){return senha;} public Long getPerfilId(){return perfilId;}
}
