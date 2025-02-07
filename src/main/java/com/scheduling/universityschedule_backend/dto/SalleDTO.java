package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class SalleDTO {
    private Long id;
    private String identifiant;
    private String type;
    private int capacite;
    private List<String> disponibilite;
}
