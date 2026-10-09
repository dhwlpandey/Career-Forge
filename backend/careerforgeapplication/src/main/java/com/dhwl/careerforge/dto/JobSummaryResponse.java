package com.dhwl.careerforge.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.dhwl.careerforge.entity.EmploymentType;
import com.dhwl.careerforge.entity.ExperienceLevel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
public class JobSummaryResponse {

    private Long id;
    private String title;
    private String companyName;
    private String location;
    private EmploymentType employmentType;
    private ExperienceLevel experienceLevel;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private LocalDate deadline;
}
