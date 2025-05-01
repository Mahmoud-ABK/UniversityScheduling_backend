package com.scheduling.universityschedule_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Data Transfer Object for Branche entity.
 * Represents an academic program or specialization.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrancheDTO {
    private Long id;           // Unique identifier for the Branche
    private String niveau;     // Level of study (e.g., undergraduate, graduate)
    private String specialite; // Specialization or major
    private int nbTD;          // Number of tutorial sessions
    private String departement;// Associated department

    // List of Seance IDs associated with this Branche
    private List<Long> seanceIds;

    private List<Long> tdIds;
}
