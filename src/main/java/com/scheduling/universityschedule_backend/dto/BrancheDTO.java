package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class BrancheDTO {
    @NotNull
    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    private String specialite;

    @NotNull
    private int nbTD;

    @NotNull
    @Size(min = 1, max = 100)
    private String departement;
}
