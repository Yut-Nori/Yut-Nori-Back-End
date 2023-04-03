package com.example.yutnoribackend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터 체인에 등록됨
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    // 인증, 인가 서비스가 필요하지 않은 endpoin 적용
    @Bean
    public WebSecurityCustomizer configure(){
        return (web) -> web.ignoring()
                .antMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui/**"
                );
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable() // csrf 비활성화
                .headers().frameOptions().sameOrigin() // 동일 도메인에서는 X-Frame-Option 활성화

                .and()
                .authorizeRequests() // 요청에 의한 보안 검사 시작
                .antMatchers().permitAll() //antMatchers 에 설정한 리소스의 접근을 인증 절차 없이 허용

                .anyRequest().authenticated() // 위에서 설정하지 않은 나머지 부분들은 인증 절차 수행
                .and()
                .build();
    }
}
