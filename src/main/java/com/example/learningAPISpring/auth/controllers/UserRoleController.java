package com.example.learningAPISpring.auth.controllers;

import com.example.learningAPISpring.auth.entities.User;
import com.example.learningAPISpring.auth.services.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/role")
@CrossOrigin
public class UserRoleController {
    @Autowired
    private UserRoleService userRoleService;

    @PutMapping("/{userId}/role")
    public ResponseEntity<User> changeUserRole(@PathVariable UUID userId, @RequestParam String roleCode) {
        User updatedUser = userRoleService.changeUserRole(userId, roleCode);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}

