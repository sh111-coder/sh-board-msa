package com.shboard.shboard.board.application.dto;

import java.util.Collections;
import java.util.List;

import com.shboard.shboard.board.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public record BoardsResponse(List<BoardListResponse> boardListResponses, BoardPageResponse boardPageResponse) {

    private static final int EMPTY_PAGE_SIZE = 0;
    private static final int EMPTY_PAGE_NUMBER = 0;

    public static BoardsResponse of(final Page<Board> boardPage, final Pageable pageable) {
        if (boardPage.isEmpty()) {
            final Pageable emptyPageable = PageRequest.of(EMPTY_PAGE_NUMBER, pageable.getPageSize());
            final BoardPageResponse emptyBoardPageResponse = BoardPageResponse.of(emptyPageable, boardPage);
            return new BoardsResponse(Collections.emptyList(), emptyBoardPageResponse);
        }

        final List<BoardListResponse> boardListResponses = boardPage.stream()
                .map(BoardListResponse::from)
                .toList();

        final BoardPageResponse boardPageResponse = BoardPageResponse.of(pageable, boardPage);

        return new BoardsResponse(boardListResponses, boardPageResponse);
    }
}
