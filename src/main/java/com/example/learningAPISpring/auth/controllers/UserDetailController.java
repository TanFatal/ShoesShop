package com.example.learningAPISpring.auth.controllers;
import com.example.learningAPISpring.auth.dto.ChangePasswordRequest;
import com.example.learningAPISpring.auth.dto.UserDetailDTO;
import com.example.learningAPISpring.auth.entities.User;
import com.example.learningAPISpring.auth.services.PasswordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserDetailController {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    PasswordService passwordService;

    @GetMapping("/profile")
    public ResponseEntity<UserDetailDTO> getUserProfile(Principal principal){
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());

        if(null == user){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        UserDetailDTO userDetailsDto = UserDetailDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .id(user.getId())
                .phoneNumber(user.getPhoneNumber())
                .addressList(user.getAddressList())
                .authorityList(user.getAuthorities().toArray()).build();

        return new ResponseEntity<>(userDetailsDto, HttpStatus.OK);

    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordRequest request,
                                            Authentication authentication) {
        // Requires: userId, oldPassword, newPassword

        try {
            // Lấy userId từ authentication thay vì từ request body
            String currentUsername = authentication.getName();

            boolean result = passwordService.changePassword(
                    currentUsername,
                    request.getOldPassword(),
                    request.getNewPassword()
            );

            if (result) {
                return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Old password incorrect"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to change password"));
        }
    }
}
