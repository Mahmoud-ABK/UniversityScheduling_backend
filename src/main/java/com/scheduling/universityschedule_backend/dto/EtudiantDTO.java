package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Data Transfer Object for Etudiant entity.
 * Extends PersonneDTO to include additional attributes specific to a Student.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EtudiantDTO extends PersonneDTO {
    private String matricule;  // Student ID

    // Associated BrancheDTO object representing the student's program or specialization
    private BrancheDTO branche;

    // Associated TPDTO object representing the student's practical session
    private TPDTO tp;
}
