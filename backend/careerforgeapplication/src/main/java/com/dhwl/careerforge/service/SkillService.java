package com.dhwl.careerforge.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dhwl.careerforge.dto.SkillCreateRequest;
import com.dhwl.careerforge.dto.SkillResponse;
import com.dhwl.careerforge.entity.Skill;
import com.dhwl.careerforge.entity.User;
import com.dhwl.careerforge.exception.SkillAlreadyExistsException;
import com.dhwl.careerforge.exception.SkillNotFoundException;
import com.dhwl.careerforge.exception.UserNotFoundException;
import com.dhwl.careerforge.repository.SkillRepository;
import com.dhwl.careerforge.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class SkillService {

    private final SkillRepository skillRepository;
    private final UserRepository userRepository;

    public SkillResponse createSkill(SkillCreateRequest request) {

        if (skillRepository.existsByNameIgnoreCase(request.getName())) {
            throw new SkillAlreadyExistsException("Skill already exists");
        }

        Skill skill = new Skill();
        skill.setName(request.getName());

        Skill savedSkill = skillRepository.save(skill);

        return toResponse(savedSkill);
    }

    private SkillResponse toResponse(Skill savedSkill) {

        SkillResponse response = new SkillResponse();

        response.setId(savedSkill.getId());
        response.setName(savedSkill.getName());

        return response;
    }

    public SkillResponse getSkillById(Long id) {

        Skill skill = skillRepository.findById(id)
                .orElseThrow(() ->
                        new SkillNotFoundException("Skill not found"));

        return toResponse(skill);
    }

    public List<SkillResponse> getAllSkills() {

        return skillRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional 
    public void addSkillToCurrentUser(Long userId, Long skillId) {

        User user = userRepository.findById(userId)
            .orElseThrow(() ->
                    new UserNotFoundException("User not found"));

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() ->
                        new SkillNotFoundException("Skill not found"));

        user.getSkills().add(skill);

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<SkillResponse> getCurrentUserSkills(Long userId) {

        User user = userRepository.findById(userId)
            .orElseThrow(() ->
                    new UserNotFoundException("User not found"));

        return user.getSkills()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional 
    public void removeSkillFromCurrentUser(Long userId, Long skillId) {

        User user = userRepository.findById(userId)
            .orElseThrow(() ->
                    new UserNotFoundException("User not found"));

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() ->
                        new SkillNotFoundException("Skill not found"));

        user.getSkills().remove(skill);

        userRepository.save(user);
    }

}
