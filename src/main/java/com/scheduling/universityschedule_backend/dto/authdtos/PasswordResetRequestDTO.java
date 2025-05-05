package com.scheduling.universityschedule_backend.dto.authdtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordResetRequestDTO {
    @NotBlank(message = "Email is required")
    private String email;
}
