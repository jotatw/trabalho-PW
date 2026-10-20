package com.br.ifg.luziania.trabalhopw.repository;

import com.br.ifg.luziania.trabalhopw.model.UsuarioPerfil;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class UsuarioPerfilRepository implements PanacheRepository<UsuarioPerfil> {

    public Optional<UsuarioPerfil> buscarPorUsuarioId(Long usuarioId) {
        return find("usuario.id", usuarioId).firstResultOptional();
    }
}