package com.example.boardservice.board.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "member-service", path = "/api/members")
public interface MemberFeignClient {

    @GetMapping
    MemberFeignResponse findMemberIdByLoginId(@RequestParam("loginId") final String loginId);
}
