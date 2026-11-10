package com.br.ifg.luziania.trabalhopw.service;

import com.br.ifg.luziania.trabalhopw.dto.CriarUsuarioDTO;
import com.br.ifg.luziania.trabalhopw.dto.AtualizarUsuarioDTO;
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

        usuario.setNome(dados.getNome().trim());
        usuario.setEmail(dados.getEmail().trim().toLowerCase());
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
    public UsuarioDTO atualizar(Long id, AtualizarUsuarioDTO dados, String atorEmail) {
        Usuario usuario = usuarioRepository.findById(id);
        if (usuario == null) throw new jakarta.ws.rs.NotFoundException("Usuário não encontrado.");
        if (usuario.getEmail().equalsIgnoreCase(atorEmail))
            throw new jakarta.ws.rs.ForbiddenException("Não é permitido alterar a própria conta por esta tela.");
        if (usuarioRepository.count("lower(email) = lower(?1) and id <> ?2", dados.getEmail().trim(), id) > 0)
            throw new IllegalArgumentException("E-mail já cadastrado.");
        Perfil perfil = perfilRepository.findById(dados.getPerfilId());
        if (perfil == null) throw new PerfilInvalidoException("Perfil selecionado não existe.");
        garantirUltimoAdministrador(usuario, perfil);
        usuario.setNome(dados.getNome().trim());
        usuario.setEmail(dados.getEmail().trim().toLowerCase());
        if (dados.getSenha() != null && !dados.getSenha().isBlank()) {
            if (dados.getSenha().length() < 8) throw new IllegalArgumentException("A senha deve ter pelo menos 8 caracteres.");
            usuario.setSenhaHash(BcryptUtil.bcryptHash(dados.getSenha()));
        }
        UsuarioPerfil perfilAtual = usuarioPerfilRepository.buscarPorUsuarioId(usuario.getId()).orElse(null);
        if (perfilAtual == null) {
            associarPerfil(usuario, perfil);
        } else if (!perfilAtual.getPerfil().getId().equals(perfil.getId())) {
            usuarioPerfilRepository.delete(perfilAtual);
            usuarioPerfilRepository.flush();
            associarPerfil(usuario, perfil);
        }
        return dto(usuario, perfil.getNome());
    }

    @Transactional
    public void desativar(Long id, String atorEmail) {
        Usuario usuario = usuarioRepository.findById(id);
        if (usuario == null) throw new jakarta.ws.rs.NotFoundException("Usuário não encontrado.");
        if (usuario.getEmail().equalsIgnoreCase(atorEmail))
            throw new jakarta.ws.rs.ForbiddenException("Não é permitido desativar a própria conta.");
        garantirUltimoAdministrador(usuario, null);
        usuario.setAtivo(false);
    }

    @Transactional
    public void alterarAtivo(Long id, boolean ativo, String atorEmail) {
        Usuario usuario = usuarioRepository.findById(id);
        if (usuario == null) throw new jakarta.ws.rs.NotFoundException("Usuário não encontrado.");
        if (usuario.getEmail().equalsIgnoreCase(atorEmail))
            throw new jakarta.ws.rs.ForbiddenException("Não é permitido alterar o status da própria conta.");
        if (!ativo) garantirUltimoAdministrador(usuario, null);
        usuario.setAtivo(ativo);
    }

    private void associarPerfil(Usuario usuario, Perfil perfil) {
        UsuarioPerfil associacao = new UsuarioPerfil();
        associacao.setId(new UsuarioPerfilId(usuario.getId(), perfil.getId()));
        associacao.setUsuario(usuario);
        associacao.setPerfil(perfil);
        usuarioPerfilRepository.persist(associacao);
    }

    private void garantirUltimoAdministrador(Usuario usuario, Perfil novoPerfil) {
        boolean atualAdmin = usuarioPerfilRepository.buscarPorUsuarioId(usuario.getId())
                .map(a -> "ADMIN".equals(a.getPerfil().getNome())).orElse(false);
        boolean continuaAdmin = novoPerfil != null && "ADMIN".equals(novoPerfil.getNome());
        if (Boolean.TRUE.equals(usuario.getAtivo()) && atualAdmin && !continuaAdmin) {
            long admins = usuarioRepository.count("ativo = true and id in " +
                    "(select up.usuario.id from UsuarioPerfil up where up.perfil.nome = 'ADMIN')");
            if (admins <= 1) throw new IllegalArgumentException("Não é possível remover o último administrador ativo.");
        }
    }

    private UsuarioDTO dto(Usuario usuario, String perfil) {
        Long perfilId = usuarioPerfilRepository.buscarPorUsuarioId(usuario.getId())
                .map(a -> a.getPerfil().getId()).orElse(null);
        return new UsuarioDTO(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getAtivo(), perfil, perfilId);
    }

    @Transactional
    public List<UsuarioDTO> listarTodosDTO() {

        List<Usuario> usuarios = usuarioRepository.listAll();

        Map<Long, UsuarioPerfil> perfisPorUsuario = new HashMap<>();

        List<UsuarioPerfil> associacoes =
                usuarioPerfilRepository.listAll();

        for (UsuarioPerfil associacao : associacoes) {

            if (associacao.getUsuario() != null
                    && associacao.getPerfil() != null) {

                perfisPorUsuario.put(associacao.getUsuario().getId(), associacao);
            }
        }

        return usuarios.stream()
                .map(usuario -> new UsuarioDTO(
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getAtivo(),
                        perfisPorUsuario.containsKey(usuario.getId())
                                ? perfisPorUsuario.get(usuario.getId()).getPerfil().getNome() : "Sem perfil",
                        perfisPorUsuario.containsKey(usuario.getId())
                                ? perfisPorUsuario.get(usuario.getId()).getPerfil().getId() : null
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
