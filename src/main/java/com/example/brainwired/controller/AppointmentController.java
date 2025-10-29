package com.example.brainwired.controller;

import com.example.brainwired.dto.ApiResponse;
import com.example.brainwired.dto.AppointmentRequest;
import com.example.brainwired.dto.AppointmentResponse;
import com.example.brainwired.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@Tag(name = "Appointments", description = "Appointment management APIs")
@SecurityRequirement(name = "bearerAuth")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping("/book")
    @PreAuthorize("hasRole('ROLE_PATIENT') and #request.patientId == authentication.principal.id")
    @Operation(summary = "Book a new appointment")
    public ResponseEntity<ApiResponse<AppointmentResponse>> bookAppointment(
            @Valid @RequestBody AppointmentRequest request) {
        AppointmentResponse response = appointmentService.bookAppointment(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Appointment booked successfully", response));
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel an appointment")
    public ResponseEntity<ApiResponse<Void>> cancelAppointment(
            @PathVariable Long id,
            @RequestParam Long userId) {
        appointmentService.cancelAppointment(id, userId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Appointment cancelled successfully"));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasRole('ROLE_DOCTOR') or #patientId == authentication.principal.id")
    @Operation(summary = "Get patient's appointments")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getPatientAppointments(
            @PathVariable Long patientId) {
        List<AppointmentResponse> appointments = appointmentService.getPatientAppointments(patientId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Appointments retrieved successfully", appointments));
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasRole('ROLE_DOCTOR') and #doctorId == authentication.principal.id")
    @Operation(summary = "Get doctor's appointments")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getDoctorAppointments(
            @PathVariable Long doctorId) {
        List<AppointmentResponse> appointments = appointmentService.getDoctorAppointments(doctorId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Appointments retrieved successfully", appointments));
    }

    @PostMapping("/{id}/prescription")
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @Operation(summary = "Add prescription to a completed appointment")
    public ResponseEntity<ApiResponse<Void>> addPrescription(
            @PathVariable Long id,
            @RequestParam Long doctorId,
            @RequestBody String notes) {
        appointmentService.addPrescription(id, doctorId, notes);
        return ResponseEntity.ok(new ApiResponse<>(true, "Prescription added successfully"));
    }
}