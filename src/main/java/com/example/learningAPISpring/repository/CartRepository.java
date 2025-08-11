package com.example.learningAPISpring.repository;

import com.example.learningAPISpring.auth.entities.User;
import com.example.learningAPISpring.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
    Optional<Cart> findByUser(User user);
}

