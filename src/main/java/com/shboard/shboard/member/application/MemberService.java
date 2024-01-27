package com.shboard.shboard.member.application;

import com.shboard.shboard.member.application.dto.MemberLoginRequest;
import com.shboard.shboard.member.application.dto.MemberRegisterRequest;
import com.shboard.shboard.member.domain.Member;
import com.shboard.shboard.member.domain.MemberRepository;
import com.shboard.shboard.member.exception.MemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Long register(final MemberRegisterRequest request) {
        final Member member = Member.builder()
                .loginId(request.id())
                .password(request.password())
                .nickname(request.nickname())
                .build();

        final Member savedMember = memberRepository.save(member);

        log.info("Member Register Success! member Id = {}", savedMember.getId());
        return savedMember.getId();
    }

    public String login(final MemberLoginRequest request) {
        final Member member = memberRepository.findByLoginIdAndPassword(request.id(), request.password())
                .orElseThrow(MemberException.FailLoginException::new);
        log.info("Member Login Success! member LoginId = {}", member.getLoginId());
        return member.getLoginId().getLoginId();
    }
}
