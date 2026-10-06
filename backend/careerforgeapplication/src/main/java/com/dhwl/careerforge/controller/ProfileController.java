package com.dhwl.careerforge.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dhwl.careerforge.dto.ProfileCreateRequest;
import com.dhwl.careerforge.dto.ProfileResponse;
import com.dhwl.careerforge.entity.User;
import com.dhwl.careerforge.security.AuthenticatedUserService;
import com.dhwl.careerforge.service.ProfileService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/api/profiles")
@RequiredArgsConstructor 
public class ProfileController {

    private final ProfileService profileService;

    private final AuthenticatedUserService authenticatedUserService;

    @PostMapping("/me")
    public ProfileResponse createProfile (@Valid @RequestBody ProfileCreateRequest request) {

        User currentUser = authenticatedUserService.getCurrentUser();

        return profileService.createProfile (currentUser, request);
    }

    @GetMapping("/me")
    public ProfileResponse getCurrentProfile() {

        User currentUser = authenticatedUserService.getCurrentUser();

        return profileService.getCurrentProfile(currentUser);
    }

    @PutMapping("/me")
    public ProfileResponse updateCurrentProfile (@Valid @RequestBody ProfileCreateRequest request) {

        User currentUser = authenticatedUserService.getCurrentUser();

        return profileService.updateCurrentProfile (currentUser, request);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCurrentProfile() {

        User currentUser =
                authenticatedUserService.getCurrentUser();

        profileService.deleteCurrentProfile(currentUser);
    }

    // @PostMapping 
    // public ProfileResponse createProfile (@RequestBody ProfileCreateRequest request) {

    //     return profileService.createProfile(request);
    // }

    // @GetMapping("/{id}")
    // public ProfileResponse getProfileById (@PathVariable Long id) {

    //     return profileService.getProfileById(id);
    // }

    // @PutMapping("/{id}")
    // public ProfileResponse updateProfile (
    //     @PathVariable Long id,
    //     @RequestBody ProfileCreateRequest request) {

    //         return profileService.updateProfile(id, request);
    // }

    // @DeleteMapping("/{id}")
    // @ResponseStatus(HttpStatus.NO_CONTENT)
    // public void deleteProfile (@PathVariable Long id) {

    //     profileService.deleteProfile(id);
    // }
}
