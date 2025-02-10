package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for PropositionDeRattrapage entity.
 * Represents a proposal submitted by a teacher to schedule a make-up session.
 */
@Data
public class PropositionDeRattrapageDTO {
    private Long id;  // Unique identifier for the proposal
    private LocalDateTime date;  // Proposed date for the catch-up session
    private String reason;  // Explanation for the catch-up session
    private String status;  // Current status of the proposal (e.g., pending, approved, rejected)

    // Associated EnseignantDTO object representing the teacher who proposed the catch-up session
    private EnseignantDTO enseignant;
}
