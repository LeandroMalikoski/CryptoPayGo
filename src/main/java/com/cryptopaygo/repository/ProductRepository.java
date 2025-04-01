package com.cryptopaygo.repository;

import com.cryptopaygo.entity.Product;
import jakarta.validation.Valid;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsByName(String name);

    Product findById(@Valid Long id);

    Optional<Product> getProductById(Long id);
}
