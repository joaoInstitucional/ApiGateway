package br.com.ucsal.apigateway.controllers;

import br.com.ucsal.apigateway.clients.SoftwareServiceClient;
import br.com.ucsal.apigateway.dtos.ApiResponse;
import br.com.ucsal.apigateway.dtos.SoftwareDTO;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/softwares")
public class SoftwareGatewayController {

    private final SoftwareServiceClient softwareServiceClient;

    public SoftwareGatewayController(SoftwareServiceClient softwareServiceClient) {
        this.softwareServiceClient = softwareServiceClient;
    }

    @GetMapping
    public Flux<SoftwareDTO> listarSoftwares() {
        return softwareServiceClient.listarSoftwares();
    }

    @PostMapping
    public Mono<ApiResponse<SoftwareDTO>> criarSoftware(@RequestBody SoftwareDTO softwareDTO) {
        return softwareServiceClient.criarSoftware(softwareDTO)
                .map(softwareCriado -> new ApiResponse<>(200, "Software criado com sucesso", softwareCriado))
                .onErrorResume(e -> Mono.just(
                        new ApiResponse<>(500, "Erro ao criar software: " + e.getMessage(), null)
                ));
    }

    @PutMapping("/{id}")
    public Mono<ApiResponse<SoftwareDTO>> atualizarSoftware(@PathVariable Long id, @RequestBody SoftwareDTO softwareDTO) {
        return softwareServiceClient.atualizarSoftware(id, softwareDTO)
                .map(softwareAtualizado -> new ApiResponse<>(200, "Software atualizado com sucesso", softwareAtualizado))
                .onErrorResume(e -> Mono.just(
                        new ApiResponse<>(500, "Erro ao atualizar software: " + e.getMessage(), null)
                ));
    }

    @DeleteMapping("/{id}")
    public Mono<ApiResponse<String>> deletarSoftware(@PathVariable Long id) {
        return softwareServiceClient.deletarSoftware(id)
                .map(resposta -> new ApiResponse<>(200, "Software deletado com sucesso", resposta))
                .onErrorResume(e -> Mono.just(
                        new ApiResponse<>(500, "Erro ao deletar software: " + e.getMessage(), null)
                ));
    }
}
