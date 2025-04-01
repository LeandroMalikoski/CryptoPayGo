package com.cryptopaygo.dto;

import com.cryptopaygo.enums.MovementType;

import java.time.LocalDateTime;

public record StockResponseDTO(
        Long id,

        Long productId,

        Long userId,

        Integer quantity,

        MovementType movementType,

        String currencyType,

        Double currencyPaid,

        Double purchasePrice,

        LocalDateTime movementDate
) {
}