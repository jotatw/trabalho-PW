package com.br.ifg.luziania.trabalhopw.util;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;

@ApplicationScoped
public class AuditService {

    @PersistenceContext
    EntityManager em;

    @Transactional
    public void registrar(Long usuarioId, String acao, String detalhes) {
        em.createNativeQuery(
                        "INSERT INTO auditoria (usuario_id, acao, detalhes, data_hora) " +
                                "VALUES (?, ?, ?, ?)")
                .setParameter(1, usuarioId)
                .setParameter(2, acao)
                .setParameter(3, detalhes)
                .setParameter(4, LocalDateTime.now())
                .executeUpdate();
    }
}