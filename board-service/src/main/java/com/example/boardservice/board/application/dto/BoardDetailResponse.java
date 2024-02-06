package com.example.boardservice.board.application.dto;

import java.time.LocalDateTime;

import com.example.boardservice.board.domain.Board;

public record BoardDetailResponse(
        Long id,
        String title,
        String content,
        String writerNickname,
        LocalDateTime createdAt,
        Long viewCount
) {

    public static BoardDetailResponse of(final Board board) {
        final Long id = board.getId();
        final String title = board.getTitle();
        final String content = board.getContent();
        final String writerNickname = board.getWriterNickname();
        final LocalDateTime createdAt = board.getCreatedAt();
        final Long viewCount = board.getViewCount();

        return new BoardDetailResponse(id, title, content, writerNickname, createdAt, viewCount);
    }
}
