package com.example.yutnoribackend.entity;

import javax.persistence.*;


@Entity
@Table(name = "piece")
public class Piece {
    @Id
    @Column(name = "piece_pk")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int piecePk;

    @Column(name = "piece_position")
    private int piecePosition;

    @Column(name = "piece_type")
    private int pieceType;

    @ManyToOne(targetEntity = Turn.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "turn_pk")
    private Turn turn;

}
