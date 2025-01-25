package com.moquawel.authentication.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ForgotPassword(
        @NotEmpty(message = "Refresh token must not be empty")
        @NotNull(message = "Refresh token must not be null")
        @Size(min = 10, max = 512, message = "Refresh token must be between 10 and 512 characters")
        String token,

        @NotEmpty(message = "Password must not be empty")
        @NotNull(message = "Password must not be null")
        @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
                message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, and one special character"
        )
        String newPassword
) {
}
