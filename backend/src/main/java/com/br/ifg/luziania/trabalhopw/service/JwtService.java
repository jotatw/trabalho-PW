package com.br.ifg.luziania.trabalhopw.service;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Set;

@ApplicationScoped
public class JwtService {

    private static final String ISSUER = "trabalho-pw";

    public String gerarToken(Long usuarioId, String email, String perfil) {
        return Jwt.issuer(ISSUER)
                .claim("sub", usuarioId.toString())
                .upn(email)
                .groups(Set.of(perfil))
                .sign();
    }
}
