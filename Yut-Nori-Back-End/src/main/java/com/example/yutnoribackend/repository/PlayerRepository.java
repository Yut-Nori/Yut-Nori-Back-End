package com.example.yutnoribackend.repository;

import com.example.yutnoribackend.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Integer> {
}
