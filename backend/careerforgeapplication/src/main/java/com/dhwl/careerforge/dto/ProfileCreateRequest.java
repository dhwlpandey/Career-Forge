package com.dhwl.careerforge.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
public class ProfileCreateRequest {

    @Size(max = 20, message = "Phone number is too long")
    private String phone;

    @Size(max = 100, message = "Location is too long")
    private String location;

    @Size(max = 500, message = "Bio is too long")
    private String bio;

    @Size(max = 255, message = "GitHub URL is too long")
    private String githubUrl;

    @Size(max = 255, message = "LinkedIn URL is too long")
    private String linkedinUrl;

}
