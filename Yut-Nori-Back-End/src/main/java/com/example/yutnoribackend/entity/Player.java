package com.example.yutnoribackend.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "player")
public class Player {
    @Id
    @Column(name = "player_pk")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int playerPk;

    @Column(name = "player_status")
    private boolean playerStatus;

    @Column(name = "player_jointime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date playerJointime;

    @ManyToOne(targetEntity = Room.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "room_pk")
    private Room room;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_pk")
    private User user;
}
