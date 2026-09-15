package com.br.ifg.luziania.trabalhopw.repository;

import com.br.ifg.luziania.trabalhopw.model.Log;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LogRepository implements PanacheRepository<Log> {
}
