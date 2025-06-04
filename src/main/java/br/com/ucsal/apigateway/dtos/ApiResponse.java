package br.com.ucsal.apigateway.dtos;

public record ApiResponse<T>(
        int status,
        String message,
        T data
) {}