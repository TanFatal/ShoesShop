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
    public User assignRoleToUser(UUID userId, String roleCode) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        Authority authority = authorityRepository.findByRoleCode(roleCode);
        if (authority == null) {
            throw new RuntimeException("Role not found: " + roleCode);
        }
        user.getRoles().add(authority);

        return userRepository.save(user);
    }


    @Transactional
    public void  removeRoleFromUser(UUID userId, String roleCode) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        Authority authority = authorityRepository.findByRoleCode(roleCode);
        if (authority == null) {
            throw new RuntimeException("Role not found: " + roleCode);
        }
        user.getRoles().remove(authority);
        userRepository.save(user);
    }

    @Transactional
    public void banUser(UUID id) {
        User user = userRepository.findById(id);
        if (user != null) {
            user.setEnabled(false);
            userRepository.save(user);
        }
    }
}
