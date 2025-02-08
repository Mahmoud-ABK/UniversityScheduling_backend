package com.scheduling.universityschedule_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TPDTO {
    private Long id;
    private int nb;

    private TDDTO td; // TD is assumed to be a DTO
    private List<EtudiantDTO> etudiants; // Etudiant is assumed to be a DTO
}
