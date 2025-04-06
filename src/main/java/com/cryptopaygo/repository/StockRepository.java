package com.cryptopaygo.repository;

import com.cryptopaygo.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Integer> {
    Optional<Stock> findStockById(Long id);

    @Query(value = """
        SELECT 
            COALESCE(SUM(CASE WHEN movement_type = 'ENTRY' THEN purchase_price ELSE 0 END), 0) AS total_entry,
            COALESCE(SUM(CASE WHEN movement_type = 'EXIT' THEN purchase_price ELSE 0 END), 0) AS total_exit,
            COALESCE(SUM(CASE WHEN movement_type = 'ENTRY' THEN purchase_price ELSE 0 END), 0) 
            - COALESCE(SUM(CASE WHEN movement_type = 'EXIT' THEN purchase_price ELSE 0 END), 0) AS balance
        FROM stock
    """, nativeQuery = true)
    List<Object[]> getBalance();
}