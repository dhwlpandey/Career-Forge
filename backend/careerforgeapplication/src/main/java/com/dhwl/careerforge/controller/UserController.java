package com.dhwl.careerforge.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dhwl.careerforge.dto.UserCreateRequest;
import com.dhwl.careerforge.dto.UserResponse;
import com.dhwl.careerforge.dto.UserUpdateRequest;
import com.dhwl.careerforge.entity.User;
import com.dhwl.careerforge.security.AuthenticatedUserService;
import com.dhwl.careerforge.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequiredArgsConstructor 
public class UserController {

    private final UserService userService;

    private final AuthenticatedUserService authenticatedUserService;

    @GetMapping("/api/users/me")
    public UserResponse getCurrentUser() {

        User user = authenticatedUserService.getCurrentUser();

        return userService.getUserById(user.getId());
    }

    @PutMapping("/api/users/me")
    public UserResponse updateCurrentUser(
            @Valid @RequestBody UserUpdateRequest request) {

        User currentUser =
                authenticatedUserService.getCurrentUser();

        return userService.updateUser(
                currentUser.getId(),
                request
        );
    }

    @DeleteMapping("/api/users/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCurrentUser() {
    
        User currentUser = authenticatedUserService.getCurrentUser();

       userService.deleteUser(currentUser.getId());
    }

    @PostMapping("/api/users")
    public UserResponse createUser(@Valid @RequestBody UserCreateRequest request) {
        
        return userService.createUser(request);
    }

    // @GetMapping("/api/admin-test")
    // public String adminTest() {
    //     return "Admin access granted";
    // }

    // @GetMapping("api/users/{id}")
    // public UserResponse getUserById(@PathVariable Long id) {

    //     return userService.getUserById(id);
    // }

    // @PutMapping("api/users/{id}")
    // public UserResponse updateUser (
    //     @PathVariable Long id,
    //     @Valid @RequestBody UserUpdateRequest request) {
            
    //     return userService.updateUser(id, request);
    // }

    // @DeleteMapping("/api/users/{id}")
    // @ResponseStatus(HttpStatus.NO_CONTENT)
    // public void deleteUser(@PathVariable Long id) {
    
    //    userService.deleteUser(id);
    // }
}
