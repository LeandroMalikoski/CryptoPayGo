package com.cryptopaygo.dto;

import com.cryptopaygo.enums.MovementType;

public record StockMovementDTO(

        Long productId,

        Integer quantity,

        MovementType movementType,

        String currencyCoin,

        String convertCoin
) {
}