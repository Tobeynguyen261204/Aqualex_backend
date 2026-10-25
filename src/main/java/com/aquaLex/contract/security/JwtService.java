package com.aquaLex.contract.security;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.aquaLex.contract.domain.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.security.Keys;
@Service
public class JwtService {
    private final SecretKey signingKey;

    public JwtService(@Value("${jwt.secret}") String secret) {
        this.signingKey = io.jsonwebtoken.security.Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
    public String generateAccessToken(User user) {
        Instant now = Instant.now();
        return Jwts.builder()
                         .setSubject(user.getEmail())
                    .claim("userId", user.getId().toString())
                    .claim("role", user.getRole().name())
                    .setIssuedAt(Date.from(now))
                    .setExpiration(Date.from(now.plusSeconds(900)))
                    .signWith(signingKey)
                    .compact();
    }
    public Claims extractAllClaims(String token) {
     return Jwts.parser()
            .verifyWith(signingKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();
}
 public String extractUsername(String token) {
    return extractAllClaims(token).getSubject();
}
public Instant extractExpiration(String token) {
    return extractAllClaims(token).getExpiration().toInstant();
}
public Duration getRemainingLifetime(String token) {
      Instant expiration = extractExpiration(token);
    Duration remaining = Duration.between(Instant.now(), expiration);

    if (remaining.isZero() || remaining.isNegative()) {
        return Duration.ZERO;
    }

    return remaining;
}
public boolean isTokenExpired(String token) {
      return getRemainingLifetime(token).isZero();
}
public boolean isTokenValid(String token, User user) {
    String username = extractUsername(token);
    return user.getEmail().equals(username)
        && !isTokenExpired(token);
}
}
