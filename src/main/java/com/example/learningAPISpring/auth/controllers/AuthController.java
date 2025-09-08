package com.example.learningAPISpring.auth.controllers;


import com.example.learningAPISpring.auth.config.JWTTokenHelper;
import com.example.learningAPISpring.auth.dto.*;
import com.example.learningAPISpring.auth.entities.User;
import com.example.learningAPISpring.auth.repository.UserDetailRepository;
import com.example.learningAPISpring.auth.services.PasswordService;
import com.example.learningAPISpring.auth.services.RegisterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;
@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RegisterService registrationService;

    @Autowired
    UserDetailRepository userDetailRepository;

    @Autowired
    JWTTokenHelper jwtTokenHelper;

    @Autowired
    PasswordService passwordService;


    @PostMapping("/login")
    public ResponseEntity<UserToken> login(@RequestBody LoginRequest loginRequest){
        try{
            Authentication authentication= UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.getUserName(),
                    loginRequest.getPassword());

            Authentication authenticationResponse = this.authenticationManager.authenticate(authentication);

            if(authenticationResponse.isAuthenticated()){
                User user= (User) authenticationResponse.getPrincipal();
                if(!user.isEnabled()) {
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }

                String token =jwtTokenHelper.generateToken(user.getEmail());
                UserToken userToken= UserToken.builder().token(token).build();
                return new ResponseEntity<>(userToken,HttpStatus.OK);
            }

        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@RequestBody RegistrationRequest request){
        RegistrationResponse registrationResponse = registrationService.createUser(request);

        return new ResponseEntity<>(registrationResponse,
                registrationResponse.getCode() == 200 ? HttpStatus.OK: HttpStatus.BAD_REQUEST);
    }
    @GetMapping("/verify")
    public ResponseEntity<?> verifyCode( @RequestParam("token") String token,
                                         @RequestParam("email") String email){

        try {
            User user = (User) userDetailRepository.findByEmail(email);
            System.out.println(user.getEmail());
            if (user != null && user.getVerificationCode().equals(token)) {
                registrationService.verifyUser(email);

                // Trả về trang HTML success
                String successHtml = """
                <html>
                <body>
                    <div style="text-align: center; margin-top: 50px;">
                        <h2 style="color: green;">Email Verified Successfully!</h2>
                        <p>Your email has been verified. You can now login to your account.</p>
                        <a href="http://localhost:3000/login" style="background-color: #4CAF50; color: white; padding: 10px 20px; text-decoration: none; border-radius: 4px;">
                            Go to Login
                        </a>
                    </div>
                </body>
                </html>
                """;

                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_HTML)
                        .body(successHtml);
            } else {
                String errorHtml = """
                <html>
                <body>
                    <div style="text-align: center; margin-top: 50px;">
                        <h2 style="color: red;">Verification Failed!</h2>
                        <p>Invalid verification link or email already verified.</p>
                    </div>
                </body>
                </html>
                """;

                return ResponseEntity.badRequest()
                        .contentType(MediaType.TEXT_HTML)
                        .body(errorHtml);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.TEXT_HTML)
                    .body("<html><body><h2>Error occurred during verification</h2></body></html>");
        }
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> payload) {
        // Requires: email
        boolean result = passwordService.requestPasswordReset(payload.get("email"));
        if (result) {
            return ResponseEntity.ok(Map.of("message", "Password reset email sent"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Email not found"));
        }
    }
}
