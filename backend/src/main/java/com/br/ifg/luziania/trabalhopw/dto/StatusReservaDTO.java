package com.br.ifg.luziania.trabalhopw.dto;
import jakarta.validation.constraints.NotBlank;
public class StatusReservaDTO {
    @NotBlank private String status;
    public String getStatus() { return status; }
}
