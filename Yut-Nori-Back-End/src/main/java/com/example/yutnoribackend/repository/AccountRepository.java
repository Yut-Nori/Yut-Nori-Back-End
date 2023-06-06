package com.example.yutnoribackend.repository;


import com.example.yutnoribackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<User, String> {

    // userId로 1명의 유저 검색
    User findUserByUserId(String userId);

}
