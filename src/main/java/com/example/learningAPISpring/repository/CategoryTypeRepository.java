package com.example.learningAPISpring.repository;

import com.example.learningAPISpring.entity.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryTypeRepository extends JpaRepository<CategoryType, UUID> {

}

