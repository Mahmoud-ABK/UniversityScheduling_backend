#!/bin/bash
#
# This script generates the mapper and service implementation files for the project.
# Make sure to set the srcprj environment variable before running:
#
#   export srcprj="/path/to/your/project/src/main/java/com/scheduling/universityschedule_backend"
#
# Then run:
#
#   ./generate_project.sh
#

# Check that srcprj is defined
if [ -z "$srcprj" ]; then
    echo "Error: srcprj environment variable is not set. Please set it to the base package directory."
    exit 1
fi

# Create directories if they do not exist
mkdir -p "$srcprj/mapper"
mkdir -p "$srcprj/service/impl"

echo "Generating mapper file..."

# Generate EntityMapper.java in $srcprj/mapper
cat << 'EOF' > "$srcprj/mapper/EntityMapper.java"
package com.scheduling.universityschedule_backend.mapper;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.model.*;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Component responsible for converting between entities and DTOs.
 */
@Component
public class EntityMapper {

    // -------------------- Personne Mapping --------------------
    public PersonneDTO toPersonneDTO(Personne personne) {
        if (personne == null) return null;
        PersonneDTO dto = new PersonneDTO();
        dto.setId(personne.getId());
        dto.setCin(personne.getCin());
        dto.setNom(personne.getNom());
        dto.setPrenom(personne.getPrenom());
        dto.setEmail(personne.getEmail());
        dto.setTel(personne.getTel());
        dto.setAdresse(personne.getAdresse());
        return dto;
    }

    // -------------------- Administrateur Mapping --------------------
    public AdministrateurDTO toAdministrateurDTO(Administrateur admin) {
        if (admin == null) return null;
        AdministrateurDTO dto = new AdministrateurDTO();
        // Map common Personne fields
        PersonneDTO personneDTO = toPersonneDTO(admin);
        dto.setId(personneDTO.getId());
        dto.setCin(personneDTO.getCin());
        dto.setNom(personneDTO.getNom());
        dto.setPrenom(personneDTO.getPrenom());
        dto.setEmail(personneDTO.getEmail());
        dto.setTel(personneDTO.getTel());
        dto.setAdresse(personneDTO.getAdresse());
        // Map administrator–specific field
        dto.setCodeAdmin(admin.getCodeAdmin());
        return dto;
    }

    public Administrateur toAdministrateur(AdministrateurDTO dto) {
        if (dto == null) return null;
        Administrateur admin = new Administrateur();
        admin.setId(dto.getId());
        admin.setCin(dto.getCin());
        admin.setNom(dto.getNom());
        admin.setPrenom(dto.getPrenom());
        admin.setEmail(dto.getEmail());
        admin.setTel(dto.getTel());
        admin.setAdresse(dto.getAdresse());
        admin.setCodeAdmin(dto.getCodeAdmin());
        return admin;
    }

    // -------------------- Branche Mapping --------------------
    public BrancheDTO toBrancheDTO(Branche branche) {
        if (branche == null) return null;
        BrancheDTO dto = new BrancheDTO();
        dto.setId(branche.getId());
        dto.setNiveau(branche.getNiveau());
        dto.setSpecialite(branche.getSpecialite());
        dto.setNbTD(branche.getNbTD());
        dto.setDepartement(branche.getDepartement());
        // Map associated seances if available
        if (branche.getSeances() != null) {
            List<SeanceDTO> seanceDTOs = branche.getSeances().stream()
                    .map(this::toSeanceDTO)
                    .collect(Collectors.toList());
            dto.setSeances(seanceDTOs);
        } else {
            dto.setSeances(new ArrayList<>());
        }
        return dto;
    }

    public Branche toBranche(BrancheDTO dto) {
        if (dto == null) return null;
        Branche branche = new Branche();
        branche.setId(dto.getId());
        branche.setNiveau(dto.getNiveau());
        branche.setSpecialite(dto.getSpecialite());
        branche.setNbTD(dto.getNbTD());
        branche.setDepartement(dto.getDepartement());
        // Note: seances are not mapped from the DTO.
        return branche;
    }

    // -------------------- Enseignant Mapping --------------------
    public EnseignantDTO toEnseignantDTO(Enseignant enseignant) {
        if (enseignant == null) return null;
        EnseignantDTO dto = new EnseignantDTO();
        PersonneDTO personneDTO = toPersonneDTO(enseignant);
        dto.setId(personneDTO.getId());
        dto.setCin(personneDTO.getCin());
        dto.setNom(personneDTO.getNom());
        dto.setPrenom(personneDTO.getPrenom());
        dto.setEmail(personneDTO.getEmail());
        dto.setTel(personneDTO.getTel());
        dto.setAdresse(personneDTO.getAdresse());
        dto.setCodeEnseignant(enseignant.getCodeEnseignant());
        dto.setHeures(enseignant.getHeures());
        // Map associated seances if available
        if (enseignant.getSeances() != null) {
            List<SeanceDTO> seanceDTOs = enseignant.getSeances().stream()
                    .map(this::toSeanceDTO)
                    .collect(Collectors.toList());
            dto.setSeances(seanceDTOs);
        } else {
            dto.setSeances(new ArrayList<>());
        }
        // Map catch-up session proposals if available
        if (enseignant.getPropositionsDeRattrapage() != null) {
            List<PropositionDeRattrapageDTO> propDTOs = enseignant.getPropositionsDeRattrapage().stream()
                    .map(this::toPropositionDeRattrapageDTO)
                    .collect(Collectors.toList());
            dto.setPropositionsDeRattrapage(propDTOs);
        } else {
            dto.setPropositionsDeRattrapage(new ArrayList<>());
        }
        return dto;
    }

    public Enseignant toEnseignant(EnseignantDTO dto) {
        if (dto == null) return null;
        Enseignant enseignant = new Enseignant();
        enseignant.setId(dto.getId());
        enseignant.setCin(dto.getCin());
        enseignant.setNom(dto.getNom());
        enseignant.setPrenom(dto.getPrenom());
        enseignant.setEmail(dto.getEmail());
        enseignant.setTel(dto.getTel());
        enseignant.setAdresse(dto.getAdresse());
        enseignant.setCodeEnseignant(dto.getCodeEnseignant());
        enseignant.setHeures(dto.getHeures());
        // Note: seances and propositions are not mapped from the DTO.
        return enseignant;
    }

