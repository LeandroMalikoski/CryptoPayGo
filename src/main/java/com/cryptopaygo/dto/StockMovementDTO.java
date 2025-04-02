package com.cryptopaygo.dto;

import com.cryptopaygo.enums.MovementType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

public record StockMovementDTO(

        @NotNull(message = "Product id is required")
        Long productId,

        @NotNull(message = "Quantity is required")
        Integer quantity,

        @JsonDeserialize(using = MovementTypeDeserializer.class)
        @NotNull(message = "It needs to be ENTRY or EXIT")
        MovementType movementType,

        String currencyCoin,

        String convertCoin,

        Double purchasePrice

) {
    @AssertTrue(message = "Purchase price must be greater than zero")
    public boolean isPurchasePriceValid() {
        if (movementType == MovementType.ENTRY) {
            return purchasePrice != null && purchasePrice > 0;
        }
        return true; // Se for EXIT, não precisa validar purchasePrice
    }
}