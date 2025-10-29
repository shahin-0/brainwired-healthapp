package com.example.brainwired.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import com.example.brainwired.validation.ValidPassword;

import java.time.LocalDate;

@Data
public class PatientRegistrationRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number")
    private String phone;

    @NotBlank(message = "Password is required")
    @ValidPassword
    private String password;

    @Past(message = "Date of birth must be in the past")
    @NotNull(message = "Date of birth is required")
    private LocalDate dob;

    private String medicalHistory;
}