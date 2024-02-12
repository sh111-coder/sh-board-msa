package com.example.boardservice.board.kafka;

import com.example.boardservice.board.domain.BoardRepository;
import com.example.boardservice.board.kafka.dto.MemberResponseMessage;
import com.example.boardservice.board.kafka.dto.MemberStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionKafkaConsumer {

    private final BoardRepository boardRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "write-board-transaction-result", groupId = "board-transaction-result-group")
    public void consumeBoardWriteTransactionResultEvent(final String memberResponseMessage) throws JsonProcessingException {
        final MemberResponseMessage message = objectMapper.readValue(memberResponseMessage, MemberResponseMessage.class);

        System.out.println("message.memberStatus().name() = " + message.memberStatus().name());
        if (message.memberStatus() == MemberStatus.NOT_REWARDED) {
            boardRepository.deleteById(message.boardId());
        }
    }
}
