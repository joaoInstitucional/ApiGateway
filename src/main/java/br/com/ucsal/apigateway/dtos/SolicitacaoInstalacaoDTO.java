package br.com.ucsal.apigateway.dtos;

import java.time.LocalDateTime;
import java.util.List;

public record SolicitacaoInstalacaoDTO(
        Long idProfessor,
        Long idLaboratorio,
        List<Long> idsSoftwares,
        LocalDateTime dataInicio
) {
}