package com.example.memberservice.member.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.example.common.ServiceTest;
import com.example.memberservice.member.application.dto.MemberFeignResponse;
import com.example.memberservice.member.application.dto.MemberLoginRequest;
import com.example.memberservice.member.application.dto.MemberRegisterRequest;
import com.example.memberservice.member.domain.Member;
import com.example.memberservice.member.domain.MemberRepository;
import com.example.memberservice.member.exception.MemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
    @CsvSource(value = {"notExistId:password1!", "seongha1:notExistPassword1!"}, delimiter = ':')
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

    @Nested
    @DisplayName("login Id로 MemberFeignDto를 조회할 때")
    class FindMemberByLoginId {

        @Test
        @DisplayName("조회에 성공한다.")
        void success() {
            // given
            final String loginId = "seongha1";
            final String password = "password1!";
            final String nickname = "성하";
            final Member member = Member.builder()
                    .loginId(loginId).password(password).nickname(nickname).build();
            final Member savedMember = memberRepository.save(member);

            // when
            final MemberFeignResponse memberFeignResponse = memberService.findMemberByLoginId(loginId);

            // then
            assertSoftly(softly -> {
                softly.assertThat(memberFeignResponse.memberId()).isEqualTo(savedMember.getId());
                softly.assertThat(memberFeignResponse.nickname()).isEqualTo(nickname);
            });
        }

        @Test
        @DisplayName("없는 login Id면 예외가 발생한다.")
        void throws_when_not_exist_login_id() {
            // given
            final String notExistLoginId = "notExistLoginId";

            // when & then
            assertThatThrownBy(() -> memberService.findMemberByLoginId(notExistLoginId))
                    .isInstanceOf(MemberException.NotFoundMemberException.class)
                    .hasMessage("해당 멤버의 ID가 존재하지 않습니다.");
        }
    }
}
