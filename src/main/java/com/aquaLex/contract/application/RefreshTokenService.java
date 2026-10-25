package com.aquaLex.contract.application;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import com.aquaLex.contract.domain.RefreshToken;
import com.aquaLex.contract.domain.User;
import com.aquaLex.contract.infrastructure.RefreshTokenRepository;

import jakarta.transaction.Transactional;

import java.util.Base64;
import java.util.UUID;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service 
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public String createRefreshToken(User user)
    {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        String refreshToken = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        String tokenHash = hashRefreshToken(refreshToken);
        RefreshToken refreshTokenEntity = new RefreshToken(
            user,
            tokenHash,
            UUID.randomUUID(),
            Instant.now().plus(7, ChronoUnit.DAYS),
            null, // createdIp  
            null  // userAgent
        );
        refreshTokenRepository.save(refreshTokenEntity);

        return refreshToken;
    }
    public String hashRefreshToken(String refreshToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(refreshToken.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing refresh token", e);
        }
    }  
    @Transactional
    public void revokeRefreshToken( String rawRefreshToken)
    {
        String tokenHash = hashRefreshToken(rawRefreshToken);
       RefreshToken refreshToken = refreshTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token not found"));
        refreshToken.revoke();
        refreshTokenRepository.save(refreshToken);
    } 
}