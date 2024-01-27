package com.shboard.shboard.member.application.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MemberRegisterRequest(
        @Size(min = 4, max = 12, message = "입력한 회원 ID는 4자 이상 12자 이하여야합니다.")
        String id,

        @Pattern(regexp = PASSWORD_REGEX, message = "입력한 비밀번호는 4자 이상 숫자, 영어, 특수문자 조합이어야합니다.")
        String password,

        @Size(min = 2, max = 10, message = "입력한 회원 닉네임은 2자 이상 10자 이하여야합니다.")
        String nickname
) {
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*()_+|~=`{}\\[\\]:\";'<>?,./\\\\-])[\\w!@#$%^&*()_+|~=`{}\\[\\]:\";'<>?,./\\\\-]{4,}$";
}
