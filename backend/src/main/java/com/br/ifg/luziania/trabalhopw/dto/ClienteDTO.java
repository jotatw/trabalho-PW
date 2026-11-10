package com.br.ifg.luziania.trabalhopw.dto;
import java.time.LocalDate;
public record ClienteDTO(Long id,String nome,String cpf,LocalDate dataNascimento,String telefone,String email,Boolean ativo) { }
