package com.example.yutnoribackend.service;

import com.example.yutnoribackend.entity.User;
import com.example.yutnoribackend.repository.AccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    public CustomUserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = accountRepository.findUserByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException(userId + " 해당 유저를 찾을 수 없습니다."));
        
        return org.springframework.security.core.userdetails.User.builder()
                .username(userId)
                .password(user.getUserPw())
                .roles(user.getUserRole().toString())
                .build();
    }
}
