package com.example.yutnoribackend.entity;

import javax.persistence.*;

@Entity
@Table(name = "room")
public class Room {
    @Id
    @Column(name = "room_pk")
    private int room_pk;
    @Column(name = "room_name")
    private String room_name;
    @Column(name = "room_pw")
    private String room_pw;
    @Column(name = "room_visible")
    private boolean room_visible;
    @Column(name = "room_status")
    private boolean room_status;


}
