package com.br.ifg.luziania.trabalhopw.service;

import com.br.ifg.luziania.trabalhopw.model.Sessao;
import com.br.ifg.luziania.trabalhopw.model.Usuario;
import com.br.ifg.luziania.trabalhopw.repository.SessaoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;

@ApplicationScoped
public class SessaoService {

    @Inject
    SessaoRepository sessaoRepository;

    @Transactional
    public Sessao iniciarSessao(Usuario usuario) {

        Sessao sessao = new Sessao();

        sessao.setUsuario(usuario);
        sessao.setInicio(OffsetDateTime.now());
        sessao.setStatus("ATIVA");

        sessaoRepository.persist(sessao);

        return sessao;
    }

    public Optional<Sessao> buscarPorId(Long id) {
        return sessaoRepository.buscarPorId(id);
    }
}
