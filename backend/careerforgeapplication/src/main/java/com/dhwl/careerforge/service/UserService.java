package com.dhwl.careerforge.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dhwl.careerforge.dto.UserCreateRequest;
import com.dhwl.careerforge.dto.UserResponse;
import com.dhwl.careerforge.dto.UserUpdateRequest;
import com.dhwl.careerforge.entity.Profile;
import com.dhwl.careerforge.entity.User;
import com.dhwl.careerforge.exception.EmailAlreadyExistsException;
import com.dhwl.careerforge.exception.UserNotFoundException;
import com.dhwl.careerforge.repository.ProfileRepository;
import com.dhwl.careerforge.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class UserService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;

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
        user.setPassword(passwordEncoder.encode(request.getPassword()));

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

    public UserResponse updateUser(Long id, UserUpdateRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (userRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        user.setUpdatedAt(LocalDateTime.now());

        User updatedUser = userRepository.save(user);

        return toResponse(updatedUser);
    }

    @Transactional 
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Profile profile = user.getProfile();

        if (profile != null) {
            user.removeProfile();
            profileRepository.delete(profile);
        }

        user.getSkills().clear();

        userRepository.delete(user);

    }
}
