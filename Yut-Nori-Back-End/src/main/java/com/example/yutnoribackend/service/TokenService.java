package com.example.yutnoribackend.service;

import com.example.yutnoribackend.jwt.TokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private final TokenProvider tokenProvider;
    private long accessTokenValidityInMilliseconds;
    private long refreshTokenValidityInMilliseconds;

    // 생성자
    public TokenService(TokenProvider tokenProvider){
        this.tokenProvider = tokenProvider;
        this.accessTokenValidityInMilliseconds = 1000L * 60 * 3; //3m
        this.refreshTokenValidityInMilliseconds = 1000L * 60 * 60 * 24 * 14; // 14d
    }

    // AccessToken 생성
    public String createAccessToken(Authentication authentication){
        String userRole = tokenProvider.getAuthorities(authentication);
        return tokenProvider.createToken(authentication.getName(), userRole, this.accessTokenValidityInMilliseconds);
    }

    // RefreshToken 생성
    public String createRefreshToken(Authentication authentication){
        return tokenProvider.createRefreshToken(refreshTokenValidityInMilliseconds);
    }
}
