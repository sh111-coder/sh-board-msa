package com.example.boardservice.board.application.dto;

import com.example.boardservice.board.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public record BoardPageResponse(int currentPageNumber, int totalPageNumber) {

    public static BoardPageResponse of(final Pageable pageable, Page<Board> boardPage) {
        final int currentPageNumber = pageable.getPageNumber() + 1;
        final int totalPageNumber = boardPage.getTotalPages();
        return new BoardPageResponse(currentPageNumber, totalPageNumber);
    }
}
