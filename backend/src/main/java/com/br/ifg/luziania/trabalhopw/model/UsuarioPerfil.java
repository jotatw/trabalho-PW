package com.br.ifg.luziania.trabalhopw.model;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario_perfis")
public class UsuarioPerfil {

    @EmbeddedId
    private UsuarioPerfilId id;

    @ManyToOne
    @MapsId("usuarioId")
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @MapsId("perfilId")
    @JoinColumn(name = "perfil_id", nullable = false)
    private Perfil perfil;

    public UsuarioPerfilId getId() {
        return id;
    }

    public void setId(UsuarioPerfilId id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }
}