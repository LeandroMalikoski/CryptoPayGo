package com.cryptopaygo.config.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret; // Chave secreta usada para assinar o token JWT
    @Value("${api.security.token.validity}")
    private int validity; // Tempo de validade do token

    // Gera um token JWT para o nome de usuário fornecido
    public String generateToken(String userName){
        try {
            var algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer("Crypto Pay Go")
                    .withSubject(userName)
                    .withExpiresAt(expirationDate())
                    .sign(algorithm);

        }catch (JWTCreationException exception){

            throw new RuntimeException("Error when generating token JWT ", exception);

        }
    }

    // Valida um token JWT e retorna o nome do usuário associado a ele
    public String validateToken(String tokenJWT){
        try{

            var algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                    .withIssuer("Crypto Pay Go")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        }catch (JWTVerificationException exception){
            throw new RuntimeException("Token JWT Invalid or Expired!", exception);
        }
    }

    private Instant expirationDate() {
        return Instant.now().plusSeconds(validity * 3600L);
    }
}