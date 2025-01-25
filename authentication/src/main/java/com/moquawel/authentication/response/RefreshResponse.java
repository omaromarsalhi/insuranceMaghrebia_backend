package com.moquawel.authentication.response;

public record RefreshResponse(
        String token,
        String refreshToken
) {
}
