package com.maghrebia.gateway.jwt;

import com.maghrebia.gateway.exception.TokenMissingException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtService jwtService;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final List<String> excludedPaths = List.of("/api/v1/auth/**", "/api/v1/password/**");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        log.info("Processing request for path: {}", path);

        if (isExcluded(path)) {
            return chain.filter(exchange);
        }

        try {
            String token = extractToken(exchange.getRequest().getHeaders().getFirst("Authorization"));
            if (token == null) {
                throw new TokenMissingException("Authorization token is missing.");
            }

            if (!jwtService.isTokenValid(token)) {
                throw new ExpiredJwtException(null, null, "JWT Token is invalid or expired.");
            }

            Claims claims = jwtService.extractAllClaims(token);
            String username = claims.getSubject();
            List<String> roles = claims.get("authorities", List.class);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    roles.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList())
            );

            SecurityContext securityContext = new SecurityContextImpl(authentication);
            return chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
        } catch (TokenMissingException | ExpiredJwtException | SignatureException e) {
            log.error("Authentication error: {}", e.getMessage());
            return handleUnauthorizedResponse(exchange, "JWT Token is invalid or expired.");
        }
    }

    private boolean isExcluded(String path) {
        return excludedPaths.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    private String extractToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        } else {
            throw new TokenMissingException("Authorization token is missing or malformed.");
        }
    }

    private Mono<Void> handleUnauthorizedResponse(ServerWebExchange exchange, String message) {
        String errorMessage = String.format("{\"error\": \"%s\"}", message);
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
        DataBuffer buffer = bufferFactory.wrap(errorMessage.getBytes(StandardCharsets.UTF_8));
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}