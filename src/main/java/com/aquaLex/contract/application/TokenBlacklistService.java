package com.aquaLex.contract.application;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class TokenBlacklistService {

    private final RedisTemplate<String, String> redisTemplate;

    public TokenBlacklistService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void blacklist(String accessToken, Duration ttl) {
        if (ttl.isZero() || ttl.isNegative()) {
            return;
        }

        redisTemplate.opsForValue().set(
                buildBlacklistKey(accessToken),
                "blacklisted",
                ttl.toMillis(),
                TimeUnit.MILLISECONDS
        );
    }

    public boolean isBlacklisted(String accessToken) {
        return Boolean.TRUE.equals(
                redisTemplate.hasKey(buildBlacklistKey(accessToken))
        );
    }

    private String buildBlacklistKey(String accessToken) {
        return "blacklist:access:" + accessToken;
    }
}