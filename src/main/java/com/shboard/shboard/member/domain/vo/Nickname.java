package com.shboard.shboard.member.domain.vo;

import java.util.Objects;

import com.shboard.shboard.member.exception.MemberException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Nickname {

    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 10;

    @Column(nullable = false)
    private String nickname;

    public Nickname(final String nickname) {
        validate(nickname);
        this.nickname = nickname;
    }

    private void validate(final String nickname) {
        if (Objects.isNull(nickname)) {
            throw new NullPointerException("nickname은 null일 수 없습니다.");
        }
        if (nickname.length() < MIN_LENGTH || nickname.length() > MAX_LENGTH) {
            throw new MemberException.WrongLengthNicknameException(MIN_LENGTH, MAX_LENGTH, nickname.length());
        }
    }
}
