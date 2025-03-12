package com.cryptopaygo.config.controller;

import com.cryptopaygo.config.records.UserRegisterRequestDTO;
import com.cryptopaygo.config.records.UserResponseDTO;
import com.cryptopaygo.config.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
