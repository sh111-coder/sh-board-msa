package com.shboard.shboard.member.exception;

public class MemberException extends RuntimeException {

    private MemberException(final String message) {
        super(message);
    }

    public static class WrongLengthLoginIdException extends MemberException {

        public WrongLengthLoginIdException(final int minLength, final int maxLength,
                                           final int wrongLength) {
            super(String.format("회원 ID는 %d자 이상 %d자 이하여야합니다. (현재 길이 - %d자)",
                    minLength, maxLength, wrongLength));
        }
    }

    public static class WrongPatternPasswordException extends MemberException {

        public WrongPatternPasswordException() {
            super("회원 비밀번호는 4자 이상의 숫자, 영어, 특수문자 조합이어야합니다.");
        }
    }

    public static class WrongLengthNicknameException extends MemberException {

        public WrongLengthNicknameException(final int minLength, final int maxLength,
                                            final int wrongLength) {
            super(String.format("회원 닉네임은 %d자 이상 %d자 이하여야합니다. (현재 길이 - %d자)",
                    minLength, maxLength, wrongLength));
        }
    }

    public static class FailLoginException extends MemberException {

        public FailLoginException() {
            super("잘못된 회원 정보를 입력하여 로그인에 실패했습니다.");
        }
    }

    public static class NotFoundMemberException extends MemberException {

        public NotFoundMemberException() {
            super("해당 멤버의 ID가 존재하지 않습니다.");
        }
    }
}
