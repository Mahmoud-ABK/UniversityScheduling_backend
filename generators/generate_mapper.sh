#!/bin/bash

# Set the source root directory (adjust it to your project structure if necessary)
SOURCE_ROOT="src/main/java/com/scheduling/universityschedule_backend"
PACKAGE="mapper"

# Create the necessary directories
mkdir -p "$SOURCE_ROOT/$PACKAGE"

# Create the Mapper interface
cat <<EOL > "$SOURCE_ROOT/$PACKAGE/EntityDtoMapper.java"
package com.scheduling.universityschedule_backend.$PACKAGE;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.entity.*;

public interface EntityDtoMapper {
    // Seance Mapping
    SeanceDTO toSeanceDTO(Seance seance);
    Seance toSeanceEntity(SeanceDTO seanceDTO);
    
    // TP Mapping
    TPDTO toTPDTO(TP tp);
    TP toTPEntity(TPDTO tpDTO);
    
    // TD Mapping
    TDDTO toTDDTO(TD td);
    TD toTDEntity(TDDTO tdDTO);
    
    // Salle Mapping
    SalleDTO toSalleDTO(Salle salle);
    Salle toSalleEntity(SalleDTO salleDTO);
    
    // Personne Mapping
    PersonneDTO toPersonneDTO(Personne personne);
    Personne toPersonneEntity(PersonneDTO personneDTO);
    
    // Branche Mapping
    BrancheDTO toBrancheDTO(Branche branche);
    Branche toBrancheEntity(BrancheDTO brancheDTO);
    
    // Notification Mapping
    NotificationDTO toNotificationDTO(Notification notification);
    Notification toNotificationEntity(NotificationDTO notificationDTO);
    
    // Signal Mapping
    SignalDTO toSignalDTO(Signal signal);
    Signal toSignalEntity(SignalDTO signalDTO);
    
    // Proposition De Rattrapage Mapping
    PropositionDeRattrapageDTO toPropositionDeRattrapageDTO(PropositionDeRattrapage propositionDeRattrapage);
    PropositionDeRattrapage toPropositionDeRattrapageEntity(PropositionDeRattrapageDTO propositionDTO);
    
    // Fichier Excel Mapping
    FichierExcelDTO toFichierExcelDTO(FichierExcel fichierExcel);
    FichierExcel toFichierExcelEntity(FichierExcelDTO fichierExcelDTO);
}
EOL

# Create the Mapper Implementation
cat <<EOL > "$SOURCE_ROOT/$PACKAGE/EntityDtoMapperImpl.java"
package com.scheduling.universityschedule_backend.$PACKAGE;

import org.springframework.stereotype.Component;
import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.entity.*;

@Component
public class EntityDtoMapperImpl implements EntityDtoMapper {

    @Override
    public SeanceDTO toSeanceDTO(Seance seance) {
        if (seance == null) {
            return null;
        }
        SeanceDTO seanceDTO = new SeanceDTO();
        seanceDTO.setId(seance.getId());
        seanceDTO.setJour(seance.getJour());
        seanceDTO.setHeureDebut(seance.getHeureDebut());
        seanceDTO.setHeureFin(seance.getHeureFin());
        seanceDTO.setType(seance.getType());
        seanceDTO.setMatiere(seance.getMatiere());
        seanceDTO.setFrequence(seance.getFrequence());
        seanceDTO.setSalle(seance.getSalle());
        seanceDTO.setEnseignant(seance.getEnseignant());
        return seanceDTO;
    }

    @Override
    public Seance toSeanceEntity(SeanceDTO seanceDTO) {
        if (seanceDTO == null) {
            return null;
        }
        Seance seance = new Seance();
        seance.setId(seanceDTO.getId());
        seance.setJour(seanceDTO.getJour());
        seance.setHeureDebut(seanceDTO.getHeureDebut());
        seance.setHeureFin(seanceDTO.getHeureFin());
        seance.setType(seanceDTO.getType());
        seance.setMatiere(seanceDTO.getMatiere());
        seance.setFrequence(seanceDTO.getFrequence());
        seance.setSalle(seanceDTO.getSalle());
        seance.setEnseignant(seanceDTO.getEnseignant());
        return seance;
    }

