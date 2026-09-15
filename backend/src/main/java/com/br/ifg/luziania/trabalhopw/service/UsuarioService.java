package com.br.ifg.luziania.trabalhopw.service;

import com.br.ifg.luziania.trabalhopw.model.Usuario;
import com.br.ifg.luziania.trabalhopw.repository.UsuarioRepository;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UsuarioService {

    @Inject
    UsuarioRepository usuarioRepository;

    public List<Usuario> listarTodos() {
        return usuarioRepository.listAll();
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.buscarPorEmail(email);
    }

    public boolean validarSenha(Usuario usuario, String senha) {
        return BcryptUtil.matches(senha, usuario.getSenhaHash());
    }
}
