package com.example.learningAPISpring.repository;

import com.example.learningAPISpring.entity.CartItem;
import com.example.learningAPISpring.entity.Cart;
import com.example.learningAPISpring.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    List<CartItem> findByCart(Cart cart);
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
}

