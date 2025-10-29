package com.example.brainwired.service.impl;

import com.example.brainwired.entity.Patient;
import com.example.brainwired.exception.ResourceNotFoundException;
import com.example.brainwired.repository.PatientRepository;
import com.example.brainwired.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Override
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
    }

    @Override
    public void updateMedicalHistory(Long id, String medicalHistory) {
        Patient patient = getPatientById(id);
        patient.setMedicalHistory(medicalHistory);
        patientRepository.save(patient);
    }
}