package com.maghrebia.user.controller;

import com.maghrebia.user.dto.request.EmailRequest;
import com.maghrebia.user.dto.request.PasswordResetRequest;
import com.maghrebia.user.repository.PasswordTokenRepository;
import com.maghrebia.user.service.PasswordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("password")
@Tag(name = "Password")
@RequiredArgsConstructor
public class PasswordController {
    private final PasswordService passwordService;

    @PostMapping("/forget-password")
    public ResponseEntity<?> forgetPassword(@RequestBody @Valid EmailRequest email) throws MessagingException {
        String generatedToken = passwordService.sendResetPassword(email);
        return ResponseEntity.ok("An email has been sent");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestBody @Valid PasswordResetRequest password) {
        passwordService.resetPassword(token, password);
        return ResponseEntity.ok("Your password has been reset");
    }

}
