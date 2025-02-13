package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Notification entity.
 * Represents messages sent to users about schedule changes, announcements, or alerts.
 */
@Data
public class NotificationDTO {
    private Long id;                // Unique identifier for the notification
    private String message;         // Content of the notification
    private LocalDateTime date;     // Date and time the notification was sent
    private String type;            // Type of notification (e.g., update, alert, reminder)
    private Boolean read;           // Indicates if the notification has been read

    // ID of the recipient (Personne)
    private Long recepteurId;

    // ID of the sender (Personne or system)
    private Long expediteurId;
}
