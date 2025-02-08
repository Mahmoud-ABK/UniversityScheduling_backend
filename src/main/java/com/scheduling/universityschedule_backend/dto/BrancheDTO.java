package com.scheduling.universityschedule_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrancheDTO {
    private Long id;
    private String niveau;
    private String specialite;
    private int nbTD;
    private String departement;

    private List<SeanceDTO> seances;
}
