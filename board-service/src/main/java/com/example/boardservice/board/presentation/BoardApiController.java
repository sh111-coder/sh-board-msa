package com.example.boardservice.board.presentation;

import java.net.URI;

import com.example.boardservice.board.application.BoardService;
import com.example.boardservice.board.application.dto.BoardDetailResponse;
import com.example.boardservice.board.application.dto.BoardWriteRequest;
import com.example.boardservice.board.application.dto.BoardsResponse;
import com.example.shboardcommon.global.auth.AuthMember;
import com.example.shboardcommon.global.auth.AuthMemberId;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardApiController {

    private final BoardService boardService;

    @PostMapping
    private ResponseEntity<Void> write(@AuthMember final AuthMemberId authMemberId, final @RequestBody BoardWriteRequest request) throws JsonProcessingException {
        final Long savedBoardId = boardService.writeBoard(authMemberId.id(), request);
        return ResponseEntity.created(URI.create("/boards/" + savedBoardId)).build();
    }

    @GetMapping
    private ResponseEntity<BoardsResponse> readByPage(final Pageable pageable) {
        final BoardsResponse boardsResponse = boardService.readByPage(pageable);
        return ResponseEntity.ok(boardsResponse);
    }

    @GetMapping("/{boardId}")
    private ResponseEntity<BoardDetailResponse> readDetail(@PathVariable(value = "boardId") Long boardId) {
        final BoardDetailResponse response = boardService.readDetail(boardId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    private ResponseEntity<BoardsResponse> searchByCondition(
            @RequestParam(value = "title", required = false) final String title,
            @RequestParam(value = "writer", required = false) final String writer,
            final Pageable pageable
    ) {
        final BoardsResponse boardsResponse = boardService.searchByCondition(title, writer, pageable);
        return ResponseEntity.ok(boardsResponse);
    }
}
