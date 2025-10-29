package com.example.brainwired.controller;

import com.example.brainwired.dto.*;
import com.example.brainwired.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register/doctor")
    @Operation(summary = "Register a new doctor")
    public ResponseEntity<ApiResponse<AuthResponse>> registerDoctor(
            @Valid @RequestBody DoctorRegistrationRequest request) {
        AuthResponse response = authService.registerDoctor(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Doctor registered successfully", response));
    }

    @PostMapping("/register/patient")
    @Operation(summary = "Register a new patient")
    public ResponseEntity<ApiResponse<AuthResponse>> registerPatient(
            @Valid @RequestBody PatientRegistrationRequest request) {
        AuthResponse response = authService.registerPatient(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Patient registered successfully", response));
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user and generate token")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", response));
    }
}