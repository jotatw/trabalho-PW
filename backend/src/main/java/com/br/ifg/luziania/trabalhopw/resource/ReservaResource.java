package com.br.ifg.luziania.trabalhopw.resource;
import com.br.ifg.luziania.trabalhopw.dto.*;
import com.br.ifg.luziania.trabalhopw.service.ReservaService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import java.util.List;
@Path("/reservas")
@Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"ADMIN", "ATENDENTE"})
public class ReservaResource {
    @Inject ReservaService reservaService;
    @Context SecurityContext security;
    @GET public List<ReservaDTO> listar() { return reservaService.listar(); }
    @POST public ReservaDTO criar(@Valid CriarReservaDTO dados) { return reservaService.criar(dados); }
    @PUT @Path("/{id}/status") public ReservaDTO alterarStatus(@PathParam("id") Long id, @Valid StatusReservaDTO dados) {
        return reservaService.alterarStatus(id, dados.getStatus(), security.isUserInRole("ADMIN"));
    }
}
