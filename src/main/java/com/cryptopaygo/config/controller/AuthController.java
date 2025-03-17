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

    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO dto) {

        // Cria um objeto de autenticação com as credenciais fornecidas
        var authenticationToken = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());

        // Autentica o usuário com base no e-mail e senha informados
        var authentication = authenticationManager.authenticate(authenticationToken);

        // Obtém o usuário autenticado a partir do contexto de autenticação
        var user = (User) authentication.getPrincipal();

        // Gera um token JWT para a sessão do usuário autenticado
        var tokenJwt = tokenService.generateToken(user.getEmail());

        // Retorna o token de acesso na resposta
        return ResponseEntity.status(HttpStatus.OK).body(new AuthResponseDTO(tokenJwt));
    }
}