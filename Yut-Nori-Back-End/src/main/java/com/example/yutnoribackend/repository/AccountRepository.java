package com.example.yutnoribackend.repository;

import com.example.yutnoribackend.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.Optional;

public interface AccountRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByUserId(String userId);

    @Query("select u.userPk from User u where u.userId = :userId")
    int findUserPkByUserId(@Param("userId") String userId);
}
