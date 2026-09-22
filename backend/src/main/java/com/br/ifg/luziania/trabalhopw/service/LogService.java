package com.br.ifg.luziania.trabalhopw.service;

import com.br.ifg.luziania.trabalhopw.model.Log;
import com.br.ifg.luziania.trabalhopw.model.Sessao;
import com.br.ifg.luziania.trabalhopw.model.Usuario;
import com.br.ifg.luziania.trabalhopw.repository.LogRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.OffsetDateTime;

@ApplicationScoped
public class LogService {

    @Inject
    LogRepository logRepository;

    @Transactional
    public void registrar(
            Usuario usuario,
            Sessao sessao,
            String tipo,
            String nivel,
            String acao,
            String mensagem
    ) {
        Log log = new Log();

        log.setUsuario(usuario);
        log.setSessao(sessao);
        log.setTipo(tipo);
        log.setNivel(nivel);
        log.setAcao(acao);
        log.setMensagem(mensagem);
        log.setDataHora(OffsetDateTime.now());

        logRepository.persist(log);
    }
}