package com.br.ifg.luziania.trabalhopw.repository;

import com.br.ifg.luziania.trabalhopw.model.Sessao;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SessaoRepository implements PanacheRepository<Sessao> {
}