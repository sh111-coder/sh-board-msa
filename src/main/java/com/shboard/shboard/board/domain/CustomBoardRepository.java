package com.shboard.shboard.board.domain;

import com.shboard.shboard.board.domain.dto.BoardSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomBoardRepository {

    Page<Board> searchByCondition(BoardSearchCondition condition, Pageable pageable);
}
