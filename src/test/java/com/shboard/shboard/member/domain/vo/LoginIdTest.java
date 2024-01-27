package com.shboard.shboard.member.domain.vo;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.shboard.shboard.member.exception.MemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class LoginIdTest {

    @ParameterizedTest
    @ValueSource(strings = {"", "1", "12", "123", "1234567890123"})
    @DisplayName("회원 ID가 4자 이상 12자 이하가 아니면 예외가 발생한다.")
    void throws_wrongLengthLoginId(final String wrongLengthLoginId) {
        // when & then
        assertThatThrownBy(() -> new LoginId(wrongLengthLoginId))
                .isInstanceOf(MemberException.WrongLengthLoginIdException.class);
    }
}
