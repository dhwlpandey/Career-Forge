package com.dhwl.careerforge.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
public class ProfileResponse {

    private Long id;
    
    private Long userId;
    
    private String phone;
    
    private String location;
    
    private String bio;
    
    private String githubUrl;
    
    private String linkedinUrl;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;

}
