package com.br.ifg.luziania.trabalhopw.service;

import com.br.ifg.luziania.trabalhopw.dto.CriarUsuarioDTO;
import com.br.ifg.luziania.trabalhopw.exception.PerfilInvalidoException;
import com.br.ifg.luziania.trabalhopw.model.Perfil;
import com.br.ifg.luziania.trabalhopw.model.Usuario;
import com.br.ifg.luziania.trabalhopw.model.UsuarioPerfil;
import com.br.ifg.luziania.trabalhopw.model.UsuarioPerfilId;
import com.br.ifg.luziania.trabalhopw.repository.PerfilRepository;
import com.br.ifg.luziania.trabalhopw.repository.UsuarioPerfilRepository;
import com.br.ifg.luziania.trabalhopw.repository.UsuarioRepository;
import com.br.ifg.luziania.trabalhopw.dto.UsuarioDTO;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class UsuarioService {

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    PerfilRepository perfilRepository;

    @Inject
    UsuarioPerfilRepository usuarioPerfilRepository;

    public List<Usuario> listarTodos() {
        return usuarioRepository.listAll();
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.buscarPorEmail(email);
    }

    public boolean validarSenha(Usuario usuario, String senha) {
        return BcryptUtil.matches(senha, usuario.getSenhaHash());
    }

    @Transactional
    public Usuario criar(CriarUsuarioDTO dados) {

        if (usuarioRepository.buscarPorEmail(dados.getEmail()).isPresent()) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }

        if (dados.getPerfilId() == null) {
            throw new PerfilInvalidoException("Selecione um perfil.");
        }

        Perfil perfil = perfilRepository.findById(dados.getPerfilId());

        if (perfil == null) {
            throw new PerfilInvalidoException("Perfil selecionado não existe.");
        }

        Usuario usuario = new Usuario();

        usuario.setNome(dados.getNome());
        usuario.setEmail(dados.getEmail());
        usuario.setSenhaHash(BcryptUtil.bcryptHash(dados.getSenha()));
        usuario.setAtivo(true);

        usuarioRepository.persist(usuario);

        UsuarioPerfil usuarioPerfil = new UsuarioPerfil();

        usuarioPerfil.setId(
                new UsuarioPerfilId(usuario.getId(), perfil.getId())
        );
        usuarioPerfil.setUsuario(usuario);
        usuarioPerfil.setPerfil(perfil);

        usuarioPerfilRepository.persist(usuarioPerfil);

        return usuario;
    }

    @Transactional
    public List<UsuarioDTO> listarTodosDTO() {

        List<Usuario> usuarios = usuarioRepository.listAll();

        Map<Long, String> perfisPorUsuario = new HashMap<>();

        List<UsuarioPerfil> associacoes =
                usuarioPerfilRepository.listAll();

        for (UsuarioPerfil associacao : associacoes) {

            if (associacao.getUsuario() != null
                    && associacao.getPerfil() != null) {

                perfisPorUsuario.put(
                        associacao.getUsuario().getId(),
                        associacao.getPerfil().getNome()
                );
            }
        }

        return usuarios.stream()
                .map(usuario -> new UsuarioDTO(
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getAtivo(),
                        perfisPorUsuario.getOrDefault(
                                usuario.getId(),
                                "Sem perfil"
                        )
                ))
                .toList();
    }

    @Transactional
    public String buscarNomePerfil(Long usuarioId) {

        return usuarioPerfilRepository
                .buscarPorUsuarioId(usuarioId)
                .map(associacao -> associacao.getPerfil().getNome())
                .orElse("Sem perfil");
    }
}