package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SignalDTO {
    private Long id;
    private String message;
    private String severity;
    private LocalDateTime timestamp;
}
