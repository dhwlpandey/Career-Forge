package com.dhwl.careerforge.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dhwl.careerforge.dto.ErrorResponse;

@RestControllerAdvice 
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFound(
            UserNotFoundException ex) {

        return new ErrorResponse(
                404,
                ex.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        ErrorResponse response = new ErrorResponse(
                400,
                "Validation failed",
                LocalDateTime.now()
        );

        response.setErrors(errors);

        return response;
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleEmailAlreadyExists(
            EmailAlreadyExistsException ex) {

        return new ErrorResponse(
                409,
                ex.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(ProfileNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleProfileNotFound(
            ProfileNotFoundException ex) {

        return new ErrorResponse(
                404,
                ex.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(SkillAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleSkillAlreadyExists(
            SkillAlreadyExistsException ex) {

        return new ErrorResponse(
                409,
                ex.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(SkillNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleSkillNotFound(
            SkillNotFoundException ex) {

        return new ErrorResponse(
                404,
                ex.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleBadCredentials(
            BadCredentialsException ex) {

        return new ErrorResponse(
                401,
                "Invalid email or password",
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(ProfileAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleProfileAlreadyExists(
            ProfileAlreadyExistsException ex) {

        return new ErrorResponse(
                409,
                ex.getMessage(),
                LocalDateTime.now()
        );
    }


}
