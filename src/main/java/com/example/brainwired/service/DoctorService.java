package com.example.brainwired.service;

import com.example.brainwired.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface DoctorService {
    Doctor getDoctorById(Long id);
    Page<Doctor> getDoctorsBySpecialization(String specialization, Pageable pageable);
    List<LocalDateTime> getDoctorAvailability(Long doctorId);
    void updateDoctorAvailability(Long doctorId, List<LocalDateTime> availableSlots);
    boolean isSlotAvailable(Long doctorId, LocalDateTime slot);
}