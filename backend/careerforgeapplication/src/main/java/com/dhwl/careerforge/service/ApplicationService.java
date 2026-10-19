package com.dhwl.careerforge.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dhwl.careerforge.dto.ApplicationCreateRequest;
import com.dhwl.careerforge.dto.ApplicationResponse;
import com.dhwl.careerforge.dto.ApplicationStatusUpdateRequest;
import com.dhwl.careerforge.dto.ApplicationUpdateRequest;
import com.dhwl.careerforge.entity.Application;
import com.dhwl.careerforge.entity.ApplicationStatus;
import com.dhwl.careerforge.entity.Job;
import com.dhwl.careerforge.entity.User;
import com.dhwl.careerforge.exception.ApplicationAlreadyExistsException;
import com.dhwl.careerforge.exception.ApplicationNotFoundException;
import com.dhwl.careerforge.exception.InvalidApplicationStateException;
import com.dhwl.careerforge.exception.JobNotFoundException;
import com.dhwl.careerforge.repository.ApplicationRepository;
import com.dhwl.careerforge.repository.JobRepository;
import com.dhwl.careerforge.security.AuthenticatedUserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final AuthenticatedUserService authenticatedUserService;

    public ApplicationResponse applyToJob (Long jobId, ApplicationCreateRequest request) {
        
        User user = authenticatedUserService.getCurrentUser();

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new JobNotFoundException("Job not found"));

        if (!job.isActive()) {
            throw new JobNotFoundException("Job is not available");
        }

        if (applicationRepository.existsByUserIdAndJobId(
                user.getId(), jobId)) {

            throw new ApplicationAlreadyExistsException(
                    "You have already applied to this job");
        }

        LocalDateTime now = LocalDateTime.now();

        Application application = new Application();
        application.setUser(user);
        application.setJob(job);
        application.setStatus(ApplicationStatus.APPLIED);
        application.setAppliedAt(now);
        application.setCreatedAt(now);
        application.setUpdatedAt(now);
        application.setCoverLetter(request.getCoverLetter());
        application.setResumeUrl(request.getResumeUrl());
        application.setNotes(request.getNotes());

        Application savedApplication =
                applicationRepository.save(application);

        return toResponse(savedApplication);
    }
    
    private ApplicationResponse toResponse(
            Application application) {

        ApplicationResponse response = new ApplicationResponse();

        response.setId(application.getId());
        response.setUserId(application.getUser().getId());
        response.setJobId(application.getJob().getId());
        response.setJobTitle(application.getJob().getTitle());
        response.setCompanyName(application.getJob().getCompanyName());
        response.setStatus(application.getStatus());
        response.setAppliedAt(application.getAppliedAt());
        response.setCoverLetter(application.getCoverLetter());
        response.setResumeUrl(application.getResumeUrl());
        response.setNotes(application.getNotes());
        response.setCreatedAt(application.getCreatedAt());
        response.setUpdatedAt(application.getUpdatedAt());

        return response;
    }

    private boolean isValidStatusTransition(
        ApplicationStatus current,
        ApplicationStatus next) {

        return switch (current) {
                case APPLIED ->
                        next == ApplicationStatus.SHORTLISTED
                        || next == ApplicationStatus.INTERVIEW
                        || next == ApplicationStatus.SELECTED
                        || next == ApplicationStatus.REJECTED;

                case SHORTLISTED ->
                        next == ApplicationStatus.INTERVIEW
                        || next == ApplicationStatus.SELECTED
                        || next == ApplicationStatus.REJECTED;

                case INTERVIEW ->
                        next == ApplicationStatus.SELECTED
                        || next == ApplicationStatus.REJECTED;

                case SELECTED, REJECTED, WITHDRAWN -> false;
        };
        }

    @Transactional(readOnly = true)
    public Page<ApplicationResponse> getMyApplications(Pageable pageable) {

        User user = authenticatedUserService.getCurrentUser();

        return applicationRepository
                .findByUserId(user.getId(), pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public ApplicationResponse getMyApplicationById(Long applicationId) {

        User user = authenticatedUserService.getCurrentUser();

        Application application = applicationRepository
                .findByIdAndUserId(applicationId, user.getId())
                .orElseThrow(() ->
                        new ApplicationNotFoundException(
                                "Application not found"
                        ));

        return toResponse(application);
    }

    @Transactional
    public ApplicationResponse updateMyApplication(
            Long applicationId,
            ApplicationUpdateRequest request) {

        User user = authenticatedUserService.getCurrentUser();

        Application application = applicationRepository
                .findByIdAndUserId(applicationId, user.getId())
                .orElseThrow(() ->
                        new ApplicationNotFoundException(
                                "Application not found"
                        ));

        if (application.getStatus() == ApplicationStatus.WITHDRAWN) {
                throw new InvalidApplicationStateException(
                        "A withdrawn application cannot be updated"
                );
        }

        application.setCoverLetter(request.getCoverLetter());
        application.setNotes(request.getNotes());
        application.setUpdatedAt(LocalDateTime.now());

        return toResponse(applicationRepository.save(application));
    }

    @Transactional
        public ApplicationResponse withdrawMyApplication(Long applicationId) {

        User user = authenticatedUserService.getCurrentUser();

        Application application = applicationRepository
                .findByIdAndUserId(applicationId, user.getId())
                .orElseThrow(() ->
                        new ApplicationNotFoundException(
                                "Application not found"
                        ));

        if (application.getStatus() == ApplicationStatus.WITHDRAWN) {
                throw new InvalidApplicationStateException(
                        "Application has already been withdrawn"
                );
        }

        if (application.getStatus() == ApplicationStatus.SELECTED
                || application.getStatus() == ApplicationStatus.REJECTED) {
                throw new InvalidApplicationStateException(
                        "This application can no longer be withdrawn"
                );
        }

        application.setStatus(ApplicationStatus.WITHDRAWN);
        application.setUpdatedAt(LocalDateTime.now());

        return toResponse(applicationRepository.save(application));
        }

        @Transactional(readOnly = true)
        public Page<ApplicationResponse> getAllApplications(
                Pageable pageable) {

        return applicationRepository
                .findAll(pageable)
                .map(this::toResponse);
        }

        @Transactional
        public ApplicationResponse updateApplicationStatus(
                Long applicationId,
                ApplicationStatusUpdateRequest request) {

        Application application = applicationRepository
                .findById(applicationId)
                .orElseThrow(() ->
                        new ApplicationNotFoundException(
                                "Application not found"
                        ));

        ApplicationStatus currentStatus = application.getStatus();
        ApplicationStatus newStatus = request.getStatus();

        if (newStatus == ApplicationStatus.WITHDRAWN) {
                throw new InvalidApplicationStateException(
                        "Only candidates can withdraw applications"
                );
        }

        if (!isValidStatusTransition(currentStatus, newStatus)) {
                throw new InvalidApplicationStateException(
                        "Invalid application status transition from "
                                + currentStatus + " to " + newStatus
                );
        }

        application.setStatus(newStatus);
        application.setUpdatedAt(LocalDateTime.now());

        return toResponse(applicationRepository.save(application));
        }
}
