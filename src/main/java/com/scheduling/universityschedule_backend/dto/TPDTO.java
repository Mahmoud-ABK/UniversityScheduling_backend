package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class TPDTO {
    @NotNull
    private Long id;
}
