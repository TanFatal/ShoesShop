package com.example.learningAPISpring.auth.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
}
