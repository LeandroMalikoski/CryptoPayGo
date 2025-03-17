package com.cryptopaygo.config.controller;

import com.cryptopaygo.config.records.UserRegisterRequestDTO;
import com.cryptopaygo.config.records.UserUpdateDTO;
import com.cryptopaygo.dto.GeneralResponseDTO;
import com.cryptopaygo.config.records.UserResponseDTO;
import com.cryptopaygo.config.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<GeneralResponseDTO> registerUser(@Valid @RequestBody UserRegisterRequestDTO dto, BindingResult bindingResult) {

        // Registra o usuário novo no banco de dados
        userService.registerUser(dto, bindingResult);

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

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUserDetails(@PathVariable Long id, @RequestBody @Valid UserUpdateDTO dto, BindingResult bindingResult) {

        var user = userService.updateUserDetails(id, dto, bindingResult);

        return ResponseEntity.ok(new UserResponseDTO(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponseDTO> deleteUser(@PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseEntity.status(HttpStatus.OK).body(new GeneralResponseDTO("User deleted successfully", true));
    }
}