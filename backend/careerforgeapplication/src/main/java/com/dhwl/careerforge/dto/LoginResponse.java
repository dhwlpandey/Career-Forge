package com.dhwl.careerforge.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
public class LoginResponse {

    private Long userId;

    private String name;

    private String email;

    private String token;
}
