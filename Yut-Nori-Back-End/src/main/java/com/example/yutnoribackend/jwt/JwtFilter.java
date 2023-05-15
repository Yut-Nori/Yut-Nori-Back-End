package com.example.yutnoribackend.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

// JWT를 위한 커스텀 필터를 만들기 위한 클래스
public class JwtFilter extends GenericFilterBean {

    private TokenProvider tokenProvider;
    public static final String AUTHORIZATION_HEADER = "Authorization";

    // 생성자
    public JwtFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    // doFilter : JWT 토큰의 인증정보를 현재 실행중인 SecurityContext에 저장하는 역활 (실제 필터링 로직은 doFilter 내부에 작성)
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String requestURI = httpServletRequest.getRequestURI();
        String jwt = resolveToken(httpServletRequest); // 토큰 정보 획득

        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) { // 토큰 유효성 검사
            Authentication authentication = tokenProvider.getAuthentication(jwt); // 토큰의 인증 정보 가져오기
            SecurityContextHolder.getContext().setAuthentication(authentication); // 토큰 인증 정보를 security context에 저장
        } else {
            logger.debug("유효한 JWT 토큰이 없습니다, uri: {} " + requestURI);
        }
        chain.doFilter(request, response);
    }

    // 토큰 정보를 가져오는 메소드
    private String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        // 인증타입 : Bearer
        if (StringUtils.hasText(bearerToken)
                && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
