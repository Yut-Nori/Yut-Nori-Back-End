package com.example.yutnoribackend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "turn")
public class Turn {
    @Id
    @Column(name = "turn_pk")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int turnPk;

    @Column(name = "turn_start_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime turnStartTime;

    @Column(name = "turn_end_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime turnEndTime;

    @ManyToOne(targetEntity = Game.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "game_pk")
    private Game game;
}
