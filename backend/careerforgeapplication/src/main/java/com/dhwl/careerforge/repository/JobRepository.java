package com.dhwl.careerforge.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.dhwl.careerforge.entity.EmploymentType;
import com.dhwl.careerforge.entity.ExperienceLevel;
import com.dhwl.careerforge.entity.Job;

public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {

    Page<Job> findByTitleContainingIgnoreCaseOrCompanyNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
        String title,
        String companyName,
        String description,
        Pageable pageable
    );

    Page<Job> findByIsActiveTrue(Pageable pageable);

    Page<Job> findByIsActiveTrueAndLocationContainingIgnoreCase(
            String location,
            Pageable pageable
    );

    Page<Job> findByIsActiveTrueAndEmploymentType(
            EmploymentType employmentType,
            Pageable pageable
    );

    Page<Job> findByIsActiveTrueAndExperienceLevel(
            ExperienceLevel experienceLevel,
            Pageable pageable
    );
}
