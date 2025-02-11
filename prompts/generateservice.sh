#!/bin/bash

# Define the target directory
srcprj="$srcprj/service"

# Ensure the target directory exists
mkdir -p "$srcprj"

# Write each service interface to its respective file

cat > "$srcprj/AdministrateurService.java" << 'EOF'
package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.* ;
import java.util.List;

/**
 * Service interface for managing Administrator operations
 */
public interface AdministrateurService {
    FichierExcelDTO importEmploiDuTemps(FichierExcelDTO fichierExcelDTO);
    List<SeanceDTO> genererEmploiDuTemps();
    PropositionDeRattrapageDTO traiterDemandeRattrapage(Long id, boolean approved);
    List<NotificationDTO> diffuserNotification(NotificationDTO notificationDTO);
}
EOF

cat > "$srcprj/BrancheService.java" << 'EOF'
package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.* ;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for managing branches/programs
 */
public interface BrancheService {
    Page<BrancheDTO> getAllBranches(Pageable pageable);
    BrancheDTO getBrancheById(Long id);
    BrancheDTO createBranche(BrancheDTO brancheDTO);
    BrancheDTO updateBranche(Long id, BrancheDTO brancheDTO);
    void deleteBranche(Long id);
}
EOF

cat > "$srcprj/EnseignantService.java" << 'EOF'
package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.* ;
import java.util.List;

/**
 * Service interface for managing teachers
 */
public interface EnseignantService {
    List<SeanceDTO> getEmploiDuTemps(Long id);
    int getHeuresEnseignees(Long id);
    PropositionDeRattrapageDTO soumettreDemandeRattrapage(Long id, PropositionDeRattrapageDTO propositionDTO);
    SignalDTO soumettreSuggestion(Long id, SignalDTO signalDTO);
    List<SignalDTO> getSignalisations(Long id);
}
EOF

cat > "$srcprj/EtudiantService.java" << 'EOF'
package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.* ;
import java.util.List;

/**
 * Service interface for managing students
 */
public interface EtudiantService {
    List<SeanceDTO> getEmploiDuTempsPersonnel(Long id);
    List<SeanceDTO> getEmploiDuTempsBranche(Long brancheId);
    List<NotificationDTO> getNotifications(Long id);
}
EOF

cat > "$srcprj/FichierExcelService.java" << 'EOF'
package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.* ;
import java.util.List;

/**
 * Service interface for managing Excel file imports
 */
public interface FichierExcelService {
    FichierExcelDTO importerFichierExcel(FichierExcelDTO fichierExcelDTO);
}
EOF

cat > "$srcprj/NotificationService.java" << 'EOF'
package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.* ;
import java.util.List;

/**
 * Service interface for notifications
 */
public interface NotificationService {
    List<NotificationDTO> getNotificationsForUser(Long userId);
    void markAsRead(Long notificationId);
}
EOF

cat > "$srcprj/PropositionDeRattrapageService.java" << 'EOF'
package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.* ;
import java.util.List;

/**
 * Service interface for make-up session proposals
 */
public interface PropositionDeRattrapageService {
    PropositionDeRattrapageDTO submitProposal(PropositionDeRattrapageDTO propositionDTO);
    List<PropositionDeRattrapageDTO> getAllProposals();
    PropositionDeRattrapageDTO approveOrRejectProposal(Long id, boolean approved);
}
EOF

cat > "$srcprj/SalleService.java" << 'EOF'
package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.* ;
import java.util.List;

/**
 * Service interface for managing rooms
 */
public interface SalleService {
    List<SalleDTO> getAllSalles();
    SalleDTO getSalleById(Long id);
    SalleDTO createSalle(SalleDTO salleDTO);
    SalleDTO updateSalle(Long id, SalleDTO salleDTO);
    void deleteSalle(Long id);
}
EOF

cat > "$srcprj/SeanceService.java" << 'EOF'
package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.* ;
import java.util.List;

/**
 * Service interface for managing class sessions
 */
public interface SeanceService {
    List<SeanceDTO> getAllSeances();
    SeanceDTO getSeanceById(Long id);
    SeanceDTO createSeance(SeanceDTO seanceDTO);
    SeanceDTO updateSeance(Long id, SeanceDTO seanceDTO);
    void deleteSeance(Long id);
    List<SeanceConflictDTO> detectConflicts(Long seanceId);
}
EOF

cat > "$srcprj/SignalService.java" << 'EOF'
package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.* ;
import java.util.List;

/**
 * Service interface for managing issue reports
 */
public interface SignalService {
    SignalDTO submitSignal(SignalDTO signalDTO);
    List<SignalDTO> getAllSignals();
}
EOF

cat > "$srcprj/TDService.java" << 'EOF'
package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.* ;
import java.util.List;

/**
 * Service interface for managing TD groups
 */
public interface TDService {
    List<TDDTO> getAllTDs();
    TDDTO getTDById(Long id);
    TDDTO createTD(TDDTO tdDTO);
    TDDTO updateTD(Long id, TDDTO tdDTO);
    void deleteTD(Long id);
}
EOF

cat > "$srcprj/TechnicienService.java" << 'EOF'
package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.* ;
import java.util.List;

/**
 * Service interface for managing technicians
 */
public interface TechnicienService {
    List<TechnicienDTO> getAllTechniciens();
    TechnicienDTO getTechnicienById(Long id);
    TechnicienDTO createTechnicien(TechnicienDTO technicienDTO);
    TechnicienDTO updateTechnicien(Long id, TechnicienDTO technicienDTO);
    void deleteTechnicien(Long id);
}
EOF

cat > "$srcprj/TPService.java" << 'EOF'
package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.* ;
import java.util.List;

/**
 * Service interface for managing TP groups
 */
public interface TPService {
    List<TPDTO> getAllTPs();
    TPDTO getTPById(Long id);
    TPDTO createTP(TPDTO tpDTO);
    TPDTO updateTP(Long id, TPDTO tpDTO);
    void deleteTP(Long id);
}
EOF

echo "Service interfaces have been successfully created in $srcprj."
