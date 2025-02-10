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
    private int nb;  // Number of practical sessions

    // Associated TDDTO object representing the tutorial session
    private TDDTO td;

    // List of associated EtudiantDTO objects representing students enrolled in the practical session
    private List<EtudiantDTO> etudiants;
}
