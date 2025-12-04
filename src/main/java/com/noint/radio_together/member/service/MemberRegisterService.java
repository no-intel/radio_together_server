package com.noint.radio_together.member.service;

import com.noint.radio_together.member.entity.Member;
import com.noint.radio_together.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberRegisterService {
    private final MemberRepository memberRepository;

    public void registerMember(Member member) {
        memberRepository.save(member);
    }
}
