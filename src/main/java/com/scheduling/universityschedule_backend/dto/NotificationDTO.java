package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationDTO {
    private Long id;
    private String message;
    private LocalDateTime date;
    private String type;
    private Boolean read;
    // Represent associated Personne entities by their IDs
    private Long recepteurId;
    private Long expediteurId;
}
