package com.dhwl.careerforge.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
public class UserUpdateRequest {

    @NotBlank (message = "Name is required")
    private String name;

    @NotBlank (message = "Email is required")
    @Email (message = "Email must be valid")
    private String email;
    
}
