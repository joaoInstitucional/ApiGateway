package br.com.ucsal.apigateway.dtos;

public record CredentialDTO(
        String email,
        String passwordHash,
        Long userId
) {}