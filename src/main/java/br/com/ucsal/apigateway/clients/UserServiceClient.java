package br.com.ucsal.apigateway.clients;

import br.com.ucsal.apigateway.dtos.ApiResponse;
import br.com.ucsal.apigateway.dtos.UsuarioDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@Service
public class UserServiceClient {

    private final WebClient webClient;
    private final String userServiceUrl;
    private final String userServicePath;

    public UserServiceClient(WebClient webClient,
                             @Value("${user-service.url}") String userServiceUrl,
                             @Value("${user-service.path}") String userServicePath) {
        this.webClient = webClient;
        this.userServiceUrl = userServiceUrl;
        this.userServicePath = userServicePath;
    }

    public Flux<UsuarioDTO> listarUsuarios() {
        return webClient.get()
                .uri(userServiceUrl + userServicePath)
                .retrieve()
                .bodyToFlux(UsuarioDTO.class)
                .onErrorResume(e -> Flux.empty());
    }

    public Mono<UsuarioDTO> buscarUsuario(Long id) {
        return webClient.get()
                .uri(userServiceUrl + userServicePath + "/" + id)
                .retrieve()
                .bodyToMono(UsuarioDTO.class)
                .onErrorResume(e -> Mono.empty());
    }

    public Mono<UsuarioDTO> criarUsuario(UsuarioDTO usuarioDTO) {
        return webClient.post()
                .uri(userServiceUrl + userServicePath)
                .bodyValue(usuarioDTO)
                .retrieve()
                .bodyToMono(UsuarioDTO.class)
                .onErrorResume(e -> Mono.empty());
    }

    public Mono<UsuarioDTO> atualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        return webClient.put()
                .uri(userServiceUrl + userServicePath + "/" + id)
                .bodyValue(usuarioDTO)
                .retrieve()
                .bodyToMono(UsuarioDTO.class)
                .onErrorResume(e -> Mono.empty());
    }

    public Mono<String> deletarUsuario(Long id) {
        return webClient.delete()
                .uri(userServiceUrl + userServicePath + "/" + id)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(e -> Mono.just("Erro ao deletar usuário"));
    }
}
