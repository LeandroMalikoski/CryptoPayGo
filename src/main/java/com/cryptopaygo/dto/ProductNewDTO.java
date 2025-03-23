package com.cryptopaygo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductNewDTO(

        @NotBlank(message = "Product name is required")
        @Size(min = 3, max = 50, message = "Product name must be between 3 and 50 characters")
        String name,

        @NotBlank(message = "Product description is required")
        @Size(max = 255, message = "Product description has max 255 characters")
        String description,

        @NotBlank(message = "Product category is required")
        @Size(max = 100, message = "Product category has max 100 characters")
        String category,

        @NotNull(message = "Product price is required")
        Double price
) {
}
