package com.example.memberservice.member.presentation;

import java.net.URI;

import com.example.memberservice.member.application.MemberService;
import com.example.memberservice.member.application.dto.MemberFeignResponse;
import com.example.memberservice.member.application.dto.MemberLoginRequest;
import com.example.memberservice.member.application.dto.MemberRegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Slf4j
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/register")
    private ResponseEntity<Void> register(@RequestBody @Valid final MemberRegisterRequest request) {
        final Long registeredId = memberService.register(request);
        return ResponseEntity
                .created(URI.create("/members/" + registeredId))
                .build();
    }

    @PostMapping("/login")
    private ResponseEntity<Void> login(@RequestBody final MemberLoginRequest request, final HttpServletRequest httpRequest) {
        final String memberLoginId = memberService.login(request);
        final HttpSession session = httpRequest.getSession();
        session.setAttribute("memberId", memberLoginId);
        session.setMaxInactiveInterval(3600);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public MemberFeignResponse findMemberIdByLoginId(@RequestParam("loginId") final String loginId) {
        return memberService.findMemberByLoginId(loginId);
    }
}
