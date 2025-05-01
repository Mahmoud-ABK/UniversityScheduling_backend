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
    private Long id;            // Unique identifier for the session
    private String name;
    private String jour;        // Day of the session
    private String heureDebut;  // Start time of the session
    private String heureFin;    // End time of the session
    private String type;        // Type of session (e.g., CR, CI, TD, TP)
    private String matiere;     // Subject
    private String frequence; // Frequency of the session (e.g., weekly, biweekly, specific date for catch-up)
    private String date ;

    // ID of the Salle assigned to the session
    private Long salleId;

    // ID of the Enseignant assigned to the session
    private Long enseignantId;

    // List of Branche IDs associated with this session
    private List<Long> brancheIds;

    // List of TD IDs associated with this session
    private List<Long> tdIds;

    // List of TP IDs associated with this session
    private List<Long> tpIds;
}
