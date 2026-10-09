package com.dhwl.careerforge.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dhwl.careerforge.dto.JobCreateRequest;
import com.dhwl.careerforge.dto.JobResponse;
import com.dhwl.careerforge.dto.JobUpdateRequest;
import com.dhwl.careerforge.entity.EmploymentType;
import com.dhwl.careerforge.entity.ExperienceLevel;
import com.dhwl.careerforge.service.JobService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping ("/api/jobs")
@RequiredArgsConstructor 
public class JobController {

    private final JobService jobService;

    @PostMapping
    public ResponseEntity<JobResponse> createJob(
            @Valid @RequestBody JobCreateRequest request) {

        JobResponse response = jobService.createJob(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<Page<JobResponse>> getAllJobs(
            Pageable pageable) {

        return ResponseEntity.ok(
                jobService.getAllJobs(pageable)
        );
    }

    @GetMapping("/search")
    public ResponseEntity<Page<JobResponse>> searchJobs(
            @RequestParam String keyword,
            Pageable pageable) {

        return ResponseEntity.ok(
                jobService.searchJobs(keyword, pageable)
        );
    }
    
    @GetMapping("/filter")
        public ResponseEntity<Page<JobResponse>> filterJobs(
                @RequestParam(required = false) String location,
                @RequestParam(required = false) EmploymentType employmentType,
                @RequestParam(required = false) ExperienceLevel experienceLevel,
                Pageable pageable) {

        return ResponseEntity.ok(
                jobService.filterJobs(
                        location,
                        employmentType,
                        experienceLevel,
                        pageable
                )
        );
}

    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getJobById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                jobService.getJobById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobResponse> updateJob(
            @PathVariable Long id,
            @Valid @RequestBody JobUpdateRequest request) {

        return ResponseEntity.ok(
                jobService.updateJob(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(
            @PathVariable Long id) {

        jobService.deleteJob(id);

        return ResponseEntity.noContent().build();
    }
    
}
