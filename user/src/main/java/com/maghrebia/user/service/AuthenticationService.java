package com.maghrebia.user.service;

import com.maghrebia.user.dto.request.RegistrationRequest;
import com.maghrebia.user.entity.Role;
import com.maghrebia.user.entity.user.Token;
import com.maghrebia.user.entity.user.User;
import com.maghrebia.user.repository.RoleRepository;
import com.maghrebia.user.repository.TokenRepository;
import com.maghrebia.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    public void register(RegistrationRequest registrationRequest) throws MessagingException {
        var userRole = roleRepository.findByName("client").orElseGet(() -> roleRepository.save(new Role("client")));
                // todo - better exception handling
               // .orElseThrow(() -> new IllegalStateException("Role Client was not initialized"));
            if(userRole == null) {
               userRole = roleRepository.save(new Role("client"));
            }
        var user = User.builder()
                .firstname(registrationRequest.getFirstName())
                .lastname(registrationRequest.getLastName())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();
        userRepository.save(user);
        sendValidationEmail(user);
    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);
        emailService.sendEmail(user.getEmail(),user.fullName(),"activate_account",activationUrl,newToken,"Account activation");

    }

    private String generateAndSaveActivationToken(User user) {
        String generatedToken = generateActivationCode(7);
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }
}
