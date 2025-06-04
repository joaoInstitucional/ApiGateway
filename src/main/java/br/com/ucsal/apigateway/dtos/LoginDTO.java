package br.com.ucsal.apigateway.dtos;

public record LoginDTO(
        String email,
        String password
) {}