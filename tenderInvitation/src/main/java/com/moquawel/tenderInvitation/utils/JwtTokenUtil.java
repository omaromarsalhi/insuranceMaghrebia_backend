package com.moquawel.tenderInvitation.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JwtTokenUtil {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    public boolean isTokenValid(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getSubjectFromToken(String token) {
        return extractAllClaims(token).getSubject();
    }


    public List<String> getRolesFromToken(String token) {
        return extractAllClaims(token).get("roles", List.class);
    }
}

