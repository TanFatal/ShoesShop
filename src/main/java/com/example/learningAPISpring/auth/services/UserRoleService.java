package com.example.learningAPISpring.auth.services;

import com.example.learningAPISpring.auth.entities.Authority;
import com.example.learningAPISpring.auth.entities.User;
import com.example.learningAPISpring.auth.repository.AuthorityRepository;
import com.example.learningAPISpring.auth.repository.UserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserRoleService {
    @Autowired
    private UserDetailRepository userRepository;
    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private AuthorityService authorityService;
    @Transactional
    public User changeUserRole(UUID userId, String roleCode) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        Authority authority = authorityRepository.findByRoleCode(roleCode);
        if (authority == null) {
            throw new RuntimeException("Role not found: " + roleCode);
        }
        user.setAuthorities(Collections.singletonList(authority));

        return userRepository.save(user);
    }
}
