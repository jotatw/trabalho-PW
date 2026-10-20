package com.br.ifg.luziania.trabalhopw.resource;

import com.br.ifg.luziania.trabalhopw.dto.PerfilDTO;
import com.br.ifg.luziania.trabalhopw.service.PerfilService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/perfis")
@Produces(MediaType.APPLICATION_JSON)
public class PerfilResource {

    @Inject
    PerfilService perfilService;

    @GET
    public List<PerfilDTO> listar() {
        return perfilService.listarTodos();
    }
}
