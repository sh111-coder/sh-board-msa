package com.shboard.shboard.global.auth;

public class AuthException extends RuntimeException {

    private AuthException(final String message) {
        super(message);
    }

    public static class FailAuthenticationMemberException extends AuthException {

        public FailAuthenticationMemberException() {
            super("인증되지 않은 사용자의 접근입니다.");
        }
    }
}
