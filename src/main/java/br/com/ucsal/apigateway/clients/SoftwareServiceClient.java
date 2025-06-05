package br.com.ucsal.apigateway.clients;

import br.com.ucsal.apigateway.dtos.SoftwareDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SoftwareServiceClient {

    private final WebClient webClient;
    private final String softwareServiceUrl;
    private final String softwareServicePath;

    public SoftwareServiceClient(WebClient webClient,
                                 @Value("${software-service.url}") String softwareServiceUrl,
                                 @Value("${software-service.path}") String softwareServicePath) {
        this.webClient = webClient;
        this.softwareServiceUrl = softwareServiceUrl;
        this.softwareServicePath = softwareServicePath;
    }

    public Flux<SoftwareDTO> listarSoftwares() {
        return webClient.get()
                .uri(softwareServiceUrl + softwareServicePath)
                .retrieve()
                .bodyToFlux(SoftwareDTO.class)
                .onErrorResume(e -> Flux.empty());
    }

    public Mono<SoftwareDTO> criarSoftware(SoftwareDTO softwareDTO) {
        return webClient.post()
                .uri(softwareServiceUrl + softwareServicePath)
                .bodyValue(softwareDTO)
                .retrieve()
                .bodyToMono(SoftwareDTO.class)
                .onErrorResume(e -> Mono.empty());
    }

    public Mono<SoftwareDTO> atualizarSoftware(Long id, SoftwareDTO softwareDTO) {
        return webClient.put()
                .uri(softwareServiceUrl + softwareServicePath + "/" + id)
                .bodyValue(softwareDTO)
                .retrieve()
                .bodyToMono(SoftwareDTO.class)
                .onErrorResume(e -> Mono.empty());
    }

    public Mono<String> deletarSoftware(Long id) {
        return webClient.delete()
                .uri(softwareServiceUrl + softwareServicePath + "/" + id)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(e -> Mono.just("Erro ao deletar software"));
    }
}
