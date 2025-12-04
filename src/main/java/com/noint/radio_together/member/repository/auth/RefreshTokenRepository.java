package com.noint.radio_together.member.repository.auth;

import com.noint.radio_together.member.entity.auth.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String>{
    Optional<RefreshToken> getRefreshTokenById(Long id);
}
