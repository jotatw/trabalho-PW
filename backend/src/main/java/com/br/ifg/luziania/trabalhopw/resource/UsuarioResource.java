package com.br.ifg.luziania.trabalhopw.resource;

import com.br.ifg.luziania.trabalhopw.model.Usuario;
import com.br.ifg.luziania.trabalhopw.service.UsuarioService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/usuarios")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    @Inject UsuarioService service;

    public static record RegistroDTO(String nome, String email, String senha) {}
    public static record LoginDTO(String email, String senha) {}

    @POST
    public Response registrar(RegistroDTO dto) {
        Usuario u = service.registrar(dto.nome, dto.email, dto.senha);
        return Response.status(Response.Status.CREATED).entity(u.id).build();
    }

    @POST
    @Path("/login")
    public Response login(LoginDTO dto) {
        return service.autenticar(dto.email, dto.senha)
                .map(u -> Response.ok(u.id).build())
                .orElse(Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Credenciais inválidas").build());
    }
}