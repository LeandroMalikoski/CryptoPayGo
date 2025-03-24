package com.cryptopaygo.dto;

public record ProductResponseDTO(

        Long id,

        String name,

        String description,

        String category,

        Double price,

        Integer quantity
) {
}