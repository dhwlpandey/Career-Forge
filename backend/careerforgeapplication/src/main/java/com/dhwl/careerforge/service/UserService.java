package com.dhwl.careerforge.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.dhwl.careerforge.dto.UserCreateRequest;
import com.dhwl.careerforge.dto.UserResponse;
import com.dhwl.careerforge.entity.User;
import com.dhwl.careerforge.exception.EmailAlreadyExistsException;
import com.dhwl.careerforge.exception.UserNotFoundException;
import com.dhwl.careerforge.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class UserService {

    private final UserRepository userRepository;

    public UserResponse createUser(UserCreateRequest request) {

        if (emailExists(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists!");
        }

        User user = toEntity(request);

        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        UserResponse response = toResponse(savedUser);

        return response;
    }

    private User toEntity(UserCreateRequest request) {

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        return user;
    }

    private UserResponse toResponse(User user) {

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setCreatedAt(user.getCreatedAt());

        return response;
    }

    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));

        return toResponse(user);
    }

    public boolean emailExists(String email) {

        return userRepository.existsByEmail(email);
    }

}
