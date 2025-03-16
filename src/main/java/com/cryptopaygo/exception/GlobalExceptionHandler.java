package com.cryptopaygo.exception;

import com.cryptopaygo.config.exception.TokenInvalidException;
import com.cryptopaygo.config.exception.UserNotFoundException;
import com.cryptopaygo.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Tratar exceção quando login falhar
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadCredentials() {
        // Retorna uma resposta com status 401 (UNAUTHORIZED) e a mensagem de erro
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDTO("Non-existent user or invalid password", false));
    }

    // Tratar exceção quando o Usuário não for encontrado
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFound(UserNotFoundException ex) {
        // Retorna uma resposta com status 404 (NOT FOUND) e a mensagem de erro
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDTO(ex.getMessage(), false));
    }

    // Tratar exceção quando token for inválido ou expirado
    @ExceptionHandler(TokenInvalidException.class)
    public ResponseEntity<ErrorResponseDTO> handleTokenInvalidException(TokenInvalidException ex) {
        // Retorna uma resposta com status 401 (UNAUTHORIZED) e a mensagem de erro
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDTO(ex.getMessage(), false));
    }

    // Tratar exceção quando o ID fornecido não pode ser convertido
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidId() {
        // Retorna uma resposta com status 400 (BAD REQUEST) e a mensagem de erro
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDTO("Invalid ID format", false));
    }

    // Exceção para erros genéricos
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException() {
        // Retorna uma resposta com status 500 (INTERNAL SERVER ERROR) e a mensagem de erro
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDTO("Unexpected error occurred", false));
    }
}
