package com.br.ifg.luziania.trabalhopw.resource;

import java.io.IOException;
import java.io.InputStream;

/** Deixa o recurso ler o corpo normalmente e guarda somente os primeiros 16 KiB para auditoria. */
final class CorpoRequisicaoCapturado extends InputStream {
    private static final int LIMITE_BYTES = 16 * 1024;
    private final InputStream original;
    private final AuditoriaRequisicaoFilter.DadosAuditoria dados;

    CorpoRequisicaoCapturado(InputStream original, AuditoriaRequisicaoFilter.DadosAuditoria dados) {
        this.original = original;
        this.dados = dados;
    }

    @Override
    public int read() throws IOException {
        int valor = original.read();
        if (valor >= 0) guardar(new byte[]{(byte) valor}, 0, 1);
        return valor;
    }

    @Override
    public int read(byte[] buffer, int inicio, int tamanho) throws IOException {
        int lidos = original.read(buffer, inicio, tamanho);
        if (lidos > 0) guardar(buffer, inicio, lidos);
        return lidos;
    }

    @Override
    public void close() throws IOException {
        original.close();
    }

    private void guardar(byte[] buffer, int inicio, int tamanho) {
        int restantes = LIMITE_BYTES - dados.bytesCapturados;
        int guardar = Math.min(restantes, tamanho);
        if (guardar > 0) {
            dados.corpo.write(buffer, inicio, guardar);
            dados.bytesCapturados += guardar;
        }
        if (guardar < tamanho) dados.corpoTruncado = true;
    }
}
