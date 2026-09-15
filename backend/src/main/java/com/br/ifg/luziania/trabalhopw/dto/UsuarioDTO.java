package com.br.ifg.luziania.trabalhopw.dto;

public class UsuarioDTO {

    private Long id;
    private String nome;
    private String email;
    private Boolean ativo;

    public UsuarioDTO() {
    }

    public UsuarioDTO(Long id, String nome, String email, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.ativo = ativo;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public Boolean getAtivo() {
        return ativo;
    }
}