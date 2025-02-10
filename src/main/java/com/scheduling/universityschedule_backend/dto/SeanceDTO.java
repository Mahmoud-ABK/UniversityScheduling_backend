package com.scheduling.universityschedule_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Data Transfer Object for Seance entity.
 * Represents a scheduled teaching session (lecture, tutorial, practical).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeanceDTO {
    private Long id;  // Unique identifier for the session
    private String jour;  // Day of the session
    private String heureDebut;  // Start time of the session
    private String heureFin;  // End time of the session
    private String type;  // Type of session (e.g., CR, CI, TD, TP)
    private String matiere;  // Subject matter
    private String frequence;  // Frequency of the session (e.g., weekly, biweekly, specific date for catch-up)

    // Associated SalleDTO object representing the room assigned to the session
    private SalleDTO salle;

    // Associated EnseignantDTO object representing the teacher assigned to the session
    private EnseignantDTO enseignant;

    // Lists of associated DTOs for branches, tutorials, and practicals
    private List<BrancheDTO> branches;
    private List<TDDTO> tds;
    private List<TPDTO> tps;
}
