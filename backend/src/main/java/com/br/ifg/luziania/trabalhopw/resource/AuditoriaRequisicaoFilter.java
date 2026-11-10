package com.br.ifg.luziania.trabalhopw.resource;

import com.br.ifg.luziania.trabalhopw.service.AuditoriaRequisicaoService;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import io.vertx.ext.web.RoutingContext;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.ext.Provider;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Registra os metadados da requisição depois que o recurso termina de processá-la. */
@Provider
@Priority(Priorities.USER)
public class AuditoriaRequisicaoFilter implements ContainerRequestFilter, ContainerResponseFilter {
    private static final String DADOS_AUDITORIA = "dadosAuditoriaRequisicao";
    private static final Logger LOGGER = Logger.getLogger(AuditoriaRequisicaoFilter.class.getName());

    @Inject AuditoriaRequisicaoService auditoriaService;
    @Context RoutingContext contextoRoteamento;

    @Override
    public void filter(ContainerRequestContext requisicao) {
        DadosAuditoria dados = new DadosAuditoria();
        dados.headers = copiarHeadersSeguros(requisicao.getHeaders());
        dados.capturaCorpo = SanitizadorAuditoria.deveCapturarCorpo(requisicao.getMediaType());
        requisicao.setProperty(DADOS_AUDITORIA, dados);

        if (dados.capturaCorpo) {
            InputStream corpoOriginal = requisicao.getEntityStream();
            requisicao.setEntityStream(new CorpoRequisicaoCapturado(corpoOriginal, dados));
        }
    }

    @Override
    public void filter(ContainerRequestContext requisicao, ContainerResponseContext resposta) {
        DadosAuditoria dados = (DadosAuditoria) requisicao.getProperty(DADOS_AUDITORIA);
        if (dados == null) dados = new DadosAuditoria();

        String corpo = dados.capturaCorpo
                ? new String(dados.corpo.toByteArray(), java.nio.charset.StandardCharsets.UTF_8)
                : "[conteúdo não textual omitido]";
        corpo = SanitizadorAuditoria.sanitizarCorpo(corpo);
        if (dados.corpoTruncado) corpo += " [truncado]";

        String url = SanitizadorAuditoria.sanitizarUrl(requisicao.getUriInfo().getRequestUri().toString());
        String usuario = requisicao.getSecurityContext().getUserPrincipal() == null
                ? null : requisicao.getSecurityContext().getUserPrincipal().getName();
        String ip = contextoRoteamento == null || contextoRoteamento.request().remoteAddress() == null
                ? null : contextoRoteamento.request().remoteAddress().host();

        // A falha de auditoria não deve substituir a resposta original ao cliente.
        try {
            auditoriaService.registrar(requisicao.getMethod(), url, ip, resposta.getStatus(),
                    corpo, SanitizadorAuditoria.serializarHeaders(dados.headers), usuario);
        } catch (RuntimeException erro) {
            LOGGER.log(Level.SEVERE, "Não foi possível persistir a auditoria da requisição");
        }
    }

    private Map<String, String> copiarHeadersSeguros(Map<String, java.util.List<String>> headers) {
        Map<String, String> seguros = new LinkedHashMap<>();
        headers.forEach((nome, valores) -> seguros.put(nome,
                SanitizadorAuditoria.ehHeaderSecreto(nome)
                        ? "[REDACTED]" : String.join(", ", valores)));
        return seguros;
    }

    /** Estado isolado por requisição; nunca é compartilhado entre chamadas. */
    static final class DadosAuditoria {
        final java.io.ByteArrayOutputStream corpo = new java.io.ByteArrayOutputStream();
        Map<String, String> headers = Map.of();
        boolean capturaCorpo;
        boolean corpoTruncado;
        int bytesCapturados;
    }
}
