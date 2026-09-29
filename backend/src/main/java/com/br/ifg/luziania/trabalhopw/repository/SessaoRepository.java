package com.br.ifg.luziania.trabalhopw.repository;

import com.br.ifg.luziania.trabalhopw.model.Sessao;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class SessaoRepository implements PanacheRepository<Sessao> {

    public Optional<Sessao> buscarPorId(Long id) {
        return findByIdOptional(id);
    }
}