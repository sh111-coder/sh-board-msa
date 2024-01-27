package com.shboard.shboard.global.auth;

import java.util.Arrays;
import java.util.Base64;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private static final String SESSION_KEY = "JSESSIONID";
    private static final String REDIS_SESSION_KEY = ":sessions:";

    @Value("${spring.session.redis.namespace}")
    private String namespace;

    private final StringRedisTemplate redisTemplate;

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        final String sessionIdByCookie = getSessionIdByCookie(request);
        final String decodedSessionId = new String(Base64.getDecoder().decode(sessionIdByCookie.getBytes()));
        if (!redisTemplate.hasKey(namespace + REDIS_SESSION_KEY + decodedSessionId)) {
            log.warn("Session Cookie exist, but Session in Storage is not exist");
            throw new AuthException.FailAuthenticationMemberException();
        }

        return true;
    }

    private String getSessionIdByCookie(final HttpServletRequest request) {
        final Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            log.warn("Session Key Not Exists");
            throw new AuthException.FailAuthenticationMemberException();
        }

        final Cookie sessionCookie = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(SESSION_KEY))
                .findFirst()
                .orElseGet(() -> {
                    log.warn("Session Key Not Exists");
                    throw new AuthException.FailAuthenticationMemberException();
                });

        return sessionCookie.getValue();
    }
}
