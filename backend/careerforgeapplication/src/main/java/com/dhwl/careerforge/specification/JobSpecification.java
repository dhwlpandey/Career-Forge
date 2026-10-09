package com.dhwl.careerforge.specification;

import org.springframework.data.jpa.domain.Specification;

import com.dhwl.careerforge.entity.EmploymentType;
import com.dhwl.careerforge.entity.ExperienceLevel;
import com.dhwl.careerforge.entity.Job;

public class JobSpecification {

    public static Specification<Job> hasLocation(String location) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("location")),
                        "%" + location.toLowerCase() + "%"
                );
    }

    public static Specification<Job> hasEmploymentType(
            EmploymentType employmentType) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("employmentType"),
                        employmentType
                );
    }

    public static Specification<Job> hasExperienceLevel(
            ExperienceLevel experienceLevel) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("experienceLevel"),
                        experienceLevel
                );
    }

    public static Specification<Job> isActive() {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isTrue(root.get("isActive"));
    }
}
