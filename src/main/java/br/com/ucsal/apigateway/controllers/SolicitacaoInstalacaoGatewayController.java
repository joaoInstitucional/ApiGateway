package br.com.ucsal.apigateway.controllers;

import br.com.ucsal.apigateway.clients.SolicitacaoInstalacaoServiceClient;
import br.com.ucsal.apigateway.dtos.AtualizaStatusDTO;
import br.com.ucsal.apigateway.dtos.SolicitacaoInstalacaoDTO;
import br.com.ucsal.apigateway.dtos.ApiResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/instalacoes")
public class SolicitacaoInstalacaoGatewayController {

    private final SolicitacaoInstalacaoServiceClient solicitacaoInstalacaoClient;

    public SolicitacaoInstalacaoGatewayController(SolicitacaoInstalacaoServiceClient solicitacaoInstalacaoClient) {
        this.solicitacaoInstalacaoClient = solicitacaoInstalacaoClient;
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
        return solicitacaoInstalacaoClient.atualizarStatusInstalacao(id, dto);
    }
}