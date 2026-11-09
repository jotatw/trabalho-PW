package com.br.ifg.luziania.trabalhopw.dto;

public class LoginRespostaDTO {

    private String token;
    private UsuarioDTO usuario;

    public LoginRespostaDTO() {
    }

    public LoginRespostaDTO(String token, UsuarioDTO usuario) {
        this.token = token;
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public UsuarioDTO getUsuario() {
        return usuario;
    }
}
