package com.example.boardservice.board.feign.fallback;

import com.example.boardservice.board.feign.MemberFeignClient;
import com.example.boardservice.board.feign.MemberFeignResponse;
import org.springframework.stereotype.Component;

@Component
public class MemberFeignFallback implements MemberFeignClient {

    @Override
    public MemberFeignResponse findMemberIdByLoginId(final String loginId) {
        return new MemberFeignResponse(-1L, "익명");
    }

    @Override
    public void writeBoard(final String loginId) {
        throw new RuntimeException("OpenFeign Member - Write Board call fail");
    }
}
