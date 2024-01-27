package com.shboard.shboard.board.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long>, CustomBoardRepository {

    Page<Board> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
