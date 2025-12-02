package com.noint.radio_together.member.repository;

import com.noint.radio_together.member.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String>{
}
