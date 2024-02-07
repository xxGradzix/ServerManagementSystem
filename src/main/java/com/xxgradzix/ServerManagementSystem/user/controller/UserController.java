package com.xxgradzix.ServerManagementSystem.user.controller;

import com.xxgradzix.ServerManagementSystem.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/v1/user")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("grantAdminAuthority/{userId}")
    public ResponseEntity<?> grantAdminAuthority(@PathVariable Long userId) {
        userService.grantAdminAuthority(userId);
        return ResponseEntity.ok().build();
    }



}
