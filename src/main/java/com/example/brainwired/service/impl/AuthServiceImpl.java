package com.example.brainwired.service.impl;

import com.example.brainwired.dto.AuthResponse;
import com.example.brainwired.dto.DoctorRegistrationRequest;
import com.example.brainwired.dto.LoginRequest;
import com.example.brainwired.dto.PatientRegistrationRequest;
import com.example.brainwired.entity.Doctor;
import com.example.brainwired.entity.Patient;
import com.example.brainwired.exception.BadRequestException;
import com.example.brainwired.repository.DoctorRepository;
import com.example.brainwired.repository.PatientRepository;
import com.example.brainwired.security.JwtUtil;
import com.example.brainwired.security.UserRole;
import com.example.brainwired.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse registerDoctor(DoctorRegistrationRequest request) {
        if (doctorRepository.existsByEmail(request.getEmail()) || patientRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }

        Doctor doctor = new Doctor();
        doctor.setName(request.getName());
        doctor.setEmail(request.getEmail());
        doctor.setPhone(request.getPhone());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setPassword(passwordEncoder.encode(request.getPassword()));

        doctorRepository.save(doctor);

        String token = jwtUtil.generateToken(new org.springframework.security.core.userdetails.User(
                doctor.getEmail(),
                doctor.getPassword(),
                java.util.Collections.singletonList(new SimpleGrantedAuthority(UserRole.ROLE_DOCTOR.name()))
        ));

        return new AuthResponse(token, UserRole.ROLE_DOCTOR.name());
    }

    @Override
    public AuthResponse registerPatient(PatientRegistrationRequest request) {
        if (doctorRepository.existsByEmail(request.getEmail()) || patientRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }

        Patient patient = new Patient();
        patient.setName(request.getName());
        patient.setEmail(request.getEmail());
        patient.setPhone(request.getPhone());
        patient.setDob(request.getDob());
        patient.setMedicalHistory(request.getMedicalHistory());
        patient.setPassword(passwordEncoder.encode(request.getPassword()));

        patientRepository.save(patient);

        String token = jwtUtil.generateToken(new org.springframework.security.core.userdetails.User(
                patient.getEmail(),
                patient.getPassword(),
                java.util.Collections.singletonList(new SimpleGrantedAuthority(UserRole.ROLE_PATIENT.name()))
        ));

        return new AuthResponse(token, UserRole.ROLE_PATIENT.name());
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String role = authentication.getAuthorities().iterator().next().getAuthority();
        String token = jwtUtil.generateToken((org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal());

        return new AuthResponse(token, role);
    }
}