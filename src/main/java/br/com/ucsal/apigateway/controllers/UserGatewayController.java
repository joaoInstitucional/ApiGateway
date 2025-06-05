package br.com.ucsal.apigateway.controllers;

import br.com.ucsal.apigateway.clients.UserServiceClient;
import br.com.ucsal.apigateway.clients.AuthServiceClient;
import br.com.ucsal.apigateway.dtos.ApiResponse;
import br.com.ucsal.apigateway.dtos.RegisterDTO;
import br.com.ucsal.apigateway.dtos.UsuarioDTO;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/usuarios")
public class UserGatewayController {

    private final UserServiceClient userServiceClient;
    private final AuthServiceClient authServiceClient;

    public UserGatewayController(UserServiceClient userServiceClient,
                                 AuthServiceClient authServiceClient) {
        this.userServiceClient = userServiceClient;
        this.authServiceClient = authServiceClient;
    }

    @GetMapping
    public Flux<UsuarioDTO> listarUsuarios() {
        return userServiceClient.listarUsuarios();
    }

    @GetMapping("/{id}")
    public Mono<UsuarioDTO> buscarUsuario(@PathVariable Long id) {
        return userServiceClient.buscarUsuario(id);
    }

    @PostMapping
    public Mono<ApiResponse<UsuarioDTO>> criarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        return userServiceClient.criarUsuario(usuarioDTO)
                .flatMap(usuarioCriado -> {
                    // Criar DTO para AuthService usando a senha original enviada
                    RegisterDTO registerDTO = new RegisterDTO(
                            usuarioCriado.email(),
                            usuarioDTO.senha(), // <-- aqui está a senha correta
                            usuarioCriado.idUsuario()
                    );
                    return authServiceClient.criarCredential(registerDTO)
                            .map(response -> new ApiResponse<>(200, "Usuário e credencial criados com sucesso", usuarioCriado));
                })
                .onErrorResume(e -> Mono.just(
                        new ApiResponse<>(500, "Erro ao criar usuário ou credencial: " + e.getMessage(), null)
                ));
    }

    @PutMapping("/{id}")
    public Mono<UsuarioDTO> atualizarUsuario(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
        return userServiceClient.atualizarUsuario(id, usuarioDTO);
    }

    @DeleteMapping("/{id}")
    public Mono<String> deletarUsuario(@PathVariable Long id) {
        return userServiceClient.deletarUsuario(id);
    }
}
