package com.scheduling.universityschedule_backend.dto;

import lombok.Data;

@Data
public class PersonneDTO {
    private Long id;
    private String cin;
    private String nom;
    private String prenom;
    private String email;
    private String tel;
    private String adresse;
}
