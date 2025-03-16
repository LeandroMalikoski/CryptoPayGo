package com.cryptopaygo.config.controller;

import com.cryptopaygo.config.entity.User;
import com.cryptopaygo.config.records.AuthResponseDTO;
import com.cryptopaygo.config.records.LoginRequestDTO;
import com.cryptopaygo.config.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    // Construtor para injeção de dependências
    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO dto) {

        // Criação de um token de autenticação usando e-mail e senha fornecidos no DTO
        var authenticationToken = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());

        // Tentativa de autenticação do usuário
        var authentication = authenticationManager.authenticate(authenticationToken);

        // Recupera o usuário autenticado a partir do principal da autenticação
        var user = (User) authentication.getPrincipal();

        // Gera um tokenJWT para o usuário autenticado
        var tokenJwt = tokenService.generateToken(user.getEmail());

        // Retorna a resposta com o token gerado
        return ResponseEntity.status(HttpStatus.OK).body(new AuthResponseDTO(tokenJwt));
    }
}