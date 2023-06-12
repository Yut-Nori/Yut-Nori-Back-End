package com.example.yutnoribackend.controller;

import com.example.yutnoribackend.dto.LoginDTO;
import com.example.yutnoribackend.dto.ResponseDTO;
import com.example.yutnoribackend.dto.SignupDTO;

import com.example.yutnoribackend.dto.TokenDTO;
import com.example.yutnoribackend.jwt.JwtFilter;
import com.example.yutnoribackend.jwt.TokenProvider;
import com.example.yutnoribackend.service.AccountService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    private final AccountService accountService;


    // 생성자
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // 회원가입 controller
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupDTO signupDto){
        Logger.info("test");
        String result = accountService.signupService(signupDto);
        Logger.info("result " + result);
        switch (result){
            case "PASSWORD": // 암호 일치 오류
                return ResponseEntity.ok(new ResponseDTO(HttpStatus.BAD_REQUEST.value(), "비밀번호가 일치하지 않습니다."));
            case "EMAIL": // 중복 이메일 오류
                return ResponseEntity.ok(new ResponseDTO(HttpStatus.CONFLICT.value(), "이미 가입된 이메일입니다"));
            case "OK": // 성공
                return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "회원가입 성공"));
            default: // 알수없는 오류
                return ResponseEntity.ok(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "예기치 못한 오류가 발생했습니다."));
        }
    }

    // 로그인 controller
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO){
        try {
            TokenDTO tokenDTO = accountService.loginService(loginDTO);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + tokenDTO.getAccessToken() + " " + tokenDTO.getRefreshToken());
            return new ResponseEntity<TokenDTO>(tokenDTO, httpHeaders, HttpStatus.OK);
        }catch (BadCredentialsException e){
            return new ResponseEntity<ResponseDTO>(new ResponseDTO(HttpStatus.UNAUTHORIZED.value(), "아이디 및 비밀번호가 일치하지 않습니다."), HttpStatus.UNAUTHORIZED);
        }catch (Exception e){
            return new ResponseEntity<ResponseDTO>(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Test controller <로그인 필요>
    @GetMapping("/test")
    public String test(){
        Logger.warn("test!!!");
        return "1111";
    }
}
