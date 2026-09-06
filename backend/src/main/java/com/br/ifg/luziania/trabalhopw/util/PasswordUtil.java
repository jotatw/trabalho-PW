package com.br.ifg.luziania.trabalhopw.util;

import jakarta.enterprise.context.ApplicationScoped;
import io.quarkus.elytron.security.common.BcryptUtil;

@ApplicationScoped
public class PasswordUtil {

    private static final int COST = 10; // força padrão

    public String hash(String plain) {
        return BcryptUtil.bcryptHash(plain.toCharArray(), COST);
    }

    public boolean matches(String plain, String hashed) {
        return BcryptUtil.matches(plain.toCharArray(), hashed);
    }
}
