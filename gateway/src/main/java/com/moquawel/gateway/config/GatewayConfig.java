package com.moquawel.gateway.config;

import com.moquawel.gateway.jwt.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsConfiguration;

import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.server.WebFilter;



import java.util.Arrays;
import java.util.List;


@Configuration
@AllArgsConstructor
public class GatewayConfig {


    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public WebFilter jwtWebFilter() {
        return jwtAuthenticationFilter;
    }

//    @Bean
//    public CorsWebFilter corsWebFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
//        config.addAllowedOrigin("http://localhost:3000");
//        config.addAllowedHeader("*");
//        config.addAllowedMethod("*");
//        source.registerCorsConfiguration("/**", config);
//        return new CorsWebFilter(source);
//    }



//    @Bean
//    public CorsWebFilter corsWebFilter() {
//        CorsConfiguration corsConfig = new CorsConfiguration();
//        corsConfig.setAllowCredentials(true);
//        corsConfig.setAllowedOrigins(List.of("http://localhost:3000"));
//        corsConfig.setAllowedHeaders(
//                Arrays.asList(
//                        HttpHeaders.ORIGIN,
//                        HttpHeaders.CONTENT_TYPE,
//                        HttpHeaders.ACCEPT,
//                        HttpHeaders.AUTHORIZATION
//                )
//        );
//        corsConfig.setAllowedMethods(
//                Arrays.asList(
//                        "GET",
//                        "POST",
//                        "DELETE",
//                        "PUT",
//                        "PATCH",
//                        "OPTIONS"
//                )
//        );
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", corsConfig);
//
//        return new CorsWebFilter(source);
//    }
}

