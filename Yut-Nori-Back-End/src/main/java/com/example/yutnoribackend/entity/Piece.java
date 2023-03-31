package com.example.yutnoribackend.entity;

import javax.persistence.*;


@Entity
@Table(name = "piece")
public class Piece {
    @Id
    @Column(name = "piece_pk")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int piece_pk;

    @Column(name = "piece_position")
    private int piece_position;

    @Column(name = "piece_type")
    private int piece_type;

    @ManyToOne(targetEntity = Turn.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "turn_pk")
    private Turn turn;

}
