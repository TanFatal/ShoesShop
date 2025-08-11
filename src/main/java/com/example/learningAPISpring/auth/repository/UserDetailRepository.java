package com.example.learningAPISpring.auth.repository;

import com.example.learningAPISpring.auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserDetailRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);
    User findById(UUID id);
}
