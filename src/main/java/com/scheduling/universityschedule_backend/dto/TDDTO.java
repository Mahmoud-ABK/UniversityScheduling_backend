package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class TDDTO {
    @NotNull
    private Long id;

    @NotNull
    private int nbTP;
}
