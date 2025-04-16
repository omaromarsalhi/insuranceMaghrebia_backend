package com.maghrebia.user.controller;

import com.maghrebia.user.dto.request.AuthenticationRequest;
import com.maghrebia.user.dto.request.RefreshTokenRequest;
import com.maghrebia.user.dto.request.RegistrationRequest;
import com.maghrebia.user.dto.response.AuthenticationResponse;
import com.maghrebia.user.service.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@Tag(name = "Authentication")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationRequest registrationRequest) throws MessagingException {
        authenticationService.register(registrationRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid AuthenticationRequest authenticationRequest, HttpServletResponse response) {
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest, response));
    }

    @GetMapping("/activate-account")
    public ResponseEntity<?> confirm(@RequestParam String token) throws MessagingException {
        authenticationService.activateAccount(token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refresh(@RequestBody @Valid RefreshTokenRequest request, HttpServletResponse response) {
        authenticationService.refreshToken(request, response);
        return ResponseEntity.ok().build();
    }
}
