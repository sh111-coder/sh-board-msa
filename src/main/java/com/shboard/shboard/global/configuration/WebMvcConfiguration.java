package com.shboard.shboard.global.configuration;

import java.util.List;

import com.shboard.shboard.global.auth.AuthInterceptor;
import com.shboard.shboard.global.auth.AuthMemberArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;
    private final AuthMemberArgumentResolver authMemberArgumentResolver;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .excludePathPatterns("/", "/api/members/register", "/api/members/login", "/register", "/login", "/error")
                .excludePathPatterns("/css/**", "/js/**", "/favicon.ico");
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authMemberArgumentResolver);
    }
}
