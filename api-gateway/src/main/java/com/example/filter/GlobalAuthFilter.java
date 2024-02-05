package com.example.filter;

import java.util.Base64;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GlobalAuthFilter extends AbstractGatewayFilterFactory<GlobalAuthFilter.Config> {

    private static final String SESSION_KEY = "JSESSIONID";
    private static final String REDIS_SESSION_KEY = ":sessions:";
    private static final List<String> excludeUris = List.of(
            "/api/members/register", "/api/members/login"
    );

    @Value("${spring.session.redis.namespace}")
    private String namespace;

    private final StringRedisTemplate redisTemplate;

    public GlobalAuthFilter(final StringRedisTemplate redisTemplate) {
        super(Config.class);
        this.redisTemplate = redisTemplate;
    }

    public static class Config {
    }

    @Override
    public GatewayFilter apply(final Config config) {
        return (exchange, chain) -> {
            final ServerHttpRequest request = exchange.getRequest();
            final String path = request.getURI().getPath();
            for (String excludeUri : excludeUris) {
                if (excludeUri.equals(path)) {
                    return chain.filter(exchange);
                }
            }

            final MultiValueMap<String, HttpCookie> cookies = request.getCookies();

            if (cookies == null || cookies.isEmpty() || !cookies.containsKey(SESSION_KEY)) {
                log.warn("Session Key Not Exists");
                return failAuthenticationResponse(exchange);
            }

            final HttpCookie httpCookie = cookies.get(SESSION_KEY).get(0);
            final String sessionCookie = httpCookie.getValue();
            final String decodedSessionId = new String(Base64.getDecoder().decode(sessionCookie.getBytes()));
            if (!redisTemplate.hasKey(namespace + REDIS_SESSION_KEY + decodedSessionId)) {
                log.warn("Session Cookie exist, but Session in Storage is not exist");
                return failAuthenticationResponse(exchange);
            }

            return chain.filter(exchange);
        };
    }

    private Mono<Void> failAuthenticationResponse(ServerWebExchange exchange) {
        final ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}
