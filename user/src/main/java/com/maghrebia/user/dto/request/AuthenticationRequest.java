package com.maghrebia.user.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationRequest {
    @NotBlank(message = "Email should not be empty")
    @Email(message = "Email is not formatted")
    private String email;

    @NotBlank(message = "Password should not be empty")
    private String password;

    @NotNull
    private boolean rememberMe;
}
