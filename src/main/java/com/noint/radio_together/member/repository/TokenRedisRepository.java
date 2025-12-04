package com.noint.radio_together.member.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TokenRedisRepository {
    private final StringRedisTemplate redisTemplate;

    @Transactional
    public void RegisterAccessToken(Long userId, String accessToken, long expiresInSeconds) {
        String key = buildKey(userId);

        redisTemplate.opsForValue()
                .set(key, accessToken, Duration.ofSeconds(expiresInSeconds));
    }

    @Transactional(readOnly = true)
    public String getAccessTokenByUserId(Long userId) {
        String key = buildKey(userId);
        return redisTemplate.opsForValue().get(key); // 없다면 null
    }

    // 삭제 (로그아웃)
    @Transactional
    public void deleteAccessToken(Long userId) {
        String key = buildKey(userId);
        redisTemplate.delete(key);
    }

    private String buildKey(Long userId) {
        return "user_access_token:" + userId;
    }
}
