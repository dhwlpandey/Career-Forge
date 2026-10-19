package com.dhwl.careerforge.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
public class ApplicationUpdateRequest {

    @Size(max = 5000, message = "Cover letter is too long")
    private String coverLetter;

    @Size(max = 2000, message = "Notes are too long")
    private String notes;
}
