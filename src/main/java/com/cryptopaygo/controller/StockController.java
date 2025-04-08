package com.cryptopaygo.controller;

import com.cryptopaygo.config.entity.User;
import com.cryptopaygo.dto.GeneralResponseDTO;
import com.cryptopaygo.dto.StockBalanceDTO;
import com.cryptopaygo.dto.StockMovementDTO;
import com.cryptopaygo.dto.StockResponseDTO;
import com.cryptopaygo.service.StockService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stock")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping("/purchase")
    public ResponseEntity<StockResponseDTO> stockMovement(@Valid @RequestBody StockMovementDTO stockMovementDTO, BindingResult bindingResult, @AuthenticationPrincipal User user) {

        return ResponseEntity.status(HttpStatus.OK).body(stockService.stockMovement(stockMovementDTO, bindingResult, user));

    }

    @GetMapping("/{id}")
    public ResponseEntity<StockResponseDTO> findStockById(@PathVariable Long id) {

        return ResponseEntity.status(HttpStatus.OK).body(stockService.findStockById(id));

    }

    @GetMapping("/balance")
    public ResponseEntity<StockBalanceDTO> getStockBalance() {

        return ResponseEntity.status(HttpStatus.OK).body(stockService.getStockBalance());

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponseDTO> deleteStock(@PathVariable Long id) {

        stockService.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).body(new GeneralResponseDTO("Stock deleted successfully", true));

    }
}