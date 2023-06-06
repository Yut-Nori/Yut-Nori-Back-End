package com.example.yutnoribackend.service;

import com.example.yutnoribackend.dto.SignupDTO;
import com.example.yutnoribackend.entity.User;
import com.example.yutnoribackend.entity.UserRole;
import com.example.yutnoribackend.repository.AccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder; // 비밀번호 암호화 (config - SecurityConfig.java)

    // 생성자
    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder){
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
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



}
