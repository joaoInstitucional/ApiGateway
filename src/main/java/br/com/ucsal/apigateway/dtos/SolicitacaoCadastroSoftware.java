package br.com.ucsal.apigateway.dtos;

public record SolicitacaoCadastroSoftware(
        Long id,
        String nome,
        String versao,
        String linkInstalacao,
        boolean proprietario,
        Long idUsuarioSolicitante,
        String status
) {
}