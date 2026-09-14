package com.br.ifg.luziania.trabalhopw.repository;

import com.br.ifg.luziania.trabalhopw.model.CategoriaVeiculo;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CategoriaVeiculoRepository implements PanacheRepository<CategoriaVeiculo> {
}
