package com.maghrebia.gateway.config;

import com.maghrebia.gateway.jwt.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.server.WebFilter;


@Configuration
@AllArgsConstructor
public class GatewayConfig {


    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public WebFilter jwtWebFilter() {
        return jwtAuthenticationFilter;
    }

}

