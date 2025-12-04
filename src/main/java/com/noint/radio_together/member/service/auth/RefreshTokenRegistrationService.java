package com.noint.radio_together.member.service.auth;

import com.noint.radio_together.member.entity.Member;
import com.noint.radio_together.member.entity.auth.RefreshToken;
import com.noint.radio_together.member.repository.auth.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenRegistrationService {

    private final RefreshTokenRepository refreshTokenRepository;

    public void RegisterRefreshToken(Member member, String token) {
        RefreshToken refreshToken = refreshTokenRepository.getRefreshTokenById(member.getId())
                .orElseGet(() -> {
                    RefreshToken rt = new RefreshToken(member, token);
                    refreshTokenRepository.save(rt);
                    return rt;
                });

        refreshToken.update(token);
    }
}
