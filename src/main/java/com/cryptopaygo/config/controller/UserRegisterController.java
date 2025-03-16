package com.cryptopaygo.config.controller;

import com.cryptopaygo.config.exception.UserNotFoundException;
import com.cryptopaygo.config.records.UserRegisterRequestDTO;
import com.cryptopaygo.config.records.UserRegisterResponseDTO;
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
public class UserRegisterController {

    private final UserService userService;

    public UserRegisterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponseDTO> registerUser(@Valid @RequestBody UserRegisterRequestDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new UserRegisterResponseDTO(errorMessage, false));
        }
        try {

            if (userService.existsByEmail(dto.email())) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UserRegisterResponseDTO("Email already exists", false));
            }
            userService.registerUser(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new UserRegisterResponseDTO("User registered successfully", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UserRegisterResponseDTO("An error occurred during registration", false));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserDetails(@PathVariable Long id) {
        try {
            var user = userService.getUserById(id);
            var userResponseDTO = new UserResponseDTO(user);
            return ResponseEntity.status(HttpStatus.OK).body(userResponseDTO);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}