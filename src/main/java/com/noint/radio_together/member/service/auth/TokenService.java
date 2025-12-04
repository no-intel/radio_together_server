package com.noint.radio_together.member.service.auth;

import com.noint.radio_together.member.dto.auth.AccessTokenDto;
import com.noint.radio_together.member.entity.Member;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
public class TokenService {

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-sec}")
    private int expire;

    public AccessTokenDto createAccessToken(Member member) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(expire);

        String accessToken = Jwts.builder()
                .setIssuer(issuer)
                .setSubject(member.getId().toString())
                .setExpiration(Date.from(exp))
                .setIssuedAt(Date.from(now))
                .claim("email", member.getEmail())
                .claim("role", member.getRole())
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();

        return AccessTokenDto.of(accessToken, exp.getEpochSecond());
    }

    public String createRefreshToken() {
        return UUID.randomUUID().toString();
    }
}
