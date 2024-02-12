package com.example.boardservice.board.kafka.dto;


public record WriteBoardMessage(Long createdBoardId, String loginId) {
}
