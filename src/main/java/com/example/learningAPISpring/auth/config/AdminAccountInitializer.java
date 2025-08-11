package com.example.learningAPISpring.auth.config;

import com.example.learningAPISpring.auth.entities.Authority;
import com.example.learningAPISpring.auth.entities.User;
import com.example.learningAPISpring.auth.repository.AuthorityRepository;
import com.example.learningAPISpring.auth.repository.UserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;

@Configuration
public class AdminAccountInitializer {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initAdminAccount(UserDetailRepository userRepository, AuthorityRepository authorityRepository) {
        return args -> {
            if (userRepository.findByEmail("admin") == null) {
                Authority adminRole = authorityRepository.findByRoleCode("ROLE_ADMIN");
                if (adminRole == null) {
                    adminRole = authorityRepository.save(Authority.builder()
                            .roleCode("ROLE_ADMIN")
                            .roleDescription("Administrator role")
                            .build());
                }
                User admin = User.builder()
                        .firstName("Admin")
                        .lastName("User")
                        .email("admin")
                        .password(passwordEncoder.encode("admin"))
                        .enabled(true)
                        .createdOn(Date.from(Instant.now()))
                        .authorities(Collections.singletonList(adminRole))
                        .provider("manual")
                        .build();
                userRepository.save(admin);
                System.out.println("Default admin account created: admin/admin");
            }
        };
    }
}

