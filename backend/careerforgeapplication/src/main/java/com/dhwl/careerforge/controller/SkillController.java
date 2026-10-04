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
import com.dhwl.careerforge.service.SkillService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/api/skills")
@RequiredArgsConstructor 
public class SkillController {

    private final SkillService skillService;

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

    @PostMapping("/users/{userId}/skills/{skillId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addSkillToUser(
            @PathVariable Long userId,
            @PathVariable Long skillId) {

        skillService.addSkillToUser(userId, skillId);
    }

    @GetMapping("/users/{userId}/skills")
    public List<SkillResponse> getUserSkills(
            @PathVariable Long userId) {

        return skillService.getUserSkills(userId);
    }

    @DeleteMapping("/users/{userId}/skills/{skillId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeSkillFromUser(
            @PathVariable Long userId,
            @PathVariable Long skillId) {

        skillService.removeSkillFromUser(userId, skillId);
    }
}