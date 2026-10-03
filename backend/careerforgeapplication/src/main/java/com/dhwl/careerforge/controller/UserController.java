package com.dhwl.careerforge.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dhwl.careerforge.dto.UserCreateRequest;
import com.dhwl.careerforge.dto.UserResponse;
import com.dhwl.careerforge.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequiredArgsConstructor 
public class UserController {

    private final UserService userService;

    @PostMapping("/api/users")
    public UserResponse createUser(@Valid @RequestBody UserCreateRequest request) {
        
        return userService.createUser(request);
    }

    @GetMapping("api/users/{id}")
    public UserResponse getUserById(@PathVariable Long id) {

        return userService.getUserById(id);
    }

}
