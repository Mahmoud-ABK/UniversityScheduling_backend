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
    private int heures;  // Total teaching hours

    // List of SeanceDTO objects representing sessions taught by the teacher
    private List<SeanceDTO> seances;

    // List of PropositionDeRattrapageDTO objects representing proposed catch-up sessions
    private List<PropositionDeRattrapageDTO> propositionsDeRattrapage;
}
