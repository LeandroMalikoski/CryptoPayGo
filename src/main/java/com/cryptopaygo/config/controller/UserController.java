package com.cryptopaygo.config.controller;

import com.cryptopaygo.config.records.UserRegisterRequestDTO;
import com.cryptopaygo.dto.ErrorResponseDTO;
import com.cryptopaygo.config.records.UserResponseDTO;
import com.cryptopaygo.config.service.UserService;
import jakarta.validation.Valid;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // Construtor para injeção de dependências
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ErrorResponseDTO> registerUser(@Valid @RequestBody UserRegisterRequestDTO dto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDTO(errorMessage, false));
        }
        try {

            if (userService.existsByEmail(dto.email())) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO("Email already exists", false));
            }
            userService.registerUser(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ErrorResponseDTO("User registered successfully", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO("An error occurred during registration", false));
        }
    }

    //Procura por um usuário pelo seu id
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserDetails(@PathVariable Long id) {

        //Procura pelo id informado na url
        var user = userService.getUserById(id);

        //Transforma em um novo UserResponseDTO
        var userResponseDTO = new UserResponseDTO(user);

        //Retorna status 200 (OK) com o UserResponseDTO criado
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDTO);

    }
}