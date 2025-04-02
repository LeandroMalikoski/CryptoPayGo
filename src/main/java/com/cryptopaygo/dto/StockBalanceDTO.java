package com.cryptopaygo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

public record StockBalanceDTO(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "#0.00")
        Double entry,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "#0.00")
        Double exit,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "#0.00")
        Double balance
) {
}
