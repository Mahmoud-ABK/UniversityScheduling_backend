package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PropositionDeRattrapageDTO {
    private Long id;
    private LocalDateTime date;
    private String reason;
    private String status;
    // Represent the associated Enseignant by its ID
    private Long enseignantId;
}
