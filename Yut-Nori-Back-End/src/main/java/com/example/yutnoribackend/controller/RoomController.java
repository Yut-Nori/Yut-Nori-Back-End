package com.example.yutnoribackend.controller;

import com.example.yutnoribackend.dto.ResponseDTO;
import com.example.yutnoribackend.dto.RoomDTO;
import com.example.yutnoribackend.exception.DataNotFoundException;
import com.example.yutnoribackend.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/room")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService){
        this.roomService = roomService;
    }

    @PostMapping("/createRoom")
    public ResponseEntity<?> CreateRoom(HttpServletRequest request, @Valid @RequestBody RoomDTO roomDTO){
        boolean createRoomResult = roomService.createRoom(request, roomDTO);
        if(createRoomResult){
            return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "방 생성 완료"));
        }else{
            return ResponseEntity.ok(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "방 생성중 오류가 발생했습니다."));
        }
    }

    @DeleteMapping("/closeRoom/{roomPk}")
    public ResponseEntity<?> CloseRoom(@PathVariable int roomPk) throws DataNotFoundException {
        try {
            roomService.closeRoom(roomPk);
        }catch (DataNotFoundException e){
            return ResponseEntity.ok(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "방 제거 실패"));
        }
        return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "방 제거 완료"));
    }

    @DeleteMapping("/leaveRoom/{roomPk}")
    public ResponseEntity<?> LeaveRoom(@PathVariable int roomPk, HttpServletRequest request){
        try {
            roomService.leaveRoom(roomPk, request);
        }catch (DataNotFoundException e) {
            return ResponseEntity.ok(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "방 나가기 실패"));
        }

        return ResponseEntity.ok(new ResponseDTO(HttpStatus.OK.value(), "방 나가기"));
    }

    // todo 랜덤 매칭

    // todo 방 이름 변경

    // todo 강퇴

    // todo 게임 시작

    // todo 방 내부 채팅

    // todo 방 리스트 조회

    // todo 방 검색 (사용자 검색, 방 이름 검색)

    // todo 방 정보 수정 (이름, 비밀번호 등)

}
