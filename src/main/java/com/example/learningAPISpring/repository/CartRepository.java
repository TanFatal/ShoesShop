package com.example.learningAPISpring.repository;

import com.example.learningAPISpring.entity.Cart;
import com.example.learningAPISpring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
    Optional<Cart> findByUser(User user);
}

