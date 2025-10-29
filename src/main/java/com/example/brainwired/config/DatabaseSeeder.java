package com.example.brainwired.config;

import com.example.brainwired.entity.Doctor;
import com.example.brainwired.entity.Patient;
import com.example.brainwired.repository.DoctorRepository;
import com.example.brainwired.repository.PatientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

@Configuration
public class DatabaseSeeder {

    @Bean
    CommandLineRunner initDatabase(
            DoctorRepository doctorRepository,
            PatientRepository patientRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            // Create sample doctors
            Doctor cardiologist = new Doctor();
            cardiologist.setName("Dr. John Smith");
            cardiologist.setSpecialization("Cardiologist");
            cardiologist.setEmail("john.smith@example.com");
            cardiologist.setPhone("+1234567890");
            cardiologist.setPassword(passwordEncoder.encode("Doctor123@"));
            cardiologist.setAvailableSlots(new ArrayList<>(Arrays.asList(
                LocalDateTime.now().plusDays(1).withHour(9).withMinute(0),
                LocalDateTime.now().plusDays(1).withHour(10).withMinute(0),
                LocalDateTime.now().plusDays(1).withHour(11).withMinute(0)
            )));

            Doctor neurologist = new Doctor();
            neurologist.setName("Dr. Sarah Johnson");
            neurologist.setSpecialization("Neurologist");
            neurologist.setEmail("sarah.johnson@example.com");
            neurologist.setPhone("+1234567891");
            neurologist.setPassword(passwordEncoder.encode("Doctor123@"));
            neurologist.setAvailableSlots(new ArrayList<>(Arrays.asList(
                LocalDateTime.now().plusDays(1).withHour(14).withMinute(0),
                LocalDateTime.now().plusDays(1).withHour(15).withMinute(0),
                LocalDateTime.now().plusDays(1).withHour(16).withMinute(0)
            )));

            doctorRepository.saveAll(Arrays.asList(cardiologist, neurologist));

            // Create sample patients
            Patient patient1 = new Patient();
            patient1.setName("Alice Brown");
            patient1.setEmail("alice.brown@example.com");
            patient1.setPhone("+1234567892");
            patient1.setPassword(passwordEncoder.encode("Patient123@"));
            patient1.setDob(LocalDate.of(1990, 1, 15));
            patient1.setMedicalHistory("No major issues");

            Patient patient2 = new Patient();
            patient2.setName("Bob Wilson");
            patient2.setEmail("bob.wilson@example.com");
            patient2.setPhone("+1234567893");
            patient2.setPassword(passwordEncoder.encode("Patient123@"));
            patient2.setDob(LocalDate.of(1985, 6, 20));
            patient2.setMedicalHistory("Mild hypertension");

            patientRepository.saveAll(Arrays.asList(patient1, patient2));
        };
    }
}