package com.br.ifg.luziania.trabalhopw.model;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "logs")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "sessao_id")
    private Sessao sessao;

    @Column(nullable = false, length = 20)
    private String tipo;

    @Column(nullable = false, length = 20)
    private String nivel;

    @Column(nullable = false, length = 100)
    private String acao;

    @Column(nullable = false)
    private String mensagem;

    @Column(name = "data_hora", nullable = false)
    private OffsetDateTime dataHora;

    @Column(name = "metodo_http", length = 10)
    private String metodoHttp;

    @Column(name = "url", length = 2048)
    private String url;

    @Column(name = "ip_cliente", length = 45)
    private String ipCliente;

    @Column(name = "status_http")
    private Integer statusHttp;

    @Column(name = "payload", columnDefinition = "TEXT")
    private String payload;

    @Column(name = "headers", columnDefinition = "TEXT")
    private String headers;

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Sessao getSessao() {
        return sessao;
    }

    public void setSessao(Sessao sessao) {
        this.sessao = sessao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public OffsetDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(OffsetDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getMetodoHttp() { return metodoHttp; }
    public void setMetodoHttp(String metodoHttp) { this.metodoHttp = metodoHttp; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getIpCliente() { return ipCliente; }
    public void setIpCliente(String ipCliente) { this.ipCliente = ipCliente; }
    public Integer getStatusHttp() { return statusHttp; }
    public void setStatusHttp(Integer statusHttp) { this.statusHttp = statusHttp; }
    public String getPayload() { return payload; }
    public void setPayload(String payload) { this.payload = payload; }
    public String getHeaders() { return headers; }
    public void setHeaders(String headers) { this.headers = headers; }
}
