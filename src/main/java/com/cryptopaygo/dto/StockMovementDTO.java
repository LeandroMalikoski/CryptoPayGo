package com.cryptopaygo.dto;

import com.cryptopaygo.enums.MovementType;

import java.time.LocalDateTime;

public record StockMovementDTO(

        Long productId,

        Long userId,

        Integer quantity,

        MovementType movementType,

        LocalDateTime movementDate
) {
}