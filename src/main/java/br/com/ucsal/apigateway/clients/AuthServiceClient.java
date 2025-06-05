package br.com.ucsal.apigateway.clients;

import br.com.ucsal.apigateway.dtos.ApiResponse;
import br.com.ucsal.apigateway.dtos.LoginDTO;
import br.com.ucsal.apigateway.dtos.RegisterDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AuthServiceClient {

    // Ferramenta para requisições HTTP que o spring oferece, não-bloqueante
    private final WebClient webClient;
    private final String authServiceUrl;
    private final String authServicePath;

    //Construtor da classe instanciando o webclient, a url e o caminho do serviço recuprando do application.properties
    public AuthServiceClient(WebClient webClient,
                             @Value("${auth-service.url}") String authServiceUrl,
                             @Value("${auth-service.path}") String authServicePath) {
        this.webClient = webClient;
        this.authServiceUrl = authServiceUrl;
        this.authServicePath = authServicePath;
    }

    //Mono é um tipo de objeto da Programação Reativa, fornecido pela biblioteca Project Reactor (usada no Spring WebFlux).
    //Estou usando ele por ser não-bloqueante e comporta-se semelhante a uma promise do javascript
    public Mono<ApiResponse> login(LoginDTO dto) {
        return webClient.post()
                .uri(authServiceUrl + authServicePath + "/login")
                .bodyValue(dto)
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.UNAUTHORIZED)) {
                        return response.bodyToMono(ApiResponse.class);
                    }
                    return response.bodyToMono(ApiResponse.class);
                })
                .onErrorResume(e -> Mono.just(
                        new ApiResponse<>(500, "Erro ao comunicar com o serviço de autenticação", null)
                ));
    }

    public Mono<ApiResponse> criarCredential(RegisterDTO registerDTO) {
        return webClient.post()
                .uri(authServiceUrl + authServicePath + "/register")
                .bodyValue(registerDTO)
                .retrieve()
                .bodyToMono(ApiResponse.class)
                .onErrorResume(e -> Mono.just(
                        new ApiResponse<>(500, "Erro ao criar credencial", null)
                ));
    }
}