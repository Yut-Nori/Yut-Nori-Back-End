package com.example.yutnoribackend.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "player")
public class Player {
    @Id
    @Column(name = "player_pk")
    private int player_pk;
    @Column(name = "player_status")
    private boolean player_status;
    @Column(name = "player_jointime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date player_jointime;

    @ManyToOne(targetEntity = Room.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "room_pk")
    private Room room;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_pk")
    private User user;
}
