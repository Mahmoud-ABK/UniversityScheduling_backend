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

    // ID of the associated Branche representing the student's program or specialization
    private Long brancheId;

    // ID of the associated TP representing the student's practical session
    private Long tpId;
}
