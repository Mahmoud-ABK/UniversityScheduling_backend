package com.scheduling.universityschedule_backend.dto;

import com.scheduling.universityschedule_backend.model.enums.UserRole;
import com.scheduling.universityschedule_backend.model.enums.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserDTO {

    private Long id;

    @NotNull(message = "Personne ID is required")
    private Long personneId;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @NotNull(message = "Role is required")
    private UserRole role;

    @NotNull(message = "Status is required")
    private UserStatus status;

    // Polymorphic field for role-specific data (AdministrateurDTO, TechnicienDTO, EnseignantDTO, EtudiantDTO)
    @NotNull(message = "Personne data is required")
    private Object personneData;
}