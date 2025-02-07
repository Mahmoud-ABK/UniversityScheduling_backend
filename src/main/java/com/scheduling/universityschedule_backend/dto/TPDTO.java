package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class TPDTO {
    private Long id;
    private int nb;
    // Represent the associated TD by its ID
    private Long tdId;
    // List of associated Etudiant IDs
    private List<Long> etudiantIds;
}
