package com.br.ifg.luziania.trabalhopw.resource;

import com.br.ifg.luziania.trabalhopw.dto.UsuarioDTO;
import com.br.ifg.luziania.trabalhopw.model.Usuario;
import com.br.ifg.luziania.trabalhopw.service.UsuarioService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import com.br.ifg.luziania.trabalhopw.dto.LoginDTO;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.Consumes;

import java.util.List;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    @Inject
    UsuarioService usuarioService;

    @GET
    public List<UsuarioDTO> listar() {
        return usuarioService.listarTodos()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    private UsuarioDTO converterParaDTO(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getAtivo()
        );
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(LoginDTO loginDTO) {

        var usuario = usuarioService.buscarPorEmail(loginDTO.getEmail());

        if (usuario.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        if (!usuarioService.validarSenha(usuario.get(), loginDTO.getSenha())) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        return Response.ok().build();
    }
}

