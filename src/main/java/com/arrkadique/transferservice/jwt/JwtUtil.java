package com.arrkadique.transferservice.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtUtil {

    private final String secret = "mysupersecurejwtsecretkey1234567";

    public String generateToken(Long userId) {
        return Jwts.builder()
            .claim("USER_ID", userId)
            .setIssuedAt(new Date())
            .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
            .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
            .compact();
    }

    public Long extractUserId(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(secret.getBytes())
            .build()
            .parseClaimsJws(token)
            .getBody();
        return claims.get("USER_ID", Long.class);
    }
}