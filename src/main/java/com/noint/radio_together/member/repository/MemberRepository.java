package com.noint.radio_together.member.repository;

import com.noint.radio_together.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> getMemberByEmail(String email);
}
