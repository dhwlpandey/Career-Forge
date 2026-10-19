package com.dhwl.careerforge.dto;

import com.dhwl.careerforge.entity.ApplicationStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
public class ApplicationStatusUpdateRequest {

    @NotNull(message = "Application status is required")
    private ApplicationStatus status;
}
