package com.cryptopaygo.exception;

import com.cryptopaygo.config.exception.RegisterInvalidException;
import com.cryptopaygo.config.exception.TokenInvalidException;
import com.cryptopaygo.config.exception.UserNotFoundException;
import com.cryptopaygo.dto.GeneralResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RegisterInvalidException.class)
    public ResponseEntity<GeneralResponseDTO> handleRegisterInvalidException(RegisterInvalidException e) {
        // Retorna uma resposta com status 409 (CONFLICT) e a mensagem de erro
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new GeneralResponseDTO(e.getMessage(), false));
    }

    // Tratar exceção quando login falhar
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<GeneralResponseDTO> handleBadCredentials() {
        // Retorna uma resposta com status 401 (UNAUTHORIZED) e a mensagem de erro
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new GeneralResponseDTO("Non-existent user or invalid password", false));
    }

    // Tratar exceção quando o Usuário não for encontrado
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<GeneralResponseDTO> handleUserNotFound(UserNotFoundException e) {
        // Retorna uma resposta com status 404 (NOT FOUND) e a mensagem de erro
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new GeneralResponseDTO(e.getMessage(), false));
    }

    // Tratar exceção quando token for inválido ou expirado
    @ExceptionHandler(TokenInvalidException.class)
    public ResponseEntity<GeneralResponseDTO> handleTokenInvalidException(TokenInvalidException e) {
        // Retorna uma resposta com status 401 (UNAUTHORIZED) e a mensagem de erro
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new GeneralResponseDTO(e.getMessage(), false));
    }

    // Tratar exceção quando o ID fornecido não pode ser convertido
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<GeneralResponseDTO> handleInvalidId() {
        // Retorna uma resposta com status 400 (BAD REQUEST) e a mensagem de erro
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new GeneralResponseDTO("Invalid ID format", false));
    }

    // Exceção para erros genéricos
    @ExceptionHandler(Exception.class)
    public ResponseEntity<GeneralResponseDTO> handleGenericException() {
        // Retorna uma resposta com status 500 (INTERNAL SERVER ERROR) e a mensagem de erro
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new GeneralResponseDTO("Unexpected error occurred", false));
    }
}