    // -------------------- Etudiant Mapping --------------------
    public EtudiantDTO toEtudiantDTO(Etudiant etudiant) {
        if (etudiant == null) return null;
        EtudiantDTO dto = new EtudiantDTO();
        PersonneDTO personneDTO = toPersonneDTO(etudiant);
        dto.setId(personneDTO.getId());
        dto.setCin(personneDTO.getCin());
        dto.setNom(personneDTO.getNom());
        dto.setPrenom(personneDTO.getPrenom());
        dto.setEmail(personneDTO.getEmail());
        dto.setTel(personneDTO.getTel());
        dto.setAdresse(personneDTO.getAdresse());
        dto.setMatricule(etudiant.getMatricule());
        dto.setBranche(toBrancheDTO(etudiant.getBranche()));
        dto.setTp(toTPDTO(etudiant.getTp()));
        return dto;
    }

    public Etudiant toEtudiant(EtudiantDTO dto) {
        if (dto == null) return null;
        Etudiant etudiant = new Etudiant();
        etudiant.setId(dto.getId());
        etudiant.setCin(dto.getCin());
        etudiant.setNom(dto.getNom());
        etudiant.setPrenom(dto.getPrenom());
        etudiant.setEmail(dto.getEmail());
        etudiant.setTel(dto.getTel());
        etudiant.setAdresse(dto.getAdresse());
        etudiant.setMatricule(dto.getMatricule());
        etudiant.setBranche(toBranche(dto.getBranche()));
        etudiant.setTp(toTP(dto.getTp()));
        return etudiant;
    }

    // -------------------- FichierExcel Mapping --------------------
    public FichierExcelDTO toFichierExcelDTO(FichierExcel fe) {
        if (fe == null) return null;
        FichierExcelDTO dto = new FichierExcelDTO();
        dto.setId(fe.getId());
        dto.setFileName(fe.getFileName());
        dto.setStatus(fe.getStatus());
        dto.setErrors(fe.getErrors());
        dto.setImportDate(fe.getImportDate());
        return dto;
    }

    public FichierExcel toFichierExcel(FichierExcelDTO dto) {
        if (dto == null) return null;
        FichierExcel fe = new FichierExcel();
        fe.setId(dto.getId());
        fe.setFileName(dto.getFileName());
        fe.setStatus(dto.getStatus());
        fe.setErrors(dto.getErrors());
        fe.setImportDate(dto.getImportDate());
        return fe;
    }

