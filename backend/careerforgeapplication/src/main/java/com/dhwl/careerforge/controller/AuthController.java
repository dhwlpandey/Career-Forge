package com.dhwl.careerforge.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dhwl.careerforge.dto.LoginRequest;
import com.dhwl.careerforge.dto.LoginResponse;
import com.dhwl.careerforge.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/api/auth")
@RequiredArgsConstructor 
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login (@Valid @RequestBody  LoginRequest request) {

        return authService.login(request);
    }

}
