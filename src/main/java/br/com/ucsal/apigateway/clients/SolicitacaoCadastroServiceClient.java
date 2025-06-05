package br.com.ucsal.apigateway.clients;

import br.com.ucsal.apigateway.dtos.AtualizaStatusDTO;
import br.com.ucsal.apigateway.dtos.SolicitacaoCadastroDTO;
import br.com.ucsal.apigateway.dtos.SolicitacaoCadastroSoftware;
import br.com.ucsal.apigateway.dtos.ApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SolicitacaoCadastroServiceClient {

    private final WebClient webClient;
    private final String solicitacaoServiceUrl;
    private final String solicitacaoServicePath;

    public SolicitacaoCadastroServiceClient(WebClient webClient,
                                            @Value("${solicitacaoCadastro-service.url}") String solicitacaoServiceUrl,
                                            @Value("${solicitacaoCadastro-service.path}") String solicitacaoServicePath) {
        this.webClient = webClient;
        this.solicitacaoServiceUrl = solicitacaoServiceUrl;
        this.solicitacaoServicePath = solicitacaoServicePath;
    }

    public Mono<ApiResponse> criarSolicitacao(SolicitacaoCadastroDTO dto) {
        return webClient.post()
                .uri(solicitacaoServiceUrl + solicitacaoServicePath)
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(ApiResponse.class)
                .onErrorResume(e -> Mono.empty());
    }

    public Flux<ApiResponse> listarSolicitacoes() {
        return webClient.get()
                .uri(solicitacaoServiceUrl + solicitacaoServicePath)
                .retrieve()
                .bodyToFlux(ApiResponse.class)
                .onErrorResume(e -> Flux.empty());
    }

    public Mono<ApiResponse> buscarPorId(Long id) {
        return webClient.get()
                .uri(solicitacaoServiceUrl + solicitacaoServicePath + "/" + id)
                .retrieve()
                .bodyToMono(ApiResponse.class)
                .onErrorResume(e -> Mono.empty());
    }

    public Mono<ApiResponse> atualizarStatus(Long id, AtualizaStatusDTO dto) {
        return webClient.put()
                .uri(solicitacaoServiceUrl + solicitacaoServicePath + "/" + id + "/status")
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(ApiResponse.class)
                .onErrorResume(e -> Mono.empty());
    }
}
