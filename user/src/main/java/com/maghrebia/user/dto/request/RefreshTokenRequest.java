package com.maghrebia.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RefreshTokenRequest {
    @NotBlank(message = "Refresh token must not be blank.")
    private String refreshToken;
    @NotBlank(message = "email must not be blank")
    private String email;
}
