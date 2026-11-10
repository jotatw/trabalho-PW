package com.br.ifg.luziania.trabalhopw.resource;

import com.br.ifg.luziania.trabalhopw.dto.PerfilDTO;
import com.br.ifg.luziania.trabalhopw.dto.PerfilEntradaDTO;
import com.br.ifg.luziania.trabalhopw.service.PerfilService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;

@Path("/perfis")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("ADMIN")
public class PerfilResource {

    @Inject
    PerfilService perfilService;

    @GET
    public List<PerfilDTO> listar() {
        return perfilService.listarTodos();
    }

    @POST
    public Response criar(@Valid PerfilEntradaDTO dados) {
        PerfilDTO criado = perfilService.criar(dados);
        return Response.status(Response.Status.CREATED).entity(criado).build();
    }

    @PUT
    @Path("/{id}")
    public PerfilDTO atualizar(@PathParam("id") Long id, @Valid PerfilEntradaDTO dados) {
        return perfilService.atualizar(id, dados);
    }

    @DELETE
    @Path("/{id}")
    public void excluir(@PathParam("id") Long id) {
        perfilService.excluir(id);
    }
}
