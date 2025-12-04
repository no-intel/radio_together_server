package com.noint.radio_together.member.service;

import com.noint.radio_together.member.entity.Member;
import com.noint.radio_together.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@Slf4j
@Service
@RequiredArgsConstructor
public class GetMemberService {
    private final MemberRepository memberRepository;

    public Optional<Member> getNullAbleMemberByEmail(String email) {
        return memberRepository.getMemberByEmail(email);
    }

    public Member getMemberByEmail(String email) {
        return memberRepository.getMemberByEmail(email)
                .orElseThrow(RuntimeException::new);
    }
}
