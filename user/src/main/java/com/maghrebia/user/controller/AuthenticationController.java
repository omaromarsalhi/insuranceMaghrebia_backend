package com.maghrebia.user.controller;

import com.maghrebia.user.dto.request.AuthenticationRequest;
import com.maghrebia.user.dto.request.RegistrationRequest;
import com.maghrebia.user.dto.response.AuthenticationResponse;
import com.maghrebia.user.dto.response.ExceptionResponse;
import com.maghrebia.user.exception.InvalidTokenException;
import com.maghrebia.user.service.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "http://localhost:4300")
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
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
    }
    @GetMapping("/activate-account")
    public ResponseEntity<?> confirm(@RequestParam String token) throws MessagingException {
            authenticationService.activateAccount(token);
        return ResponseEntity.ok().build();
    }
}
