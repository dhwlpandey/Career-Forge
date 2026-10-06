package com.dhwl.careerforge.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dhwl.careerforge.dto.ProfileCreateRequest;
import com.dhwl.careerforge.dto.ProfileResponse;
import com.dhwl.careerforge.entity.Profile;
import com.dhwl.careerforge.entity.User;
import com.dhwl.careerforge.exception.ProfileAlreadyExistsException;
import com.dhwl.careerforge.exception.ProfileNotFoundException;
import com.dhwl.careerforge.exception.UserNotFoundException;
import com.dhwl.careerforge.repository.ProfileRepository;
import com.dhwl.careerforge.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class ProfileService {

    private final ProfileRepository profileRepository;

    // private final UserRepository userRepository;

    public ProfileResponse createProfile(User user, ProfileCreateRequest request) {

        // User usr = userRepository.findById(user.getId())
        //         .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (profileRepository.findByUserId(user.getId()).isPresent()) {
            throw new ProfileAlreadyExistsException("Profile already exists");
        }

        Profile profile = new Profile();

        profile.setUser(user);
        user.setProfile(profile);

        profile.setPhone(request.getPhone());
        profile.setLocation(request.getLocation());
        profile.setBio(request.getBio());
        profile.setGithubUrl(request.getGithubUrl());
        profile.setLinkedinUrl(request.getLinkedinUrl());

        profile.setCreatedAt(LocalDateTime.now());
        profile.setUpdatedAt(LocalDateTime.now());

        Profile savedProfile = profileRepository.save(profile);

        return toResponse(savedProfile);
    }

    private ProfileResponse toResponse(Profile profile) {

        ProfileResponse response = new ProfileResponse();

        response.setId(profile.getId());
        response.setUserId(profile.getUser().getId());
        response.setPhone(profile.getPhone());
        response.setLocation(profile.getLocation());
        response.setBio(profile.getBio());
        response.setGithubUrl(profile.getGithubUrl());
        response.setLinkedinUrl(profile.getLinkedinUrl());
        response.setCreatedAt(profile.getCreatedAt());
        response.setUpdatedAt(profile.getUpdatedAt());

        return response;
    }

    public ProfileResponse updateCurrentProfile (User user, ProfileCreateRequest request) {

        Profile profile = profileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found!"));
               
        profile.setPhone(request.getPhone());
        profile.setLocation(request.getLocation());
        profile.setBio(request.getBio());
        profile.setGithubUrl(request.getGithubUrl());
        profile.setLinkedinUrl(request.getLinkedinUrl());

        profile.setUpdatedAt(LocalDateTime.now());

        Profile updatedProfile = profileRepository.save(profile);

        return toResponse(updatedProfile);
    }
    
    @Transactional 
    public void deleteCurrentProfile(User user) {

        Profile profile = profileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found!"));

        profile.removeUser();

        profileRepository.delete(profile);
    }

    public ProfileResponse getCurrentProfile(User user) {

        Profile profile = profileRepository
                .findByUserId(user.getId())
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found!"));

        return toResponse(profile); 
    }
}
