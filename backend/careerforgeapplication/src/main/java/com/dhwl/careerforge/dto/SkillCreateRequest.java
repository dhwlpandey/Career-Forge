package com.dhwl.careerforge.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
public class SkillCreateRequest {

    @NotBlank (message = "Name is required")
    private String name;
}
