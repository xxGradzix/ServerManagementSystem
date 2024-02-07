package com.xxgradzix.ServerManagementSystem.controllers;

import com.xxgradzix.ServerManagementSystem.websocket.service.WebSocketService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/admin/websocket")
public class WebSocketController {

    private final WebSocketService webSocketService;

    public WebSocketController(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }
    @GetMapping("/sendCommand/{command}")
    public ResponseEntity<String> sendMessage(@PathVariable(name = "command") String command) {
        try {
            webSocketService.sendMessage(command);
            return ResponseEntity.ok().body("Message sent");
        } catch (InterruptedException e) {
            return ResponseEntity.badRequest().body("Error, Connection is not open");
        }
    }
    @GetMapping("/con")
    public ResponseEntity<String> sendMessage() {

        webSocketService.connect();
        return ResponseEntity.ok().body("Connected to websocket");
    }
    // write endpoint to disconnect from websocket
    @GetMapping("/dis")
    public ResponseEntity<String> disconnect() {

        webSocketService.disconnect();
        return ResponseEntity.ok().body("Disconnected from websocket");
    }
}