package com.example.brainwired.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import com.example.brainwired.validation.FutureDateTime;

import java.time.LocalDateTime;

@Data
public class AppointmentRequest {
    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    @NotNull(message = "Appointment time is required")
    @FutureDateTime
    private LocalDateTime appointmentTime;
}