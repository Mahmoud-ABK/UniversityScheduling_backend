package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

/**
 * Data Transfer Object for Enseignant entity.
 * Extends PersonneDTO to include additional attributes specific to a Teacher.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EnseignantDTO extends PersonneDTO {
    private String codeEnseignant;  // Unique code for the teacher
    private int heures;             // Total teaching hours

    // List of Seance IDs representing sessions taught by the teacher
    private List<Long> seanceIds;

    // List of PropositionDeRattrapage IDs representing proposed catch-up sessions
    private List<Long> propositionIds;
}
