package com.example.learningAPISpring.repository;

import com.example.learningAPISpring.auth.entities.User;
import com.example.learningAPISpring.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByUser(User user);
}
