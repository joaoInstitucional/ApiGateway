package br.com.ucsal.apigateway.dtos;

public record UsuarioDTO(
        Long idUsuario,
        String nome,
        String email,
        String tipoUsuario,
        String senha
) {}