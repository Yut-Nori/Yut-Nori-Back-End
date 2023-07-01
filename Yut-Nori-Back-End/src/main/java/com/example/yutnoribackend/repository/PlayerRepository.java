package com.example.yutnoribackend.repository;

import com.example.yutnoribackend.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Integer> {
    List<Player> findPlayersByRoom_RoomPk(int roomPk);
    Long deletePlayersByRoom_RoomPk(int roomPk);
    Long deletePlayersByUser_UserId(String userId);
    Long countPlayerByRoom_RoomPk(int roomPk);
}
