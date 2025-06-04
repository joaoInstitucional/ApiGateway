package br.com.ucsal.apigateway.controllers;

import br.com.ucsal.apigateway.clients.AuthServiceClient;
import br.com.ucsal.apigateway.dtos.ApiResponse;
import br.com.ucsal.apigateway.dtos.LoginDTO;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class AuthGatewayController {

    private final AuthServiceClient authServiceClient;

    public AuthGatewayController(AuthServiceClient authServiceClient) {
        this.authServiceClient = authServiceClient;
    }

    @PostMapping("/login")
    public Mono<ApiResponse> login(@RequestBody LoginDTO dto) {
        return authServiceClient.login(dto);
    }
}