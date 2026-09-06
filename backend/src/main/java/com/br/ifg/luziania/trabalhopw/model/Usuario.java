package com.br.ifg.luziania.trabalhopw.model;

import jakarta.persistence.*;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.time.LocalDateTime;

/**
 * Entidade que representa um usuário da aplicação.
 *
 * – O campo {@code email} tem constraint UNIQUE (não pode haver dois usuários com o mesmo e‑mail).<br>
 * – {@code senhaHash} armazenará a hash BCrypt da senha (nunca a senha em texto‑plano).<br>
 * – {@code criadoEm} registra quando o usuário foi criado.<br>
 * – {@code ativo} permite “desativar” um usuário sem apagá‑lo.
 */
@Entity
@Table(name = "usuarios",
        uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Usuario extends PanacheEntityBase {

    /** Identificador autogerado (PK). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    /** Nome completo do usuário. */
    @Column(nullable = false, length = 100)
    public String nome;

    /** E‑mail – usado para login. Deve ser único. */
    @Column(nullable = false, unique = true, length = 150)
    public String email;

    /** Hash da senha (BCrypt). Nunca armazene a senha em texto‑plano. */
    @Column(nullable = false)
    public String senhaHash;

    /** Momento da criação do registro. */
    @Column(nullable = false, updatable = false)
    public LocalDateTime criadoEm = LocalDateTime.now();

    /** Flag que permite “desativar” o usuário sem apagá‑lo. */
    @Column(nullable = false)
    public Boolean ativo = true;
}