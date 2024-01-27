package com.shboard.shboard.board.exception;

public class BoardException extends RuntimeException {

    public BoardException(final String message) {
        super(message);
    }

    public static class NotFoundBoardException extends BoardException {

        public NotFoundBoardException() {
            super("해당하는 게시글을 찾을 수 없습니다.");
        }
    }
}
