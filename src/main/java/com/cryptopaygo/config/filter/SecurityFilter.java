package com.cryptopaygo.config.filter;

import com.cryptopaygo.config.exception.TokenInvalidException;
import com.cryptopaygo.config.service.TokenService;
import com.cryptopaygo.config.service.UserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;

    public SecurityFilter(TokenService tokenService, UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // Recupera o token JWT da requisição
            var tokenJwt = recoverToken(request);

            if (tokenJwt != null) {
                var userName = tokenService.validateToken(tokenJwt);
                var user = userDetailsService.loadUserByUsername(userName);
                if (user == null) {
                    throw new RuntimeException("User not found");
                }

                // Cria uma autenticação com o usuário recuperado
                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                // Define autenticação no contexto de segurança do Spring
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);

        } catch (TokenInvalidException ex) {
            sendErrorResponse(response, ex.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (RuntimeException ex) {
            sendErrorResponse(response, "Authentication error", HttpStatus.UNAUTHORIZED);
        }
    }

    // Envia uma resposta de erro com a mensagem e status definidos
    private void sendErrorResponse(HttpServletResponse response, String message, HttpStatus status) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");

        // Cria o corpo da resposta com a mensagem e o status
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("message", message);
        errorDetails.put("status", false);

        // Converte o mapa para JSON e envia como resposta
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(errorDetails));
    }

    // Recupera o token JWT da requisição no cabeçalho Authorization
    public String recoverToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            // Remove o prefixo "Bearer " e retorna o token
            return authorizationHeader.replace("Bearer ", "").trim();
        }
        return null;
    }
}