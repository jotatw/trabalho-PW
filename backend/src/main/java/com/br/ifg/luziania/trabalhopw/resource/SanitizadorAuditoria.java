package com.br.ifg.luziania.trabalhopw.resource;

import jakarta.ws.rs.core.MediaType;

import java.util.Map;
import java.util.regex.Pattern;

/** Regras centralizadas para evitar gravar credenciais nos registros de auditoria. */
final class SanitizadorAuditoria {
    private static final Pattern SEGREDO_JSON = Pattern.compile(
            "(?i)(\\\"(?:password|senha|token|access_token|refresh_token|authorization|secret|api_key|cpf)\\\"\\s*:\\s*)\\\"(?:\\\\.|[^\\\"\\\\])*\\\"");
    private static final Pattern SEGREDO_FORM = Pattern.compile(
            "(?i)(password|senha|token|access_token|refresh_token|secret|api_key)=[^&\\s]*");
    private static final Pattern SEGREDO_URL = Pattern.compile(
            "(?i)([?&](?:password|senha|token|access_token|refresh_token|secret|api_key)=)[^&]*");

    private SanitizadorAuditoria() { }

    static boolean deveCapturarCorpo(MediaType tipo) {
        if (tipo == null) return false;
        return tipo.isCompatible(MediaType.APPLICATION_JSON_TYPE)
                || tipo.isCompatible(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
                || tipo.getSubtype().endsWith("+json");
    }

    static boolean ehHeaderSecreto(String nome) {
        String normalizado = nome.toLowerCase();
        return normalizado.equals("authorization") || normalizado.equals("cookie")
                || normalizado.equals("set-cookie") || normalizado.contains("token")
                || normalizado.contains("secret") || normalizado.contains("api-key");
    }

    static String sanitizarCorpo(String corpo) {
        String seguro = SEGREDO_JSON.matcher(corpo).replaceAll("$1\"[REDACTED]\"");
        seguro = SEGREDO_FORM.matcher(seguro).replaceAll("$1=[REDACTED]");
        int limite = 16 * 1024 + 64;
        return seguro.length() <= limite ? seguro : seguro.substring(0, limite);
    }

    static String sanitizarUrl(String url) {
        String segura = SEGREDO_URL.matcher(url).replaceAll("$1[REDACTED]");
        return segura.length() <= 2048 ? segura : segura.substring(0, 2048);
    }

    static String serializarHeaders(Map<String, String> headers) {
        StringBuilder json = new StringBuilder("{");
        boolean primeiro = true;
        for (var header : headers.entrySet()) {
            if (!primeiro) json.append(',');
            primeiro = false;
            json.append('"').append(escapar(header.getKey())).append("\":\"")
                    .append(escapar(header.getValue())).append('"');
            if (json.length() > 16 * 1024) {
                json.setLength(16 * 1024);
                json.append(" [TRUNCATED]");
                return json.toString();
            }
        }
        return json.append('}').toString();
    }

    private static String escapar(String valor) {
        return valor.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "\\r");
    }
}
