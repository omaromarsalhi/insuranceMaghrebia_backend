package com.maghrebia.user.service;

import com.maghrebia.user.dto.request.EmailRequest;
import com.maghrebia.user.dto.request.PasswordResetRequest;
import com.maghrebia.user.entity.PasswordToken;
import com.maghrebia.user.entity.User;
import com.maghrebia.user.exception.EmailNotFoundException;
import com.maghrebia.user.exception.LinkExpiredException;
import com.maghrebia.user.exception.LinkInvalidException;
import com.maghrebia.user.exception.PasswordDoesNotMatchException;
import com.maghrebia.user.repository.PasswordTokenRepository;
import com.maghrebia.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordTokenRepository passwordTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public void sendResetPassword(EmailRequest email) throws MessagingException {
        var user = userRepository.findByEmail(email.getEmail()).orElseThrow(() -> new EmailNotFoundException("This email is not registered"));
        var generatedToken = generateAndSavePasswordToken(user);
        emailService.sendResetPasswordEmail(user.getEmail(),
                user.fullName(),
                "forget-password",
                "http://localhost:4300/account/reset-password?token=" + generatedToken,
                "Reset Password");
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
        PasswordToken savedToken = passwordTokenRepository.findByToken(token).orElseThrow(() -> new LinkInvalidException("This link is invalid"));
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            passwordTokenRepository.delete(savedToken);
            throw new LinkExpiredException("This link is expired");
        }
        if(!passwordResetRequest.getPassword().equals(passwordResetRequest.getConfirmPassword())){
            throw new PasswordDoesNotMatchException("Reset password does not match confirm password.");
        }
        var user = userRepository.findById(savedToken.getUser().getId()).orElseThrow(() -> new EmailNotFoundException("This email is not registered"));
        user.setPassword(passwordEncoder.encode(passwordResetRequest.getPassword()));
        userRepository.save(user);
        passwordTokenRepository.delete(savedToken);
    }

    public void verifyPasswordToken(String token){
        PasswordToken savedToken = passwordTokenRepository.findByToken(token).orElseThrow(() -> new LinkInvalidException("This link is invalid"));
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            passwordTokenRepository.delete(savedToken);
            throw new LinkExpiredException("This link is expired");
        }
    }
}
