package com.example.brainwired.controller;

import com.example.brainwired.dto.ApiResponse;
import com.example.brainwired.entity.Doctor;
import com.example.brainwired.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
@Tag(name = "Doctors", description = "Doctor management APIs")
@SecurityRequirement(name = "bearerAuth")
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping("/specialization/{specialization}")
    @Operation(summary = "Search doctors by specialization")
    public ResponseEntity<ApiResponse<Page<Doctor>>> getDoctorsBySpecialization(
            @PathVariable String specialization,
            Pageable pageable) {
        Page<Doctor> doctors = doctorService.getDoctorsBySpecialization(specialization, pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "Doctors retrieved successfully", doctors));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get doctor by ID")
    public ResponseEntity<ApiResponse<Doctor>> getDoctorById(@PathVariable Long id) {
        Doctor doctor = doctorService.getDoctorById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Doctor retrieved successfully", doctor));
    }

    @GetMapping("/{id}/availability")
    @Operation(summary = "Get doctor's available slots")
    public ResponseEntity<ApiResponse<List<LocalDateTime>>> getDoctorAvailability(@PathVariable Long id) {
        List<LocalDateTime> slots = doctorService.getDoctorAvailability(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Available slots retrieved successfully", slots));
    }

    @PutMapping("/{id}/availability")
    @PreAuthorize("hasRole('ROLE_DOCTOR') and #id == authentication.principal.id")
    @Operation(summary = "Update doctor's available slots")
    public ResponseEntity<ApiResponse<Void>> updateDoctorAvailability(
            @PathVariable Long id,
            @RequestBody List<LocalDateTime> availableSlots) {
        doctorService.updateDoctorAvailability(id, availableSlots);
        return ResponseEntity.ok(new ApiResponse<>(true, "Availability updated successfully"));
    }
}