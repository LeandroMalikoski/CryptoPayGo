package com.cryptopaygo.exception;

import com.cryptopaygo.config.exception.*;
import com.cryptopaygo.dto.GeneralResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Tratar quando um registro do estoque não for encontrado
    @ExceptionHandler(StockRegisterNotFound.class)
    public ResponseEntity<GeneralResponseDTO> stockRegisterNotFound(StockRegisterNotFound e) {
        // Retorna uma resposta com status 404 (NOT FOUND) e a mensagem de erro
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new GeneralResponseDTO(e.getMessage(), false));
    }

    // Tratar exceção quando ocorrer erro no registro de entrada ou saida do estoque
    @ExceptionHandler(MovementErrorException.class)
    public ResponseEntity<GeneralResponseDTO> movementErrorException(MovementErrorException e) {
        // Retorna uma resposta com status 400 (BAD REQUEST) e a mensagem de erro
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new GeneralResponseDTO(e.getMessage(), false));
    }

    // Tratar exceção quando o request(json) enviado estiver com erros do validation
    @ExceptionHandler(RequestInvalidException.class)
    public ResponseEntity<GeneralResponseDTO> handleRequestInvalidException(RequestInvalidException e) {
        // Retorna uma resposta com status 400 (BAD REQUEST) e a mensagem de erro
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new GeneralResponseDTO(e.getMessage(), false));
    }

    @ExceptionHandler(RegisterInvalidException.class)
    public ResponseEntity<GeneralResponseDTO> handleRegisterInvalidException(RegisterInvalidException e) {
        // Retorna uma resposta com status 409 (CONFLICT) e a mensagem de erro
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new GeneralResponseDTO(e.getMessage(), false));
    }

    // Tratar exceção quando login falhar
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<GeneralResponseDTO> handleBadCredentialsException() {
        // Retorna uma resposta com status 401 (UNAUTHORIZED) e a mensagem de erro
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new GeneralResponseDTO("Non-existent user or invalid password", false));
    }

    // Tratar exceção quando o Usuário não for encontrado
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<GeneralResponseDTO> handleUserNotFoundException(UserNotFoundException e) {
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
    public ResponseEntity<GeneralResponseDTO> handleInvalidIdException() {
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

    // Exceção para Produto não encontrado
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<GeneralResponseDTO> handleProductNotFoundException(ProductNotFoundException e) {
        // Retorna uma resposta com status 404 (NOT FOUND) e a mensagem de erro
        return ResponseEntity.status((HttpStatus.NOT_FOUND)).body(new GeneralResponseDTO(e.getMessage(), false));
    }
}