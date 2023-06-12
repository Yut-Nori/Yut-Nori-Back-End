package com.example.yutnoribackend.repository;


import com.example.yutnoribackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<User, String> {

    // userId로 1명의 유저 검색
    Optional<User> findUserByUserId(String userId);

}
