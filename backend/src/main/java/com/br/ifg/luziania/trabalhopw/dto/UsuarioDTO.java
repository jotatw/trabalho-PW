package com.br.ifg.luziania.trabalhopw.dto;

public class UsuarioDTO {
    private Long id;
    private String nome;
    private String email;
    private Boolean ativo;
    private String perfil;
    private Long perfilId;

    public UsuarioDTO() { }
    public UsuarioDTO(Long id, String nome, String email, Boolean ativo, String perfil, Long perfilId) {
        this.id=id; this.nome=nome; this.email=email; this.ativo=ativo; this.perfil=perfil; this.perfilId=perfilId;
    }
    public UsuarioDTO(Long id, String nome, String email, Boolean ativo, String perfil) { this(id,nome,email,ativo,perfil,null); }
    public UsuarioDTO(Long id, String nome, String email, Boolean ativo) { this(id,nome,email,ativo,null,null); }
    public Long getId(){return id;} public String getNome(){return nome;} public String getEmail(){return email;}
    public Boolean getAtivo(){return ativo;} public String getPerfil(){return perfil;} public Long getPerfilId(){return perfilId;}
}
