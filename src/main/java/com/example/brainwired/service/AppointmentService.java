package com.example.brainwired.service;

import com.example.brainwired.dto.AppointmentRequest;
import com.example.brainwired.dto.AppointmentResponse;
import com.example.brainwired.entity.Appointment;

import java.util.List;

public interface AppointmentService {
    AppointmentResponse bookAppointment(AppointmentRequest request);
    void cancelAppointment(Long appointmentId, Long userId);
    List<AppointmentResponse> getPatientAppointments(Long patientId);
    List<AppointmentResponse> getDoctorAppointments(Long doctorId);
    void addPrescription(Long appointmentId, Long doctorId, String notes);
    Appointment getAppointmentById(Long id);
}