package com.scheduling.universityschedule_backend.mapper;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.model.*;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Central component that maps between Entity and DTO objects.
 * It provides bidirectional mapping methods for each entity in the system.
 */
@Component
public class EntityMapper {

    /**
     * Generic helper method to extract a list of IDs from a list of entities.
     *
     * @param entities    The list of entities.
     * @param idExtractor A lambda expression to extract the ID from an entity.
     * @param <T>         The type of the entity.
     * @return A list of IDs, or null if the input list is null.
     */
    private <T> List<Long> mapIds(List<T> entities, Function<T, Long> idExtractor) {
        if (entities == null) {
            return null;
        }
        return entities.stream().map(idExtractor).collect(Collectors.toList());
    }

    /* ================= Common Personne Mappings ================= */

    /**
     * Copies common fields from a Personne entity to a PersonneDTO.
     *
     * @param entity The source Personne entity.
     * @param dto    The target PersonneDTO.
     */
    private void mapPersonneToDTO(Personne entity, PersonneDTO dto) {
        if (entity == null || dto == null) {
            return;
        }
        dto.setId(entity.getId());
        dto.setCin(entity.getCin());
        dto.setNom(entity.getNom());
        dto.setPrenom(entity.getPrenom());
        dto.setEmail(entity.getEmail());
        dto.setTel(entity.getTel());
        dto.setAdresse(entity.getAdresse());
    }

    /**
     * Copies common fields from a PersonneDTO to a Personne entity.
     *
     * @param dto    The source PersonneDTO.
     * @param entity The target Personne entity.
     */
    private void mapPersonneToEntity(PersonneDTO dto, Personne entity) {
        if (dto == null || entity == null) {
            return;
        }
        entity.setId(dto.getId());
        entity.setCin(dto.getCin());
        entity.setNom(dto.getNom());
        entity.setPrenom(dto.getPrenom());
        entity.setEmail(dto.getEmail());
        entity.setTel(dto.getTel());
        entity.setAdresse(dto.getAdresse());
    }

    /* ================= Administrateur Mappings ================= */

    /**
     * Maps an Administrateur entity to its DTO.
     *
     * @param admin The Administrateur entity.
     * @return The corresponding AdministrateurDTO.
     */
    public AdministrateurDTO toAdministrateurDTO(Administrateur admin) {
        if (admin == null) {
            return null;
        }
        AdministrateurDTO dto = new AdministrateurDTO();
        mapPersonneToDTO(admin, dto);
        dto.setCodeAdmin(admin.getCodeAdmin());
        return dto;
    }

    /**
     * Maps an AdministrateurDTO back to its entity.
     *
     * @param dto The AdministrateurDTO.
     * @return The corresponding Administrateur entity.
     */
    public Administrateur toAdministrateur(AdministrateurDTO dto) {
        if (dto == null) {
            return null;
        }
        Administrateur admin = new Administrateur();
        mapPersonneToEntity(dto, admin);
        admin.setCodeAdmin(dto.getCodeAdmin());
        return admin;
    }

    /* ================= Enseignant Mappings ================= */

    /**
     * Maps an Enseignant entity to its DTO.
     *
     * @param enseignant The Enseignant entity.
     * @return The corresponding EnseignantDTO.
     */
    public EnseignantDTO toEnseignantDTO(Enseignant enseignant) {
        if (enseignant == null) {
            return null;
        }
        EnseignantDTO dto = new EnseignantDTO();
        mapPersonneToDTO(enseignant, dto);
        dto.setCodeEnseignant(enseignant.getCodeEnseignant());
        dto.setHeures(enseignant.getHeures());
        // Map associated Seance and PropositionDeRattrapage IDs
        dto.setSeanceIds(mapIds(enseignant.getSeances(), Seance::getId));
        dto.setPropositionIds(mapIds(enseignant.getPropositionsDeRattrapage(), PropositionDeRattrapage::getId));
        return dto;
    }

