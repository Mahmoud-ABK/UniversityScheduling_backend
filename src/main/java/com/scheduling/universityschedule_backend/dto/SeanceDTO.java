package com.scheduling.universityschedule_backend.dto;

import lombok.Data;

@Data
public class SeanceDTO {
    private Long id;
    private String jour;
    private String heureDebut;
    private String heureFin;
    private String type;
    private String matiere;
    private String frequence;
    // Associated entities represented by their IDs
    private Long salleId;
    private Long enseignantId;
}
