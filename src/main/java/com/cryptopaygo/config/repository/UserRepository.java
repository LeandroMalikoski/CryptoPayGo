package com.cryptopaygo.config.repository;

import com.cryptopaygo.config.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
