#!/bin/bash

# Define the target directory for the DTO files
DTO_DIR="src/main/java/com/scheduling/universityschedule_backend/dto"

# Create the DTO directory if it doesn't exist
mkdir -p "$DTO_DIR"

# Create the PersonneDTO.java file
cat <<EOL > "$DTO_DIR/PersonneDTO.java"
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
EOL

# Create the EtudiantDTO.java file
cat <<EOL > "$DTO_DIR/EtudiantDTO.java"
package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
public class EtudiantDTO extends PersonneDTO {
    @NotNull
    private String matricule;

    @NotNull
    private String branche;

    private List<String> tp;
}
EOL

# Create the EnseignantDTO.java file
cat <<EOL > "$DTO_DIR/EnseignantDTO.java"
package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
public class EnseignantDTO extends PersonneDTO {
    @NotNull
    private String codeEnseignant;

    private List<String> heures;

    private List<String> seances;

    private List<String> propositionsDeRattrapage;
}
EOL

# Create the AdministrateurDTO.java file
cat <<EOL > "$DTO_DIR/AdministrateurDTO.java"
package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class AdministrateurDTO extends PersonneDTO {
    @NotNull
    private String codeAdmin;
}
EOL

# Create the TechnicienDTO.java file
cat <<EOL > "$DTO_DIR/TechnicienDTO.java"
package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class TechnicienDTO extends PersonneDTO {
    @NotNull
    private String codeTechnicien;
}
EOL

# Create the SeanceDTO.java file
cat <<EOL > "$DTO_DIR/SeanceDTO.java"
package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class SeanceDTO {
    @NotNull
    private Long id;

    @NotNull
    private LocalDateTime jour;

    @NotNull
    private LocalDateTime heureDebut;

    @NotNull
    private LocalDateTime heureFin;

    @NotNull
    @Size(min = 3, max = 100)
    private String type;

    @NotNull
    @Size(min = 3, max = 100)
    private String matiere;

    @NotNull
    @Size(min = 1, max = 100)
    private String salle;

    @NotNull
    private String enseignant;
}
EOL

# Create the SalleDTO.java file
cat <<EOL > "$DTO_DIR/SalleDTO.java"
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
EOL

# Create the SignalDTO.java file
cat <<EOL > "$DTO_DIR/SignalDTO.java"
package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class SignalDTO {
    @NotNull
    private Long id;

    @NotNull
    @Size(min = 5, max = 500)
    private String message;

    @NotNull
    private String severity;

    private LocalDateTime timestamp;
}
EOL

# Create the PropositionDeRattrapageDTO.java file
cat <<EOL > "$DTO_DIR/PropositionDeRattrapageDTO.java"
package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class PropositionDeRattrapageDTO {
    @NotNull
    private Long id;

    @NotNull
    private LocalDateTime date;

    @NotNull
    @Size(min = 5, max = 500)
    private String reason;

    @NotNull
    private String status;
}
EOL

# Create the FichierExcelDTO.java file
cat <<EOL > "$DTO_DIR/FichierExcelDTO.java"
package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class FichierExcelDTO {
    @NotNull
    private Long id;

    @NotNull
    @Size(min = 1, max = 255)
    private String fileName;

    private String status;

    private String errors;

    private LocalDateTime importDate;
}
EOL

# Create the BrancheDTO.java file
cat <<EOL > "$DTO_DIR/BrancheDTO.java"
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
EOL

# Create the TDDTO.java file
cat <<EOL > "$DTO_DIR/TDDTO.java"
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
EOL

# Create the TPDTO.java file
cat <<EOL > "$DTO_DIR/TPDTO.java"
package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class TPDTO {
    @NotNull
    private Long id;
}
EOL

echo "DTO classes have been created successfully in $DTO_DIR"
