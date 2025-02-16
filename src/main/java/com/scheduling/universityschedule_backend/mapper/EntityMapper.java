package com.scheduling.universityschedule_backend.mapper;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.model.*;
import com.scheduling.universityschedule_backend.repository.*;
import com.scheduling.universityschedule_backend.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.mapstruct.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class EntityMapper {

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
    @Autowired
    protected PropositionDeRattrapageRepository propositionDeRattrapageRepository;
    @Autowired
    protected SignalRepository signalRepository;

    // ------------------ Base Personne Mappings ------------------
    public abstract PersonneDTO toPersonneDTO(Personne personne);
    public abstract Personne toPersonne(PersonneDTO dto);

    // ------------------ Administrateur ------------------
    @Mapping(target = "codeAdmin", source = "codeAdmin")
    public abstract AdministrateurDTO toAdministrateurDTO(Administrateur admin);
    public abstract Administrateur toAdministrateur(AdministrateurDTO dto);

    // ------------------ Enseignant ------------------
    @Mapping(target = "seanceIds", source = "seances", qualifiedByName = "mapSeancesToIds")
    @Mapping(target = "propositionIds", source = "propositionsDeRattrapage", qualifiedByName = "mapPropositionsToIds")
    @Mapping(target = "signalIds",source = "signals" , qualifiedByName = "mapSignalsToIds")
    public abstract EnseignantDTO toEnseignantDTO(Enseignant enseignant);

    @Mapping(target = "seances", source = "seanceIds", qualifiedByName = "mapIdListToSeanceList")
    @Mapping(target = "propositionsDeRattrapage", source = "propositionIds", qualifiedByName = "mapIdListToPropositionList")
    @Mapping(target = "signals",source = "signalIds" , qualifiedByName = "mapIdsToSignals")
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
    @Mapping(target = "isread", source = "isread")
    public abstract NotificationDTO toNotificationDTO(Notification notification);

    @Mapping(target = "recepteur", source = "recepteurId", qualifiedByName = "idToPersonne")
    @Mapping(target = "expediteur", source = "expediteurId", qualifiedByName = "idToPersonne")
    @Mapping(target = "isread", source = "isread")
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
    @Mapping(target = "frequence", source = "frequence")
    public abstract SeanceDTO toSeanceDTO(Seance seance);

    @Mapping(target = "salle", source = "salleId", qualifiedByName = "idToSalle")
    @Mapping(target = "enseignant", source = "enseignantId", qualifiedByName = "idToEnseignant")
    @Mapping(target = "branches", source = "brancheIds", qualifiedByName = "mapIdListToBrancheList")
    @Mapping(target = "tds", source = "tdIds", qualifiedByName = "mapIdListToTDList")
    @Mapping(target = "tps", source = "tpIds", qualifiedByName = "mapIdListToTPList")
    @Mapping(target = "frequence", source = "frequence")
    public abstract Seance toSeance(SeanceDTO dto);

    // ------------------ SeanceConflict ------------------
    // ------------------ Conflict Mappings ------------------

    /**
     * Maps a List<Object[]> from findConflictingSeancePairs to a List<SeanceConflictDTO>.
     */
    public List<SeanceConflictDTO> toSeanceConflictDTOList(List<Object[]> conflictPairs) {
        return conflictPairs.stream()
                .map(this::toSeanceConflictDTO)
                .collect(Collectors.toList());
    }

    /**
     * Maps a single Object[] from findConflictingSeancePairs to a SeanceConflictDTO.
     */
    public SeanceConflictDTO toSeanceConflictDTO(Object[] conflictPair) {
        Seance seance1 = (Seance) conflictPair[0];
        Seance seance2 = (Seance) conflictPair[1];
        String conflictTypes = (String) conflictPair[2];

        return new SeanceConflictDTO(
                seance1.getId(),
                seance2.getId(),
                List.of(conflictTypes.split("; "))
        );
    }

    /**
     * Maps a List<Object[]> from findConflictingByRooms to a List<SeanceRoomConflictDTO>.
     */
    public List<SeanceRoomConflictDTO> toSeanceRoomConflictDTOList(List<Object[]> roomConflicts) {
        return roomConflicts.stream()
                .map(this::toSeanceRoomConflictDTO)
                .collect(Collectors.toList());
    }

    /**
     * Maps a single Object[] from findConflictingByRooms to a SeanceRoomConflictDTO.
     */
    public SeanceRoomConflictDTO toSeanceRoomConflictDTO(Object[] roomConflict) {
        Seance seance1 = (Seance) roomConflict[0];
        Seance seance2 = (Seance) roomConflict[1];
        String conflictType = (String) roomConflict[2];

        return new SeanceRoomConflictDTO(
                seance1.getId(),
                seance2.getId(),
                conflictType
        );
    }

    /**
     * Maps a List<Object[]> from findRoomConflictsForSeance to a List<SingleSeanceConflictDTO>.
     */
    public List<SingleSeanceConflictDTO> toSingleSeanceConflictDTOList(List<Object[]> conflictsForSeance) {
        return conflictsForSeance.stream()
                .map(this::toSingleSeanceConflictDTO)
                .collect(Collectors.toList());
    }

    /**
     * Maps a single Object[] from findRoomConflictsForSeance to a SingleSeanceConflictDTO.
     */
    public SingleSeanceConflictDTO toSingleSeanceConflictDTO(Object[] conflictForSeance) {
        Seance seance = (Seance) conflictForSeance[0];
        String conflictTypes = (String) conflictForSeance[1];

        return new SingleSeanceConflictDTO(
                seance.getId(),
                List.of(conflictTypes.split("; "))
        );
    }

    // ------------------ Signal ------------------
    @Mapping(target = "enseignantId", source = "enseignant.id")
    public abstract SignalDTO toSignalDTO(Signal signal);

    @Mapping(target = "enseignant", source = "enseignantId", qualifiedByName = "idToEnseignant")
    public abstract Signal toSignal(SignalDTO dto);

    // ------------------ TD ------------------
    @Mapping(target = "brancheId", source = "branche.id")
    @Mapping(target = "tpIds", source = "tpList", qualifiedByName = "mapTPListToIds")
    @Mapping(target = "seanceIds", source = "seances", qualifiedByName = "mapSeancesToIds")
    public abstract TDDTO toTDDTO(TD td);

    @Mapping(target = "branche", source = "brancheId", qualifiedByName = "idToBranche")
    @Mapping(target = "tpList", source = "tpIds", qualifiedByName = "mapIdListToTPList")
    @Mapping(target = "seances", source = "seanceIds", qualifiedByName = "mapIdListToSeanceList")
    public abstract TD toTD(TDDTO dto);

    // ------------------ Technicien ------------------
    @Mapping(target = "codeTechnicien", source = "codeTechnicien")
    public abstract TechnicienDTO toTechnicienDTO(Technicien technicien);
    public abstract Technicien toTechnicien(TechnicienDTO dto);

    // ------------------ TP ------------------
    @Mapping(target = "tdId", source = "td.id")
    @Mapping(target = "etudiantIds", source = "etudiants", qualifiedByName = "mapEtudiantsToIds")
    @Mapping(target = "seanceIds", source = "seances", qualifiedByName = "mapSeancesToIds")
    public abstract TPDTO toTPDTO(TP tp);

    @Mapping(target = "td", source = "tdId", qualifiedByName = "idToTD")
    @Mapping(target = "etudiants", source = "etudiantIds", qualifiedByName = "mapIdListToEtudiantList")
    @Mapping(target = "seances", source = "seanceIds", qualifiedByName = "mapIdListToSeanceList")
    public abstract TP toTP(TPDTO dto);

    // ------------------ Helper Methods for Collections ------------------
    @Named("mapSeancesToIds")
    public List<Long> mapSeancesToIds(List<Seance> seances) {
        return seances == null ? Collections.emptyList() : seances.stream().map(Seance::getId).collect(Collectors.toList());
    }

    @Named("mapPropositionsToIds")
    public List<Long> mapPropositionsToIds(List<PropositionDeRattrapage> propositions) {
        return propositions == null ? Collections.emptyList() : propositions.stream().map(PropositionDeRattrapage::getId).collect(Collectors.toList());
    }

    @Named("mapBrancheListToIds")
    public List<Long> mapBrancheListToIds(List<Branche> branches) {
        return branches == null ? Collections.emptyList() : branches.stream().map(Branche::getId).collect(Collectors.toList());
    }

    @Named("mapTDListToIds")
    public List<Long> mapTDListToIds(List<TD> tds) {
        return tds == null ? Collections.emptyList() : tds.stream().map(TD::getId).collect(Collectors.toList());
    }

    @Named("mapTPListToIds")
    public List<Long> mapTPListToIds(List<TP> tps) {
        return tps == null ? Collections.emptyList() : tps.stream().map(TP::getId).collect(Collectors.toList());
    }

    @Named("mapEtudiantsToIds")
    public List<Long> mapEtudiantsToIds(List<Etudiant> etudiants) {
        return etudiants == null ? Collections.emptyList() : etudiants.stream().map(Etudiant::getId).collect(Collectors.toList());
    }
    @Named("mapSignalsToIds")
    public List<Long> mapSignalsToIds(List<Signal> signals) {
        return signals == null ? Collections.emptyList() : signals.stream().map(Signal::getId).collect(Collectors.toList());
    }

    // ------------------ Helper Methods for ID -> Entity Conversions ------------------
    @Named("idToBranche")
    public Branche idToBranche(Long id) throws CustomException {
        if (id == null) return null;
        return brancheRepository.findById(id)
                .orElseThrow(() -> new CustomException("Branche not found with ID: " + id));
    }

    @Named("idToTP")
    public TP idToTP(Long id) throws CustomException {
        if (id == null) return null;
        return tpRepository.findById(id)
                .orElseThrow(() -> new CustomException("TP not found with ID: " + id));
    }

    @Named("idToPersonne")
    public Personne idToPersonne(Long id) throws CustomException {
        if (id == null) return null;
        return personneRepository.findById(id)
                .orElseThrow(() -> new CustomException("Personne not found with ID: " + id));
    }

    @Named("idToEnseignant")
    public Enseignant idToEnseignant(Long id) throws CustomException {
        if (id == null) return null;
        return enseignantRepository.findById(id)
                .orElseThrow(() -> new CustomException("Enseignant not found with ID: " + id));
    }

    @Named("idToSalle")
    public Salle idToSalle(Long id) throws CustomException {
        if (id == null) return null;
        return salleRepository.findById(id)
                .orElseThrow(() -> new CustomException("Salle not found with ID: " + id));
    }

    @Named("idToTD")
    public TD idToTD(Long id) throws CustomException {
        if (id == null) return null;
        return tdRepository.findById(id)
                .orElseThrow(() -> new CustomException("TD not found with ID: " + id));
    }

    // ------------------ Helper Methods for Mapping List of IDs to Entities ------------------
    @Named("mapIdListToSeanceList")
    public List<Seance> mapIdListToSeanceList(List<Long> ids) throws CustomException {
        if (ids == null || ids.isEmpty()) return Collections.emptyList();
        List<Seance> seances = seanceRepository.findAllById(ids);
        if (seances.size() != ids.size()) {
            throw new CustomException("One or more Seances not found in the provided ID list.");
        }
        return seances;
    }

    @Named("mapIdListToPropositionList")
    public List<PropositionDeRattrapage> mapIdListToPropositionList(List<Long> ids) throws CustomException {
        if (ids == null || ids.isEmpty()) return Collections.emptyList();
        List<PropositionDeRattrapage> propositions = propositionDeRattrapageRepository.findAllById(ids);
        if (propositions.size() != ids.size()) {
            throw new CustomException("One or more Propositions not found in the provided ID list.");
        }
        return propositions;
    }
    @Named("mapIdsToSignals")
    public List<Signal> mapIdsToSignals(List<Long> ids) throws CustomException {
        if (ids == null || ids.isEmpty()) return Collections.emptyList();
        List<Signal> signals = signalRepository.findAllById(ids);
        if (signals.size() != ids.size()) {
            throw new CustomException("One or more Signals not found in the provided ID list.");
        }
        return signals;
    }

    @Named("mapIdListToBrancheList")
    public List<Branche> mapIdListToBrancheList(List<Long> ids) throws CustomException {
        if (ids == null || ids.isEmpty()) return Collections.emptyList();
        List<Branche> branches = brancheRepository.findAllById(ids);
        if (branches.size() != ids.size()) {
            throw new CustomException("One or more Branches not found in the provided ID list.");
        }
        return branches;
    }

    @Named("mapIdListToTDList")
    public List<TD> mapIdListToTDList(List<Long> ids) throws CustomException {
        if (ids == null || ids.isEmpty()) return Collections.emptyList();
        List<TD> tds = tdRepository.findAllById(ids);
        if (tds.size() != ids.size()) {
            throw new CustomException("One or more TDs not found in the provided ID list.");
        }
        return tds;
    }

    @Named("mapIdListToTPList")
    public List<TP> mapIdListToTPList(List<Long> ids) throws CustomException {
        if (ids == null || ids.isEmpty()) return Collections.emptyList();
        List<TP> tps = tpRepository.findAllById(ids);
        if (tps.size() != ids.size()) {
            throw new CustomException("One or more TPs not found in the provided ID list.");
        }
        return tps;
    }

    @Named("mapIdListToEtudiantList")
    public List<Etudiant> mapIdListToEtudiantList(List<Long> ids) throws CustomException {
        if (ids == null || ids.isEmpty()) return Collections.emptyList();
        List<Etudiant> etudiants = etudiantRepository.findAllById(ids);
        if (etudiants.size() != ids.size()) {
            throw new CustomException("One or more Etudiants not found in the provided ID list.");
        }
        return etudiants;
    }
}