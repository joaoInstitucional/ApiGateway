package br.com.ucsal.apigateway.dtos;


import java.time.LocalDateTime;

public record SoftwareDTO(
        Long idSoftware,
        String nome,
        String versao,
        String linkInstalacao,
        boolean proprietario,
        boolean disponivel,
        LocalDateTime dataCadastro
) {}