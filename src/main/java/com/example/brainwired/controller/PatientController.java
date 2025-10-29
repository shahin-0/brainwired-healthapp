package com.example.brainwired.controller;

import com.example.brainwired.dto.ApiResponse;
import com.example.brainwired.entity.Patient;
import com.example.brainwired.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Tag(name = "Patients", description = "Patient management APIs")
@SecurityRequirement(name = "bearerAuth")
public class PatientController {

    private final PatientService patientService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_DOCTOR') or #id == authentication.principal.id")
    @Operation(summary = "Get patient by ID")
    public ResponseEntity<ApiResponse<Patient>> getPatientById(@PathVariable Long id) {
        Patient patient = patientService.getPatientById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Patient retrieved successfully", patient));
    }

    @PutMapping("/{id}/medical-history")
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @Operation(summary = "Update patient's medical history")
    public ResponseEntity<ApiResponse<Void>> updateMedicalHistory(
            @PathVariable Long id,
            @RequestBody String medicalHistory) {
        patientService.updateMedicalHistory(id, medicalHistory);
        return ResponseEntity.ok(new ApiResponse<>(true, "Medical history updated successfully"));
    }
}