    // -------------------- Notification Mapping --------------------
    public NotificationDTO toNotificationDTO(Notification notif) {
        if (notif == null) return null;
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notif.getId());
        dto.setMessage(notif.getMessage());
        dto.setDate(notif.getDate());
        dto.setType(notif.getType());
        dto.setRead(notif.getRead());
        dto.setRecepteur(toPersonneDTO(notif.getRecepteur()));
        dto.setExpediteur(toPersonneDTO(notif.getExpediteur()));
        return dto;
    }

    public Notification toNotification(NotificationDTO dto) {
        if (dto == null) return null;
        Notification notif = new Notification();
        notif.setId(dto.getId());
        notif.setMessage(dto.getMessage());
        notif.setDate(dto.getDate());
        notif.setType(dto.getType());
        notif.setRead(dto.getRead());
        // Note: Mapping for recepteur and expediteur is omitted since Personne is abstract.
        return notif;
    }

    // -------------------- PropositionDeRattrapage Mapping --------------------
    public PropositionDeRattrapageDTO toPropositionDeRattrapageDTO(PropositionDeRattrapage prop) {
        if (prop == null) return null;
        PropositionDeRattrapageDTO dto = new PropositionDeRattrapageDTO();
        dto.setId(prop.getId());
        dto.setDate(prop.getDate());
        dto.setReason(prop.getReason());
        dto.setStatus(prop.getStatus());
        dto.setEnseignant(toEnseignantDTO(prop.getEnseignant()));
        return dto;
    }

    public PropositionDeRattrapage toPropositionDeRattrapage(PropositionDeRattrapageDTO dto) {
        if (dto == null) return null;
        PropositionDeRattrapage prop = new PropositionDeRattrapage();
        prop.setId(dto.getId());
        prop.setDate(dto.getDate());
        prop.setReason(dto.getReason());
        prop.setStatus(dto.getStatus());
        prop.setEnseignant(toEnseignant(dto.getEnseignant()));
        return prop;
    }

    // -------------------- Salle Mapping --------------------
    public SalleDTO toSalleDTO(Salle salle) {
        if (salle == null) return null;
        SalleDTO dto = new SalleDTO();
        dto.setId(salle.getId());
        dto.setIdentifiant(salle.getIdentifiant());
        dto.setType(salle.getType());
        dto.setCapacite(salle.getCapacite());
        dto.setDisponibilite(salle.getDisponibilite());
        return dto;
    }

    public Salle toSalle(SalleDTO dto) {
        if (dto == null) return null;
        Salle salle = new Salle();
        salle.setId(dto.getId());
        salle.setIdentifiant(dto.getIdentifiant());
        salle.setType(dto.getType());
        salle.setCapacite(dto.getCapacite());
        salle.setDisponibilite(dto.getDisponibilite());
        return salle;
    }

    // -------------------- Seance Mapping --------------------
    public SeanceDTO toSeanceDTO(Seance seance) {
        if (seance == null) return null;
        SeanceDTO dto = new SeanceDTO();
        dto.setId(seance.getId());
        dto.setJour(seance.getJour());
        dto.setHeureDebut(seance.getHeureDebut());
        dto.setHeureFin(seance.getHeureFin());
        dto.setType(seance.getType());
        dto.setMatiere(seance.getMatiere());
        dto.setFrequence(seance.getFrequence());
        dto.setSalle(toSalleDTO(seance.getSalle()));
        dto.setEnseignant(toEnseignantDTO(seance.getEnseignant()));
        // Map lists if available
        if (seance.getBranches() != null) {
            dto.setBranches(seance.getBranches().stream()
                    .map(this::toBrancheDTO)
                    .collect(Collectors.toList()));
        } else {
            dto.setBranches(new ArrayList<>());
        }
        if (seance.getTds() != null) {
            dto.setTds(seance.getTds().stream()
                    .map(this::toTDDTO)
                    .collect(Collectors.toList()));
        } else {
            dto.setTds(new ArrayList<>());
        }
        if (seance.getTps() != null) {
            dto.setTps(seance.getTps().stream()
                    .map(this::toTPDTO)
                    .collect(Collectors.toList()));
        } else {
            dto.setTps(new ArrayList<>());
        }
        return dto;
    }

    public Seance toSeance(SeanceDTO dto) {
        if (dto == null) return null;
        Seance seance = new Seance();
        seance.setId(dto.getId());
        seance.setJour(dto.getJour());
        seance.setHeureDebut(dto.getHeureDebut());
        seance.setHeureFin(dto.getHeureFin());
        seance.setType(dto.getType());
        seance.setMatiere(dto.getMatiere());
        seance.setFrequence(dto.getFrequence());
        seance.setSalle(toSalle(dto.getSalle()));
        seance.setEnseignant(toEnseignant(dto.getEnseignant()));
        // Note: Collections (branches, tds, tps) are expected to be managed elsewhere.
        return seance;
    }

    // -------------------- Signal Mapping --------------------
    public SignalDTO toSignalDTO(Signal signal) {
        if (signal == null) return null;
        SignalDTO dto = new SignalDTO();
        dto.setId(signal.getId());
        dto.setMessage(signal.getMessage());
        dto.setSeverity(signal.getSeverity());
        dto.setTimestamp(signal.getTimestamp());
        return dto;
    }

    public Signal toSignal(SignalDTO dto) {
        if (dto == null) return null;
        Signal signal = new Signal();
        signal.setId(dto.getId());
        signal.setMessage(dto.getMessage());
        signal.setSeverity(dto.getSeverity());
        signal.setTimestamp(dto.getTimestamp());
        return signal;
    }

    // -------------------- TD Mapping --------------------
    public TDDTO toTDDTO(TD td) {
        if (td == null) return null;
        TDDTO dto = new TDDTO();
        dto.setId(td.getId());
        dto.setNb(td.getNb());
        dto.setNbTP(td.getNbTP());
        dto.setBranche(toBrancheDTO(td.getBranche()));
        if (td.getTpList() != null) {
            dto.setTpList(td.getTpList().stream()
                    .map(this::toTPDTO)
                    .collect(Collectors.toList()));
        } else {
            dto.setTpList(new ArrayList<>());
        }
        return dto;
    }

    public TD toTD(TDDTO dto) {
        if (dto == null) return null;
        TD td = new TD();
        td.setId(dto.getId());
        td.setNb(dto.getNb());
        td.setNbTP(dto.getNbTP());
        td.setBranche(toBranche(dto.getBranche()));
        // Note: tpList is not mapped from the DTO.
        return td;
    }

    // -------------------- Technicien Mapping --------------------
    public TechnicienDTO toTechnicienDTO(Technicien tech) {
        if (tech == null) return null;
        TechnicienDTO dto = new TechnicienDTO();
        PersonneDTO personneDTO = toPersonneDTO(tech);
        dto.setId(personneDTO.getId());
        dto.setCin(personneDTO.getCin());
        dto.setNom(personneDTO.getNom());
        dto.setPrenom(personneDTO.getPrenom());
        dto.setEmail(personneDTO.getEmail());
        dto.setTel(personneDTO.getTel());
        dto.setAdresse(personneDTO.getAdresse());
        dto.setCodeTechnicien(tech.getCodeTechnicien());
        return dto;
    }

    public Technicien toTechnicien(TechnicienDTO dto) {
        if (dto == null) return null;
        Technicien tech = new Technicien();
        tech.setId(dto.getId());
        tech.setCin(dto.getCin());
        tech.setNom(dto.getNom());
        tech.setPrenom(dto.getPrenom());
        tech.setEmail(dto.getEmail());
        tech.setTel(dto.getTel());
        tech.setAdresse(dto.getAdresse());
        tech.setCodeTechnicien(dto.getCodeTechnicien());
        return tech;
    }

    // -------------------- TP Mapping --------------------
    public TPDTO toTPDTO(TP tp) {
        if (tp == null) return null;
        TPDTO dto = new TPDTO();
        dto.setId(tp.getId());
        dto.setNb(tp.getNb());
        dto.setTd(toTDDTO(tp.getTd()));
        if (tp.getEtudiants() != null) {
            dto.setEtudiants(tp.getEtudiants().stream()
                    .map(this::toEtudiantDTO)
                    .collect(Collectors.toList()));
        } else {
            dto.setEtudiants(new ArrayList<>());
        }
        return dto;
    }

    public TP toTP(TPDTO dto) {
        if (dto == null) return null;
        TP tp = new TP();
        tp.setId(dto.getId());
        tp.setNb(dto.getNb());
        tp.setTd(toTD(dto.getTd()));
        // Note: etudiants are not mapped from the DTO.
        return tp;
    }
}
EOF

echo "Mapper generated at: $srcprj/mapper/EntityMapper.java"

echo "Generating service implementation files..."

# Now generate each service implementation file under $srcprj/service/impl

