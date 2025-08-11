package com.example.learningAPISpring.auth.services;

import com.example.learningAPISpring.auth.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public String sendMail(User user){
        String baseUrl = "http://localhost:8088/test";
        String verificationUrl = baseUrl + "/auth/verify?token=" + user.getVerificationCode() + "&email=" + user.getEmail();


        String subject = "Verify your email";
        String senderName = "ShoesShop";
        String mailContent = """
        <html>
        <body>
            <h2>Hello %s,</h2>
            <p>Please click the button below to verify your email:</p>
            <a href="%s" style="background-color: #4CAF50; color: white; padding: 14px 20px; text-decoration: none; display: inline-block; border-radius: 4px;">
                Verify Email
            </a>
            <p>Or copy this link: <a href="%s">%s</a></p>
            <br/>
            <p>Best regards,<br/>%s</p>
        </body>
        </html>
        """.formatted(user.getUsername(), verificationUrl, verificationUrl, verificationUrl, senderName);

        try{
            SimpleMailMessage mailMessage
                    = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(user.getEmail());
            mailMessage.setText(mailContent);
            mailMessage.setSubject(subject);
            javaMailSender.send(mailMessage);
        }
        catch (Exception e){
            System.out.println("lỗi khi gửi gmail");
            e.printStackTrace(); // In full stack trace
            return "Error while Sending Mail: " + e.getMessage();

        }
        System.out.println("gmail đã được gửi");
        return "Email sent";
    }

    public String sendPasswordResetMail(User user, String newPassword) {
        String subject = "Your New Password";
        String senderName = "ShoesShop";
        String mailContent = """
        <html>
        <body>
            <h2>Hello %s,</h2>
            <p>Your password has been reset. Here is your new password:</p>
            <div style='font-size:18px; font-weight:bold; color:#2d3748;'>%s</div>
            <p>Please log in and change your password as soon as possible for security.</p>
            <br/>
            <p>Best regards,<br/>%s</p>
        </body>
        </html>
        """.formatted(user.getUsername(), newPassword, senderName);

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject(subject);
            mailMessage.setText("Hello %s,\n\nYour password has been reset. Your new password is: %s\n\nPlease log in and change your password as soon as possible.\n\nBest regards,\n%s".formatted(user.getUsername(), newPassword, senderName));
            javaMailSender.send(mailMessage);
        } catch (Exception e) {
            System.out.println("Error sending password reset email");
            e.printStackTrace();
            return "Error while Sending Mail: " + e.getMessage();
        }
        System.out.println("Password reset email sent");
        return "Password reset email sent";
    }
}
