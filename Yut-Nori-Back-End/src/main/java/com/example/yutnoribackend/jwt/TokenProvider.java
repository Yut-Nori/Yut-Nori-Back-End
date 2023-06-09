package com.example.yutnoribackend.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.InitializingBean;
import io.jsonwebtoken.io.Decoders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.tinylog.Logger;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
// jwt 패키지를 생성하고, 토큰의 생성과 토큰의 유효성 검증등을 담당
public class TokenProvider implements InitializingBean {

    private final String secret;
    private Key key;
    private static final String AUTHORITIES_KEY = "auth";
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    // 생성자
    public TokenProvider(
            @Value("${jwt.secret}") String secret, //jwt secret
            AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.secret = secret;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    // 빈이 생성이 되고 의존성 주입 받은 secret값을 Base64 Decode해서 key변수에 할당
    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes); // HMAC-SHA 로 암호화
    }

    // access 토큰 생성
    public String createToken(String userEmail, String userRole, long tokenValidTime){

        Date now = new Date(); //현재 시간
        long nowTime = now.getTime();
        Date validity = new Date(nowTime + tokenValidTime); // 토큰의 유효기간 설정

        return Jwts.builder() // 빌더 객체 생성
                .setHeaderParam("type", "jwt")
                .setSubject(userEmail) // 클레임중 subject 클레임 이름 생성
                .claim(AUTHORITIES_KEY, userRole)  // payload에 들어갈 정보 <key, value>
                .signWith(key, SignatureAlgorithm.HS512) // signature
                .setExpiration(validity) // 만료 시간 설정
                .compact();
    }

    // refreshToken 생성
    public String createRefreshToken(long refreshTokenValidTime){
        long now = (new Date()).getTime(); //현재 시간 가져오고
        Date validity = new Date(now + refreshTokenValidTime); // 토큰의 유효기간

        return Jwts.builder() // 빌더 객체 생성
                .setHeaderParam("type", "jwt")
                .signWith(key, SignatureAlgorithm.HS512) // signature
                .setExpiration(validity) // 만료 시간 설정
                .compact();
    }

    // authentication 객체 리턴
    public Authentication getAuthentication(String token){
        // token -> claim 생성 (Claims : JWT 의 속성정보, java 에서 Claims 는 Json map 형식의 인터페이스)
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // 권한 정보 획득
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(
                        claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    // 토큰의 유효성 검증 수행
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
//            logger.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
//            logger.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
//            logger.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
//            logger.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    // authenticationToken 생성
    public Authentication createAuthenticate(String userId, String userPassword){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, userPassword);

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication); // SecurityContextHolder에 등록

        return authentication;
    }

    // 해당 User의 권한을 얻기
    public String getAuthorities(Authentication authentication){
        String authorities = authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        return authorities;
    }

    // 만료일 확인
    public Date getExpiration(String token){
        Date expiration = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration;
    }

    // 토큰이 만료기간이 지났는지 확인
    public boolean checkUnauthorize(String token){
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            return true;
        }
        return false;
    }


}
