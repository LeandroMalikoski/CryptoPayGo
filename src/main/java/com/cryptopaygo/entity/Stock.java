package com.cryptopaygo.entity;

import com.cryptopaygo.config.entity.User;
import com.cryptopaygo.config.repository.UserRepository;
import com.cryptopaygo.enums.MovementType;
import com.cryptopaygo.repository.ProductRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "stock")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Relaciona o id de produto na movimentação
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    //Relaciona o id do usuário na movimentação
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Integer quantity;

    // Armazena a role como string no banco, em vez de índice numérico
    @Enumerated(EnumType.STRING)
    private MovementType movementType;

    // Será registrado o tipo de moeda como Dólar, Bitcoin, etc...
    private String currencyType;

    // Preço da moeda no momento
    private Double currencyPaid;

    // Será registrado o preço total
    private Double purchasePrice;

    private LocalDateTime movementDate = LocalDateTime.now();

    // Repositórios para buscar as entidades Product e User
    @Transient
    private ProductRepository productRepository;

    @Transient
    private UserRepository userRepository;

    public Stock(Long productId, Long userId, Integer quantity, MovementType movementType, String currencyType, Double currencyPaid, Double purchasePrice, LocalDateTime movementDate, ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;

        // Buscar os objetos Product e User usando seus respectivos repositórios
        this.product = productRepository.getProductById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        this.user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        this.quantity = quantity;
        this.movementType = movementType;
        this.currencyType = currencyType;
        this.currencyPaid = currencyPaid;
        this.purchasePrice = purchasePrice;
        this.movementDate = movementDate;
    }
}