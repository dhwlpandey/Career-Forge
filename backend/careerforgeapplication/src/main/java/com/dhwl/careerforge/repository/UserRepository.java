package com.dhwl.careerforge.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dhwl.careerforge.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
    
    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);
}
