package com.maghrebia.user.service;

import com.maghrebia.user.dto.request.EmailRequest;
import com.maghrebia.user.dto.request.PasswordResetRequest;
import com.maghrebia.user.entity.PasswordToken;
import com.maghrebia.user.entity.User;
import com.maghrebia.user.repository.PasswordTokenRepository;
import com.maghrebia.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordTokenRepository passwordTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public String sendResetPassword(EmailRequest email) throws MessagingException {
        var user = userRepository.findByEmail(email.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
        var generatedToken = generateAndSavePasswordToken(user);
        emailService.sendResetPasswordEmail(user.getEmail(),
                user.fullName(),
                "forget-password",
                "http://localhost:4300/forget-password?token=" + generatedToken,
                "Reset Password");
        return generatedToken;
    }
    private String generateAndSavePasswordToken(User user) {
        String generatedToken = UUID.randomUUID().toString();
        var token = PasswordToken.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(60))
                .user(user)
                .build();
        passwordTokenRepository.save(token);
        return generatedToken;
    }
    public void resetPassword(String token, PasswordResetRequest passwordResetRequest) {
        PasswordToken savedToken = passwordTokenRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Token not found"));
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            passwordTokenRepository.delete(savedToken);
            throw new RuntimeException("Reset password token has expired .");
        }
        if(!passwordResetRequest.getPassword().equals(passwordResetRequest.getConfirmPassword())){
            throw new RuntimeException("Reset password does not match confirm password.");
        }
        var user = userRepository.findById(savedToken.getUser().getId()).orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(passwordEncoder.encode(passwordResetRequest.getPassword()));
        userRepository.save(user);
        passwordTokenRepository.delete(savedToken);
    }
}
