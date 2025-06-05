package br.com.ucsal.apigateway.controllers;

import br.com.ucsal.apigateway.clients.SolicitacaoInstalacaoServiceClient;
import br.com.ucsal.apigateway.clients.LaboratorioServiceClient;
import br.com.ucsal.apigateway.dtos.AtualizaStatusDTO;
import br.com.ucsal.apigateway.dtos.SolicitacaoInstalacaoDTO;
import br.com.ucsal.apigateway.dtos.ApiResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/instalacoes")
public class SolicitacaoInstalacaoGatewayController {

    private final SolicitacaoInstalacaoServiceClient solicitacaoInstalacaoClient;
    private final LaboratorioServiceClient laboratorioServiceClient;

    public SolicitacaoInstalacaoGatewayController(SolicitacaoInstalacaoServiceClient solicitacaoInstalacaoClient,
                                                  LaboratorioServiceClient laboratorioServiceClient) {
        this.solicitacaoInstalacaoClient = solicitacaoInstalacaoClient;
        this.laboratorioServiceClient = laboratorioServiceClient;
    }

    @PostMapping
    public Mono<ApiResponse> criarSolicitacaoInstalacao(@RequestBody SolicitacaoInstalacaoDTO dto) {
        return solicitacaoInstalacaoClient.criarSolicitacaoInstalacao(dto);
    }

    @GetMapping
    public Flux<ApiResponse> listarSolicitacoesInstalacao() {
        return solicitacaoInstalacaoClient.listarSolicitacoesInstalacao();
    }

    @GetMapping("/{id}")
    public Mono<ApiResponse> buscarSolicitacaoInstalacaoPorId(@PathVariable Long id) {
        return solicitacaoInstalacaoClient.buscarSolicitacaoInstalacaoPorId(id);
    }

    @PutMapping("/{id}/status")
    public Mono<ApiResponse> atualizarStatusInstalacao(@PathVariable Long id, @RequestBody AtualizaStatusDTO dto) {
        return solicitacaoInstalacaoClient.atualizarStatusInstalacao(id, dto)
                .flatMap(apiResponse -> {
                    Object data = apiResponse.data();

                    if (data instanceof Map map) {
                        String status = dto.status(); // status que o usuário quer atualizar
                        Integer idLaboratorio = (Integer) map.get("idLaboratorio");

                        if (idLaboratorio != null) {
                            String novoStatusLab = null;

                            if ("EM ANDAMENTO".equalsIgnoreCase(status)) {
                                novoStatusLab = "INDISPONIVEL";
                            } else if ("FINALIZADA".equalsIgnoreCase(status)) {
                                novoStatusLab = "DISPONIVEL";
                            }

                            if (novoStatusLab != null) {
                                AtualizaStatusDTO atualizaStatusLaboratorio = new AtualizaStatusDTO(novoStatusLab);
                                return laboratorioServiceClient.atualizarStatus(Long.valueOf(idLaboratorio), atualizaStatusLaboratorio)
                                        .map(labAtualizado -> new ApiResponse<>(200, "Status da solicitação e do laboratório atualizados com sucesso", labAtualizado))
                                        .onErrorResume(e -> Mono.just(new ApiResponse<>(500, "Erro ao atualizar laboratório: " + e.getMessage(), null)));
                            }
                        }
                    }
                    return Mono.just(apiResponse);
                });
    }
}