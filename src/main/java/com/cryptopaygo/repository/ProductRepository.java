package com.cryptopaygo.repository;

import com.cryptopaygo.entity.Product;
import jakarta.validation.Valid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsByName(String name);

    Product findById(@Valid Long id);
}
