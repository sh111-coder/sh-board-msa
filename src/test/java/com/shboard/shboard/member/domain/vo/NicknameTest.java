package com.shboard.shboard.member.domain.vo;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.shboard.shboard.member.exception.MemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class NicknameTest {

    @ParameterizedTest
    @ValueSource(strings = {"", "S", "영일이삼사오육칠팔구십1"})
    @DisplayName("회원 닉네임이 2자 이상 10자 이하가 아니면 예외가 발생한다.")
    void throws_wrongLengthNickname(final String wrongLengthNickname) {
        // when & then
        assertThatThrownBy(() -> new Nickname(wrongLengthNickname))
                .isInstanceOf(MemberException.WrongLengthNicknameException.class);
    }
}
