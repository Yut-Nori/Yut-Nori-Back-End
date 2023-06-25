package com.example.yutnoribackend.service;

import com.example.yutnoribackend.dto.RoomDTO;
import com.example.yutnoribackend.entity.Player;
import com.example.yutnoribackend.entity.Room;
import com.example.yutnoribackend.entity.User;
import com.example.yutnoribackend.entity.UserRole;
import com.example.yutnoribackend.repository.AccountRepository;
import com.example.yutnoribackend.repository.PlayerRepository;
import com.example.yutnoribackend.repository.RoomRepository;
import com.mysql.cj.log.Log;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tinylog.Logger;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;


@Service
public class RoomService {

    private final TokenService tokenService;
    private final AccountRepository accountRepository;
    private final RoomRepository roomRepository;
    private final PlayerRepository playerRepository;
    private final PasswordEncoder passwordEncoder;

    public RoomService(TokenService tokenService, AccountRepository accountRepository, RoomRepository roomRepository, PlayerRepository playerRepository, PasswordEncoder passwordEncoder){
        this.tokenService = tokenService;
        this.accountRepository = accountRepository;
        this.roomRepository = roomRepository;
        this.playerRepository = playerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /***
     * 1. 토큰에서 사용자 정보 획득 (유저 pk 얻기)
     * 2. Room 생성 (이름, 비밀번호, 비밀방여부, 게임중 상태)
     * 3. Player 생성 (유저 pk, room pk, 상태, 참가시각)
     */
    @Transactional
    public boolean createRoom(HttpServletRequest request, RoomDTO roomDTO){
        String userId = tokenService.getUserIdFromToken(request);
        if (userId == null){
            // todo 오류 보내기
            Logger.warn("userId is null");
            return false;
        }

        User user = accountRepository.findUserByUserId(userId)
                .orElse(null);

        Room room = makeRoom(roomDTO);
        Player player = enterPlayer(user, room);

        return true;
    }

    @Transactional
    public Room makeRoom(RoomDTO roomDTO){
        Room createRoom = Room.builder()
                .roomName(roomDTO.getRoomName())
                .roomPw(passwordEncoder.encode(roomDTO.getRoomPassword()))
                .roomVisible(roomDTO.isRoomVisible())
                .roomStatus(false)
                .build();

        Room room = roomRepository.save(createRoom);

        return room;
    }

    @Transactional
    public Player enterPlayer(User user, Room room){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        Logger.info(LocalDateTime.now());
        Logger.info(LocalDateTime.now().format(dateTimeFormatter));
        Player roomPlayer = Player.builder()
                .playerJointime(LocalDateTime.now())
                .room(room)
                .user(user)
                .playerStatus(true)
                .build();

        return playerRepository.save(roomPlayer);
    }


}
