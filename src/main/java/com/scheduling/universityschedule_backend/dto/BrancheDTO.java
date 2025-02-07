package com.scheduling.universityschedule_backend.dto;

import lombok.Data;

@Data
public class BrancheDTO {
    private Long id;
    private String niveau;
    private String specialite;
    private int nbTD;
    private String departement;
}
