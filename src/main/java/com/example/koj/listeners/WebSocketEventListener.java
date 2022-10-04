package com.example.koj.listeners;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class WebSocketEventListener {
    SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void onSessionConnect(SessionConnectedEvent e) {
        log.info("Connected");
    }

    @EventListener
    void onSessionDisconnect(SessionDisconnectEvent e) {
        log.info("Disconnected");
    }
}
