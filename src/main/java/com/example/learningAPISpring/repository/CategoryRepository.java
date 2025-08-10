package com.example.learningAPISpring.repository;

import com.example.learningAPISpring.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface  CategoryRepository extends JpaRepository<Category, UUID> {
}
