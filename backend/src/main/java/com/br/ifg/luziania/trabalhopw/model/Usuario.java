package com.br.ifg.luziania.trabalhopw.model;

import jakarta.persistence.*;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Usuario extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, length = 100)
    public String nome;

    @Column(nullable = false, unique = true, length = 150)
    public String email;

    @Column(nullable = false)
    public String senhaHash;          // hash BCrypt, nunca a senha em texto puro

    @Column(nullable = false, updatable = false)
    public LocalDateTime criadoEm = LocalDateTime.now();

    @Column(nullable = false)
    public Boolean ativo = true;
}