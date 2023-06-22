package com.example.yutnoribackend.socket;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.tinylog.Logger;

public class CustomWebSocketHandler extends TextWebSocketHandler {

    // todo
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 클라이언트와 연결 시 호출
        Logger.info("connected");
        session.sendMessage(new TextMessage("ttt"));
        Logger.info("session : {}", session);
    }

    // todo
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        // 클라이언트로부터 메시지 수신시 호출
        Logger.info("message send");
        String payload = (String) message.getPayload();
        Logger.info("payload : {}", payload);
        Logger.info("session : {}", session);

        session.sendMessage(new TextMessage(payload));


    }

    // todo
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 클라이언트와 연결 종료시 호출
        Logger.info("disConnected");
        Logger.info("session : {}", session);
        Logger.info("status : {}", status);
        session.sendMessage(new TextMessage("DisConnected"));
    }
}
