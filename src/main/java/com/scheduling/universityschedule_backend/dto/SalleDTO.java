package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class SalleDTO {
    @NotNull
    private Long identifiant;

    @NotNull
    @Size(min = 1, max = 100)
    private String type;

    @NotNull
    private int capacite;

    @NotNull
    @Size(min = 1, max = 255)
    private String disponibilite;
}
