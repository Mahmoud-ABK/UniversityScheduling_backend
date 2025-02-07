package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class SignalDTO {
    @NotNull
    private Long id;

    @NotNull
    @Size(min = 5, max = 500)
    private String message;

    @NotNull
    private String severity;

    private LocalDateTime timestamp;
}
