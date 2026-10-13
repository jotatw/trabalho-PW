package com.br.ifg.luziania.trabalhopw;

import io.quarkus.elytron.security.common.BcryptUtil;

public class TesteBcrypt {

    public static void main(String[] args) {

        String senhaAdmin = "admin123";
        String senhaAtendente = "atendente123";

        String hashAdmin = BcryptUtil.bcryptHash(senhaAdmin);
        String hashAtendente = BcryptUtil.bcryptHash(senhaAtendente);

        System.out.println("Admin:");
        System.out.println(hashAdmin);

        System.out.println();

        System.out.println("Atendente:");
        System.out.println(hashAtendente);
    }
}