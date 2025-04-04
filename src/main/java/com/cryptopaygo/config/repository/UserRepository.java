package com.cryptopaygo.config.repository;

import com.cryptopaygo.config.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    User getUserById(Long id);

    boolean existsByName(String name);
}