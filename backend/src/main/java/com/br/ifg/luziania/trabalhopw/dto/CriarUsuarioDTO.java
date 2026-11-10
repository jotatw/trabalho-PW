package com.br.ifg.luziania.trabalhopw.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class CriarUsuarioDTO {

    @NotBlank(message = "Informe o nome.")
    @Size(max = 100, message = "O nome deve ter até 100 caracteres.")
    private String nome;

    @NotBlank(message = "Informe o e-mail.")
    @Email(message = "Informe um e-mail válido.")
    @Size(max = 150, message = "O e-mail deve ter até 150 caracteres.")
    private String email;

    @NotBlank(message = "Informe a senha.")
    @Size(min = 8, max = 72, message = "A senha deve ter entre 8 e 72 caracteres.")
    private String senha;

    @NotNull(message = "Selecione um perfil.")
    @Positive(message = "O perfil selecionado é inválido.")
    private Long perfilId;

    public CriarUsuarioDTO() {
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public Long getPerfilId(){
        return perfilId;
    }
}
