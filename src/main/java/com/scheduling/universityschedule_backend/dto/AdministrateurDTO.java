package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Data Transfer Object for Administrateur entity.
 * Extends PersonneDTO to include additional attributes specific to an Administrator.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AdministrateurDTO extends PersonneDTO {
    private String codeAdmin;  // Unique code for the administrator
}
