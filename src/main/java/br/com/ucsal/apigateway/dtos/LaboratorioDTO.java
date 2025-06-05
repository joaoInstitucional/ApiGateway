package br.com.ucsal.apigateway.dtos;

public record LaboratorioDTO(
        Long id,
        String Codigo,
        String nome,
        String status
) {
}