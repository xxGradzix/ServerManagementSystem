package com.xxgradzix.ServerManagementSystem.websocket.service;

import com.xxgradzix.ServerManagementSystem.websocket.McServerWebSocketClient;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;

@Service
public class WebSocketService {

    private McServerWebSocketClient webSocketClient;


    public WebSocketService() {
        try {
            this.webSocketClient = new McServerWebSocketClient("ws://localhost:8080/ws");
        } catch (URISyntaxException e) {
            throw new RuntimeException("Błąd przy tworzeniu klienta WebSocket", e);
        }
    }

    public void connect() {
        try {
            this.webSocketClient = new McServerWebSocketClient("ws://localhost:8080/ws");
        } catch (URISyntaxException e) {
            throw new RuntimeException("Błąd przy tworzeniu klienta WebSocket", e);
        }
        webSocketClient.connect();
    }

    public void sendMessage(String message) throws InterruptedException {

        if (webSocketClient.isOpen()) {
            webSocketClient.send(message);
        } else {
            throw new InterruptedException("Connection is not open");
        }

    }

    public void disconnect() {
        webSocketClient.close();
    }
}