# AdministrateurServiceImpl.java
cat << 'EOF' > "$srcprj/service/impl/AdministrateurServiceImpl.java"
package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.FichierExcelDTO;
import com.scheduling.universityschedule_backend.dto.NotificationDTO;
import com.scheduling.universityschedule_backend.dto.PropositionDeRattrapageDTO;
import com.scheduling.universityschedule_backend.dto.SeanceDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.FichierExcel;
import com.scheduling.universityschedule_backend.model.Notification;
import com.scheduling.universityschedule_backend.model.PropositionDeRattrapage;
import com.scheduling.universityschedule_backend.model.Seance;
import com.scheduling.universityschedule_backend.repository.FichierExcelRepository;
import com.scheduling.universityschedule_backend.repository.NotificationRepository;
import com.scheduling.universityschedule_backend.repository.PropositionDeRattrapageRepository;
import com.scheduling.universityschedule_backend.repository.SeanceRepository;
import com.scheduling.universityschedule_backend.service.AdministrateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdministrateurServiceImpl implements AdministrateurService {

    private final FichierExcelRepository fichierExcelRepository;
    private final SeanceRepository seanceRepository;
    private final PropositionDeRattrapageRepository propositionRepo;
    private final NotificationRepository notificationRepository;
    private final EntityMapper mapper;

    @Autowired
    public AdministrateurServiceImpl(FichierExcelRepository fichierExcelRepository,
                                     SeanceRepository seanceRepository,
                                     PropositionDeRattrapageRepository propositionRepo,
                                     NotificationRepository notificationRepository,
                                     EntityMapper mapper) {
        this.fichierExcelRepository = fichierExcelRepository;
        this.seanceRepository = seanceRepository;
        this.propositionRepo = propositionRepo;
        this.notificationRepository = notificationRepository;
        this.mapper = mapper;
    }

    /**
     * Imports the schedule (emploi du temps) from an Excel file.
     */
    @Override
    public FichierExcelDTO importEmploiDuTemps(FichierExcelDTO fichierExcelDTO) throws CustomException {
        try {
            FichierExcel fe = mapper.toFichierExcel(fichierExcelDTO);
            fe = fichierExcelRepository.save(fe);
            return mapper.toFichierExcelDTO(fe);
        } catch (Exception e) {
            throw new CustomException("Failed to import emploi du temps", e);
        }
    }

    /**
     * Generates the schedule by retrieving all sessions.
     */
    @Override
    public List<SeanceDTO> genererEmploiDuTemps() throws CustomException {
        try {
            List<Seance> seances = seanceRepository.findAll();
            List<SeanceDTO> seanceDTOList = new ArrayList<>();
            for (Seance seance : seances) {
                seanceDTOList.add(mapper.toSeanceDTO(seance));
            }
            return seanceDTOList;
        } catch (Exception e) {
            throw new CustomException("Failed to generate emploi du temps", e);
        }
    }

    /**
     * Processes a catch-up session proposal (demande de rattrapage) by updating its status.
     */
    @Override
    public PropositionDeRattrapageDTO traiterDemandeRattrapage(Long id, boolean approved) throws CustomException {
        PropositionDeRattrapage proposal = propositionRepo.findById(id)
                .orElseThrow(() -> new CustomException("Proposition de rattrapage not found"));
        proposal.setStatus(approved ? "approved" : "rejected");
        proposal = propositionRepo.save(proposal);
        return mapper.toPropositionDeRattrapageDTO(proposal);
    }

    /**
     * Diffuses (sends) a notification.
     */
    @Override
    public List<NotificationDTO> diffuserNotification(NotificationDTO notificationDTO) throws CustomException {
        try {
            Notification notif = mapper.toNotification(notificationDTO);
            notif = notificationRepository.save(notif);
            List<NotificationDTO> list = new ArrayList<>();
            list.add(mapper.toNotificationDTO(notif));
            return list;
        } catch (Exception e) {
            throw new CustomException("Failed to diffuse notification", e);
        }
    }
}
EOF

# BrancheServiceImpl.java
cat << 'EOF' > "$srcprj/service/impl/BrancheServiceImpl.java"
package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.BrancheDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.Branche;
import com.scheduling.universityschedule_backend.repository.BrancheRepository;
import com.scheduling.universityschedule_backend.service.BrancheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class BrancheServiceImpl implements BrancheService {

    private final BrancheRepository brancheRepository;
    private final EntityMapper mapper;

    @Autowired
    public BrancheServiceImpl(BrancheRepository brancheRepository, EntityMapper mapper) {
        this.brancheRepository = brancheRepository;
        this.mapper = mapper;
    }

    /**
     * Retrieves all branches with pagination.
     */
    @Override
    public Page<BrancheDTO> getAllBranches(Pageable pageable) {
        return brancheRepository.findAll(pageable)
                .map(mapper::toBrancheDTO);
    }

    /**
     * Retrieves a branch by its ID.
     */
    @Override
    public BrancheDTO getBrancheById(Long id) throws CustomException {
        Branche branche = brancheRepository.findById(id)
                .orElseThrow(() -> new CustomException("Branche not found"));
        return mapper.toBrancheDTO(branche);
    }

    /**
     * Creates a new branch.
     */
    @Override
    public BrancheDTO createBranche(BrancheDTO brancheDTO) throws CustomException {
        try {
            Branche branche = mapper.toBranche(brancheDTO);
            branche = brancheRepository.save(branche);
            return mapper.toBrancheDTO(branche);
        } catch (Exception e) {
            throw new CustomException("Failed to create branche", e);
        }
    }

    /**
     * Updates an existing branch.
     */
    @Override
    public BrancheDTO updateBranche(Long id, BrancheDTO brancheDTO) throws CustomException {
        Branche branche = brancheRepository.findById(id)
                .orElseThrow(() -> new CustomException("Branche not found"));
        branche.setNiveau(brancheDTO.getNiveau());
        branche.setSpecialite(brancheDTO.getSpecialite());
        branche.setNbTD(brancheDTO.getNbTD());
        branche.setDepartement(brancheDTO.getDepartement());
        branche = brancheRepository.save(branche);
        return mapper.toBrancheDTO(branche);
    }

    /**
     * Deletes a branch by its ID.
     */
    @Override
    public void deleteBranche(Long id) throws CustomException {
        if (!brancheRepository.existsById(id)) {
            throw new CustomException("Branche not found");
        }
        brancheRepository.deleteById(id);
    }
}
EOF

