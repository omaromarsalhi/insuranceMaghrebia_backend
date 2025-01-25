package com.moquawel.authentication.service;


import com.moquawel.authentication.user.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final JwtService jwtService;
    private final TokenBlackListService tokenBlackListService;
    private final UserRepository userRepository;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authHeader = request.getHeader("Authorization");
        final String token;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        token = authHeader.substring(7);
        var user = userRepository
                .findByEmail(jwtService.extractClaim(token, Claims::getSubject))
                .orElse(null);
        if (user != null) {
            tokenBlackListService.removeUserTokensBtJti(user.getUserId());
            SecurityContextHolder.clearContext();
        }
    }
}
