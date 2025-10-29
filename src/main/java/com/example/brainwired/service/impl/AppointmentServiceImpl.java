package com.example.brainwired.service.impl;

import com.example.brainwired.dto.AppointmentRequest;
import com.example.brainwired.dto.AppointmentResponse;
import com.example.brainwired.entity.Appointment;
import com.example.brainwired.entity.AppointmentStatus;
import com.example.brainwired.entity.Doctor;
import com.example.brainwired.entity.Patient;
import com.example.brainwired.exception.BadRequestException;
import com.example.brainwired.exception.ResourceNotFoundException;
import com.example.brainwired.repository.AppointmentRepository;
import com.example.brainwired.service.AppointmentService;
import com.example.brainwired.service.DoctorService;
import com.example.brainwired.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;

    @Override
    @Transactional
    public AppointmentResponse bookAppointment(AppointmentRequest request) {
        Doctor doctor = doctorService.getDoctorById(request.getDoctorId());
        Patient patient = patientService.getPatientById(request.getPatientId());

        // Validate appointment time
        if (request.getAppointmentTime().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Appointment time must be in the future");
        }

        // Check if slot is available
        if (!doctorService.isSlotAvailable(doctor.getId(), request.getAppointmentTime())) {
            throw new BadRequestException("Selected time slot is not available");
        }

        // Check for double booking
        if (appointmentRepository.existsByDoctorIdAndAppointmentTime(doctor.getId(), request.getAppointmentTime())) {
            throw new BadRequestException("Doctor is already booked for this time slot");
        }

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        appointment = appointmentRepository.save(appointment);
        return convertToResponse(appointment);
    }

    @Override
    @Transactional
    public void cancelAppointment(Long appointmentId, Long userId) {
        Appointment appointment = getAppointmentById(appointmentId);
        
        // Verify that the cancellation is requested by either the doctor or the patient
        if (!appointment.getDoctor().getId().equals(userId) && !appointment.getPatient().getId().equals(userId)) {
            throw new BadRequestException("Only the doctor or patient can cancel this appointment");
        }

        if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
            throw new BadRequestException("Only scheduled appointments can be cancelled");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }

    @Override
    public List<AppointmentResponse> getPatientAppointments(Long patientId) {
        // Verify patient exists
        patientService.getPatientById(patientId);
        return appointmentRepository.findByPatientId(patientId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponse> getDoctorAppointments(Long doctorId) {
        // Verify doctor exists
        doctorService.getDoctorById(doctorId);
        return appointmentRepository.findByDoctorId(doctorId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addPrescription(Long appointmentId, Long doctorId, String notes) {
        Appointment appointment = getAppointmentById(appointmentId);

        if (!appointment.getDoctor().getId().equals(doctorId)) {
            throw new BadRequestException("Only the assigned doctor can add prescription");
        }

        if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
            throw new BadRequestException("Can only add prescription to scheduled appointments");
        }

        appointment.setNotes(notes);
        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);
    }

    @Override
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));
    }

    private AppointmentResponse convertToResponse(Appointment appointment) {
        AppointmentResponse response = new AppointmentResponse();
        response.setId(appointment.getId());
        response.setDoctorId(appointment.getDoctor().getId());
        response.setDoctorName(appointment.getDoctor().getName());
        response.setPatientId(appointment.getPatient().getId());
        response.setPatientName(appointment.getPatient().getName());
        response.setAppointmentTime(appointment.getAppointmentTime());
        response.setStatus(appointment.getStatus());
        response.setNotes(appointment.getNotes());
        return response;
    }
}