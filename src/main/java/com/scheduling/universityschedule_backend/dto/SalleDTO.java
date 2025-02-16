package com.scheduling.universityschedule_backend.dto;

import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;
import java.util.List;

/**
 * Data Transfer Object for Salle entity.
 * Represents a physical classroom or lab space.
 */
@Data
public class SalleDTO {
    private Long id;             // Unique identifier for the room
    private String identifiant;  // Room identifier
    private String type;         // Room type (e.g., lecture hall, lab)
    private int capacite;        // Capacity of the room

    // List of available time slots for the room
    private List<String> disponibilite;

    // List of Seance IDs representing sessions scheduled in the room
    private List<Long> seanceIds;
}
