package com.scheduling.universityschedule_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Data Transfer Object for TP entity.
 * Represents hands-on practical or lab sessions.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TPDTO {
    private Long id;  // Unique identifier for the practical session
    private int nb;   // Number of practical sessions

    // ID of the associated TD representing the tutorial session
    private Long tdId;

    // List of Etudiant IDs representing students enrolled in the practical session
    private List<Long> etudiantIds;
}
