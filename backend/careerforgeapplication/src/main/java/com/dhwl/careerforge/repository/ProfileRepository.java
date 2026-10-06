package com.dhwl.careerforge.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dhwl.careerforge.entity.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Long>{

    Optional<Profile> findByUserId(Long userId);
}
