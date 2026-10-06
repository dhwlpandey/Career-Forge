package com.dhwl.careerforge.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dhwl.careerforge.entity.User;

@Service 
public class AuthenticatedUserService {

    public User getCurrentUser () {

        Authentication authentication = 
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !(authentication.getPrincipal()
                        instanceof CustomUserDetails)) {

            throw new IllegalStateException(
                    "No authenticated user found"
            );
                    }
                        
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        return userDetails.getUser();
    }

    public Long getCurrentUserId () {

        return getCurrentUser().getId();
    }

}
