package com.moquawel.authentication.controller;

import com.moquawel.authentication.request.ForgotPassword;
import com.moquawel.authentication.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/password")
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordResetService passwordResetService;


    @PostMapping("/forgot")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        return ResponseEntity.ok(passwordResetService.forgotPassword(email));
    }


    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ForgotPassword request) {
        if (!passwordResetService.validatePasswordResetToken(request.token())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
        return ResponseEntity.ok(passwordResetService.resetPassword(request.token(), request.newPassword()));
    }
}
