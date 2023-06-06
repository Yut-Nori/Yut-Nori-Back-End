package com.example.yutnoribackend.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "game")
public class Game {
    @Id
    @Column(name = "game_pk")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int gamePk;

    @Column(name = "game_start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date gameStartTime;

    @Column(name = "game_end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date gameEndTime;

    @ManyToOne(targetEntity = Room.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "room_pk")
    private Room room;
}
