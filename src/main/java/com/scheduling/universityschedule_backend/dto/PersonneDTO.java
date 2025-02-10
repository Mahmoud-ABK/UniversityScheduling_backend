package com.scheduling.universityschedule_backend.dto;

import lombok.Data;

/**
 * Base Data Transfer Object for Personne entity.
 * Represents common attributes for all individuals within the system.
 */
@Data
public class PersonneDTO {
    private Long id;  // Unique identifier for the person
    private String cin;  // National identification number
    private String nom;  // Last name
    private String prenom;  // First name
    private String email;  // Contact email
    private String tel;  // Telephone number
    private String adresse;  // Physical address
}
