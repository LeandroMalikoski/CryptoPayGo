package com.cryptopaygo.config.records;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateDTO(
                            @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
                            String name,

                            @Size(min = 8, message = "Password must be at least 8 characters long")
                            String password,

                            @Email(message = "Invalid email format")
                            String email,

                            String role) {
}
