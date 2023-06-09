package com.example.yutnoribackend.service;

import com.example.yutnoribackend.entity.User;
import com.example.yutnoribackend.jwt.TokenProvider;
import com.example.yutnoribackend.repository.AccountRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.log.Log;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.tinylog.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class TokenService {

    private final TokenProvider tokenProvider;
    private long accessTokenValidityInMilliseconds;
    private long refreshTokenValidityInMilliseconds;
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private final RedisService redisService;
    private final AccountRepository accountRepository;

    // 생성자
    public TokenService(TokenProvider tokenProvider, RedisService redisService, AccountRepository accountRepository){
        this.tokenProvider = tokenProvider;
        this.redisService = redisService;
        this.accountRepository = accountRepository;
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

    // HttpServletRequest에서 토큰 정보 획득
    public String getToken(HttpServletRequest request){
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        String token = null;
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        }

        return token;
    }

    // redis blackList 설정
    public void setBlackList(HttpServletRequest request){
        String token = getToken(request);

        Date expiration = tokenProvider.getExpiration(token);
        Date now = new Date();

        redisService.setValues(token,"logout", expiration.getTime(), TimeUnit.MILLISECONDS);
    }

    // 토큰 만료 확인
    public boolean checkAccessToken(HttpServletRequest request){
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        String token = null;

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        }

        //토큰이 있다면 유효성 검사후 결과 리턴
        if(tokenProvider.checkUnauthorize(token)){
            return true; //만료
        }else{
            return false; //만료되지 않음
        }
    }

    // refresh 토큰으로 access 토큰 재발행
    public String reIssueAccessToken(String userId) {
        User user = accountRepository.findUserByUserId(userId)
                .orElse(null);

        if (user == null){
            return "NoUser";
        }

        return tokenProvider.createToken(userId, user.getUserRole().toString(), this.accessTokenValidityInMilliseconds);
    }

    // 토큰에서 유저 정보 얻기
    public String getUserIdFromToken(HttpServletRequest request){
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        String token = null;
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        }

        if(token.equals(null)){
            return token;
        }

        return getUserId(token);
    }

    // 토큰에서 유저 아이디 획득
    public String getUserId(String token)  {
        String[] check = token.split("\\.");
        Base64.Decoder decoder = Base64.getDecoder();
        String payload = new String(decoder.decode(check[1]));
        ObjectMapper mapper = new ObjectMapper();
        try{
            Map<String,Object> returnMap = mapper.readValue(payload,Map.class);
            return (String)returnMap.get("sub");
        }catch(JsonProcessingException e){
            return null;
        }
    }

    // refresh 토큰 유효성 확인
    public boolean checkRefreshToken(String userId) {
        String refreshToken = redisService.getValues(userId);
        //유효한 토큰인 경우
        if (tokenProvider.validateToken(refreshToken)) {
            return true;
        }else{ //유효하지 않은 토큰인 경우
            return false;
        }
    }

}
