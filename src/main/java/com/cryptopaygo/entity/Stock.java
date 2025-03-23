package com.cryptopaygo.entity;

import com.cryptopaygo.config.entity.User;
import com.cryptopaygo.enums.MovementType;
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

    private LocalDateTime movementDate = LocalDateTime.now();
}