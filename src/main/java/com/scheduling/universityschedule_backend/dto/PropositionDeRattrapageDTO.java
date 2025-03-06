package com.scheduling.universityschedule_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropositionDeRattrapageDTO {
    private Long id;
    private String name;
    private String matiere;
    private String type;        // Will be mapped to/from SeanceType enum
    private String heureDebut;  // Format: HH:mm
    private String heureFin;    // Format: HH:mm
    private String date;        // Format: yyyy-MM-dd'T'HH:mm:ss
    private String reason;
    private String status;      // PENDING, APPROVED, REJECTED,SCHEDULED

    // Relationships
    private Long enseignantId;
    private List<Long> brancheIds = new ArrayList<>();
    private List<Long> tdIds = new ArrayList<>();
    private List<Long> tpIds = new ArrayList<>();
}