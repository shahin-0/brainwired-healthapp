package com.example.brainwired.service;

import com.example.brainwired.dto.AuthResponse;
import com.example.brainwired.dto.DoctorRegistrationRequest;
import com.example.brainwired.dto.LoginRequest;
import com.example.brainwired.dto.PatientRegistrationRequest;

public interface AuthService {
    AuthResponse registerDoctor(DoctorRegistrationRequest request);
    AuthResponse registerPatient(PatientRegistrationRequest request);
    AuthResponse login(LoginRequest request);
}