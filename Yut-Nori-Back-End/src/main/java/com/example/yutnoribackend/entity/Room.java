package com.example.yutnoribackend.entity;

import javax.persistence.*;

@Entity
@Table(name = "room")
public class Room {
    @Id
    @Column(name = "room_pk")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roomPk;

    @Column(name = "room_name")
    private String roomName;

    @Column(name = "room_pw")
    private String roomPw;

    @Column(name = "room_visible")
    private boolean roomVisible;

    @Column(name = "room_status")
    private boolean roomStatus;


}
