package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Data Transfer Object for Technicien entity.
 * Extends PersonneDTO to include additional attributes specific to a Technician.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TechnicienDTO extends PersonneDTO {
    private String codeTechnicien;  // Unique code for the technician
}
