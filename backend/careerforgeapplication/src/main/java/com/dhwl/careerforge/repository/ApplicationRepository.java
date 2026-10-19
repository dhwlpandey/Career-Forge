package com.dhwl.careerforge.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dhwl.careerforge.entity.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    boolean existsByUserIdAndJobId(Long userId, Long jobId);

    Page<Application> findByUserId(Long userId, Pageable pageable);

    Optional<Application> findByIdAndUserId(Long id, Long userId);

    Page<Application> findAll(Pageable pageable);
}
