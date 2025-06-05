package br.com.ucsal.apigateway.clients;

import br.com.ucsal.apigateway.dtos.AtualizaStatusDTO;
import br.com.ucsal.apigateway.dtos.LaboratorioDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class LaboratorioServiceClient {

    private final WebClient webClient;
    private final String laboratorioServiceUrl;
    private final String laboratorioServicePath;

    public LaboratorioServiceClient(WebClient webClient,
                                    @Value("${laboratorio-service.url}") String laboratorioServiceUrl,
                                    @Value("${laboratorio-service.path}") String laboratorioServicePath) {
        this.webClient = webClient;
        this.laboratorioServiceUrl = laboratorioServiceUrl;
        this.laboratorioServicePath = laboratorioServicePath;
    }

    public Mono<LaboratorioDTO> criarLaboratorio(LaboratorioDTO dto) {
        return webClient.post()
                .uri(laboratorioServiceUrl + laboratorioServicePath)
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(LaboratorioDTO.class)
                .onErrorResume(e -> Mono.empty());
    }

    public Flux<LaboratorioDTO> listarLaboratorios() {
        return webClient.get()
                .uri(laboratorioServiceUrl + laboratorioServicePath)
                .retrieve()
                .bodyToFlux(LaboratorioDTO.class)
                .onErrorResume(e -> Flux.empty());
    }

    public Mono<LaboratorioDTO> buscarPorId(Long id) {
        return webClient.get()
                .uri(laboratorioServiceUrl + laboratorioServicePath + "/" + id)
                .retrieve()
                .bodyToMono(LaboratorioDTO.class)
                .onErrorResume(e -> Mono.empty());
    }

    public Mono<LaboratorioDTO> atualizarStatus(Long id, AtualizaStatusDTO dto) {
        return webClient.patch()
                .uri(laboratorioServiceUrl + laboratorioServicePath + "/" + id + "/status")
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(LaboratorioDTO.class)
                .onErrorResume(e -> Mono.empty());
    }
}
