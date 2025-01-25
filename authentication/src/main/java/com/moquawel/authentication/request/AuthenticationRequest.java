package com.moquawel.authentication.request;

import jakarta.validation.constraints.*;

public record AuthenticationRequest(
        @NotEmpty(message = "Email must not be empty")
        @NotNull(message = "Email must not be null")
        @Email(message = "Email must be a valid email address")
        @Pattern(
                regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
                message = "Email must be a valid email address"
        )
        String email,

        @NotEmpty(message = "Password must not be empty")
        @NotNull(message = "Password must not be null")
        @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
                message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, and one special character"
        )
        String password
) {
}
