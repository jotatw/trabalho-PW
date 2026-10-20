package com.br.ifg.luziania.trabalhopw.resource;

import com.br.ifg.luziania.trabalhopw.dto.CriarUsuarioDTO;
import com.br.ifg.luziania.trabalhopw.dto.UsuarioDTO;
import com.br.ifg.luziania.trabalhopw.model.Sessao;
import com.br.ifg.luziania.trabalhopw.model.Usuario;
import com.br.ifg.luziania.trabalhopw.service.LogService;
import com.br.ifg.luziania.trabalhopw.service.UsuarioService;
import com.br.ifg.luziania.trabalhopw.service.SessaoService;
import com.br.ifg.luziania.trabalhopw.exception.PerfilInvalidoException;
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

    @Inject
    SessaoService sessaoService;

    @Inject
    LogService logService;

    @GET
    public List<UsuarioDTO> listar() {
        return usuarioService.listarTodosDTO();
    }

    private UsuarioDTO converterParaDTO(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getAtivo(),
                usuarioService.buscarNomePerfil(usuario.getId())
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

        Usuario usuarioAutenticado = usuario.get();

        Sessao sessao = sessaoService.iniciarSessao(usuarioAutenticado);

        logService.registrar(
                usuarioAutenticado,
                sessao,
                "USO",                "INFO",
                "LOGIN",
                "Usuário realizou login no sistema"
        );

        UsuarioDTO usuarioDTO = converterParaDTO(usuarioAutenticado);

        return Response.ok(usuarioDTO).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response criar(CriarUsuarioDTO dados) {

        try {

            Usuario usuario = usuarioService.criar(dados);

            UsuarioDTO usuarioDTO = converterParaDTO(usuario);

            return Response
                    .status(Response.Status.CREATED)
                    .entity(usuarioDTO)
                    .build();

        } catch (PerfilInvalidoException e) {

            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("{\"erro\":\"" + e.getMessage() + "\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } catch (IllegalArgumentException e) {

            return Response
                    .status(Response.Status.CONFLICT)
                    .entity("{\"erro\":\"" + e.getMessage() + "\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }
}

