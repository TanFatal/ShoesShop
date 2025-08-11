package com.example.learningAPISpring.auth.repository;

import com.example.learningAPISpring.auth.entities.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, UUID> {
    Authority findByRoleCode(String roleCode);
    boolean existsByRoleCode(String roleCode); // Thêm method này
}
