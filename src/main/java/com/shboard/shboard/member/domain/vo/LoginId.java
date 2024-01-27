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
public class LoginId {

    private static final int MIN_LENGTH = 4;
    private static final int MAX_LENGTH = 12;

    @Column(nullable = false)
    private String loginId;

    public LoginId(final String loginId) {
        validate(loginId);
        this.loginId = loginId;
    }

    private void validate(final String loginId) {
        if (Objects.isNull(loginId)) {
            throw new NullPointerException("loginId는 null일 수 없습니다.");
        }
        if (loginId.length() < MIN_LENGTH || loginId.length() > MAX_LENGTH) {
            throw new MemberException.WrongLengthLoginIdException(MIN_LENGTH, MAX_LENGTH, loginId.length());
        }
    }
}
