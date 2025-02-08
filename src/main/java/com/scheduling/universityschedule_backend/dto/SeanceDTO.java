package com.scheduling.universityschedule_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeanceDTO {
    private Long id;
    private String jour;
    private String heureDebut;
    private String heureFin;
    private String type; // CR, CI, TD, TP
    private String matiere;
    private String frequence; // empty string for weekly sessions, "1/15" for biweekly, or specific session date for catch-up

    private SalleDTO salle; // Salle is assumed to be a DTO
    private EnseignantDTO enseignant; // Enseignant is assumed to be a DTO

    private List<BrancheDTO> branches;
    private List<TDDTO> tds;
    private List<TPDTO> tps;
}
