package com.br.ifg.luziania.trabalhopw.service;

import com.br.ifg.luziania.trabalhopw.model.Log;
import com.br.ifg.luziania.trabalhopw.model.Sessao;
import com.br.ifg.luziania.trabalhopw.model.Usuario;
import com.br.ifg.luziania.trabalhopw.repository.LogRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.OffsetDateTime;
import java.util.Map;

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

    @Transactional
    public void registrarRequisicao(String metodo, String url, String ip, int status,
                                   String payload, String headers, String usuarioEmail) {
        Log log = new Log();
        log.setTipo("USO");
        log.setNivel(status >= 500 ? "ERROR" : status >= 400 ? "WARN" : "INFO");
        String acao = metodo + " " + url;
        log.setAcao(acao.length() <= 100 ? acao : acao.substring(0, 100));
        log.setMensagem("Requisição HTTP concluída");
        log.setDataHora(OffsetDateTime.now());
        log.setMetodoHttp(metodo);
        log.setUrl(url);
        log.setIpCliente(ip);
        log.setStatusHttp(status);
        log.setPayload(payload);
        log.setHeaders(headers);
        if (usuarioEmail != null && !usuarioEmail.isBlank()) {
            logRepository.getEntityManager().createQuery(
                    "select u from Usuario u where u.email = :email", com.br.ifg.luziania.trabalhopw.model.Usuario.class)
                    .setParameter("email", usuarioEmail).getResultStream().findFirst()
                    .ifPresent(log::setUsuario);
        }
        logRepository.persist(log);
    }
}
