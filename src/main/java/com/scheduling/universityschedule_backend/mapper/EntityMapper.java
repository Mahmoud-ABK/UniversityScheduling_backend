package com.scheduling.universityschedule_backend.mapper;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.model.*;
import com.scheduling.universityschedule_backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class EntityMapper {

    // Inject required repositories
    @Autowired
    protected BrancheRepository brancheRepository;

    @Autowired
    protected TPRepository tpRepository;

    @Autowired
    protected PersonneRepository personneRepository;

    @Autowired
    protected EnseignantRepository enseignantRepository;

    @Autowired
    protected SalleRepository salleRepository;

    @Autowired
    protected TDRepository tdRepository;

    @Autowired
    protected EtudiantRepository etudiantRepository;

    @Autowired
    protected SeanceRepository seanceRepository;

    // ------------------ Administrateur ------------------

    @Mapping(target = "codeAdmin", source = "codeAdmin")
    public abstract AdministrateurDTO toAdministrateurDTO(Administrateur admin);

    @Mapping(target = "codeAdmin", source = "codeAdmin")
    public abstract Administrateur toAdministrateur(AdministrateurDTO dto);

    // ------------------ Enseignant ------------------

    @Mapping(target = "seanceIds", source = "seances", qualifiedByName = "mapSeancesToIds")
    @Mapping(target = "propositionIds", source = "propositionsDeRattrapage", qualifiedByName = "mapPropositionsToIds")
    public abstract EnseignantDTO toEnseignantDTO(Enseignant enseignant);

    // In DTO -> Entity, look up associations via repositories.
    @Mapping(target = "seances", source = "seanceIds", qualifiedByName = "mapIdListToSeanceList")
    @Mapping(target = "propositionsDeRattrapage", source = "propositionIds", qualifiedByName = "mapIdListToPropositionList")
    public abstract Enseignant toEnseignant(EnseignantDTO dto);

    // ------------------ Etudiant ------------------

    @Mapping(target = "brancheId", source = "branche.id")
    @Mapping(target = "tpId", source = "tp.id")
    public abstract EtudiantDTO toEtudiantDTO(Etudiant etudiant);

    @Mapping(target = "branche", source = "brancheId", qualifiedByName = "idToBranche")
    @Mapping(target = "tp", source = "tpId", qualifiedByName = "idToTP")
    public abstract Etudiant toEtudiant(EtudiantDTO dto);

    // ------------------ Branche ------------------

    @Mapping(target = "seanceIds", source = "seances", qualifiedByName = "mapSeancesToIds")
    public abstract BrancheDTO toBrancheDTO(Branche branche);

    @Mapping(target = "seances", source = "seanceIds", qualifiedByName = "mapIdListToSeanceList")
    public abstract Branche toBranche(BrancheDTO dto);

    // ------------------ FichierExcel ------------------

    public abstract FichierExcelDTO toFichierExcelDTO(FichierExcel fichier);
    public abstract FichierExcel toFichierExcel(FichierExcelDTO dto);

    // ------------------ Notification ------------------

    @Mapping(target = "recepteurId", source = "recepteur.id")
    @Mapping(target = "expediteurId", source = "expediteur.id")
    public abstract NotificationDTO toNotificationDTO(Notification notification);

    @Mapping(target = "recepteur", source = "recepteurId", qualifiedByName = "idToPersonne")
    @Mapping(target = "expediteur", source = "expediteurId", qualifiedByName = "idToPersonne")
    public abstract Notification toNotification(NotificationDTO dto);

    // ------------------ PropositionDeRattrapage ------------------

    @Mapping(target = "enseignantId", source = "enseignant.id")
    public abstract PropositionDeRattrapageDTO toPropositionDeRattrapageDTO(PropositionDeRattrapage proposition);

    @Mapping(target = "enseignant", source = "enseignantId", qualifiedByName = "idToEnseignant")
    public abstract PropositionDeRattrapage toPropositionDeRattrapage(PropositionDeRattrapageDTO dto);

    // ------------------ Salle ------------------

    @Mapping(target = "seanceIds", source = "seances", qualifiedByName = "mapSeancesToIds")
    public abstract SalleDTO toSalleDTO(Salle salle);

    @Mapping(target = "seances", source = "seanceIds", qualifiedByName = "mapIdListToSeanceList")
    public abstract Salle toSalle(SalleDTO dto);

    // ------------------ Seance ------------------

    @Mapping(target = "salleId", source = "salle.id")
    @Mapping(target = "enseignantId", source = "enseignant.id")
    @Mapping(target = "brancheIds", source = "branches", qualifiedByName = "mapBrancheListToIds")
    @Mapping(target = "tdIds", source = "tds", qualifiedByName = "mapTDListToIds")
    @Mapping(target = "tpIds", source = "tps", qualifiedByName = "mapTPListToIds")
    public abstract SeanceDTO toSeanceDTO(Seance seance);

    @Mapping(target = "salle", source = "salleId", qualifiedByName = "idToSalle")
    @Mapping(target = "enseignant", source = "enseignantId", qualifiedByName = "idToEnseignant")
    @Mapping(target = "branches", source = "brancheIds", qualifiedByName = "mapIdListToBrancheList")
    @Mapping(target = "tds", source = "tdIds", qualifiedByName = "mapIdListToTDList")
    @Mapping(target = "tps", source = "tpIds", qualifiedByName = "mapIdListToTPList")
    public abstract Seance toSeance(SeanceDTO dto);

    // ------------------ Signal ------------------

    public abstract SignalDTO toSignalDTO(Signal signal);
    public abstract Signal toSignal(SignalDTO dto);

    // ------------------ TD ------------------

    @Mapping(target = "brancheId", source = "branche.id")
    @Mapping(target = "tpIds", source = "tpList", qualifiedByName = "mapTPListToIds")
    public abstract TDDTO toTDDTO(TD td);

    @Mapping(target = "branche", source = "brancheId", qualifiedByName = "idToBranche")
    @Mapping(target = "tpList", source = "tpIds", qualifiedByName = "mapIdListToTPList")
    public abstract TD toTD(TDDTO dto);

    // ------------------ Technicien ------------------

    @Mapping(target = "codeTechnicien", source = "codeTechnicien")
    public abstract TechnicienDTO toTechnicienDTO(Technicien technicien);

    public abstract Technicien toTechnicien(TechnicienDTO dto);

    // ------------------ TP ------------------

    @Mapping(target = "td", source = "tdId", qualifiedByName = "idToTD")
    @Mapping(target = "etudiants", source = "etudiantIds", qualifiedByName = "mapIdListToEtudiantList")
    public abstract TPDTO toTPDTO(TP tp);

    @Mapping(target = "td", source = "tdId", qualifiedByName = "idToTD")
    @Mapping(target = "etudiants", source = "etudiantIds", qualifiedByName = "mapIdListToEtudiantList")
    public abstract TP toTP(TPDTO dto);

    // ------------------ Helper Methods for Collections ------------------

    @Named("mapSeancesToIds")
    public List<Long> mapSeancesToIds(List<Seance> seances) {
        return seances == null ? null : seances.stream().map(Seance::getId).collect(Collectors.toList());
    }

    @Named("mapPropositionsToIds")
    public List<Long> mapPropositionsToIds(List<PropositionDeRattrapage> propositions) {
        return propositions == null ? null : propositions.stream().map(PropositionDeRattrapage::getId).collect(Collectors.toList());
    }

    @Named("mapBrancheListToIds")
    public List<Long> mapBrancheListToIds(List<Branche> branches) {
        return branches == null ? null : branches.stream().map(Branche::getId).collect(Collectors.toList());
    }

    @Named("mapTDListToIds")
    public List<Long> mapTDListToIds(List<TD> tds) {
        return tds == null ? null : tds.stream().map(TD::getId).collect(Collectors.toList());
    }

    @Named("mapTPListToIds")
    public List<Long> mapTPListToIds(List<TP> tps) {
        return tps == null ? null : tps.stream().map(TP::getId).collect(Collectors.toList());
    }

    // ------------------ Helper Methods for ID -> Entity Conversions ------------------

    @Named("idToBranche")
    public Branche idToBranche(Long id) {
        return id == null ? null : brancheRepository.findById(id).orElse(null);
    }

    @Named("idToTP")
    public TP idToTP(Long id) {
        return id == null ? null : tpRepository.findById(id).orElse(null);
    }

    @Named("idToPersonne")
    public Personne idToPersonne(Long id) {
        return id == null ? null : personneRepository.findById(id).orElse(null);
    }

    @Named("idToEnseignant")
    public Enseignant idToEnseignant(Long id) {
        return id == null ? null : enseignantRepository.findById(id).orElse(null);
    }

    @Named("idToSalle")
    public Salle idToSalle(Long id) {
        return id == null ? null : salleRepository.findById(id).orElse(null);
    }

    @Named("idToTD")
    public TD idToTD(Long id) {
        return id == null ? null : tdRepository.findById(id).orElse(null);
    }

    // ------------------ Helper Methods for Mapping List of IDs to Lists of Entities ------------------

    @Named("mapIdListToSeanceList")
    public List<Seance> mapIdListToSeanceList(List<Long> ids) {
        return ids == null ? null : seanceRepository.findAllById(ids);
    }

    @Named("mapIdListToPropositionList")
    public List<PropositionDeRattrapage> mapIdListToPropositionList(List<Long> ids) {
        if (ids == null) return null;
        return ids.stream()
                .map(id -> enseignantRepository.findById(id)
                        .map(e -> {
                            PropositionDeRattrapage p = new PropositionDeRattrapage();
                            p.setId(id);
                            return p;
                        }).orElse(null))
                .collect(Collectors.toList());
        // Alternatively, if you have a PropositionDeRattrapageRepository, use it.
    }

    @Named("mapIdListToBrancheList")
    public List<Branche> mapIdListToBrancheList(List<Long> ids) {
        return ids == null ? null : ids.stream().map(this::idToBranche).collect(Collectors.toList());
    }

    @Named("mapIdListToTDList")
    public List<TD> mapIdListToTDList(List<Long> ids) {
        return ids == null ? null : ids.stream().map(this::idToTD).collect(Collectors.toList());
    }

    @Named("mapIdListToTPList")
    public List<TP> mapIdListToTPList(List<Long> ids) {
        return ids == null ? null : ids.stream().map(this::idToTP).collect(Collectors.toList());
    }

    @Named("mapIdListToEtudiantList")
    public List<Etudiant> mapIdListToEtudiantList(List<Long> ids) {
        return ids == null ? null : ids.stream().map(id -> etudiantRepository.findById(id).orElse(null)).collect(Collectors.toList());
    }
}
