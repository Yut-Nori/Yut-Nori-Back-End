package com.example.yutnoribackend.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "turn")
public class Turn {
    @Id
    @Column(name = "turn_pk")
    private int turn_pk;
    @Column(name = "turn_start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date turn_start_time;
    @Column(name = "turn_end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date turn_end_time;

    @ManyToOne(targetEntity = Game.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "game_pk")
    private Game game;
}
