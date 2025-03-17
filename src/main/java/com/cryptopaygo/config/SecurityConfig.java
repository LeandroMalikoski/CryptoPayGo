package com.cryptopaygo.config;

import com.cryptopaygo.config.filter.SecurityFilter;
import com.cryptopaygo.config.service.UserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final SecurityFilter securityFilter;

    // Construtor para injeção de dependências
    public SecurityConfig(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http

                // Desabilita a proteção contra CSRF (Cross-Site Request Forgery)
                .csrf(AbstractHttpConfigurer::disable)

                // Define a política de gerenciamento de sessão como STATELESS (sem armazenamento de sessão no servidor)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Configuração das permissões de acesso para diferentes endpoints
                .authorizeHttpRequests(auth -> auth

                        // Permite acesso público à rota raiz ("/")
                        .requestMatchers(HttpMethod.GET, "/").permitAll()

                        // Permite que qualquer usuário acesse as rotas de login e registro
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()

                        // Permite acesso público à documentação da API gerada pelo Swagger
                        .requestMatchers(HttpMethod.POST, "/users/register").permitAll()

                        // Qualquer outra requisição precisa estar autenticada
                        .requestMatchers(HttpMethod.GET, "/v3/api-docs", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui/index.html").permitAll()

                        // Quaisquer outras deverão ser autenticadas
                        .anyRequest().authenticated()
                )
                // Define o filtro de segurança personalizado (securityFilter) antes do padrão do Spring Boot
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // Autentica usuários com base nos dados fornecidos
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {

        // Cria um provedor de autenticação
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        // Define o serviço responsável por carregar os usuários do banco de dados
        authProvider.setUserDetailsService(userDetailsService);

        // Define o codificador de senhas para serem verificadas corretamente
        authProvider.setPasswordEncoder(passwordEncoder);

        // Retorna um AuthenticationManager configurado com o provedor de autenticação criado
        return new ProviderManager(authProvider);
    }

    // Codificador de senha que utiliza o algoritmo BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}