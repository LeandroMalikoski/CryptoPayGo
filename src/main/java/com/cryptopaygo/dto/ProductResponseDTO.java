package com.cryptopaygo.dto;

public record ProductResponseDTO(

        Long id,

        String name,

        String description,

        String category,

        Double price,

        Integer quantity
) {
    public ProductResponseDTO(ProductResponseDTO product) {
        this(product.id(), product.name(), product.description(), product.category(), product.price(), product.quantity());
    }
}