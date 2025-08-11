package com.example.learningAPISpring.auth.config;

import com.example.learningAPISpring.auth.entities.Authority;
import com.example.learningAPISpring.auth.repository.AuthorityRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class RoleInitializer {
    @Bean
    public CommandLineRunner initRoles(AuthorityRepository authorityRepository) {
        return args -> {
            Map<String, String> rolesToCreate = Map.of(
                    "ROLE_ADMIN", "Administrator role",
                    "ROLE_CUSTOMER", "Customer role",
                    "ROLE_MANAGER", "Manager role" // Có thể thêm role khác
            );

            // Tạo các roles chưa tồn tại
            rolesToCreate.forEach((roleCode, description) -> {
                if (!authorityRepository.existsByRoleCode(roleCode)) {
                    authorityRepository.save(Authority.builder()
                            .roleCode(roleCode)
                            .roleDescription(description)
                            .build());
                    System.out.println("Created role: " + roleCode);
                }
            });
        };
    }
}
