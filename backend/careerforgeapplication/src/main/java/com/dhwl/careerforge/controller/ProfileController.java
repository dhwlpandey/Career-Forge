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
import com.dhwl.careerforge.service.ProfileService;

import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/api/profiles")
@RequiredArgsConstructor 
public class ProfileController {

    private final ProfileService profileService;

    // @PostMapping 
    // public ProfileResponse createProfile (@RequestBody ProfileCreateRequest request) {

    //     return profileService.createProfile(request);
    // }

    @GetMapping("/{id}")
    public ProfileResponse getProfileById (@PathVariable Long id) {

        return profileService.getProfileById(id);
    }

    @PutMapping("/{id}")
    public ProfileResponse updateProfile (
        @PathVariable Long id,
        @RequestBody ProfileCreateRequest request) {

            return profileService.updateProfile(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile (@PathVariable Long id) {

        profileService.deleteProfile(id);
    }
}
