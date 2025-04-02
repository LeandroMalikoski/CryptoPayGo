package com.cryptopaygo.repository;

import com.cryptopaygo.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Integer> {
    Optional<Stock> findStockById(Long id);
}
