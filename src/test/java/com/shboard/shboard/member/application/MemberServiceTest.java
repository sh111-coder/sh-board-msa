package com.shboard.shboard.member.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.shboard.shboard.member.application.dto.MemberLoginRequest;
import com.shboard.shboard.member.application.dto.MemberRegisterRequest;
import com.shboard.shboard.member.common.ServiceTest;
import com.shboard.shboard.member.domain.Member;
import com.shboard.shboard.member.domain.MemberRepository;
import com.shboard.shboard.member.exception.MemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

class MemberServiceTest extends ServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원가입에 성공한다.")
    void success_save() {
        // given
        final String loginId = "sh111";
        final String password = "password1!";
        final String nickname = "seongha";
        final MemberRegisterRequest request = new MemberRegisterRequest(loginId, password, nickname);

        // when
        final Long registeredMemberId = memberService.register(request);

        // then
        assertThat(registeredMemberId).isNotNull();
    }

    @ParameterizedTest
    @CsvSource(value = {"notExistId:password1!", "seongha1:notExistPassword"}, delimiter = ':')
    @DisplayName("잘못된 회원 정보로 로그인에 실패한다.")
    void fail_login_with_wrongMemberInfo(final String inputLoginId, final String inputPassword) {
        // given
        final String loginId = "seongha1";
        final String password = "password1!";
        final String nickname = "성하";
        final Member member = Member.builder()
                .loginId(loginId).password(password).nickname(nickname).build();
        memberRepository.save(member);
        final MemberLoginRequest request = new MemberLoginRequest(inputLoginId, inputPassword);

        // when & then
        assertThatThrownBy(() -> memberService.login(request))
                .isInstanceOf(MemberException.FailLoginException.class)
                .hasMessage("잘못된 회원 정보를 입력하여 로그인에 실패했습니다.");
    }
}
