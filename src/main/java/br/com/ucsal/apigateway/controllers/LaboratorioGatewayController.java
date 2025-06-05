package br.com.ucsal.apigateway.controllers;

import br.com.ucsal.apigateway.clients.LaboratorioServiceClient;
import br.com.ucsal.apigateway.dtos.ApiResponse;
import br.com.ucsal.apigateway.dtos.AtualizaStatusDTO;
import br.com.ucsal.apigateway.dtos.LaboratorioDTO;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/laboratorios")
public class LaboratorioGatewayController {

    private final LaboratorioServiceClient laboratorioServiceClient;

    public LaboratorioGatewayController(LaboratorioServiceClient laboratorioServiceClient) {
        this.laboratorioServiceClient = laboratorioServiceClient;
    }

    @PostMapping
    public Mono<ApiResponse<LaboratorioDTO>> criarLaboratorio(@RequestBody LaboratorioDTO dto) {
        return laboratorioServiceClient.criarLaboratorio(dto)
                .map(labCriado -> new ApiResponse<>(200, "Laboratório criado com sucesso", labCriado))
                .onErrorResume(e -> Mono.just(
                        new ApiResponse<>(500, "Erro ao criar laboratório: " + e.getMessage(), null)
                ));
    }

    @GetMapping
    public Flux<LaboratorioDTO> listarLaboratorios() {
        return laboratorioServiceClient.listarLaboratorios();
    }

    @GetMapping("/{id}")
    public Mono<LaboratorioDTO> buscarLaboratorioPorId(@PathVariable Long id) {
        return laboratorioServiceClient.buscarPorId(id);
    }

    @PatchMapping("/{id}/status")
    public Mono<ApiResponse<LaboratorioDTO>> atualizarStatus(@PathVariable Long id, @RequestBody AtualizaStatusDTO dto) {
        return laboratorioServiceClient.atualizarStatus(id, dto)
                .map(labAtualizado -> new ApiResponse<>(200, "Status atualizado com sucesso", labAtualizado))
                .onErrorResume(e -> Mono.just(
                        new ApiResponse<>(500, "Erro ao atualizar status: " + e.getMessage(), null)
                ));
    }
}
