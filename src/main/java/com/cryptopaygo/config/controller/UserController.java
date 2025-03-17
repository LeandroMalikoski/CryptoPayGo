package com.cryptopaygo.config.controller;

import com.cryptopaygo.config.exception.RegisterInvalidException;
import com.cryptopaygo.config.records.UserRegisterRequestDTO;
import com.cryptopaygo.dto.GeneralResponseDTO;
import com.cryptopaygo.config.records.UserResponseDTO;
import com.cryptopaygo.config.service.UserService;
import jakarta.validation.Valid;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<GeneralResponseDTO> registerUser(@Valid @RequestBody UserRegisterRequestDTO dto, BindingResult bindingResult) {

        // Valida os dados da requisição e retorna erros, se houver
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", "));

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GeneralResponseDTO(errorMessage, false));
        }
        // Verifica se o e-mail já está cadastrado
        if (userService.existsByEmail(dto.email())) {
            throw new RegisterInvalidException("Email address already in use");
        }

        // Registra o usuário novo no banco de dados
        userService.registerUser(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(new GeneralResponseDTO("User registered successfully", true));
    }

    // Lista todos os usuários
    @GetMapping("/list")
    public ResponseEntity<List<UserResponseDTO>> getUsers() {

        // Retorna todos os usuários cadastrados
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserDetails(@PathVariable Long id) {

        // Busca o usuário pelo ID informado
        var user = userService.getUserById(id);

        return ResponseEntity.status(HttpStatus.OK).body(new UserResponseDTO(user));
    }
}