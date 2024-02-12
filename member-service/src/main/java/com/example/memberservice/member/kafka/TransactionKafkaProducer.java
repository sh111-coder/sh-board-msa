package com.example.memberservice.member.kafka;

import com.example.memberservice.member.kafka.dto.MemberResponseMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionKafkaProducer {

    private static final String WRITE_BOARD_TRANSACTION_RESULT_TOPIC = "write-board-transaction-result";

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendTransactionResultMessage(final MemberResponseMessage memberResponseMessage) throws JsonProcessingException {
        final String message = objectMapper.writeValueAsString(memberResponseMessage);
        kafkaTemplate.send(WRITE_BOARD_TRANSACTION_RESULT_TOPIC, message);
    }
}
