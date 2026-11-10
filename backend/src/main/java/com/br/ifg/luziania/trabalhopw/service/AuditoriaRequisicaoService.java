package com.br.ifg.luziania.trabalhopw.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/** Encapsula a gravação dos dados HTTP usando o serviço comum de logs. */
@ApplicationScoped
public class AuditoriaRequisicaoService {
    @Inject LogService logService;

    @Transactional
    public void registrar(String metodo, String url, String ip, int status,
                          String corpo, String headers, String usuario) {
        logService.registrarRequisicao(metodo, url, ip, status, corpo, headers, usuario);
    }
}
