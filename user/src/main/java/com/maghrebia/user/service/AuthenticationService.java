package com.maghrebia.user.service;

import com.maghrebia.user.dto.request.AuthenticationRequest;
import com.maghrebia.user.dto.request.RefreshTokenRequest;
import com.maghrebia.user.dto.request.RegistrationRequest;
import com.maghrebia.user.dto.response.AuthenticationResponse;
import com.maghrebia.user.entity.Role;
import com.maghrebia.user.entity.ActivationToken;
import com.maghrebia.user.entity.User;
import com.maghrebia.user.exception.EmailAlreadyExistsException;
import com.maghrebia.user.exception.ExpiredTokenException;
import com.maghrebia.user.exception.InvalidTokenException;
import com.maghrebia.user.repository.RoleRepository;
import com.maghrebia.user.repository.ActivationTokenRepository;
import com.maghrebia.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ActivationTokenRepository tokenRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;
    @Value("${application.security.jwt.access-expiration}")
    private long accessExpiration;
    @Value("${application.security.jwt.refresh-expiration}")
    private long refreshExpiration;

    public void register(RegistrationRequest registrationRequest) throws MessagingException {
        var userRole = roleRepository.findByName("client")
                .orElseGet(() -> roleRepository.save(new Role("client")));
        var user = User.builder()
                .firstname(registrationRequest.getFirstName())
                .lastname(registrationRequest.getLastName())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .canContinue(false)
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
        var token = ActivationToken.builder()
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

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest, HttpServletResponse response) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
        );
        var user = (User) auth.getPrincipal();
        user.setLastLoginDate(LocalDateTime.now());
        userRepository.save(user);
        // Generate tokens
        var accessToken = jwtService.generateAccessToken(user);
        Map<String, Object> claims = new HashMap<>();
        claims.put("firstname", user.getFirstname());
        claims.put("id", user.getId());
        var refreshToken = jwtService.generateRefreshToken(claims, user);
        refreshTokenService.storeRefreshToken(refreshToken, user);

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", refreshToken)
                .secure(false)
                .path("/")
                .maxAge(authenticationRequest.isRememberMe() ? refreshExpiration / 1000 : -1)
                .sameSite("Lax")
                .build();
        ResponseCookie accessTokenCookie = ResponseCookie.from("access_token", accessToken)
                .secure(false)
                .path("/")
                .maxAge(accessExpiration / 1000)
                .sameSite("Lax")
                .build();

        // Add cookie to the response
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public void activateAccount(String token) throws MessagingException {
        ActivationToken savedToken = tokenRepository.findByToken(token).orElseThrow(() -> new InvalidTokenException("Invalid token"));
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

    public void refreshToken(RefreshTokenRequest request, HttpServletResponse response) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!jwtService.isRefreshTokenValid(request.getRefreshToken(), user) && refreshTokenService.findByToken(request.getRefreshToken())==null) {
            throw new InvalidTokenException("Invalid refresh token");
        }
        String newAccessToken = jwtService.generateAccessToken(user);
        ResponseCookie accessTokenCookie = ResponseCookie.from("access_token", newAccessToken)
                .secure(false)
                .path("/")
                .maxAge(accessExpiration / 1000)
                .sameSite("Lax")
                .build();
        // Add cookie to the response
        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
    }
}

//for deployement
                /*.secure(true) // Set to true for HTTPS (production)
                .domain(".yourdomain.com") // Allow subdomains*/