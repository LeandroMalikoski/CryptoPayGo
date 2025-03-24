package com.cryptopaygo.dto;

public record StatisticsDTO(

        int quantityInTotal,
        Double priceSum,
        Double priceMin,
        Double priceMax,
        Double average

) {
}
