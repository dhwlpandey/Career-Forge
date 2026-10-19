package com.dhwl.careerforge.dto;

import java.time.LocalDateTime;

import com.dhwl.careerforge.entity.ApplicationStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
public class ApplicationResponse {

    private Long id;
    private Long userId;
    private Long jobId;
    private String jobTitle;
    private String companyName;
    private ApplicationStatus status;
    private LocalDateTime appliedAt;
    private String coverLetter;
    private String resumeUrl;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
