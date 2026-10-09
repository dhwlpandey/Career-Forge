package com.dhwl.careerforge.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.dhwl.careerforge.dto.JobCreateRequest;
import com.dhwl.careerforge.dto.JobResponse;
import com.dhwl.careerforge.dto.JobUpdateRequest;
import com.dhwl.careerforge.entity.EmploymentType;
import com.dhwl.careerforge.entity.ExperienceLevel;
import com.dhwl.careerforge.entity.Job;
import com.dhwl.careerforge.exception.JobNotFoundException;
import com.dhwl.careerforge.repository.JobRepository;
import com.dhwl.careerforge.specification.JobSpecification;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class JobService {

    private final JobRepository jobRepository;

    public JobResponse createJob (JobCreateRequest request) {

        Job job = new Job();

        job.setTitle(request.getTitle());
        job.setCompanyName(request.getCompanyName());
        job.setLocation(request.getLocation());
        job.setDescription(request.getDescription());
        job.setEmploymentType(request.getEmploymentType());
        job.setExperienceLevel(request.getExperienceLevel());
        job.setSalaryMin(request.getSalaryMin());
        job.setSalaryMax(request.getSalaryMax());
        job.setApplicationUrl(request.getApplicationUrl());
        job.setDeadline(request.getDeadline());

        LocalDateTime now = LocalDateTime.now();

        job.setPostedAt(now);
        job.setCreatedAt(now);
        job.setUpdatedAt(now);
        job.setActive(true);

        Job savedJob = jobRepository.save(job);

        return toResponse(savedJob);
    }

    public JobResponse getJobById(Long id) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() ->
                        new JobNotFoundException("Job not found"));

        return toResponse(job);
    }

    public JobResponse updateJob(Long id, JobUpdateRequest request) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() ->
                        new JobNotFoundException("Job not found"));

        job.setTitle(request.getTitle());
        job.setCompanyName(request.getCompanyName());
        job.setLocation(request.getLocation());
        job.setDescription(request.getDescription());
        job.setEmploymentType(request.getEmploymentType());
        job.setExperienceLevel(request.getExperienceLevel());
        job.setSalaryMin(request.getSalaryMin());
        job.setSalaryMax(request.getSalaryMax());
        job.setApplicationUrl(request.getApplicationUrl());
        job.setDeadline(request.getDeadline());
        job.setActive(request.isActive());

        job.setUpdatedAt(LocalDateTime.now());

        Job updatedJob = jobRepository.save(job);

        return toResponse(updatedJob);
    }

    public void deleteJob(Long id) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() ->
                        new JobNotFoundException("Job not found"));

        jobRepository.delete(job);
    }

    private JobResponse toResponse(Job job) {

        JobResponse response = new JobResponse();

        response.setId(job.getId());
        response.setTitle(job.getTitle());
        response.setCompanyName(job.getCompanyName());
        response.setLocation(job.getLocation());
        response.setDescription(job.getDescription());
        response.setEmploymentType(job.getEmploymentType());
        response.setExperienceLevel(job.getExperienceLevel());
        response.setSalaryMin(job.getSalaryMin());
        response.setSalaryMax(job.getSalaryMax());
        response.setApplicationUrl(job.getApplicationUrl());
        response.setPostedAt(job.getPostedAt());
        response.setDeadline(job.getDeadline());
        response.setActive(job.isActive());
        response.setCreatedAt(job.getCreatedAt());
        response.setUpdatedAt(job.getUpdatedAt());

        return response;
    }

    public Page<JobResponse> getAllJobs(Pageable pageable) {

        return jobRepository.findAll(pageable)
                .map(this::toResponse);
    }

    public Page<JobResponse> searchJobs(
        String keyword,
        Pageable pageable) {

        return jobRepository
                .findByTitleContainingIgnoreCaseOrCompanyNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                        keyword,
                        keyword,
                        keyword,
                        pageable
                )
                .map(this::toResponse);
    }

    public Page<JobResponse> filterJobs(
        String location,
        EmploymentType employmentType,
        ExperienceLevel experienceLevel,
        Pageable pageable) {
            
            Specification<Job> specification = JobSpecification.isActive();

            if (location != null && !location.isBlank()) {
                specification = specification.and(
                        JobSpecification.hasLocation(location)
                );
            }

            if (employmentType != null) {
                specification = specification.and(
                        JobSpecification.hasEmploymentType(employmentType)
                );
            }

            if (experienceLevel != null) {
                specification = specification.and(
                        JobSpecification.hasExperienceLevel(experienceLevel)
                );
            }

            return jobRepository
                    .findAll(specification, pageable)
                    .map(this::toResponse);
        }
}
