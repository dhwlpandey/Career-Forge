package com.dhwl.careerforge.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.dhwl.careerforge.entity.EmploymentType;
import com.dhwl.careerforge.entity.ExperienceLevel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
public class JobUpdateRequest {

    @NotBlank(message = "Job title is required")
    @Size(max = 255, message = "Job title is too long")
    private String title;

    @NotBlank(message = "Company name is required")
    @Size(max = 255, message = "Company name is too long")
    private String companyName;

    @NotBlank(message = "Location is required")
    @Size(max = 255, message = "Location is too long")
    private String location;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Employment type is required")
    private EmploymentType employmentType;

    @NotNull(message = "Experience level is required")
    private ExperienceLevel experienceLevel;

    private BigDecimal salaryMin;

    private BigDecimal salaryMax;

    private String applicationUrl;

    private LocalDate deadline;

    private boolean isActive;

}
