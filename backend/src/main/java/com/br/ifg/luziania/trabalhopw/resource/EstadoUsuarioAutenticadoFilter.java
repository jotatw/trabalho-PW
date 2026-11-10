package com.br.ifg.luziania.trabalhopw.resource;

import com.br.ifg.luziania.trabalhopw.dto.ErroRespostaDTO;
import com.br.ifg.luziania.trabalhopw.service.UsuarioService;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

/** Invalida tokens existentes quando a conta é desativada ou seu papel é alterado. */
@Provider
@Priority(Priorities.AUTHENTICATION + 1)
public class EstadoUsuarioAutenticadoFilter implements ContainerRequestFilter {
    @Inject UsuarioService usuarioService;

    @Override
    public void filter(ContainerRequestContext requisicao) {
        var principal = requisicao.getSecurityContext().getUserPrincipal();
        if (principal == null) return; // Requisições públicas, como login.

        var usuario = usuarioService.buscarPorEmail(principal.getName());
        if (usuario.isEmpty() || !Boolean.TRUE.equals(usuario.get().getAtivo())) {
            requisicao.abortWith(naoAutorizado());
            return;
        }

        String perfilAtual = usuarioService.buscarNomePerfil(usuario.get().getId());
        if (perfilAtual == null || perfilAtual.isBlank()
                || !requisicao.getSecurityContext().isUserInRole(perfilAtual))
            requisicao.abortWith(naoAutorizado());
    }

    private Response naoAutorizado() {
        return Response.status(Response.Status.UNAUTHORIZED)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(new ErroRespostaDTO(401, "Sessão inválida ou expirada. Entre novamente.", java.util.List.of()))
                .build();
    }
}
