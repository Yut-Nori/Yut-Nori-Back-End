package com.example.yutnoribackend.jwt;

import com.example.yutnoribackend.service.RedisService;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// TokenProvider, JwtFilter 를 SecurityConfig에 적용할때 사용할 클래스
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final TokenProvider tokenProvider;
    private final RedisService redisService;

    // 생성자
    public JwtSecurityConfig(TokenProvider tokenProvider, RedisService redisService) {
        this.tokenProvider = tokenProvider;
        this.redisService = redisService;
    }

    // JwtFilter를 Security 로직에 필터로 등록
    @Override
    public void configure(HttpSecurity http) throws Exception {

        JwtFilter customFilter = new JwtFilter(tokenProvider, redisService);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
