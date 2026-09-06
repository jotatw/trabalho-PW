package com.br.ifg.luziania.trabalhopw.service;

import com.br.ifg.luziania.trabalhopw.model.Usuario;
import com.br.ifg.luziania.trabalhopw.repository.UsuarioRepository;
import com.br.ifg.luziania.trabalhopw.util.PasswordUtil;
import com.br.ifg.luziania.trabalhopw.util.AuditService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;

@ApplicationScoped
public class UsuarioService {

    @Inject UsuarioRepository repo;
    @Inject PasswordUtil passwordUtil;
    @Inject AuditService audit;

    @Transactional
    public Usuario registrar(String nome, String email, String senha) {
        if (repo.existsByEmail(email)) {
            throw new IllegalArgumentException("E‑mail já cadastrado");
        }
        Usuario u = new Usuario();
        u.nome = nome;
        u.email = email;
        u.senhaHash = passwordUtil.hash(senha);
        u.persist();
        audit.registrar(u.id, "CADASTRO_USUARIO", "Criado via API");
        return u;
    }

    public Optional<Usuario> autenticar(String email, String senha) {
        return repo.findByEmail(email)
                .filter(u -> passwordUtil.matches(senha, u.senhaHash));
    }
}