package com.xxgradzix.ServerManagementSystem.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomePageController {

    @GetMapping("/")
    public String homePage() {
        return "Welcome to Server Management System";
    }
    

}
