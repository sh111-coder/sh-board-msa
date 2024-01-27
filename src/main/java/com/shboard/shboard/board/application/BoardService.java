package com.shboard.shboard.board.application;

import com.shboard.shboard.board.application.dto.BoardDetailResponse;
import com.shboard.shboard.board.application.dto.BoardWriteRequest;
import com.shboard.shboard.board.application.dto.BoardsResponse;
import com.shboard.shboard.board.domain.Board;
import com.shboard.shboard.board.domain.BoardRepository;
import com.shboard.shboard.board.domain.dto.BoardSearchCondition;
import com.shboard.shboard.board.exception.BoardException;
import com.shboard.shboard.member.domain.Member;
import com.shboard.shboard.member.domain.MemberRepository;
import com.shboard.shboard.member.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public BoardsResponse readByPage(final Pageable pageable) {
        final Page<Board> boardPage = boardRepository.findAllByOrderByCreatedAtDesc(pageable);

        return BoardsResponse.of(boardPage, pageable);
    }

    public BoardDetailResponse readDetail(final Long boardId) {
        final Board findBoard = boardRepository.findById(boardId)
                .orElseThrow(BoardException.NotFoundBoardException::new);

        return BoardDetailResponse.of(findBoard);
    }

    @Transactional(readOnly = true)
    public BoardsResponse searchByCondition(final String title, final String writer, final Pageable pageable) {
        final BoardSearchCondition condition = new BoardSearchCondition(title, writer);
        final Page<Board> searchBoards = boardRepository.searchByCondition(condition, pageable);

        return BoardsResponse.of(searchBoards, pageable);
    }

    public Long writeBoard(final String loginId, final BoardWriteRequest request) {
        final Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(MemberException.NotFoundMemberException::new);
        final Board board = new Board(member, request.title(), request.content());
        final Board savedBoard = boardRepository.save(board);
        return savedBoard.getId();
    }
}
