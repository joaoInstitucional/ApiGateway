package br.com.ucsal.apigateway.controllers;

import br.com.ucsal.apigateway.clients.SolicitacaoCadastroServiceClient;
import br.com.ucsal.apigateway.clients.SoftwareServiceClient;
import br.com.ucsal.apigateway.dtos.AtualizaStatusDTO;
import br.com.ucsal.apigateway.dtos.SolicitacaoCadastroDTO;
import br.com.ucsal.apigateway.dtos.ApiResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/solicitacoesCadastro")
public class SolicitacaoCadastroGatewayController {

    private final SolicitacaoCadastroServiceClient solicitacaoClient;
    private final SoftwareServiceClient softwareClient;

    public SolicitacaoCadastroGatewayController(SolicitacaoCadastroServiceClient solicitacaoClient,
                                                SoftwareServiceClient softwareClient) {
        this.solicitacaoClient = solicitacaoClient;
        this.softwareClient = softwareClient;
    }

    @PostMapping
    public Mono<ApiResponse> criarSolicitacao(@RequestBody SolicitacaoCadastroDTO dto) {
        return solicitacaoClient.criarSolicitacao(dto);
    }

    @GetMapping
    public Flux<ApiResponse> listarSolicitacoes() {
        return solicitacaoClient.listarSolicitacoes();
    }

    @GetMapping("/{id}")
    public Mono<ApiResponse> buscarSolicitacaoPorId(@PathVariable Long id) {
        return solicitacaoClient.buscarPorId(id);
    }

    @PutMapping("/{id}/status")
    public Mono<ApiResponse> atualizarStatus(@PathVariable Long id, @RequestBody AtualizaStatusDTO dto) {
        return solicitacaoClient.atualizarStatus(id, dto);
    }
}
