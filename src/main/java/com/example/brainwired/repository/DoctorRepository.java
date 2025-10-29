package com.example.brainwired.repository;

import com.example.brainwired.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByEmail(String email);
    Page<Doctor> findBySpecializationIgnoreCase(String specialization, Pageable pageable);
    boolean existsByEmail(String email);
}