#!/bin/bash

# Directory containing the DTOs
dto_dir="$srcprj/dto"

# Ensure the DTO directory exists
if [ ! -d "$dto_dir" ]; then
  echo "DTO directory does not exist: $dto_dir"
  exit 1
fi

# Function to create a DTO file with the given content
create_dto_file() {
  local file_name=$1
  local content=$2
  echo "$content" > "$dto_dir/$file_name"
}

# DTO class contents
administrateur_dto=$(cat <<'EOF'
package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Data Transfer Object for Administrateur entity.
 * Extends PersonneDTO to include additional attributes specific to an Administrator.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AdministrateurDTO extends PersonneDTO {
    private String codeAdmin;  // Unique code for the administrator
}
EOF
)

branche_dto=$(cat <<'EOF'
package com.scheduling.universityschedule_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Data Transfer Object for Branche entity.
 * Represents an academic program or specialization.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrancheDTO {
    private Long id;  // Unique identifier for the Branche
    private String niveau;  // Level of study (e.g., undergraduate, graduate)
    private String specialite;  // Specialization or major
    private int nbTD;  // Number of tutorial sessions
    private String departement;  // Associated department

    // List of SeanceDTO objects associated with this Branche
    private List<SeanceDTO> seances;
}
EOF
)

enseignant_dto=$(cat <<'EOF'
package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

/**
 * Data Transfer Object for Enseignant entity.
 * Extends PersonneDTO to include additional attributes specific to a Teacher.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EnseignantDTO extends PersonneDTO {
    private String codeEnseignant;  // Unique code for the teacher
    private int heures;  // Total teaching hours

    // List of SeanceDTO objects representing sessions taught by the teacher
    private List<SeanceDTO> seances;

    // List of PropositionDeRattrapageDTO objects representing proposed catch-up sessions
    private List<PropositionDeRattrapageDTO> propositionsDeRattrapage;
}
EOF
)

etudiant_dto=$(cat <<'EOF'
package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Data Transfer Object for Etudiant entity.
 * Extends PersonneDTO to include additional attributes specific to a Student.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EtudiantDTO extends PersonneDTO {
    private String matricule;  // Student ID

    // Associated BrancheDTO object representing the student's program or specialization
    private BrancheDTO branche;

    // Associated TPDTO object representing the student's practical session
    private TPDTO tp;
}
EOF
)

fichier_excel_dto=$(cat <<'EOF'
package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for FichierExcel entity.
 * Represents scheduling data imported into the system.
 */
@Data
public class FichierExcelDTO {
    private Long id;  // Unique identifier for the file
    private String fileName;  // Name of the Excel file
    private String status;  // Import status (e.g., successful, failed)
    
    // List of errors encountered during import
    private List<String> errors;

    private LocalDateTime importDate;  // Date and time of import
}
EOF
)

notification_dto=$(cat <<'EOF'
package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Notification entity.
 * Represents messages sent to users about schedule changes, announcements, or alerts.
 */
@Data
public class NotificationDTO {
    private Long id;  // Unique identifier for the notification
    private String message;  // Content of the notification
    private LocalDateTime date;  // Date and time the notification was sent
    private String type;  // Type of notification (e.g., update, alert, reminder)
    private Boolean read;  // Indicates if the notification has been read

    // Associated PersonneDTO object representing the recipient of the notification
    private PersonneDTO recepteur;

    // Associated PersonneDTO object representing the sender of the notification
    private PersonneDTO expediteur;
}
EOF
)

personne_dto=$(cat <<'EOF'
package com.scheduling.universityschedule_backend.dto;

import lombok.Data;

/**
 * Base Data Transfer Object for Personne entity.
 * Represents common attributes for all individuals within the system.
 */
@Data
public class PersonneDTO {
    private Long id;  // Unique identifier for the person
    private String cin;  // National identification number
    private String nom;  // Last name
    private String prenom;  // First name
    private String email;  // Contact email
    private String tel;  // Telephone number
    private String adresse;  // Physical address
}
EOF
)

proposition_de_rattrapage_dto=$(cat <<'EOF'
package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for PropositionDeRattrapage entity.
 * Represents a proposal submitted by a teacher to schedule a make-up session.
 */
@Data
public class PropositionDeRattrapageDTO {
    private Long id;  // Unique identifier for the proposal
    private LocalDateTime date;  // Proposed date for the catch-up session
    private String reason;  // Explanation for the catch-up session
    private String status;  // Current status of the proposal (e.g., pending, approved, rejected)

    // Associated EnseignantDTO object representing the teacher who proposed the catch-up session
    private EnseignantDTO enseignant;
}
EOF
)