    /**
     * Maps an EnseignantDTO back to its entity.
     *
     * @param dto The EnseignantDTO.
     * @return The corresponding Enseignant entity.
     */
    public Enseignant toEnseignant(EnseignantDTO dto) {
        if (dto == null) {
            return null;
        }
        Enseignant enseignant = new Enseignant();
        mapPersonneToEntity(dto, enseignant);
        enseignant.setCodeEnseignant(dto.getCodeEnseignant());
        enseignant.setHeures(dto.getHeures());
        // Associations (seances and propositions) are not set from DTO IDs.
        return enseignant;
    }

    /* ================= Etudiant Mappings ================= */

    /**
     * Maps an Etudiant entity to its DTO.
     *
     * @param etudiant The Etudiant entity.
     * @return The corresponding EtudiantDTO.
     */
    public EtudiantDTO toEtudiantDTO(Etudiant etudiant) {
        if (etudiant == null) {
            return null;
        }
        EtudiantDTO dto = new EtudiantDTO();
        mapPersonneToDTO(etudiant, dto);
        dto.setMatricule(etudiant.getMatricule());
        dto.setBrancheId(etudiant.getBranche() != null ? etudiant.getBranche().getId() : null);
        dto.setTpId(etudiant.getTp() != null ? etudiant.getTp().getId() : null);
        return dto;
    }

    /**
     * Maps an EtudiantDTO back to its entity.
     *
     * @param dto The EtudiantDTO.
     * @return The corresponding Etudiant entity.
     */
    public Etudiant toEtudiant(EtudiantDTO dto) {
        if (dto == null) {
            return null;
        }
        Etudiant etudiant = new Etudiant();
        mapPersonneToEntity(dto, etudiant);
        etudiant.setMatricule(dto.getMatricule());
        if (dto.getBrancheId() != null) {
            Branche branche = new Branche();
            branche.setId(dto.getBrancheId());
            etudiant.setBranche(branche);
        }
        if (dto.getTpId() != null) {
            TP tp = new TP();
            tp.setId(dto.getTpId());
            etudiant.setTp(tp);
        }
        return etudiant;
    }

    /* ================= Branche Mappings ================= */

    /**
     * Maps a Branche entity to its DTO.
     *
     * @param branche The Branche entity.
     * @return The corresponding BrancheDTO.
     */
    public BrancheDTO toBrancheDTO(Branche branche) {
        if (branche == null) {
            return null;
        }
        BrancheDTO dto = new BrancheDTO();
        dto.setId(branche.getId());
        dto.setNiveau(branche.getNiveau());
        dto.setSpecialite(branche.getSpecialite());
        dto.setNbTD(branche.getNbTD());
        dto.setDepartement(branche.getDepartement());
        dto.setSeanceIds(mapIds(branche.getSeances(), Seance::getId));
        return dto;
    }

    /**
     * Maps a BrancheDTO back to its entity.
     *
     * @param dto The BrancheDTO.
     * @return The corresponding Branche entity.
     */
    public Branche toBranche(BrancheDTO dto) {
        if (dto == null) {
            return null;
        }
        Branche branche = new Branche();
        branche.setId(dto.getId());
        branche.setNiveau(dto.getNiveau());
        branche.setSpecialite(dto.getSpecialite());
        branche.setNbTD(dto.getNbTD());
        branche.setDepartement(dto.getDepartement());
        // Note: seanceIds are not mapped to Seance objects.
        return branche;
    }

    /* ================= FichierExcel Mappings ================= */

    /**
     * Maps a FichierExcel entity to its DTO.
     *
     * @param fichier The FichierExcel entity.
     * @return The corresponding FichierExcelDTO.
     */
    public FichierExcelDTO toFichierExcelDTO(FichierExcel fichier) {
        if (fichier == null) {
            return null;
        }
        FichierExcelDTO dto = new FichierExcelDTO();
        dto.setId(fichier.getId());
        dto.setFileName(fichier.getFileName());
        dto.setStatus(fichier.getStatus());
        dto.setErrors(fichier.getErrors());
        dto.setImportDate(fichier.getImportDate());
        return dto;
    }

