package com.example.boardservice.board.kafka.dto;

public record MemberResponseMessage(Long boardId, MemberStatus memberStatus) {
}
