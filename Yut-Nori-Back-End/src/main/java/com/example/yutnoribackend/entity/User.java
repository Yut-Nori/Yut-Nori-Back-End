package com.example.yutnoribackend.entity;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class User {
    @Id
    @Column(name = "user_pk")
    private int user_pk;
    @Column(name = "user_id")
    private String user_id;
    @Column(name = "user_pw")
    private String user_pw;
    @Column(name = "user_nickname")
    private String user_nickname;
    @Column(name = "user_point")
    private int user_point;

    @Enumerated(EnumType.STRING)
    private UserRole user_role;
}
