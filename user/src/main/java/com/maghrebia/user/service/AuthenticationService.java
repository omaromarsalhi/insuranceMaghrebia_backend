package com.maghrebia.user.service;

import com.maghrebia.user.dto.request.AuthenticationRequest;
import com.maghrebia.user.dto.request.RegistrationRequest;
import com.maghrebia.user.dto.response.AuthenticationResponse;
import com.maghrebia.user.entity.Role;
import com.maghrebia.user.entity.Token;
import com.maghrebia.user.entity.User;
import com.maghrebia.user.exception.EmailAlreadyExistsException;
import com.maghrebia.user.exception.ExpiredTokenException;
import com.maghrebia.user.exception.InvalidTokenException;
import com.maghrebia.user.repository.RoleRepository;
import com.maghrebia.user.repository.TokenRepository;
import com.maghrebia.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    public void register(RegistrationRequest registrationRequest) throws MessagingException {
        var userRole = roleRepository.findByName("client")
                .orElseGet(() -> roleRepository.save(new Role("client")));
        var user = User.builder()
                .firstname(registrationRequest.getFirstName())
                .lastname(registrationRequest.getLastName())
                .email(registrationRequest.getEmail())
                .dateOfBirth(registrationRequest.getDateOfBirth())
                .gender(registrationRequest.getGender())
                .phone(registrationRequest.getPhone())
                .address(registrationRequest.getAddress())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException("The email is already registered.");
        }

        userRepository.save(user);

        sendValidationEmail(user);
    }


    public void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);
        emailService.sendVerificationEmail(user.getEmail(),
                user.fullName(),
                "activate_account",
                activationUrl,
                newToken,
                "Account activation");
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

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
        );
        var claims = new HashMap<String, Object>();
        var user = (User) auth.getPrincipal();
        claims.put("fullName", user.fullName());
        var jwtToken = jwtService.generateToken(claims, user);
        user.setLastLoginDate(LocalDateTime.now());
        userRepository.save(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    @Transactional
    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token).orElseThrow(() -> new InvalidTokenException("Invalid token"));
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            tokenRepository.deleteById(savedToken.getId());
            sendValidationEmail(savedToken.getUser());
            throw new ExpiredTokenException("Activation token has expired . A new token has been sent");
        }
        var user = userRepository.findById(savedToken.getUser().getId()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);
        tokenRepository.deleteById(savedToken.getId());
    }


}
