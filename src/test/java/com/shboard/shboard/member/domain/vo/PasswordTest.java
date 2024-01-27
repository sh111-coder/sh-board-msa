package com.shboard.shboard.member.domain.vo;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.shboard.shboard.member.exception.MemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PasswordTest {

    @ParameterizedTest
    @ValueSource(strings = {"0", "A0!", "1111", "AAaa", "!!@@", "11AA", "11!@", "AA!@"})
    @DisplayName("회원 비밀번호가 4자 이상의 숫자, 영어, 특수문자 조합이 아니면 예외가 발생한다.")
    void throws_wrongPatternPassword(final String wrongPassword) {
        // when & then
        assertThatThrownBy(() -> new Password(wrongPassword))
                .isInstanceOf(MemberException.WrongPatternPasswordException.class);
    }
}
