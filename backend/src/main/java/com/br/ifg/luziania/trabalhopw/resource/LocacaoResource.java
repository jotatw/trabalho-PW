package com.br.ifg.luziania.trabalhopw.resource;

import com.br.ifg.luziania.trabalhopw.dto.*;
import com.br.ifg.luziania.trabalhopw.service.LocacaoService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/locacoes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"ADMIN", "ATENDENTE"})
public class LocacaoResource {
    @Inject LocacaoService service;
    @GET public List<LocacaoDTO> listar() { return service.listar(); }
    @POST public LocacaoDTO iniciar(@Valid IniciarLocacaoDTO dados) { return service.iniciar(dados); }
    @PUT @Path("/{id}/devolucao")
    public LocacaoDTO devolver(@PathParam("id") Long id, @Valid DevolverLocacaoDTO dados) {
        return service.devolver(id, dados);
    }
}