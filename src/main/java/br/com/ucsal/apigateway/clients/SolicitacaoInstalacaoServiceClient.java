package br.com.ucsal.apigateway.clients;

import br.com.ucsal.apigateway.dtos.AtualizaStatusDTO;
import br.com.ucsal.apigateway.dtos.SolicitacaoInstalacaoDTO;
import br.com.ucsal.apigateway.dtos.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SolicitacaoInstalacaoServiceClient {

    private final WebClient webClient;
    private final String solicitacaoInstalacaoServiceUrl;
    private final String solicitacaoInstalacaoServicePath;

    public SolicitacaoInstalacaoServiceClient(WebClient webClient,
                                              @Value("${solicitacaoInstalacao-service.url}") String solicitacaoInstalacaoServiceUrl,
                                              @Value("${solicitacaoInstalacao-service.path}") String solicitacaoInstalacaoServicePath) {
        this.webClient = webClient;
        this.solicitacaoInstalacaoServiceUrl = solicitacaoInstalacaoServiceUrl;
        this.solicitacaoInstalacaoServicePath = solicitacaoInstalacaoServicePath;
    }

    public Mono<ApiResponse> criarSolicitacaoInstalacao(SolicitacaoInstalacaoDTO dto) {
        return webClient.post()
                .uri(solicitacaoInstalacaoServiceUrl + solicitacaoInstalacaoServicePath)
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(ApiResponse.class)
                .onErrorResume(e -> Mono.empty());
    }

    public Flux<ApiResponse> listarSolicitacoesInstalacao() {
        return webClient.get()
                .uri(solicitacaoInstalacaoServiceUrl + solicitacaoInstalacaoServicePath)
                .retrieve()
                .bodyToFlux(ApiResponse.class)
                .onErrorResume(e -> Flux.empty());
    }

    public Mono<ApiResponse> buscarSolicitacaoInstalacaoPorId(Long id) {
        return webClient.get()
                .uri(solicitacaoInstalacaoServiceUrl + solicitacaoInstalacaoServicePath + "/" + id)
                .retrieve()
                .bodyToMono(ApiResponse.class)
                .onErrorResume(e -> Mono.empty());
    }

    public Mono<ApiResponse> atualizarStatusInstalacao(Long id, AtualizaStatusDTO dto) {
        return webClient.put()
                .uri(solicitacaoInstalacaoServiceUrl + solicitacaoInstalacaoServicePath + "/" + id + "/status")
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(ApiResponse.class)
                .onErrorResume(e -> Mono.empty());
    }
}