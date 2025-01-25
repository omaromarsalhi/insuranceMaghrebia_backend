package com.moquawel.gateway.jwt;

import com.moquawel.gateway.exception.TokenMissingException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtService jwtService;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final List<String> excludedPaths = List.of("/api/v1/auth/**","/api/v1/password/**");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        log.info("Processing request for path: {}", path);

//        if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
//            return handleCorsPreflight(exchange);
//        }

        if (isExcluded(path)) {
            return chain.filter(exchange);
        }

        try {
            String token = extractToken(exchange.getRequest().getHeaders().getFirst("Authorization"));
            if (jwtService.isTokenValid(token)) {
                throw new ExpiredJwtException(null, null, "JWT Token has expired");
            }

            return chain.filter(exchange);
        } catch (TokenMissingException | ExpiredJwtException e) {
            log.error("Authentication error: {}", e.getMessage());
            return handleUnauthorizedResponse(exchange, "JWT Token is invalid or expired. ");
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
        exchange.getResponse().getHeaders().set("Access-Control-Allow-Origin", "http://localhost:3000");
        exchange.getResponse().getHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponse().getHeaders().set("Access-Control-Allow-Headers", "authorization, content-type, xsrf-token");
        DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
        DataBuffer buffer = bufferFactory.wrap(errorMessage.getBytes(StandardCharsets.UTF_8));
        return exchange.getResponse().writeWith(Flux.just(buffer));
    }

    private Mono<Void> handleCorsPreflight(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.OK);
        exchange.getResponse().getHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponse().getHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponse().getHeaders().set("Access-Control-Allow-Headers", "authorization, content-type, xsrf-token");
        return exchange.getResponse().setComplete();
    }
}
