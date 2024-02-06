package com.example.boardservice.board.application;

import com.example.boardservice.board.application.dto.BoardDetailResponse;
import com.example.boardservice.board.application.dto.BoardWriteRequest;
import com.example.boardservice.board.application.dto.BoardsResponse;
import com.example.boardservice.board.domain.Board;
import com.example.boardservice.board.domain.BoardRepository;
import com.example.boardservice.board.domain.MemberInfo;
import com.example.boardservice.board.domain.MemberInfoRepository;
import com.example.boardservice.board.domain.dto.BoardSearchCondition;
import com.example.boardservice.board.exception.BoardException;
import com.example.boardservice.board.feign.MemberFeignClient;
import com.example.boardservice.board.feign.MemberFeignResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
// TODO : 멤버 정보(닉네임) 업데이트 시 정보 동기화 처리하기
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final MemberFeignClient memberFeignClient;

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
        final MemberFeignResponse response = memberFeignClient.findMemberIdByLoginId(loginId);
        final MemberInfo memberInfo = new MemberInfo(response.nickname());
        final MemberInfo savedMemberInfo = memberInfoRepository.save(memberInfo);
        final Board board = new Board(savedMemberInfo, request.title(), request.content());
        final Board savedBoard = boardRepository.save(board);
        return savedBoard.getId();
    }
}
