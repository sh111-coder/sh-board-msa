package com.example.boardservice.board.feign;

import com.example.boardservice.board.feign.fallback.MemberFeignFallback;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "member-service", url = "${member.find.feign-endpoint}", path = "/api/members", fallback = MemberFeignFallback.class)
@CircuitBreaker(name = "circuit")
public interface MemberFeignClient {

    @GetMapping
    @Bulkhead(name = "board")
    MemberFeignResponse findMemberIdByLoginId(@RequestParam("loginId") final String loginId);
}
