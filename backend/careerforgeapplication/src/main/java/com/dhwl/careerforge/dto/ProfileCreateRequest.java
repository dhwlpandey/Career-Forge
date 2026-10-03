package com.dhwl.careerforge.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
public class ProfileCreateRequest {

    private Long userId;

    private String phone;
    
    private String location;
    
    private String bio;
    
    private String githubUrl;
    
    private String linkedinUrl;

}
