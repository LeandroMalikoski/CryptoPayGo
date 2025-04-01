package com.cryptopaygo.repository;

import com.cryptopaygo.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Integer> {
}