    @Override
    public TPDTO toTPDTO(TP tp) {
        if (tp == null) {
            return null;
        }
        TPDTO tpDTO = new TPDTO();
        tpDTO.setId(tp.getId());
        tpDTO.setJour(tp.getJour());
        tpDTO.setHeureDebut(tp.getHeureDebut());
        tpDTO.setFrequence(tp.getFrequence());
        tpDTO.setSalle(tp.getSalle());
        tpDTO.setEnseignant(tp.getEnseignant());
        return tpDTO;
    }

    @Override
    public TP toTPEntity(TPDTO tpDTO) {
        if (tpDTO == null) {
            return null;
        }
        TP tp = new TP();
        tp.setId(tpDTO.getId());
        tp.setJour(tpDTO.getJour());
        tp.setHeureDebut(tpDTO.getHeureDebut());
        tp.setFrequence(tpDTO.getFrequence());
        tp.setSalle(tpDTO.getSalle());
        tp.setEnseignant(tpDTO.getEnseignant());
        return tp;
    }

    @Override
    public TDDTO toTDDTO(TD td) {
        if (td == null) {
            return null;
        }
        TDDTO tdDTO = new TDDTO();
        tdDTO.setId(td.getId());
        tdDTO.setJour(td.getJour());
        tdDTO.setHeureDebut(td.getHeureDebut());
        tdDTO.setFrequence(td.getFrequence());
        tdDTO.setSalle(td.getSalle());
        tdDTO.setBranche(td.getBranche());
        return tdDTO;
    }

    @Override
    public TD toTDEntity(TDDTO tdDTO) {
        if (tdDTO == null) {
            return null;
        }
        TD td = new TD();
        td.setId(tdDTO.getId());
        td.setJour(tdDTO.getJour());
        td.setHeureDebut(tdDTO.getHeureDebut());
        td.setFrequence(tdDTO.getFrequence());
        td.setSalle(tdDTO.getSalle());
        td.setBranche(tdDTO.getBranche());
        return td;
    }

    @Override
    public SalleDTO toSalleDTO(Salle salle) {
        if (salle == null) {
            return null;
        }
        SalleDTO salleDTO = new SalleDTO();
        salleDTO.setIdentifiant(salle.getIdentifiant());
        salleDTO.setType(salle.getType());
        salleDTO.setCapacite(salle.getCapacite());
        salleDTO.setDisponibilite(salle.getDisponibilite());
        return salleDTO;
    }

    @Override
    public Salle toSalleEntity(SalleDTO salleDTO) {
        if (salleDTO == null) {
            return null;
        }
        Salle salle = new Salle();
        salle.setIdentifiant(salleDTO.getIdentifiant());
        salle.setType(salleDTO.getType());
        salle.setCapacite(salleDTO.getCapacite());
        salle.setDisponibilite(salleDTO.getDisponibilite());
        return salle;
    }

    @Override
    public PersonneDTO toPersonneDTO(Personne personne) {
        if (personne == null) {
            return null;
        }
        PersonneDTO personneDTO = new PersonneDTO();
        personneDTO.setCIN(personne.getCIN());
        personneDTO.setNom(personne.getNom());
        personneDTO.setPrenom(personne.getPrenom());
        personneDTO.setEmail(personne.getEmail());
        personneDTO.setTel(personne.getTel());
        personneDTO.setAdresse(personne.getAdresse());
        return personneDTO;
    }

    @Override
    public Personne toPersonneEntity(PersonneDTO personneDTO) {
        if (personneDTO == null) {
            return null;
        }
        Personne personne = new Personne();
        personne.setCIN(personneDTO.getCIN());
        personne.setNom(personneDTO.getNom());
        personne.setPrenom(personneDTO.getPrenom());
        personne.setEmail(personneDTO.getEmail());
        personne.setTel(personneDTO.getTel());
        personne.setAdresse(personneDTO.getAdresse());
        return personne;
    }

    @Override
    public BrancheDTO toBrancheDTO(Branche branche) {
        if (branche == null) {
            return null;
        }
        BrancheDTO brancheDTO = new BrancheDTO();
        brancheDTO.setNiveau(branche.getNiveau());
        brancheDTO.setSpecialite(branche.getSpecialite());
        brancheDTO.setNbTD(branche.getNbTD());
        brancheDTO.setDepartement(branche.getDepartement());
        return brancheDTO;
    }

