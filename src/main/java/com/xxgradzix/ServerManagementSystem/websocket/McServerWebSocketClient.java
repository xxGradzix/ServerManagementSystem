package com.xxgradzix.ServerManagementSystem.websocket;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class McServerWebSocketClient extends WebSocketClient {


    public McServerWebSocketClient(String serverUri) throws URISyntaxException {
        super(new URI(serverUri));
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("Połączono z serwerem WebSocket");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Otrzymano wiadomość: " + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Zamknięto połączenie: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

}