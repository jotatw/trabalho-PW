package com.br.ifg.luziania.trabalhopw.service;

import com.br.ifg.luziania.trabalhopw.dto.PerfilDTO;
import com.br.ifg.luziania.trabalhopw.repository.PerfilRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class PerfilService {

    @Inject
    PerfilRepository perfilRepository;

    public List<PerfilDTO> listarTodos() {
        return perfilRepository.listAll()
                .stream()
                .map(perfil -> new PerfilDTO(
                        perfil.getId(),
                        perfil.getNome(),
                        perfil.getDescricao()
                ))
                .toList();
    }
}
