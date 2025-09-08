package com.example.learningAPISpring.auth.controllers;
import com.example.learningAPISpring.auth.dto.UserDetailDTO;
import com.example.learningAPISpring.auth.entities.User;
import com.example.learningAPISpring.auth.repository.UserDetailRepository;
import com.example.learningAPISpring.auth.services.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserDetailRepository userDetailRepository;
    @Autowired
    private UserRoleService userRoleService;
    // API chỉ cho ADMIN
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userDetailRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{userId}/role")
    public ResponseEntity<User> changeUserRole(@PathVariable UUID userId, @RequestParam String roleCode) {
        User updatedUser = userRoleService.assignRoleToUser(userId, roleCode);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
    @PutMapping("/{userId}/role/remove")
    public ResponseEntity<Void> removeUserRole(@PathVariable UUID userId, @RequestParam String roleCode) {
        userRoleService.removeRoleFromUser(userId, roleCode);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{userId}/ban")
    public ResponseEntity<Void> banUser(@PathVariable UUID userId) {
        userRoleService.banUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
