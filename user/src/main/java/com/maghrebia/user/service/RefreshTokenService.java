package com.maghrebia.user.service;

import com.maghrebia.user.entity.ActivationToken;
import com.maghrebia.user.entity.RefreshToken;
import com.maghrebia.user.entity.User;
import com.maghrebia.user.exception.InvalidTokenException;
import com.maghrebia.user.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token).orElse(null);
    }
    public void storeRefreshToken(String refreshToken, User user) {
        RefreshToken refToken = RefreshToken.builder()
                .token(refreshToken)
                .user(user)
                .expiresAt(LocalDateTime.now().plusDays(7))
                .createdAt(LocalDateTime.now())
                .build();

        refreshTokenRepository.findByUser(user).ifPresent(existingToken -> {
                    refreshTokenRepository.delete(existingToken);
                }
        );

        refreshTokenRepository.save(refToken);
    }
}
