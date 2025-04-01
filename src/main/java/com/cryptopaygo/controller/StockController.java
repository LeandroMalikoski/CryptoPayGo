package com.cryptopaygo.controller;

import com.cryptopaygo.config.entity.User;
import com.cryptopaygo.dto.StockMovementDTO;
import com.cryptopaygo.dto.StockResponseDTO;
import com.cryptopaygo.service.StockService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stock")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping("/purchase")
    public ResponseEntity<StockResponseDTO> stockMovement(@Valid @RequestBody StockMovementDTO stockMovementDTO, BindingResult bindingResult, @AuthenticationPrincipal User user) {

        StockResponseDTO stock = stockService.stockMovement(stockMovementDTO, bindingResult, user);

        return ResponseEntity.status(HttpStatus.OK).body(new StockResponseDTO(stock.id(), stock.productId(), stock.userId(), stock.quantity(),
                stock.movementType(), stock.currencyType(), stock.currencyPaid(), stock.purchasePrice(), stock.movementDate()));
    }
}