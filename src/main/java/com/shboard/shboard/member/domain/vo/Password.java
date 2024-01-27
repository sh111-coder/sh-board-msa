package com.shboard.shboard.member.domain.vo;

import java.util.Objects;
import java.util.regex.Pattern;

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
public class Password {

    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*()_+|~=`{}\\[\\]:\";'<>?,./\\\\-])[\\w!@#$%^&*()_+|~=`{}\\[\\]:\";'<>?,./\\\\-]{4,}$";

    @Column(nullable = false)
    private String password;

    public Password(final String password) {
        validate(password);
        this.password = password;
    }

    private void validate(final String password) {
        if (Objects.isNull(password)) {
            throw new NullPointerException("password는 null일 수 없습니다.");
        }
        if (!Pattern.matches(PASSWORD_REGEX, password)) {
            throw new MemberException.WrongPatternPasswordException();
        }
    }
}
