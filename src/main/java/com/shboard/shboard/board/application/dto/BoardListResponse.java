package com.shboard.shboard.board.application.dto;

import java.time.LocalDateTime;

import com.shboard.shboard.board.domain.Board;

public record BoardListResponse(
        Long id,
        String title,
        String writerNickname,
        LocalDateTime createdAt,
        Long viewCount
) {

    public static BoardListResponse from(final Board board) {
        final Long id = board.getId();
        final String title = board.getTitle();
        final String writerNickname = board.getWriterNickname();
        final LocalDateTime createdAt = board.getCreatedAt();
        final Long viewCount = board.getViewCount();

        return new BoardListResponse(id, title, writerNickname, createdAt, viewCount);
    }
}
