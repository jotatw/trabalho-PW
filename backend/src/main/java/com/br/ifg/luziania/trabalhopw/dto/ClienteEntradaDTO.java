package com.br.ifg.luziania.trabalhopw.dto;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
public class ClienteEntradaDTO {
 @NotBlank @Size(max=120) private String nome;
 @NotBlank @Pattern(regexp="\\d{11}", message="CPF deve conter 11 dígitos.") private String cpf;
 @NotNull @Past private LocalDate dataNascimento;
 @NotBlank @Size(max=20) private String telefone;
 @Email @Size(max=150) private String email;
 public String getNome(){return nome;} public String getCpf(){return cpf;} public LocalDate getDataNascimento(){return dataNascimento;} public String getTelefone(){return telefone;} public String getEmail(){return email;}
}
