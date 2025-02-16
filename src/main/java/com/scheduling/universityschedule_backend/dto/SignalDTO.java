package com.scheduling.universityschedule_backend.dto;

import com.scheduling.universityschedule_backend.model.Enseignant;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Signal entity.
 * Represents a message sent by a teacher to report a problem or suggest a change regarding their schedule.
 */
@Data
public class SignalDTO {
    private Long id;                // Unique identifier for the signal
    private String message;         // Details of the issue or suggestion
    private String severity;        // Importance level
    private LocalDateTime timestamp;// Date and time the signal was submitted

    private String enseignantId;
}
