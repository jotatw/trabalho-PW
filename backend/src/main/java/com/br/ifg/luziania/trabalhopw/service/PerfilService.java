package com.br.ifg.luziania.trabalhopw.service;

import com.br.ifg.luziania.trabalhopw.dto.PerfilDTO;
import com.br.ifg.luziania.trabalhopw.dto.PerfilEntradaDTO;
import com.br.ifg.luziania.trabalhopw.model.Perfil;
import com.br.ifg.luziania.trabalhopw.repository.PerfilRepository;
import com.br.ifg.luziania.trabalhopw.repository.UsuarioPerfilRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.List;

@ApplicationScoped
public class PerfilService {
    @Inject PerfilRepository perfilRepository;
    @Inject UsuarioPerfilRepository usuarioPerfilRepository;

    public List<PerfilDTO> listarTodos() {
        return perfilRepository.listAll().stream().map(this::dto).toList();
    }

    @Transactional
    public PerfilDTO criar(PerfilEntradaDTO dados) {
        String nome = normalizarNome(dados.getNome());
        if (perfilRepository.count("lower(nome) = lower(?1)", nome) > 0)
            throw new IllegalArgumentException("Já existe um perfil com esse nome.");
        Perfil perfil = new Perfil();
        perfil.setNome(nome);
        perfil.setDescricao(limparDescricao(dados.getDescricao()));
        perfilRepository.persist(perfil);
        return dto(perfil);
    }

    @Transactional
    public PerfilDTO atualizar(Long id, PerfilEntradaDTO dados) {
        Perfil perfil = perfilRepository.findById(id);
        if (perfil == null) throw new NotFoundException("Perfil não encontrado.");
        String novoNome = normalizarNome(dados.getNome());
        if (!perfil.getNome().equals(novoNome) && nomeProtegido(perfil.getNome()))
            throw new IllegalArgumentException("Os perfis ADMIN e ATENDENTE não podem ser renomeados.");
        if (perfilRepository.count("lower(nome) = lower(?1) and id <> ?2", novoNome, id) > 0)
            throw new IllegalArgumentException("Já existe um perfil com esse nome.");
        if (!perfil.getNome().equals(novoNome) && usuarioPerfilRepository.count("perfil.id", id) > 0)
            throw new IllegalArgumentException("Não é possível renomear um perfil atribuído a usuários.");
        perfil.setNome(novoNome);
        perfil.setDescricao(limparDescricao(dados.getDescricao()));
        return dto(perfil);
    }

    @Transactional
    public void excluir(Long id) {
        Perfil perfil = perfilRepository.findById(id);
        if (perfil == null) throw new NotFoundException("Perfil não encontrado.");
        if (nomeProtegido(perfil.getNome()))
            throw new IllegalArgumentException("Os perfis ADMIN e ATENDENTE são protegidos.");
        if (usuarioPerfilRepository.count("perfil.id", id) > 0)
            throw new IllegalArgumentException("Remova o perfil dos usuários antes de excluí-lo.");
        perfilRepository.delete(perfil);
    }

    private boolean nomeProtegido(String nome) {
        return "ADMIN".equalsIgnoreCase(nome) || "ATENDENTE".equalsIgnoreCase(nome);
    }
    private String normalizarNome(String nome) { return nome.trim().toUpperCase(); }
    private String limparDescricao(String descricao) { return descricao == null || descricao.isBlank() ? null : descricao.trim(); }
    private PerfilDTO dto(Perfil perfil) { return new PerfilDTO(perfil.getId(), perfil.getNome(), perfil.getDescricao()); }
}
