package com.moquawel.authentication.request;

import jakarta.validation.constraints.*;

public record RefreshTokenRequest(
        @NotEmpty(message = "Refresh token must not be empty")
        @NotNull(message = "Refresh token must not be null")
        @Size(min = 10, max = 512, message = "Refresh token must be between 10 and 512 characters")
        String refreshToken,

        @NotEmpty(message = "Email must not be empty")
        @NotNull(message = "Email must not be null")
        @Email(message = "Email must be a valid email address")
        @Pattern(
                regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
                message = "Email must be a valid email address"
        )
        String username
) {
}

