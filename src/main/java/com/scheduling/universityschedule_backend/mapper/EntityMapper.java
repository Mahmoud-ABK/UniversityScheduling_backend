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
