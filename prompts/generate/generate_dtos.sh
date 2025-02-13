#!/bin/bash

# Create the target directory if it doesn't exist
mkdir -p "$srcprj/dto"

# PersonneDTO.java
cat <<'EOF' > "$srcprj/dto/PersonneDTO.java"
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
EOF

# AdministrateurDTO.java
cat <<'EOF' > "$srcprj/dto/AdministrateurDTO.java"
package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdministrateurDTO extends PersonneDTO {
    private String codeAdmin;
}
EOF

# BrancheDTO.java
cat <<'EOF' > "$srcprj/dto/BrancheDTO.java"
package com.scheduling.universityschedule_backend.dto;

import lombok.Data;

@Data
public class BrancheDTO {
    private Long id;
    private String niveau;
    private String specialite;
    private int nbTD;
    private String departement;
}
EOF

# EnseignantDTO.java
cat <<'EOF' > "$srcprj/dto/EnseignantDTO.java"
package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class EnseignantDTO extends PersonneDTO {
    private String codeEnseignant;
    private int heures;
    // Association fields represented by IDs
    private List<Long> seanceIds;
    private List<Long> propositionsDeRattrapageIds;
}
EOF

# EtudiantDTO.java
cat <<'EOF' > "$srcprj/dto/EtudiantDTO.java"
package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class EtudiantDTO extends PersonneDTO {
    private String matricule;
    // Representing the associated Branche by its ID
    private Long brancheId;
    // Representation of associated TP records by their IDs
    private List<Long> tpIds;
}
EOF

# FichierExcelDTO.java
cat <<'EOF' > "$srcprj/dto/FichierExcelDTO.java"
package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class FichierExcelDTO {
    private Long id;
    private String fileName;
    private String status;
    private List<String> errors;
    private LocalDateTime importDate;
}
EOF

# NotificationDTO.java
cat <<'EOF' > "$srcprj/dto/NotificationDTO.java"
package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationDTO {
    private Long id;
    private String message;
    private LocalDateTime date;
    private String type;
    private Boolean read;
    // Represent associated Personne entities by their IDs
    private Long recepteurId;
    private Long expediteurId;
}
EOF

# PropositionDeRattrapageDTO.java
cat <<'EOF' > "$srcprj/dto/PropositionDeRattrapageDTO.java"
package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PropositionDeRattrapageDTO {
    private Long id;
    private LocalDateTime date;
    private String reason;
    private String status;
    // Represent the associated Enseignant by its ID
    private Long enseignantId;
}
EOF

# SalleDTO.java
cat <<'EOF' > "$srcprj/dto/SalleDTO.java"
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
EOF

# SeanceDTO.java
cat <<'EOF' > "$srcprj/dto/SeanceDTO.java"
package com.scheduling.universityschedule_backend.dto;

import lombok.Data;

@Data
public class SeanceDTO {
    private Long id;
    private String jour;
    private String heureDebut;
    private String heureFin;
    private String type;
    private String matiere;
    private String frequence;
    // Associated entities represented by their IDs
    private Long salleId;
    private Long enseignantId;
}
EOF

# SignalDTO.java
cat <<'EOF' > "$srcprj/dto/SignalDTO.java"
package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SignalDTO {
    private Long id;
    private String message;
    private String severity;
    private LocalDateTime timestamp;
}
EOF

# TDDTO.java
cat <<'EOF' > "$srcprj/dto/TDDTO.java"
package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class TDDTO {
    private Long id;
    private int nbTP;
    // Represent associated Branche by its ID
    private Long brancheId;
    // List of associated TP IDs
    private List<Long> tpIds;
}
EOF

# TechnicienDTO.java
cat <<'EOF' > "$srcprj/dto/TechnicienDTO.java"
package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TechnicienDTO extends PersonneDTO {
    private String codeTechnicien;
}
EOF

# TPDTO.java
cat <<'EOF' > "$srcprj/dto/TPDTO.java"
package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class TPDTO {
    private Long id;
    // Represent the associated TD by its ID
    private Long tdId;
    // List of associated Etudiant IDs
    private List<Long> etudiantIds;
}
EOF

echo "DTO files have been generated under $srcprj/dto"