package com.br.ifg.luziania.trabalhopw.repository;

import com.br.ifg.luziania.trabalhopw.model.Perfil;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PerfilRepository implements PanacheRepository<Perfil> {
}