# EnseignantServiceImpl.java
cat << 'EOF' > "$srcprj/service/impl/EnseignantServiceImpl.java"
package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.EnseignantDTO;
import com.scheduling.universityschedule_backend.dto.PropositionDeRattrapageDTO;
import com.scheduling.universityschedule_backend.dto.SeanceDTO;
import com.scheduling.universityschedule_backend.dto.SignalDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.Enseignant;
import com.scheduling.universityschedule_backend.model.PropositionDeRattrapage;
import com.scheduling.universityschedule_backend.model.Seance;
import com.scheduling.universityschedule_backend.model.Signal;
import com.scheduling.universityschedule_backend.repository.EnseignantRepository;
import com.scheduling.universityschedule_backend.repository.PropositionDeRattrapageRepository;
import com.scheduling.universityschedule_backend.repository.SeanceRepository;
import com.scheduling.universityschedule_backend.repository.SignalRepository;
import com.scheduling.universityschedule_backend.service.EnseignantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EnseignantServiceImpl implements EnseignantService {

    private final EnseignantRepository enseignantRepository;
    private final PropositionDeRattrapageRepository propositionRepo;
    private final SignalRepository signalRepository;
    private final SeanceRepository seanceRepository;
    private final EntityMapper mapper;

    @Autowired
    public EnseignantServiceImpl(EnseignantRepository enseignantRepository,
                                 PropositionDeRattrapageRepository propositionRepo,
                                 SignalRepository signalRepository,
                                 SeanceRepository seanceRepository,
                                 EntityMapper mapper) {
        this.enseignantRepository = enseignantRepository;
        this.propositionRepo = propositionRepo;
        this.signalRepository = signalRepository;
        this.seanceRepository = seanceRepository;
        this.mapper = mapper;
    }

    /**
     * Retrieves the teaching schedule (seances) for a given teacher.
     */
    @Override
    public List<SeanceDTO> getEmploiDuTemps(Long id) throws CustomException {
        Enseignant enseignant = enseignantRepository.findById(id)
                .orElseThrow(() -> new CustomException("Enseignant not found"));
        List<SeanceDTO> seanceDTOList = new ArrayList<>();
        if (enseignant.getSeances() != null) {
            for (Seance s : enseignant.getSeances()) {
                seanceDTOList.add(mapper.toSeanceDTO(s));
            }
        }
        return seanceDTOList;
    }

    /**
     * Retrieves the total teaching hours for a teacher.
     */
    @Override
    public int getHeuresEnseignees(Long id) throws CustomException {
        Enseignant enseignant = enseignantRepository.findById(id)
                .orElseThrow(() -> new CustomException("Enseignant not found"));
        return enseignant.getHeures();
    }

    /**
     * Submits a catch-up session proposal.
     */
    @Override
    public PropositionDeRattrapageDTO soumettreDemandeRattrapage(Long id, PropositionDeRattrapageDTO propositionDTO) throws CustomException {
        Enseignant enseignant = enseignantRepository.findById(id)
                .orElseThrow(() -> new CustomException("Enseignant not found"));
        PropositionDeRattrapage proposal = mapper.toPropositionDeRattrapage(propositionDTO);
        proposal.setEnseignant(enseignant);
        proposal.setStatus("pending");
        proposal.setDate(LocalDateTime.now());
        proposal = propositionRepo.save(proposal);
        return mapper.toPropositionDeRattrapageDTO(proposal);
    }

    /**
     * Submits a suggestion (signal).
     */
    @Override
    public SignalDTO soumettreSuggestion(Long id, SignalDTO signalDTO) throws CustomException {
        // Note: In a full implementation, the teacher id would be used to associate the signal.
        Signal signal = mapper.toSignal(signalDTO);
        signal.setTimestamp(LocalDateTime.now());
        signal = signalRepository.save(signal);
        return mapper.toSignalDTO(signal);
    }

    /**
     * Retrieves signals (suggestions).
     */
    @Override
    public List<SignalDTO> getSignalisations(Long id) throws CustomException {
        List<Signal> signals = signalRepository.findAll();
        List<SignalDTO> dtoList = new ArrayList<>();
        for (Signal signal : signals) {
            dtoList.add(mapper.toSignalDTO(signal));
        }
        return dtoList;
    }
}
EOF

# EtudiantServiceImpl.java
cat << 'EOF' > "$srcprj/service/impl/EtudiantServiceImpl.java"
package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.EtudiantDTO;
import com.scheduling.universityschedule_backend.dto.NotificationDTO;
import com.scheduling.universityschedule_backend.dto.SeanceDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.Etudiant;
import com.scheduling.universityschedule_backend.model.Notification;
import com.scheduling.universityschedule_backend.model.Seance;
import com.scheduling.universityschedule_backend.repository.EtudiantRepository;
import com.scheduling.universityschedule_backend.repository.NotificationRepository;
import com.scheduling.universityschedule_backend.service.EtudiantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class EtudiantServiceImpl implements EtudiantService {

    private final EtudiantRepository etudiantRepository;
    private final NotificationRepository notificationRepository;
    private final EntityMapper mapper;

    @Autowired
    public EtudiantServiceImpl(EtudiantRepository etudiantRepository,
                               NotificationRepository notificationRepository,
                               EntityMapper mapper) {
        this.etudiantRepository = etudiantRepository;
        this.notificationRepository = notificationRepository;
        this.mapper = mapper;
    }

    /**
     * Retrieves the personal schedule based on the student's practical session (TP).
     */
    @Override
    public List<SeanceDTO> getEmploiDuTempsPersonnel(Long id) throws CustomException {
        Etudiant etudiant = etudiantRepository.findById(id)
                .orElseThrow(() -> new CustomException("Etudiant not found"));
        List<SeanceDTO> dtoList = new ArrayList<>();
        if (etudiant.getTp() != null && etudiant.getTp().getSeances() != null) {
            etudiant.getTp().getSeances().forEach(seance -> dtoList.add(mapper.toSeanceDTO(seance)));
        }
        return dtoList;
    }

    /**
     * Retrieves the schedule for a given branch.
     * (For simplicity, this is not fully implemented.)
     */
    @Override
    public List<SeanceDTO> getEmploiDuTempsBranche(Long brancheId) throws CustomException {
        // Not implemented – would normally query the branche's seances.
        return new ArrayList<>();
    }

    /**
     * Retrieves notifications for the student.
     */
    @Override
    public List<NotificationDTO> getNotifications(Long id) throws CustomException {
        List<Notification> notifications = notificationRepository.findAll();
        List<NotificationDTO> dtoList = new ArrayList<>();
        // For simplicity, we are not filtering by recepteur (student) here.
        notifications.forEach(n -> dtoList.add(mapper.toNotificationDTO(n)));
        return dtoList;
    }
}
EOF

# FichierExcelServiceImpl.java
cat << 'EOF' > "$srcprj/service/impl/FichierExcelServiceImpl.java"
package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.FichierExcelDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.FichierExcel;
import com.scheduling.universityschedule_backend.repository.FichierExcelRepository;
import com.scheduling.universityschedule_backend.service.FichierExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FichierExcelServiceImpl implements FichierExcelService {

    private final FichierExcelRepository fichierExcelRepository;
    private final EntityMapper mapper;

    @Autowired
    public FichierExcelServiceImpl(FichierExcelRepository fichierExcelRepository, EntityMapper mapper) {
        this.fichierExcelRepository = fichierExcelRepository;
        this.mapper = mapper;
    }

    /**
     * Imports an Excel file by saving it and returning the saved DTO.
     */
    @Override
    public FichierExcelDTO importerFichierExcel(FichierExcelDTO fichierExcelDTO) throws CustomException {
        try {
            FichierExcel fe = mapper.toFichierExcel(fichierExcelDTO);
            fe = fichierExcelRepository.save(fe);
            return mapper.toFichierExcelDTO(fe);
        } catch (Exception e) {
            throw new CustomException("Failed to import Excel file", e);
        }
    }
}
EOF

