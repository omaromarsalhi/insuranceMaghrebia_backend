package com.maghrebia.user.controller;

import com.maghrebia.user.dto.request.AuthenticationRequest;
import com.maghrebia.user.dto.request.EmailRequest;
import com.maghrebia.user.dto.request.RegistrationRequest;
import com.maghrebia.user.dto.response.AuthenticationResponse;
import com.maghrebia.user.service.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@Tag(name = "Authentication")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationRequest registrationRequest, HttpServletResponse httpServletResponse) throws MessagingException {
        authenticationService.register(registrationRequest);
        return ResponseEntity.accepted().body("You've received an email for confirmation.");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
    }

    @GetMapping("/activate-account")
    public ResponseEntity<?> confirm(@RequestParam String token) throws MessagingException {
        authenticationService.activateAccount(token);
        return ResponseEntity.accepted().body("Account activated.");
    }

}
