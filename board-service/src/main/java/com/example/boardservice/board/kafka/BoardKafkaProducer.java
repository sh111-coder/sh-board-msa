package com.example.boardservice.board.kafka;

import com.example.boardservice.board.kafka.dto.WriteBoardMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoardKafkaProducer {

    private static final String WRITE_BOARD_TOPIC = "write-board";

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void writeBoard(final Long createdBoardId, final String loginId) throws JsonProcessingException {
//        final WriteBoardMessage writeBoardMessage = new WriteBoardMessage(createdBoardId, loginId);
        final WriteBoardMessage writeBoardMessage = new WriteBoardMessage(createdBoardId, "NotExist");
        kafkaTemplate.send(WRITE_BOARD_TOPIC, objectMapper.writeValueAsString(writeBoardMessage));
    }
}
