package br.com.ucsal.apigateway.dtos;

public record SolicitacaoCadastroDTO(
        String nome,
        String versao,
        String linkInstalacao,
        boolean proprietario,
        Long idUsuarioSolicitante
) {
}