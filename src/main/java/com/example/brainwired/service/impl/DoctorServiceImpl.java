package com.example.brainwired.service.impl;

import com.example.brainwired.entity.Doctor;
import com.example.brainwired.exception.ResourceNotFoundException;
import com.example.brainwired.repository.DoctorRepository;
import com.example.brainwired.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    @Override
    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));
    }

    @Override
    public Page<Doctor> getDoctorsBySpecialization(String specialization, Pageable pageable) {
        return doctorRepository.findBySpecializationIgnoreCase(specialization, pageable);
    }

    @Override
    public List<LocalDateTime> getDoctorAvailability(Long doctorId) {
        Doctor doctor = getDoctorById(doctorId);
        return doctor.getAvailableSlots();
    }

    @Override
    public void updateDoctorAvailability(Long doctorId, List<LocalDateTime> availableSlots) {
        Doctor doctor = getDoctorById(doctorId);
        doctor.setAvailableSlots(availableSlots);
        doctorRepository.save(doctor);
    }

    @Override
    public boolean isSlotAvailable(Long doctorId, LocalDateTime slot) {
        Doctor doctor = getDoctorById(doctorId);
        return doctor.getAvailableSlots().contains(slot);
    }
}