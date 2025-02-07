package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class AdministrateurDTO extends PersonneDTO {
    @NotNull
    private String codeAdmin;
}
