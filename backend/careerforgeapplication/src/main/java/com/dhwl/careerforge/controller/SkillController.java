package com.dhwl.careerforge.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dhwl.careerforge.dto.SkillCreateRequest;
import com.dhwl.careerforge.dto.SkillResponse;
import com.dhwl.careerforge.entity.User;
import com.dhwl.careerforge.security.AuthenticatedUserService;
import com.dhwl.careerforge.service.SkillService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/api/skills")
@RequiredArgsConstructor 
public class SkillController {

    private final SkillService skillService;

    private final AuthenticatedUserService authenticatedUserService;

    @PostMapping
    public SkillResponse createSkill(
            @Valid @RequestBody SkillCreateRequest request) {

        return skillService.createSkill(request);
    }

    @GetMapping("/{id}")
    public SkillResponse getSkillById(
            @PathVariable Long id) {

        return skillService.getSkillById(id);
    }

    @GetMapping
    public List<SkillResponse> getAllSkills() {

        return skillService.getAllSkills();
    }

    @GetMapping("/me")
    public List<SkillResponse> getCurrentUserSkills() {

        Long currentUserId = authenticatedUserService.getCurrentUserId();

        return skillService.getCurrentUserSkills(currentUserId);
    }

    @PostMapping("/me/{skillId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addSkillToCurrentUser(@PathVariable Long skillId) {

        Long currentUserId = authenticatedUserService.getCurrentUserId();

        skillService.addSkillToCurrentUser(currentUserId,skillId);
    }

    @DeleteMapping("/me/{skillId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeSkillFromCurrentUser(@PathVariable Long skillId) {

        Long currentUserId = authenticatedUserService.getCurrentUserId();

        skillService.removeSkillFromCurrentUser(currentUserId,skillId);
    }
}