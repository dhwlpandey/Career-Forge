package com.dhwl.careerforge.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dhwl.careerforge.dto.ApplicationCreateRequest;
import com.dhwl.careerforge.dto.ApplicationResponse;
import com.dhwl.careerforge.dto.ApplicationStatusUpdateRequest;
import com.dhwl.careerforge.dto.ApplicationUpdateRequest;
import com.dhwl.careerforge.service.ApplicationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping ("/api/applications")
@RequiredArgsConstructor 
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping("/jobs/{jobId}")
    public ResponseEntity<ApplicationResponse> applyToJob(
            @PathVariable Long jobId,
            @Valid @RequestBody ApplicationCreateRequest request) {

        ApplicationResponse response =
                applicationService.applyToJob(jobId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/me")
        public ResponseEntity<Page<ApplicationResponse>> getMyApplications(
                Pageable pageable) {

        return ResponseEntity.ok(
                applicationService.getMyApplications(pageable)
        );
        }

        @GetMapping("/{applicationId}")
        public ResponseEntity<ApplicationResponse> getMyApplicationById(
                @PathVariable Long applicationId) {

        return ResponseEntity.ok(
                applicationService.getMyApplicationById(applicationId)
        );
        }

        @PutMapping("/{applicationId}")
        public ResponseEntity<ApplicationResponse> updateMyApplication(
                @PathVariable Long applicationId,
                @Valid @RequestBody ApplicationUpdateRequest request) {

        return ResponseEntity.ok(
                applicationService.updateMyApplication(
                        applicationId,
                        request
                )
        );
        }

        @PatchMapping("/{applicationId}/withdraw")
        public ResponseEntity<ApplicationResponse> withdrawMyApplication(
                @PathVariable Long applicationId) {

        return ResponseEntity.ok(
                applicationService.withdrawMyApplication(applicationId)
        );
        }

        @GetMapping("/admin")
        public ResponseEntity<Page<ApplicationResponse>> getAllApplications(
                Pageable pageable) {

        return ResponseEntity.ok(
                applicationService.getAllApplications(pageable)
        );
        }

        @PatchMapping("/admin/{applicationId}/status")
        public ResponseEntity<ApplicationResponse> updateApplicationStatus(
                @PathVariable Long applicationId,
                @Valid @RequestBody ApplicationStatusUpdateRequest request) {

        return ResponseEntity.ok(
                applicationService.updateApplicationStatus(
                        applicationId,
                        request
                )
        );
        }
}