# NotificationServiceImpl.java
cat << 'EOF' > "$srcprj/service/impl/NotificationServiceImpl.java"
package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.NotificationDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.Notification;
import com.scheduling.universityschedule_backend.repository.NotificationRepository;
import com.scheduling.universityschedule_backend.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final EntityMapper mapper;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository, EntityMapper mapper) {
        this.notificationRepository = notificationRepository;
        this.mapper = mapper;
    }

    /**
     * Retrieves all notifications for a given user.
     */
    @Override
    public List<NotificationDTO> getNotificationsForUser(Long userId) throws CustomException {
        List<Notification> notifications = notificationRepository.findAll();
        List<NotificationDTO> dtoList = new ArrayList<>();
        // For simplicity, no filtering by user is applied.
        notifications.forEach(n -> dtoList.add(mapper.toNotificationDTO(n)));
        return dtoList;
    }

    /**
     * Marks a notification as read.
     */
    @Override
    public void markAsRead(Long notificationId) throws CustomException {
        Notification notif = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new CustomException("Notification not found"));
        notif.setRead(true);
        notificationRepository.save(notif);
    }
}
EOF

# PropositionDeRattrapageServiceImpl.java
cat << 'EOF' > "$srcprj/service/impl/PropositionDeRattrapageServiceImpl.java"
package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.PropositionDeRattrapageDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.PropositionDeRattrapage;
import com.scheduling.universityschedule_backend.repository.PropositionDeRattrapageRepository;
import com.scheduling.universityschedule_backend.service.PropositionDeRattrapageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class PropositionDeRattrapageServiceImpl implements PropositionDeRattrapageService {

    private final PropositionDeRattrapageRepository propositionRepo;
    private final EntityMapper mapper;

    @Autowired
    public PropositionDeRattrapageServiceImpl(PropositionDeRattrapageRepository propositionRepo, EntityMapper mapper) {
        this.propositionRepo = propositionRepo;
        this.mapper = mapper;
    }

    /**
     * Submits a catch-up session proposal.
     */
    @Override
    public PropositionDeRattrapageDTO submitProposal(PropositionDeRattrapageDTO propositionDTO) throws CustomException {
        try {
            PropositionDeRattrapage proposal = mapper.toPropositionDeRattrapage(propositionDTO);
            proposal.setStatus("pending");
            proposal = propositionRepo.save(proposal);
            return mapper.toPropositionDeRattrapageDTO(proposal);
        } catch (Exception e) {
            throw new CustomException("Failed to submit proposal", e);
        }
    }

    /**
     * Retrieves all proposals.
     */
    @Override
    public List<PropositionDeRattrapageDTO> getAllProposals() throws CustomException {
        List<PropositionDeRattrapageDTO> dtoList = new ArrayList<>();
        propositionRepo.findAll().forEach(prop -> dtoList.add(mapper.toPropositionDeRattrapageDTO(prop)));
        return dtoList;
    }

    /**
     * Approves or rejects a proposal.
     */
    @Override
    public PropositionDeRattrapageDTO approveOrRejectProposal(Long id, boolean approved) throws CustomException {
        PropositionDeRattrapage proposal = propositionRepo.findById(id)
                .orElseThrow(() -> new CustomException("Proposal not found"));
        proposal.setStatus(approved ? "approved" : "rejected");
        proposal = propositionRepo.save(proposal);
        return mapper.toPropositionDeRattrapageDTO(proposal);
    }
}
EOF

# SalleServiceImpl.java
cat << 'EOF' > "$srcprj/service/impl/SalleServiceImpl.java"
package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.SalleDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.Salle;
import com.scheduling.universityschedule_backend.repository.SalleRepository;
import com.scheduling.universityschedule_backend.service.SalleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class SalleServiceImpl implements SalleService {

    private final SalleRepository salleRepository;
    private final EntityMapper mapper;

    @Autowired
    public SalleServiceImpl(SalleRepository salleRepository, EntityMapper mapper) {
        this.salleRepository = salleRepository;
        this.mapper = mapper;
    }

    /**
     * Retrieves all rooms.
     */
    @Override
    public List<SalleDTO> getAllSalles() throws CustomException {
        List<Salle> salles = salleRepository.findAll();
        List<SalleDTO> dtoList = new ArrayList<>();
        salles.forEach(salle -> dtoList.add(mapper.toSalleDTO(salle)));
        return dtoList;
    }

    /**
     * Retrieves a room by its ID.
     */
    @Override
    public SalleDTO getSalleById(Long id) throws CustomException {
        Salle salle = salleRepository.findById(id)
                .orElseThrow(() -> new CustomException("Salle not found"));
        return mapper.toSalleDTO(salle);
    }

    /**
     * Creates a new room.
     */
    @Override
    public SalleDTO createSalle(SalleDTO salleDTO) throws CustomException {
        try {
            Salle salle = mapper.toSalle(salleDTO);
            salle = salleRepository.save(salle);
            return mapper.toSalleDTO(salle);
        } catch (Exception e) {
            throw new CustomException("Failed to create Salle", e);
        }
    }

    /**
     * Updates an existing room.
     */
    @Override
    public SalleDTO updateSalle(Long id, SalleDTO salleDTO) throws CustomException {
        Salle salle = salleRepository.findById(id)
                .orElseThrow(() -> new CustomException("Salle not found"));
        salle.setIdentifiant(salleDTO.getIdentifiant());
        salle.setType(salleDTO.getType());
        salle.setCapacite(salleDTO.getCapacite());
        salle.setDisponibilite(salleDTO.getDisponibilite());
        salle = salleRepository.save(salle);
        return mapper.toSalleDTO(salle);
    }

    /**
     * Deletes a room by its ID.
     */
    @Override
    public void deleteSalle(Long id) throws CustomException {
        if (!salleRepository.existsById(id)) {
            throw new CustomException("Salle not found");
        }
        salleRepository.deleteById(id);
    }
}
EOF