    @Override
    public Branche toBrancheEntity(BrancheDTO brancheDTO) {
        if (brancheDTO == null) {
            return null;
        }
        Branche branche = new Branche();
        branche.setNiveau(brancheDTO.getNiveau());
        branche.setSpecialite(brancheDTO.getSpecialite());
        branche.setNbTD(brancheDTO.getNbTD());
        branche.setDepartement(brancheDTO.getDepartement());
        return branche;
    }

    @Override
    public NotificationDTO toNotificationDTO(Notification notification) {
        if (notification == null) {
            return null;
        }
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setMessage(notification.getMessage());
        notificationDTO.setDate(notification.getDate());
        notificationDTO.setRecepteur(notification.getRecepteur());
        notificationDTO.setExpediteur(notification.getExpediteur());
        notificationDTO.setType(notification.getType());
        return notificationDTO;
    }

    @Override
    public Notification toNotificationEntity(NotificationDTO notificationDTO) {
        if (notificationDTO == null) {
            return null;
        }
        Notification notification = new Notification();
        notification.setMessage(notificationDTO.getMessage());
        notification.setDate(notificationDTO.getDate());
        notification.setRecepteur(notificationDTO.getRecepteur());
        notification.setExpediteur(notificationDTO.getExpediteur());
        notification.setType(notificationDTO.getType());
        return notification;
    }

    @Override
    public SignalDTO toSignalDTO(Signal signal) {
        if (signal == null) {
            return null;
        }
        SignalDTO signalDTO = new SignalDTO();
        signalDTO.setMessage(signal.getMessage());
        signalDTO.setSeverity(signal.getSeverity());
        signalDTO.setTimestamp(signal.getTimestamp());
        return signalDTO;
    }

    @Override
    public Signal toSignalEntity(SignalDTO signalDTO) {
        if (signalDTO == null) {
            return null;
        }
        Signal signal = new Signal();
        signal.setMessage(signalDTO.getMessage());
        signal.setSeverity(signalDTO.getSeverity());
        signal.setTimestamp(signalDTO.getTimestamp());
        return signal;
    }

    @Override
    public PropositionDeRattrapageDTO toPropositionDeRattrapageDTO(PropositionDeRattrapage propositionDeRattrapage) {
        if (propositionDeRattrapage == null) {
            return null;
        }
        PropositionDeRattrapageDTO propositionDTO = new PropositionDeRattrapageDTO();
        propositionDTO.setDate(propositionDeRattrapage.getDate());
        propositionDTO.setReason(propositionDeRattrapage.getReason());
        propositionDTO.setStatus(propositionDeRattrapage.getStatus());
        return propositionDTO;
    }

    @Override
    public PropositionDeRattrapage toPropositionDeRattrapageEntity(PropositionDeRattrapageDTO propositionDTO) {
        if (propositionDTO == null) {
            return null;
        }
        PropositionDeRattrapage propositionDeRattrapage = new PropositionDeRattrapage();
        propositionDeRattrapage.setDate(propositionDTO.getDate());
        propositionDeRattrapage.setReason(propositionDTO.getReason());
        propositionDeRattrapage.setStatus(propositionDTO.getStatus());
        return propositionDeRattrapage;
    }

    @Override
    public FichierExcelDTO toFichierExcelDTO(FichierExcel fichierExcel) {
        if (fichierExcel == null) {
            return null;
        }
        FichierExcelDTO fichierExcelDTO = new FichierExcelDTO();
        fichierExcelDTO.setFileName(fichierExcel.getFileName());
        fichierExcelDTO.setStatus(fichierExcel.getStatus());
        fichierExcelDTO.setErrors(fichierExcel.getErrors());
        fichierExcelDTO.setImportDate(fichierExcel.getImportDate());
        return fichierExcelDTO;
    }

    @Override
    public FichierExcel toFichierExcelEntity(FichierExcelDTO fichierExcelDTO) {
        if (fichierExcelDTO == null) {
            return null;
        }
        FichierExcel fichierExcel = new FichierExcel();
        fichierExcel.setFileName(fichierExcelDTO.getFileName());
        fichierExcel.setStatus(fichierExcelDTO.getStatus());
        fichierExcel.setErrors(fichierExcelDTO.getErrors());
        fichierExcel.setImportDate(fichierExcelDTO.getImportDate());
        return fichierExcel;
    }
}
EOL

echo "Mapper Interface and Implementation generated successfully in the '$SOURCE_ROOT/$PACKAGE' directory!"
