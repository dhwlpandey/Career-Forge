package com.dhwl.careerforge.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
public class UserCreateRequest {

    private String name;

    private String email;

    private String password;
    
}
