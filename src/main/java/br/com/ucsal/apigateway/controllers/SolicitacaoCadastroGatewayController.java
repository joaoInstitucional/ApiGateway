package br.com.ucsal.apigateway.controllers;

import br.com.ucsal.apigateway.clients.SolicitacaoCadastroServiceClient;
import br.com.ucsal.apigateway.clients.SoftwareServiceClient;
import br.com.ucsal.apigateway.dtos.AtualizaStatusDTO;
import br.com.ucsal.apigateway.dtos.SoftwareDTO;
import br.com.ucsal.apigateway.dtos.SolicitacaoCadastroDTO;
import br.com.ucsal.apigateway.dtos.ApiResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;

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
    public Mono<ApiResponse<Object>> atualizarStatus(@PathVariable Long id, @RequestBody AtualizaStatusDTO dto) {
        return solicitacaoClient.atualizarStatus(id, dto)
                .flatMap(apiResponse -> {
                    // Aqui pegamos a solicitacao atualizada
                    Object solicitacaoData = apiResponse.data();

                    // Convertemos para Map, já que API genericamente devolve como LinkedHashMap
                    Map<String, Object> solicitacaoMap = (Map<String, Object>) solicitacaoData;

                    String status = (String) solicitacaoMap.get("status");

                    if ("FINALIZADO".equalsIgnoreCase(status)) {
                        // Monta SoftwareDTO com base na solicitacaoMap
                        SoftwareDTO softwareDTO = new SoftwareDTO(
                                null,
                                (String) solicitacaoMap.get("nome"),
                                (String) solicitacaoMap.get("versao"),
                                (String) solicitacaoMap.get("linkInstalacao"),
                                Boolean.TRUE.equals(solicitacaoMap.get("proprietario")),
                                true,
                                LocalDateTime.now()
                        );

                        return softwareClient.criarSoftware(softwareDTO)
                                .map(softwareCriado -> new ApiResponse<>(200, "Status atualizado para FINALIZADO e software criado com sucesso", apiResponse.data()))
                                .onErrorResume(e -> Mono.just(
                                        new ApiResponse<>(500, "Status atualizado, mas erro ao criar software: " + e.getMessage(), apiResponse.data())
                                ));
                    } else {
                        return Mono.just(new ApiResponse<>(200, "Status atualizado com sucesso", apiResponse.data()));
                    }
                })
                .onErrorResume(e -> Mono.just(
                        new ApiResponse<>(500, "Erro ao atualizar status: " + e.getMessage(), null)
                ));
    }
}
