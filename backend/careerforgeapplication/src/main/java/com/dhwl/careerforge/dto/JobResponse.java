package com.dhwl.careerforge.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.dhwl.careerforge.entity.EmploymentType;
import com.dhwl.careerforge.entity.ExperienceLevel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
public class JobResponse {
    
    private Long id;
    private String title;
    private String companyName;
    private String location;
    private String description;
    private EmploymentType employmentType;
    private ExperienceLevel experienceLevel;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private String applicationUrl;
    private LocalDateTime postedAt;
    private LocalDate deadline;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
