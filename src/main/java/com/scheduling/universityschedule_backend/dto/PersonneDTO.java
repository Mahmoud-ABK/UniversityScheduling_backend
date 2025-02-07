package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;

@Data
public class PersonneDTO {
    @NotNull
    private String cin;

    @NotNull
    @Size(min = 1, max = 100)
    private String nom;

    @NotNull
    @Size(min = 1, max = 100)
    private String prenom;

    @NotNull
    @Email
    private String email;

    @Size(min = 10, max = 15)
    private String tel;

    @Size(min = 1, max = 255)
    private String adresse;
}
