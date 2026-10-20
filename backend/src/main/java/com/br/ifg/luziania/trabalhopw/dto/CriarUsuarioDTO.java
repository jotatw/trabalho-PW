package com.br.ifg.luziania.trabalhopw.dto;

public class CriarUsuarioDTO {

    private String nome;
    private String email;
    private String senha;
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