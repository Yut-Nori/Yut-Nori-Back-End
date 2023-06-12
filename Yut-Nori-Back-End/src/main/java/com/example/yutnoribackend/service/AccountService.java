package com.example.yutnoribackend.service;

import com.example.yutnoribackend.dto.LoginDTO;
import com.example.yutnoribackend.dto.SignupDTO;
import com.example.yutnoribackend.dto.TokenDTO;
import com.example.yutnoribackend.entity.User;
import com.example.yutnoribackend.entity.UserRole;
import com.example.yutnoribackend.jwt.TokenProvider;
import com.example.yutnoribackend.repository.AccountRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화 (config - SecurityConfig.java)
    private final TokenProvider tokenProvider;
    private final TokenService tokenService;
    private final RedisService redisService;
    // 생성자
    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder, TokenProvider tokenProvider, TokenService tokenService, RedisService redisService){
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.tokenService = tokenService;
        this.redisService = redisService;
    }

    // signup service
    public String signupService(SignupDTO signupDto){
        // 비밀번호 일치 여부 판단
        if (!isCorrectPassword(signupDto)){
            return "PASSWORD";
        }
        // 중복 이메일 존재 여부 판단
        if (!isUsableId(signupDto)){
            return "EMAIL";
        }

        // DB에 유저 등록
        // 1. 등록할 User 객체 생성
        User signupUser = User.builder()
                .userId(signupDto.getUserId())
                .userPw(passwordEncoder.encode(signupDto.getUserPassword()))
                .userNickname(signupDto.getUserNickname())
                .userRole(UserRole.USER)
                .build();
        // 2. DB에 저장
        accountRepository.save(signupUser);

        Logger.info("성공적으로 회원가입 완료");
        return "OK";
    }

    // 이메일 중복 여부 판단
    public boolean isUsableId(SignupDTO signupDto){
        if (accountRepository.findUserByUserId(signupDto.getUserId()) != null){
            Logger.warn("이미 가입되어 있는 아이디입니다.");
            return false;
        }
        return true;
    }

    // 비밀번호 일치 여부 판단
    public boolean isCorrectPassword(SignupDTO signupDto){
        if (!signupDto.getUserPassword().equals(signupDto.getUserPasswordVarification())){
            Logger.warn("비밀번호가 일치하지 않습니다.");
            return false;
        }
        return true;
    }

    // 로그인
    public TokenDTO loginService(LoginDTO loginDTO) throws BadCredentialsException{
        Authentication authentication;

        // authentication 얻기
        try {
            authentication = tokenProvider.createAuthenticate(loginDTO.getUserId(), loginDTO.getUserPassword());
        }catch (BadCredentialsException e){
            Logger.warn("login err : unauthorized");
            throw e;
        }

        // access token, refresh token 생성
        String accessToken = tokenService.createAccessToken(authentication);
        String refreshToken = tokenService.createRefreshToken(authentication);
        TokenDTO tokenDTO = TokenDTO.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

//        //userId을 key 값으로 refreshToken을 value로 설정
        redisService.setValues(loginDTO.getUserId(), refreshToken);

        return tokenDTO;
    }

}
