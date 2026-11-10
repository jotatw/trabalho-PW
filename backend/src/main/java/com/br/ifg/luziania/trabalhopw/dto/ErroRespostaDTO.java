package com.br.ifg.luziania.trabalhopw.dto;

import java.util.List;

/** Formato único de erro enviado pela API. */
public class ErroRespostaDTO {
    private final int status;
    private final String erro;
    private final List<String> detalhes;

    public ErroRespostaDTO(int status, String erro, List<String> detalhes) {
        this.status = status;
        this.erro = erro;
        this.detalhes = detalhes == null ? List.of() : List.copyOf(detalhes);
    }

    public int getStatus() { return status; }
    public String getErro() { return erro; }
    public List<String> getDetalhes() { return detalhes; }
}
