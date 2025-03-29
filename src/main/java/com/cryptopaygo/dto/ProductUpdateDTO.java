package com.cryptopaygo.dto;

import jakarta.validation.constraints.Size;

public record ProductUpdateDTO(

        @Size(min = 3, max = 50, message = "Product name must be between 3 and 50 characters")
        String name,

        @Size(max = 255, message = "Product description has max 255 characters")
        String description,

        @Size(max = 100, message = "Product category has max 100 characters")
        String category,

        Double price,

        Integer quantity
) {
}
