package com.maghrebia.authentication.response;

public record RefreshResponse(
        String token,
        String refreshToken
) {
}
