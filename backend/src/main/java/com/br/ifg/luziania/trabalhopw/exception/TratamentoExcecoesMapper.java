package com.br.ifg.luziania.trabalhopw.exception;

import com.br.ifg.luziania.trabalhopw.dto.ErroRespostaDTO;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.List;

/** Converte erros conhecidos e inesperados para respostas JSON sem detalhes internos. */
@Provider
public class TratamentoExcecoesMapper implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception excecao) {
        if (excecao instanceof ConstraintViolationException validacao) {
            List<String> detalhes = validacao.getConstraintViolations().stream()
                    .map(violacao -> violacao.getPropertyPath() + ": " + violacao.getMessage())
                    .distinct().toList();
            return resposta(400, "Dados inválidos.", detalhes);
        }

        if (excecao instanceof PerfilInvalidoException) {
            return resposta(400, "Perfil inválido.", List.of(excecao.getMessage()));
        }

        if (excecao instanceof IllegalArgumentException) {
            return resposta(409, "Não foi possível concluir a operação.", List.of());
        }

        if (excecao instanceof BadRequestException) {
            return resposta(400, "Requisição inválida.", List.of());
        }

        if (excecao instanceof WebApplicationException web) {
            int status = web.getResponse().getStatus();
            String mensagem = status >= 500 ? "Erro interno do servidor." : "Não foi possível processar a requisição.";
            return resposta(status, mensagem, List.of());
        }

        // Não devolver excecao.getMessage() nem stack trace: podem revelar dados internos.
        return resposta(500, "Erro interno do servidor.", List.of());
    }

    private Response resposta(int status, String mensagem, List<String> detalhes) {
        return Response.status(status)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(new ErroRespostaDTO(status, mensagem, detalhes))
                .build();
    }
}
