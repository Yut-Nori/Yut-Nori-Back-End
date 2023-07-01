package com.example.yutnoribackend.service;

import com.example.yutnoribackend.dto.RoomDTO;
import com.example.yutnoribackend.entity.Player;
import com.example.yutnoribackend.entity.Room;
import com.example.yutnoribackend.entity.User;
import com.example.yutnoribackend.entity.UserRole;
import com.example.yutnoribackend.exception.DataNotFoundException;
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

    // 방 생성 및 생성자 입장
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

    // 방 생성
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

    // todo 방장 필요 - 권한 주기
    // 플레이어 방 참가
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

    @Transactional
    public boolean closeRoom(int roomPk) throws DataNotFoundException{
        if(!isEmptyRoom(roomPk)){
            try {
                deleteAllPlayerInRoom(roomPk);
            } catch (DataNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            deleteRoom(roomPk);
        } catch (DataNotFoundException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    // player 유무 판단
    public boolean isEmptyRoom(int roomPk){
        if(playerRepository.findPlayersByRoom_RoomPk(roomPk).isEmpty()){
            return true;
        }
        return false;
    }

    // player 제거
    @Transactional
    public boolean deleteAllPlayerInRoom(int roomPk) throws DataNotFoundException {
        Long res = playerRepository.deletePlayersByRoom_RoomPk(roomPk);
        if (res == 0){
            throw new DataNotFoundException("해당 방의 플레이어를 찾을 수 없습니다. : " + roomPk);
        }
        return true;
    }

    // room 제거
    @Transactional
    public boolean deleteRoom(int roomPk) throws DataNotFoundException {
        Long res = roomRepository.deleteRoomByRoomPk(roomPk);
        if (res == 0){
            throw new DataNotFoundException("해당 방을 찾을수 없습니다. : " + roomPk);
        }
        return true;
    }

    // todo
    @Transactional
    public boolean leaveRoom(int roomPk, HttpServletRequest request) throws DataNotFoundException {
        String userId = tokenService.getUserIdFromToken(request);
        long deleteResult = playerRepository.deletePlayersByUser_UserId(userId);
        if(deleteResult == 0){
            throw new DataNotFoundException("해당 플레이어를 찾을 수 없습니다. : " + userId);
        }
        if(Long.compare(getRoomPlayerNum(roomPk), 0) == 0){
            deleteRoom(roomPk);
        }
        return true;
    }

    // 현재 방의 참가자 수 검색
    public long getRoomPlayerNum(int roomPk){
        long roomPlayerCount = playerRepository.countPlayerByRoom_RoomPk(roomPk);
        return roomPlayerCount;
    }
}
