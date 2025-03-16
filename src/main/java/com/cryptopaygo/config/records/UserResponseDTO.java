package com.cryptopaygo.config.records;

public record UserResponseDTO(Long id, String name, String email,String role) {
    public UserResponseDTO(UserResponseDTO user) {
        this(user.id(), user.name(), user.email(), user.role());
    }
}