# SeanceServiceImpl.java
cat << 'EOF' > "$srcprj/service/impl/SeanceServiceImpl.java"
package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.SeanceConflictDTO;
import com.scheduling.universityschedule_backend.dto.SeanceDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.Seance;
import com.scheduling.universityschedule_backend.repository.SeanceRepository;
import com.scheduling.universityschedule_backend.service.SeanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class SeanceServiceImpl implements SeanceService {

    private final SeanceRepository seanceRepository;
    private final EntityMapper mapper;

    @Autowired
    public SeanceServiceImpl(SeanceRepository seanceRepository, EntityMapper mapper) {
        this.seanceRepository = seanceRepository;
        this.mapper = mapper;
    }

    /**
     * Retrieves all sessions.
     */
    @Override
    public List<SeanceDTO> getAllSeances() throws CustomException {
        List<Seance> seances = seanceRepository.findAll();
        List<SeanceDTO> dtoList = new ArrayList<>();
        seances.forEach(s -> dtoList.add(mapper.toSeanceDTO(s)));
        return dtoList;
    }

    /**
     * Retrieves a session by its ID.
     */
    @Override
    public SeanceDTO getSeanceById(Long id) throws CustomException {
        Seance seance = seanceRepository.findById(id)
                .orElseThrow(() -> new CustomException("Seance not found"));
        return mapper.toSeanceDTO(seance);
    }

    /**
     * Creates a new session.
     */
    @Override
    public SeanceDTO createSeance(SeanceDTO seanceDTO) throws CustomException {
        try {
            Seance seance = mapper.toSeance(seanceDTO);
            seance = seanceRepository.save(seance);
            return mapper.toSeanceDTO(seance);
        } catch (Exception e) {
            throw new CustomException("Failed to create Seance", e);
        }
    }

    /**
     * Updates an existing session.
     */
    @Override
    public SeanceDTO updateSeance(Long id, SeanceDTO seanceDTO) throws CustomException {
        Seance seance = seanceRepository.findById(id)
                .orElseThrow(() -> new CustomException("Seance not found"));
        seance.setJour(seanceDTO.getJour());
        seance.setHeureDebut(seanceDTO.getHeureDebut());
        seance.setHeureFin(seanceDTO.getHeureFin());
        seance.setType(seanceDTO.getType());
        seance.setMatiere(seanceDTO.getMatiere());
        seance.setFrequence(seanceDTO.getFrequence());
        seance = seanceRepository.save(seance);
        return mapper.toSeanceDTO(seance);
    }

    /**
     * Deletes a session by its ID.
     */
    @Override
    public void deleteSeance(Long id) throws CustomException {
        if (!seanceRepository.existsById(id)) {
            throw new CustomException("Seance not found");
        }
        seanceRepository.deleteById(id);
    }

    /**
     * Detects conflicts for a given session. For simplicity, only room conflicts are checked.
     */
    @Override
    public List<SeanceConflictDTO> detectConflicts(Long seanceId) throws CustomException {
        Seance seance = seanceRepository.findById(seanceId)
                .orElseThrow(() -> new CustomException("Seance not found"));
        // Here we call a custom repository query to check for room conflicts.
        List<Seance> conflictingSeances = seanceRepository.findRoomConflictsForSeance(
                seance.getId(),
                seance.getJour(),
                seance.getSalle() != null ? seance.getSalle().getId() : null,
                seance.getHeureDebut(),
                seance.getHeureFin()
        );
        List<SeanceConflictDTO> conflictDTOList = new ArrayList<>();
        for (Seance conflicting : conflictingSeances) {
            SeanceConflictDTO conflictDTO = new SeanceConflictDTO();
            conflictDTO.setSeance1(mapper.toSeanceDTO(seance));
            conflictDTO.setSeance2(mapper.toSeanceDTO(conflicting));
            List<String> conflictTypes = new ArrayList<>();
            conflictTypes.add("Room Conflict");
            conflictDTO.setConflictTypes(conflictTypes);
            conflictDTOList.add(conflictDTO);
        }
        return conflictDTOList;
    }
}
EOF

# SignalServiceImpl.java
cat << 'EOF' > "$srcprj/service/impl/SignalServiceImpl.java"
package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.SignalDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.Signal;
import com.scheduling.universityschedule_backend.repository.SignalRepository;
import com.scheduling.universityschedule_backend.service.SignalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class SignalServiceImpl implements SignalService {

    private final SignalRepository signalRepository;
    private final EntityMapper mapper;

    @Autowired
    public SignalServiceImpl(SignalRepository signalRepository, EntityMapper mapper) {
        this.signalRepository = signalRepository;
        this.mapper = mapper;
    }

    /**
     * Submits a new signal.
     */
    @Override
    public SignalDTO submitSignal(SignalDTO signalDTO) throws CustomException {
        try {
            Signal signal = mapper.toSignal(signalDTO);
            signal = signalRepository.save(signal);
            return mapper.toSignalDTO(signal);
        } catch (Exception e) {
            throw new CustomException("Failed to submit signal", e);
        }
    }

    /**
     * Retrieves all signals.
     */
    @Override
    public List<SignalDTO> getAllSignals() throws CustomException {
        List<Signal> signals = signalRepository.findAll();
        List<SignalDTO> dtoList = new ArrayList<>();
        signals.forEach(s -> dtoList.add(mapper.toSignalDTO(s)));
        return dtoList;
    }
}
EOF