    /**
     * Maps a FichierExcelDTO back to its entity.
     *
     * @param dto The FichierExcelDTO.
     * @return The corresponding FichierExcel entity.
     */
    public FichierExcel toFichierExcel(FichierExcelDTO dto) {
        if (dto == null) {
            return null;
        }
        FichierExcel fichier = new FichierExcel();
        fichier.setId(dto.getId());
        fichier.setFileName(dto.getFileName());
        fichier.setStatus(dto.getStatus());
        fichier.setErrors(dto.getErrors());
        fichier.setImportDate(dto.getImportDate());
        return fichier;
    }

    /* ================= Notification Mappings ================= */

    /**
     * Maps a Notification entity to its DTO.
     *
     * @param notification The Notification entity.
     * @return The corresponding NotificationDTO.
     */
    public NotificationDTO toNotificationDTO(Notification notification) {
        if (notification == null) {
            return null;
        }
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getId());
        dto.setMessage(notification.getMessage());
        dto.setDate(notification.getDate());
        dto.setType(notification.getType());
        dto.setRead(notification.getRead());
        dto.setRecepteurId(notification.getRecepteur() != null ? notification.getRecepteur().getId() : null);
        dto.setExpediteurId(notification.getExpediteur() != null ? notification.getExpediteur().getId() : null);
        return dto;
    }

    /**
     * Maps a NotificationDTO back to its entity.
     *
     * @param dto The NotificationDTO.
     * @return The corresponding Notification entity.
     */
    public Notification toNotification(NotificationDTO dto) {
        if (dto == null) {
            return null;
        }
        Notification notification = new Notification();
        notification.setId(dto.getId());
        notification.setMessage(dto.getMessage());
        notification.setDate(dto.getDate());
        notification.setType(dto.getType());
        notification.setRead(dto.getRead());
        if (dto.getRecepteurId() != null) {
            Personne recepteur = new Personne();
            recepteur.setId(dto.getRecepteurId());
            notification.setRecepteur(recepteur);
        }
        if (dto.getExpediteurId() != null) {
            Personne expediteur = new Personne();
            expediteur.setId(dto.getExpediteurId());
            notification.setExpediteur(expediteur);
        }
        return notification;
    }

    /* ================= PropositionDeRattrapage Mappings ================= */

    /**
     * Maps a PropositionDeRattrapage entity to its DTO.
     *
     * @param proposition The PropositionDeRattrapage entity.
     * @return The corresponding PropositionDeRattrapageDTO.
     */
    public PropositionDeRattrapageDTO toPropositionDeRattrapageDTO(PropositionDeRattrapage proposition) {
        if (proposition == null) {
            return null;
        }
        PropositionDeRattrapageDTO dto = new PropositionDeRattrapageDTO();
        dto.setId(proposition.getId());
        dto.setDate(proposition.getDate());
        dto.setReason(proposition.getReason());
        dto.setStatus(proposition.getStatus());
        dto.setEnseignantId(proposition.getEnseignant() != null ? proposition.getEnseignant().getId() : null);
        return dto;
    }

    /**
     * Maps a PropositionDeRattrapageDTO back to its entity.
     *
     * @param dto The PropositionDeRattrapageDTO.
     * @return The corresponding PropositionDeRattrapage entity.
     */
    public PropositionDeRattrapage toPropositionDeRattrapage(PropositionDeRattrapageDTO dto) {
        if (dto == null) {
            return null;
        }
        PropositionDeRattrapage proposition = new PropositionDeRattrapage();
        proposition.setId(dto.getId());
        proposition.setDate(dto.getDate());
        proposition.setReason(dto.getReason());
        proposition.setStatus(dto.getStatus());
        if (dto.getEnseignantId() != null) {
            Enseignant enseignant = new Enseignant();
            enseignant.setId(dto.getEnseignantId());
            proposition.setEnseignant(enseignant);
        }
        return proposition;
    }

    /* ================= Salle Mappings ================= */

    /**
     * Maps a Salle entity to its DTO.
     *
     * @param salle The Salle entity.
     * @return The corresponding SalleDTO.
     */
    public SalleDTO toSalleDTO(Salle salle) {
        if (salle == null) {
            return null;
        }
        SalleDTO dto = new SalleDTO();
        dto.setId(salle.getId());
        dto.setIdentifiant(salle.getIdentifiant());
        dto.setType(salle.getType());
        dto.setCapacite(salle.getCapacite());
        dto.setDisponibilite(salle.getDisponibilite());
        dto.setSeanceIds(mapIds(salle.getSeances(), Seance::getId));
        return dto;
    }

    /**
     * Maps a SalleDTO back to its entity.
     *
     * @param dto The SalleDTO.
     * @return The corresponding Salle entity.
     */
    public Salle toSalle(SalleDTO dto) {
        if (dto == null) {
            return null;
        }
        Salle salle = new Salle();
        salle.setId(dto.getId());
        salle.setIdentifiant(dto.getIdentifiant());
        salle.setType(dto.getType());
        salle.setCapacite(dto.getCapacite());
        salle.setDisponibilite(dto.getDisponibilite());
        // Note: seanceIds are not mapped to actual Seance objects.
        return salle;
    }

    /* ================= Seance Mappings ================= */

    /**
     * Maps a Seance entity to its DTO.
     *
     * @param seance The Seance entity.
     * @return The corresponding SeanceDTO.
     */
    public SeanceDTO toSeanceDTO(Seance seance) {
        if (seance == null) {
            return null;
        }
        SeanceDTO dto = new SeanceDTO();
        dto.setId(seance.getId());
        dto.setJour(seance.getJour());
        dto.setHeureDebut(seance.getHeureDebut());
        dto.setHeureFin(seance.getHeureFin());
        dto.setType(seance.getType());
        dto.setMatiere(seance.getMatiere());
        dto.setFrequence(seance.getFrequence());
        dto.setSalleId(seance.getSalle() != null ? seance.getSalle().getId() : null);
        dto.setEnseignantId(seance.getEnseignant() != null ? seance.getEnseignant().getId() : null);
        dto.setBrancheIds(mapIds(seance.getBranches(), Branche::getId));
        dto.setTdIds(mapIds(seance.getTds(), TD::getId));
        dto.setTpIds(mapIds(seance.getTps(), TP::getId));
        return dto;
    }

    /**
     * Maps a SeanceDTO back to its entity.
     *
     * @param dto The SeanceDTO.
     * @return The corresponding Seance entity.
     */
    public Seance toSeance(SeanceDTO dto) {
        if (dto == null) {
            return null;
        }
        Seance seance = new Seance();
        seance.setId(dto.getId());
        seance.setJour(dto.getJour());
        seance.setHeureDebut(dto.getHeureDebut());
        seance.setHeureFin(dto.getHeureFin());
        seance.setType(dto.getType());
        seance.setMatiere(dto.getMatiere());
        seance.setFrequence(dto.getFrequence());
        if (dto.getSalleId() != null) {
            Salle salle = new Salle();
            salle.setId(dto.getSalleId());
            seance.setSalle(salle);
        }
        if (dto.getEnseignantId() != null) {
            Enseignant enseignant = new Enseignant();
            enseignant.setId(dto.getEnseignantId());
            seance.setEnseignant(enseignant);
        }
        // Note: brancheIds, tdIds, and tpIds are not mapped to actual collections.
        return seance;
    }

    /* ================= Signal Mappings ================= */

    /**
     * Maps a Signal entity to its DTO.
     *
     * @param signal The Signal entity.
     * @return The corresponding SignalDTO.
     */
    public SignalDTO toSignalDTO(Signal signal) {
        if (signal == null) {
            return null;
        }
        SignalDTO dto = new SignalDTO();
        dto.setId(signal.getId());
        dto.setMessage(signal.getMessage());
        dto.setSeverity(signal.getSeverity());
        dto.setTimestamp(signal.getTimestamp());
        return dto;
    }

    /**
     * Maps a SignalDTO back to its entity.
     *
     * @param dto The SignalDTO.
     * @return The corresponding Signal entity.
     */
    public Signal toSignal(SignalDTO dto) {
        if (dto == null) {
            return null;
        }
        Signal signal = new Signal();
        signal.setId(dto.getId());
        signal.setMessage(dto.getMessage());
        signal.setSeverity(dto.getSeverity());
        signal.setTimestamp(dto.getTimestamp());
        return signal;
    }

    /* ================= TD Mappings ================= */

    /**
     * Maps a TD entity to its DTO.
     *
     * @param td The TD entity.
     * @return The corresponding TDDTO.
     */
    public TDDTO toTDDTO(TD td) {
        if (td == null) {
            return null;
        }
        TDDTO dto = new TDDTO();
        dto.setId(td.getId());
        dto.setNb(td.getNb());
        dto.setNbTP(td.getNbTP());
        dto.setBrancheId(td.getBranche() != null ? td.getBranche().getId() : null);
        dto.setTpIds(mapIds(td.getTpList(), TP::getId));
        return dto;
    }

    /**
     * Maps a TDDTO back to its TD entity.
     *
     * @param dto The TDDTO.
     * @return The corresponding TD entity.
     */
    public TD toTD(TDDTO dto) {
        if (dto == null) {
            return null;
        }
        TD td = new TD();
        td.setId(dto.getId());
        td.setNb(dto.getNb());
        td.setNbTP(dto.getNbTP());
        if (dto.getBrancheId() != null) {
            Branche branche = new Branche();
            branche.setId(dto.getBrancheId());
            td.setBranche(branche);
        }
        // Note: tpIds are not mapped to actual TP objects.
        return td;
    }

    /* ================= Technicien Mappings ================= */

    /**
     * Maps a Technicien entity to its DTO.
     *
     * @param technicien The Technicien entity.
     * @return The corresponding TechnicienDTO.
     */
    public TechnicienDTO toTechnicienDTO(Technicien technicien) {
        if (technicien == null) {
            return null;
        }
        TechnicienDTO dto = new TechnicienDTO();
        mapPersonneToDTO(technicien, dto);
        dto.setCodeTechnicien(technicien.getCodeTechnicien());
        return dto;
    }

    /**
     * Maps a TechnicienDTO back to its entity.
     *
     * @param dto The TechnicienDTO.
     * @return The corresponding Technicien entity.
     */
    public Technicien toTechnicien(TechnicienDTO dto) {
        if (dto == null) {
            return null;
        }
        Technicien technicien = new Technicien();
        mapPersonneToEntity(dto, technicien);
        technicien.setCodeTechnicien(dto.getCodeTechnicien());
        return technicien;
    }

    /* ================= TP Mappings ================= */

    /**
     * Maps a TP entity to its DTO.
     *
     * @param tp The TP entity.
     * @return The corresponding TPDTO.
     */
    public TPDTO toTPDTO(TP tp) {
        if (tp == null) {
            return null;
        }
        TPDTO dto = new TPDTO();
        dto.setId(tp.getId());
        dto.setNb(tp.getNb());
        dto.setTdId(tp.getTd() != null ? tp.getTd().getId() : null);
        dto.setEtudiantIds(mapIds(tp.getEtudiants(), Etudiant::getId));
        return dto;
    }

    /**
     * Maps a TPDTO back to its entity.
     *
     * @param dto The TPDTO.
     * @return The corresponding TP entity.
     */
    public TP toTP(TPDTO dto) {
        if (dto == null) {
            return null;
        }
        TP tp = new TP();
        tp.setId(dto.getId());
        tp.setNb(dto.getNb());
        if (dto.getTdId() != null) {
            TD td = new TD();
            td.setId(dto.getTdId());
            tp.setTd(td);
        }
        // Note: etudiantIds are not mapped to actual Etudiant objects.
        return tp;
    }
}
