package com.cryptopaygo.config.controller;

import com.cryptopaygo.config.exception.UserNotFoundException;
import com.cryptopaygo.config.records.UserRegisterRequestDTO;
import com.cryptopaygo.config.records.UserResponseDTO;
import com.cryptopaygo.config.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserRegisterController {

    private final UserService userService;

    public UserRegisterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserRegisterRequestDTO dto) {
        var userResponse = userService.registerUser(dto);
        return ResponseEntity.status(201).body(userResponse);
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