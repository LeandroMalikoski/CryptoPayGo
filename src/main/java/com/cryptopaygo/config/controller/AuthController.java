package com.cryptopaygo.config.controller;

import com.cryptopaygo.config.entity.User;
import com.cryptopaygo.config.records.AuthResponseDTO;
import com.cryptopaygo.config.records.LoginRequestDTO;
import com.cryptopaygo.config.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
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
        try {
            var authenticationToken = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());

            var authentication = authenticationManager.authenticate(authenticationToken);

            var user = (User) authentication.getPrincipal();

            var tokenJwt = tokenService.generateToken(user.getEmail());

            return ResponseEntity.status(HttpStatus.OK).body(new AuthResponseDTO(tokenJwt));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponseDTO("Authentication Failed"));
        }
    }
}