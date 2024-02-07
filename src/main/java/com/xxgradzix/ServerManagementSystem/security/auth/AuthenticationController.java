package com.xxgradzix.ServerManagementSystem.security.auth;

import com.xxgradzix.ServerManagementSystem.security.auth.entity.AuthenticationRequest;
import com.xxgradzix.ServerManagementSystem.security.auth.entity.AuthenticationResponse;
import com.xxgradzix.ServerManagementSystem.security.auth.entity.RegisterRequest;
import com.xxgradzix.ServerManagementSystem.security.auth.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Controller
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authenticationService.register(registerRequest));
    }


}
