package com.example.brainwired.service;

import com.example.brainwired.entity.Patient;

public interface PatientService {
    Patient getPatientById(Long id);
    void updateMedicalHistory(Long id, String medicalHistory);
}