salle_dto=$(cat <<'EOF'
package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import java.util.List;

/**
 * Data Transfer Object for Salle entity.
 * Represents a physical classroom or lab space.
 */
@Data
public class SalleDTO {
    private Long id;  // Unique identifier for the room
    private String identifiant;  // Room identifier
    private String type;  // Room type (e.g., lecture hall, lab)
    private int capacite;  // Capacity of the room

    // List of available time slots for the room
    private List<String> disponibilite;

    // List of SeanceDTO objects representing sessions scheduled in the room
    private List<SeanceDTO> seances;
}
EOF
)

seance_conflict_dto=$(cat <<'EOF'
package com.scheduling.universityschedule_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Data Transfer Object for Seance conflicts.
 * Represents conflicts between two SeanceDTO objects.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeanceConflictDTO {
    private SeanceDTO seance1;  // First conflicting session
    private SeanceDTO seance2;  // Second conflicting session

    // List of conflict types between the two sessions
    private List<String> conflictTypes;
}
EOF
)

seance_dto=$(cat <<'EOF'
package com.scheduling.universityschedule_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Data Transfer Object for Seance entity.
 * Represents a scheduled teaching session (lecture, tutorial, practical).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeanceDTO {
    private Long id;  // Unique identifier for the session
    private String jour;  // Day of the session
    private String heureDebut;  // Start time of the session
    private String heureFin;  // End time of the session
    private String type;  // Type of session (e.g., CR, CI, TD, TP)
    private String matiere;  // Subject matter
    private String frequence;  // Frequency of the session (e.g., weekly, biweekly, specific date for catch-up)

    // Associated SalleDTO object representing the room assigned to the session
    private SalleDTO salle;

    // Associated EnseignantDTO object representing the teacher assigned to the session
    private EnseignantDTO enseignant;

    // Lists of associated DTOs for branches, tutorials, and practicals
    private List<BrancheDTO> branches;
    private List<TDDTO> tds;
    private List<TPDTO> tps;
}
EOF
)

signal_dto=$(cat <<'EOF'
package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Signal entity.
 * Represents a message sent by a teacher to report a problem or suggest a change regarding their schedule.
 */
@Data
public class SignalDTO {
    private Long id;  // Unique identifier for the signal
    private String message;  // Details of the issue or suggestion
    private String severity;  // Importance level
    private LocalDateTime timestamp;  // Date and time the signal was submitted
}
EOF
)

td_dto=$(cat <<'EOF'
package com.scheduling.universityschedule_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Data Transfer Object for TD entity.
 * Represents group tutorial sessions associated with a Branche.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TDDTO {
    private Long id;  // Unique identifier for the tutorial session
    private int nb;  // Number of tutorial sessions
    private int nbTP;  // Number of practical sessions associated

    // Associated BrancheDTO object representing the academic program or specialization
    private BrancheDTO branche;

    // List of associated TPDTO objects representing practical sessions
    private List<TPDTO> tpList;
}
EOF
)

technicien_dto=$(cat <<'EOF'
package com.scheduling.universityschedule_backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Data Transfer Object for Technicien entity.
 * Extends PersonneDTO to include additional attributes specific to a Technician.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TechnicienDTO extends PersonneDTO {
    private String codeTechnicien;  // Unique code for the technician
}
EOF
)

tp_dto=$(cat <<'EOF'
package com.scheduling.universityschedule_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Data Transfer Object for TP entity.
 * Represents hands-on practical or lab sessions.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TPDTO {
    private Long id;  // Unique identifier for the practical session
    private int nb;  // Number of practical sessions

    // Associated TDDTO object representing the tutorial session
    private TDDTO td;

    // List of associated EtudiantDTO objects representing students enrolled in the practical session
    private List<EtudiantDTO> etudiants;
}
EOF
)

# Create DTO files
create_dto_file "AdministrateurDTO.java" "$administrateur_dto"
create_dto_file "BrancheDTO.java" "$branche_dto"
create_dto_file "EnseignantDTO.java" "$enseignant_dto"
create_dto_file "EtudiantDTO.java" "$etudiant_dto"
create_dto_file "FichierExcelDTO.java" "$fichier_excel_dto"
create_dto_file "NotificationDTO.java" "$notification_dto"
create_dto_file "PersonneDTO.java" "$personne_dto"
create_dto_file "PropositionDeRattrapageDTO.java" "$proposition_de_rattrapage_dto"
create_dto_file "SalleDTO.java" "$salle_dto"
create_dto_file "SeanceConflictDTO.java" "$seance_conflict_dto"
create_dto_file "SeanceDTO.java" "$seance_dto"
create_dto_file "SignalDTO.java" "$signal_dto"
create_dto_file "TDDTO.java" "$td_dto"
create_dto_file "TechnicienDTO.java" "$technicien_dto"
create_dto_file "TPDTO.java" "$tp_dto"

echo "DTO classes have been created in $dto_dir."
