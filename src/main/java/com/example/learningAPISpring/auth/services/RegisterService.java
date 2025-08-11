package com.example.learningAPISpring.auth.services;

import com.example.learningAPISpring.auth.dto.RegistrationRequest;
import com.example.learningAPISpring.auth.dto.RegistrationResponse;
import com.example.learningAPISpring.auth.entities.User;
import com.example.learningAPISpring.auth.helper.VerificationCodeGeneration;
import com.example.learningAPISpring.auth.repository.UserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerErrorException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

@Service
public class RegisterService {
    @Autowired
    private UserDetailRepository userDetailRepository;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public RegistrationResponse createUser(RegistrationRequest request) {
        // kiểm tra email tồn tại chưa
        System.out.println("đã gọi chổ này");
        User existing = userDetailRepository.findByEmail(request.getEmail());

        if(null != existing){
            return  RegistrationResponse.builder()
                    .code(400)
                    .message("Email already exist!")
                    .build();
        }

        try{

            User user = new User();
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEmail(request.getEmail());
            user.setPhoneNumber(request.getPhoneNumber());
            user.setEnabled(false);
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setProvider("manual");
            user.setCreatedOn(Date.from(Instant.now()));
            String code = VerificationCodeGeneration.generateCode();

            user.setVerificationCode(code);
            user.setAuthorities(authorityService.getCustomerAuthorities());
            userDetailRepository.save(user);
            emailService.sendMail(user);


            return RegistrationResponse.builder()
                    .code(200)
                    .message("User created!")
                    .build();


        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ServerErrorException(e.getMessage(),e.getCause());
        }
    }

    public void verifyUser(String userName) {
        User user= userDetailRepository.findByEmail(userName);
        user.setEnabled(true);
        userDetailRepository.save(user);
    }
}
