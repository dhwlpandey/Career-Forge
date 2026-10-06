package com.dhwl.careerforge.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.dhwl.careerforge.dto.LoginRequest;
import com.dhwl.careerforge.dto.LoginResponse;
import com.dhwl.careerforge.security.CustomUserDetails;
import com.dhwl.careerforge.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public LoginResponse login (LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String token = jwtService.generateToken(userDetails);

        return new LoginResponse(
            userDetails.getUser().getId(),
            userDetails.getUser().getName(),
            userDetails.getUser().getEmail(),
            token
        );
    }
}