# TDServiceImpl.java
cat << 'EOF' > "$srcprj/service/impl/TDServiceImpl.java"
package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.TDDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.TD;
import com.scheduling.universityschedule_backend.repository.TDRepository;
import com.scheduling.universityschedule_backend.service.TDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class TDServiceImpl implements TDService {

    private final TDRepository tdRepository;
    private final EntityMapper mapper;

    @Autowired
    public TDServiceImpl(TDRepository tdRepository, EntityMapper mapper) {
        this.tdRepository = tdRepository;
        this.mapper = mapper;
    }

    /**
     * Retrieves all tutorial sessions (TDs).
     */
    @Override
    public List<TDDTO> getAllTDs() throws CustomException {
        List<TD> tds = tdRepository.findAll();
        List<TDDTO> dtoList = new ArrayList<>();
        tds.forEach(td -> dtoList.add(mapper.toTDDTO(td)));
        return dtoList;
    }

    /**
     * Retrieves a TD by its ID.
     */
    @Override
    public TDDTO getTDById(Long id) throws CustomException {
        TD td = tdRepository.findById(id)
                .orElseThrow(() -> new CustomException("TD not found"));
        return mapper.toTDDTO(td);
    }

    /**
     * Creates a new TD.
     */
    @Override
    public TDDTO createTD(TDDTO tdDTO) throws CustomException {
        try {
            TD td = mapper.toTD(tdDTO);
            td = tdRepository.save(td);
            return mapper.toTDDTO(td);
        } catch (Exception e) {
            throw new CustomException("Failed to create TD", e);
        }
    }

    /**
     * Updates an existing TD.
     */
    @Override
    public TDDTO updateTD(Long id, TDDTO tdDTO) throws CustomException {
        TD td = tdRepository.findById(id)
                .orElseThrow(() -> new CustomException("TD not found"));
        td.setNb(tdDTO.getNb());
        td.setNbTP(tdDTO.getNbTP());
        td = tdRepository.save(td);
        return mapper.toTDDTO(td);
    }

    /**
     * Deletes a TD by its ID.
     */
    @Override
    public void deleteTD(Long id) throws CustomException {
        if (!tdRepository.existsById(id)) {
            throw new CustomException("TD not found");
        }
        tdRepository.deleteById(id);
    }
}
EOF

# TechnicienServiceImpl.java
cat << 'EOF' > "$srcprj/service/impl/TechnicienServiceImpl.java"
package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.TechnicienDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.Technicien;
import com.scheduling.universityschedule_backend.repository.TechnicienRepository;
import com.scheduling.universityschedule_backend.service.TechnicienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class TechnicienServiceImpl implements TechnicienService {

    private final TechnicienRepository technicienRepository;
    private final EntityMapper mapper;

    @Autowired
    public TechnicienServiceImpl(TechnicienRepository technicienRepository, EntityMapper mapper) {
        this.technicienRepository = technicienRepository;
        this.mapper = mapper;
    }

    /**
     * Retrieves all technicians.
     */
    @Override
    public List<TechnicienDTO> getAllTechniciens() throws CustomException {
        List<Technicien> techs = technicienRepository.findAll();
        List<TechnicienDTO> dtoList = new ArrayList<>();
        techs.forEach(t -> dtoList.add(mapper.toTechnicienDTO(t)));
        return dtoList;
    }

    /**
     * Retrieves a technician by its ID.
     */
    @Override
    public TechnicienDTO getTechnicienById(Long id) throws CustomException {
        Technicien tech = technicienRepository.findById(id)
                .orElseThrow(() -> new CustomException("Technicien not found"));
        return mapper.toTechnicienDTO(tech);
    }

    /**
     * Creates a new technician.
     */
    @Override
    public TechnicienDTO createTechnicien(TechnicienDTO technicienDTO) throws CustomException {
        try {
            Technicien tech = mapper.toTechnicien(technicienDTO);
            tech = technicienRepository.save(tech);
            return mapper.toTechnicienDTO(tech);
        } catch (Exception e) {
            throw new CustomException("Failed to create Technicien", e);
        }
    }

    /**
     * Updates an existing technician.
     */
    @Override
    public TechnicienDTO updateTechnicien(Long id, TechnicienDTO technicienDTO) throws CustomException {
        Technicien tech = technicienRepository.findById(id)
                .orElseThrow(() -> new CustomException("Technicien not found"));
        tech.setCin(technicienDTO.getCin());
        tech.setNom(technicienDTO.getNom());
        tech.setPrenom(technicienDTO.getPrenom());
        tech.setEmail(technicienDTO.getEmail());
        tech.setTel(technicienDTO.getTel());
        tech.setAdresse(technicienDTO.getAdresse());
        tech.setCodeTechnicien(technicienDTO.getCodeTechnicien());
        tech = technicienRepository.save(tech);
        return mapper.toTechnicienDTO(tech);
    }

    /**
     * Deletes a technician by its ID.
     */
    @Override
    public void deleteTechnicien(Long id) throws CustomException {
        if (!technicienRepository.existsById(id)) {
            throw new CustomException("Technicien not found");
        }
        technicienRepository.deleteById(id);
    }
}
EOF

# TPServiceImpl.java
cat << 'EOF' > "$srcprj/service/impl/TPServiceImpl.java"
package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.TPDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.TP;
import com.scheduling.universityschedule_backend.repository.TPRepository;
import com.scheduling.universityschedule_backend.service.TPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class TPServiceImpl implements TPService {

    private final TPRepository tpRepository;
    private final EntityMapper mapper;

    @Autowired
    public TPServiceImpl(TPRepository tpRepository, EntityMapper mapper) {
        this.tpRepository = tpRepository;
        this.mapper = mapper;
    }

    /**
     * Retrieves all practical sessions (TPs).
     */
    @Override
    public List<TPDTO> getAllTPs() throws CustomException {
        List<TP> tps = tpRepository.findAll();
        List<TPDTO> dtoList = new ArrayList<>();
        tps.forEach(tp -> dtoList.add(mapper.toTPDTO(tp)));
        return dtoList;
    }

    /**
     * Retrieves a TP by its ID.
     */
    @Override
    public TPDTO getTPById(Long id) throws CustomException {
        TP tp = tpRepository.findById(id)
                .orElseThrow(() -> new CustomException("TP not found"));
        return mapper.toTPDTO(tp);
    }

    /**
     * Creates a new TP.
     */
    @Override
    public TPDTO createTP(TPDTO tpDTO) throws CustomException {
        try {
            TP tp = mapper.toTP(tpDTO);
            tp = tpRepository.save(tp);
            return mapper.toTPDTO(tp);
        } catch (Exception e) {
            throw new CustomException("Failed to create TP", e);
        }
    }

    /**
     * Updates an existing TP.
     */
    @Override
    public TPDTO updateTP(Long id, TPDTO tpDTO) throws CustomException {
        TP tp = tpRepository.findById(id)
                .orElseThrow(() -> new CustomException("TP not found"));
        tp.setNb(tpDTO.getNb());
        tp = tpRepository.save(tp);
        return mapper.toTPDTO(tp);
    }

    /**
     * Deletes a TP by its ID.
     */
    @Override
    public void deleteTP(Long id) throws CustomException {
        if (!tpRepository.existsById(id)) {
            throw new CustomException("TP not found");
        }
        tpRepository.deleteById(id);
    }
}
EOF

echo "Service implementation files generated under: $srcprj/service/impl"
echo "Project generation complete."
