package com.example.learningAPISpring.auth.services;

import com.example.learningAPISpring.auth.entities.User;
import com.example.learningAPISpring.auth.repository.UserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerErrorException;

import java.util.UUID;

@Service
public class PasswordService {
    @Autowired
    private UserDetailRepository userDetailRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;

    public boolean changePassword(String curentUserName, String oldPassword, String newPassword) {
        User user = userDetailRepository.findByEmail(curentUserName);
        if (user == null) throw new RuntimeException("User not found");
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userDetailRepository.save(user);
        return true;
    }

    public boolean requestPasswordReset(String email) {
        User user = userDetailRepository.findByEmail(email);
        if (user == null) return false;
        // Generate a new random password
        String newPassword = generateRandomPassword();
        user.setPassword(passwordEncoder.encode(newPassword));
        userDetailRepository.save(user);
        // Send the new password to the user's email
        emailService.sendPasswordResetMail(user, newPassword);
        return true;
    }

    private String generateRandomPassword() {
        // You can use a more secure generator if needed
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
