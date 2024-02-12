package com.example.memberservice.member.kafka;

import com.example.memberservice.member.application.MemberService;
import com.example.memberservice.member.kafka.dto.MemberResponseMessage;
import com.example.memberservice.member.kafka.dto.MemberStatus;
import com.example.memberservice.member.kafka.dto.WriteBoardMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberKafkaConsumer {

    private final MemberService memberService;
    private final TransactionKafkaProducer transactionKafkaProducer;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "write-board", groupId = "board-write-group")
    public void consumeBoardWriteEvent(final String writeBoardMessage) throws JsonProcessingException {
        final WriteBoardMessage message = objectMapper.readValue(writeBoardMessage, WriteBoardMessage.class);

        try {
            memberService.rewardWritePoint(message);
            final MemberResponseMessage successMessage = new MemberResponseMessage(message.createdBoardId(), MemberStatus.REWARDED);
            transactionKafkaProducer.sendTransactionResultMessage(successMessage);
        } catch (Exception e) {
            final MemberResponseMessage failMessage = new MemberResponseMessage(message.createdBoardId(), MemberStatus.NOT_REWARDED);
            transactionKafkaProducer.sendTransactionResultMessage(failMessage);
        }
    }
}
