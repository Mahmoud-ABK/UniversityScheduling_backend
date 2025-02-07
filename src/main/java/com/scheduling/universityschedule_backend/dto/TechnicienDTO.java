package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TechnicienDTO extends PersonneDTO {
    private String codeTechnicien;
}
