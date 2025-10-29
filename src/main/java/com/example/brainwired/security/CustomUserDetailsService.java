package com.example.brainwired.security;

import com.example.brainwired.entity.Doctor;
import com.example.brainwired.entity.Patient;
import com.example.brainwired.repository.DoctorRepository;
import com.example.brainwired.repository.PatientRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public CustomUserDetailsService(DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // First try to find a doctor
        Doctor doctor = doctorRepository.findByEmail(email).orElse(null);
        if (doctor != null) {
            return new User(doctor.getEmail(), doctor.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority(UserRole.ROLE_DOCTOR.name())));
        }

        // If not a doctor, try to find a patient
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new User(patient.getEmail(), patient.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(UserRole.ROLE_PATIENT.name())));
    }
}