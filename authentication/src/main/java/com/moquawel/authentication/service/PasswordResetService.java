package com.moquawel.authentication.service;


import com.moquawel.authentication.exception.EmailSendFailedException;
import com.moquawel.authentication.password.PasswordResetToken;
import com.moquawel.authentication.password.PasswordResetTokenRepository;
import com.moquawel.authentication.user.User;
import com.moquawel.authentication.user.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;

    public String forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);
        tokenRepository.save(passwordResetToken);

        emailService.sendResetPasswordEmail(passwordResetToken);

        return "Password reset email sent!";
    }

    public boolean validatePasswordResetToken(String token) {
        Optional<PasswordResetToken> passwordResetToken = tokenRepository.findByToken(token);
        return passwordResetToken.isPresent() && passwordResetToken.get().getExpiryDate().after(new Date());
    }

    public String resetPassword(String token, String newPassword) {

        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        User user = resetToken.getUser();
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        userRepository.save(user);
        tokenRepository.delete(resetToken);

        return "Password has been reset successfully";
    }
}
