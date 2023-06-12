package com.example.yutnoribackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@Getter
@Table(name = "user")
public class User {
    @Id
    @Column(name = "user_pk")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autoincrement
    private int userPk;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_pw")
    @JsonIgnore // 출력 방지
    private String userPw;

    @Column(name = "user_nickname")
    @JsonIgnore
    private String userNickname;

    @Column(name = "user_point")
    @ColumnDefault("0") // 생성시 기본값
    private int userPoint;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    public User() {

    }
}
