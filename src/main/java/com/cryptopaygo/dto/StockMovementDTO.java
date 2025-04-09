package com.cryptopaygo.dto;

import com.cryptopaygo.enums.MovementType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(hidden = true)
    public boolean isPurchasePriceValid() {
        if (movementType == MovementType.ENTRY) {
            return purchasePrice != null && purchasePrice > 0;
        }
        return true;
    }

    @AssertTrue(message = "Coin of currency and conversion currency should be null to an entry movement")
    @Schema(hidden = true)
    public boolean isEntryCurrencyValid() {
        if (movementType == MovementType.ENTRY) {
            return currencyCoin.trim().isEmpty() && convertCoin.trim().isEmpty();
        }
        return true;
    }

    @AssertTrue(message = "Currency and convert coin nulls or with incorrect crypto and currency symbol")
    @Schema(hidden = true)
    public boolean isExitCurrencyValid() {
        if (movementType == MovementType.EXIT) {
            return !currencyCoin.trim().isBlank() && !convertCoin.trim().isBlank();
        }
        return true;
    }
}