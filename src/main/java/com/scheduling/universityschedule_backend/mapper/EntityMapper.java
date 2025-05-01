package com.scheduling.universityschedule_backend.mapper;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.model.*;
import com.scheduling.universityschedule_backend.repository.*;
import com.scheduling.universityschedule_backend.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.mapstruct.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Entity mapper for converting between entity and DTO objects.
 * Uses MapStruct for automated mapping with custom mapping methods for complex cases.
 * Includes specialized conversion methods for date/time types introduced in the model.
 */
@Mapper(componentModel = "spring")
@Transactional
public abstract class EntityMapper {

    /* Repository dependencies for entity lookup */
    @Autowired protected BrancheRepository brancheRepository;
    @Autowired protected TPRepository tpRepository;
    @Autowired protected PersonneRepository personneRepository;
    @Autowired protected EnseignantRepository enseignantRepository;
    @Autowired protected SalleRepository salleRepository;
    @Autowired protected TDRepository tdRepository;
    @Autowired protected EtudiantRepository etudiantRepository;
    @Autowired protected SeanceRepository seanceRepository;
    @Autowired protected PropositionDeRattrapageRepository propositionDeRattrapageRepository;
    @Autowired protected SignalRepository signalRepository;

    /* Constants for date/time formatting */
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /* --------------------------- */
    /* DATE/TIME TYPE CONVERSIONS  */
    /* --------------------------- */

    /**
     * Converts a string representation to DayOfWeek.
     * Handles both English enum names and French day names.
     *
     * @param day String representation of day
     * @return DayOfWeek enum value, or null if conversion fails
     */
    @Named("stringToDayOfWeek")
    protected DayOfWeek stringToDayOfWeek(String day) {
        if (day == null || day.isEmpty()) {
            return null;
        }

        try {
            // Try direct enum parsing (MONDAY, TUESDAY, etc.)
            return DayOfWeek.valueOf(day.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Handle French day names as fallback
            return switch (day.toLowerCase()) {
                case "lundi" -> DayOfWeek.MONDAY;
                case "mardi" -> DayOfWeek.TUESDAY;
                case "mercredi" -> DayOfWeek.WEDNESDAY;
                case "jeudi" -> DayOfWeek.THURSDAY;
                case "vendredi" -> DayOfWeek.FRIDAY;
                case "samedi" -> DayOfWeek.SATURDAY;
                case "dimanche" -> DayOfWeek.SUNDAY;
                default -> null;
            };
        }
    }

    /**
     * Converts DayOfWeek to string representation.
     *
     * @param day DayOfWeek enum value
     * @return String representation of the day
     */
    @Named("dayOfWeekToString")
    protected String dayOfWeekToString(DayOfWeek day) {
        if (day == null) {
            return null;
        }
        return day.toString();
    }

    /**
     * Converts a time string in HH:mm format to LocalTime.
     *
     * @param time Time string in HH:mm format
     * @return LocalTime object, or null if conversion fails
     */
    @Named("stringToLocalTime")
    protected LocalTime stringToLocalTime(String time) {
        if (time == null || time.isEmpty()) {
            return null;
        }

        try {
            return LocalTime.parse(time, TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Converts LocalTime to string in HH:mm format.
     *
     * @param time LocalTime object
     * @return Time string in HH:mm format
     */
    @Named("localTimeToString")
    protected String localTimeToString(LocalTime time) {
        if (time == null) {
            return null;
        }
        return time.format(TIME_FORMATTER);
    }

    /**
     * Converts string representation to FrequenceType enum.
     * Delegates to FrequenceType.fromString for the actual conversion.
     *
     * @param frequence String representation of frequency
     * @return FrequenceType enum value
     */
    @Named("stringToFrequenceType")
    protected FrequenceType stringToFrequenceType(String frequence) {
        return FrequenceType.fromString(frequence);
    }

    /**
     * Converts FrequenceType enum to string.
     *
     * @param frequence FrequenceType enum value
     * @return String representation of the frequency
     */
    @Named("frequenceTypeToString")
    protected String frequenceTypeToString(FrequenceType frequence) {
        if (frequence == null) {
            return null;
        }
        return frequence.toString();
    }

    /**
     * Converts a date string in yyyy-MM-dd format to LocalDate.
     *
     * @param date Date string in yyyy-MM-dd format
     * @return LocalDate object, or null if conversion fails
     */
    @Named("stringToLocalDate")
    protected LocalDate stringToLocalDate(String date) {
        if (date == null || date.isEmpty()) {
            return null;
        }

        try {
            return LocalDate.parse(date, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Converts LocalDate to string in yyyy-MM-dd format.
     *
     * @param date LocalDate object
     * @return Date string in yyyy-MM-dd format
     */
    @Named("localDateToString")
    protected String localDateToString(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(DATE_FORMATTER);
    }

    /* --------------------------- */
    /* BASE ENTITY MAPPINGS        */
    /* --------------------------- */

    /**
     * Maps a Personne entity to PersonneDTO.
     */
    public abstract PersonneDTO toPersonneDTO(Personne personne);

    /**
     * Maps a PersonneDTO to Personne entity.
     */

    public abstract Personne toPersonne(PersonneDTO dto);

    /* --------------------------- */
    /* ADMINISTRATEUR MAPPINGS     */
    /* --------------------------- */

    /**
     * Maps an Administrateur entity to AdministrateurDTO.
     */
    @Mapping(target = "codeAdmin", source = "codeAdmin")
    public abstract AdministrateurDTO toAdministrateurDTO(Administrateur admin);

    /**
     * Maps an AdministrateurDTO to Administrateur entity.
     */
    public abstract Administrateur toAdministrateur(AdministrateurDTO dto);

    /**
     * Updates an existing Administrateur entity with data from AdministrateurDTO.
     */
    @Mapping(target = "codeAdmin", source = "codeAdmin")
    public abstract void updateFromDto(AdministrateurDTO dto, @MappingTarget Administrateur admin);

    /* --------------------------- */
    /* ENSEIGNANT MAPPINGS         */
    /* --------------------------- */

    /**
     * Maps an Enseignant entity to EnseignantDTO.
     * Converts collections to lists of IDs for DTO.
     */
    @Mapping(target = "seanceIds", source = "seances", qualifiedByName = "mapSeancesToIds")
    @Mapping(target = "propositionIds", source = "propositionsDeRattrapage", qualifiedByName = "mapPropositionsToIds")
    @Mapping(target = "signalIds", source = "signals", qualifiedByName = "mapSignalsToIds")
    public abstract EnseignantDTO toEnseignantDTO(Enseignant enseignant);

    /**
     * Maps an EnseignantDTO to Enseignant entity.
     * Converts lists of IDs to entity collections.
     */
    @Mapping(target = "seances", source = "seanceIds", qualifiedByName = "mapIdListToSeanceList")
    @Mapping(target = "propositionsDeRattrapage", source = "propositionIds", qualifiedByName = "mapIdListToPropositionList")
    @Mapping(target = "signals", source = "signalIds", qualifiedByName = "mapIdsToSignals")
    public abstract Enseignant toEnseignant(EnseignantDTO dto);

    /**
     * Updates an existing Enseignant entity with data from EnseignantDTO.
     */
    @Mapping(target = "seances", ignore = true) // Avoid updating collections directly
    @Mapping(target = "propositionsDeRattrapage", ignore = true)
    @Mapping(target = "signals", ignore = true)
    public abstract void updateFromDto(EnseignantDTO dto, @MappingTarget Enseignant enseignant);

    /* --------------------------- */
    /* ETUDIANT MAPPINGS           */
    /* --------------------------- */

    /**
     * Maps an Etudiant entity to EtudiantDTO.
     * Converts relations to IDs for DTO
     */
    @Mapping(target = "brancheId", source = "branche.id")
    @Mapping(target = "tpId", source = "tp.id")
    public abstract EtudiantDTO toEtudiantDTO(Etudiant etudiant);

    /**
     * Maps an EtudiantDTO to Etudiant entity.
     * Converts IDs to entity references.
     */
    @Mapping(target = "branche", source = "brancheId", qualifiedByName = "idToBranche")
    @Mapping(target = "tp", source = "tpId", qualifiedByName = "idToTP")
    public abstract Etudiant toEtudiant(EtudiantDTO dto);

    /**
     * Updates an existing Etudiant entity with data from EtudiantDTO.
     */
    @Mapping(target = "branche", source = "brancheId", qualifiedByName = "idToBranche")
    @Mapping(target = "tp", source = "tpId", qualifiedByName = "idToTP")
    public abstract void updateFromDto(EtudiantDTO dto, @MappingTarget Etudiant etudiant);

    /* --------------------------- */
    /* BRANCHE MAPPINGS            */
    /* --------------------------- */

    /**
     * Maps a Branche entity to BrancheDTO.
     * Converts collections to lists of IDs.
     */
    @Mapping(target = "seanceIds", source = "seances", qualifiedByName = "mapSeancesToIds")
    @Mapping(target = "tdIds", source = "tds", qualifiedByName = "mapTDListToIds")
    public abstract BrancheDTO toBrancheDTO(Branche branche);

    /**
     * Maps a BrancheDTO to Branche entity.
     * Converts lists of IDs to entity collections.
     */
    @Mapping(target = "seances", source = "seanceIds", qualifiedByName = "mapIdListToSeanceList")
    @Mapping(target = "tds", source = "tdIds", qualifiedByName = "mapIdListToTDList")
    public abstract Branche toBranche(BrancheDTO dto);

    /**
     * Updates an existing Branche entity with data from BrancheDTO.
     */
    @Mapping(target = "seances", ignore = true) // Avoid updating collections directly
    @Mapping(target = "tds", source = "tdIds", qualifiedByName = "mapIdListToTDList")
    public abstract void updateFromDto(BrancheDTO dto, @MappingTarget Branche branche);

    /* --------------------------- */
    /* FICHIER EXCEL MAPPINGS      */
    /* --------------------------- */

    /**
     * Maps a FichierExcel entity to FichierExcelDTO.
     */
    public abstract FichierExcelDTO toFichierExcelDTO(FichierExcel fichier);

    /**
     * Maps a FichierExcelDTO to FichierExcel entity.
     */
    public abstract FichierExcel toFichierExcel(FichierExcelDTO dto);

    /**
     * Updates an existing FichierExcel entity with data from FichierExcelDTO.
     */
    public abstract void updateFromDto(FichierExcelDTO dto, @MappingTarget FichierExcel fichier);

    /* --------------------------- */
    /* NOTIFICATION MAPPINGS       */
    /* --------------------------- */

    /**
     * Maps a Notification entity to NotificationDTO.
     * Converts entity references to IDs.
     */
    @Mapping(target = "recepteurId", source = "recepteur.id")
    @Mapping(target = "expediteurId", source = "expediteur.id")
    @Mapping(target = "isread", source = "isread")
    public abstract NotificationDTO toNotificationDTO(Notification notification);

    /**
     * Maps a NotificationDTO to Notification entity.
     * Converts IDs to entity references.
     */
    @Mapping(target = "recepteur", source = "recepteurId", qualifiedByName = "idToPersonne")
    @Mapping(target = "expediteur", source = "expediteurId", qualifiedByName = "idToPersonne")
    @Mapping(target = "isread", source = "isread")
    public abstract Notification toNotification(NotificationDTO dto);

    /**
     * Updates an existing Notification entity with data from NotificationDTO.
     */
    @Mapping(target = "recepteur", source = "recepteurId", qualifiedByName = "idToPersonne")
    @Mapping(target = "expediteur", source = "expediteurId", qualifiedByName = "idToPersonne")
    @Mapping(target = "isread", source = "isread")
    public abstract void updateFromDto(NotificationDTO dto, @MappingTarget Notification notification);

    /* --------------------------- */
    /* PROPOSITION DE RATTRAPAGE   */
    /* --------------------------- */

    /**
     * Maps a PropositionDeRattrapage entity to PropositionDeRattrapageDTO.
     * Converts entity references to IDs and enum types to strings.
     */
    @Mapping(target = "enseignantId", source = "enseignant.id")
    @Mapping(target = "brancheIds", source = "branches", qualifiedByName = "mapBrancheListToIds")
    @Mapping(target = "tdIds", source = "tds", qualifiedByName = "mapTDListToIds")
    @Mapping(target = "tpIds", source = "tps", qualifiedByName = "mapTPListToIds")
    @Mapping(target = "heureDebut", source = "heureDebut", qualifiedByName = "localTimeToString")
    @Mapping(target = "heureFin", source = "heureFin", qualifiedByName = "localTimeToString")
    @Mapping(target = "date", source = "date", qualifiedByName = "localDateTimeToString")
    @Mapping(target = "type", source = "type", qualifiedByName = "seanceTypeToString")
    @Mapping(target = "status",source = "status",qualifiedByName = "statusToString")
    public abstract PropositionDeRattrapageDTO toPropositionDeRattrapageDTO(PropositionDeRattrapage proposition);

    /**
     * Maps a PropositionDeRattrapageDTO to PropositionDeRattrapage entity.
     * Converts IDs to entity references and strings to enum types.
     */
    @Mapping(target = "enseignant", source = "enseignantId", qualifiedByName = "idToEnseignant")
    @Mapping(target = "branches", source = "brancheIds", qualifiedByName = "mapIdListToBrancheList")
    @Mapping(target = "tds", source = "tdIds", qualifiedByName = "mapIdListToTDList")
    @Mapping(target = "tps", source = "tpIds", qualifiedByName = "mapIdListToTPList")
    @Mapping(target = "heureDebut", source = "heureDebut", qualifiedByName = "stringToLocalTime")
    @Mapping(target = "heureFin", source = "heureFin", qualifiedByName = "stringToLocalTime")
    @Mapping(target = "date", source = "date", qualifiedByName = "stringToLocalDateTime")
    @Mapping(target = "type", source = "type", qualifiedByName = "stringToSeanceType")
    @Mapping(target = "status",source = "status",qualifiedByName = "stringToStatus")
    public abstract PropositionDeRattrapage toPropositionDeRattrapage(PropositionDeRattrapageDTO dto);

    /**
     * Updates an existing PropositionDeRattrapage entity with data from DTO.
     */
    @Mapping(target = "enseignant", source = "enseignantId", qualifiedByName = "idToEnseignant")
    @Mapping(target = "branches", source = "brancheIds", qualifiedByName = "mapIdListToBrancheList")
    @Mapping(target = "tds", source = "tdIds", qualifiedByName = "mapIdListToTDList")
    @Mapping(target = "tps", source = "tpIds", qualifiedByName = "mapIdListToTPList")
    @Mapping(target = "heureDebut", source = "heureDebut", qualifiedByName = "stringToLocalTime")
    @Mapping(target = "heureFin", source = "heureFin", qualifiedByName = "stringToLocalTime")
    @Mapping(target = "date", source = "date", qualifiedByName = "stringToLocalDateTime")
    @Mapping(target = "type", source = "type", qualifiedByName = "stringToSeanceType")
    @Mapping(target = "status",source = "status",qualifiedByName = "stringToStatus")
    public abstract void updateFromDto(PropositionDeRattrapageDTO dto, @MappingTarget PropositionDeRattrapage proposition);

    /**
     * Converts LocalDateTime to String.
     */
    @Named("localDateTimeToString")
    protected String localDateTimeToString(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    /**
     * Converts String to LocalDateTime.
     */
    @Named("stringToLocalDateTime")
    protected LocalDateTime stringToLocalDateTime(String dateTime) throws CustomException {
        if (dateTime == null || dateTime.isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (DateTimeParseException e) {
            throw new CustomException("Invalid datetime format. Expected format: yyyy-MM-dd'T'HH:mm:ss");
        }
    }
    @Named("stringToStatus")
    protected Status stringToStatus(String status) {
        if (status == null) {
            return null;
        }
        return Status.valueOf(status.toUpperCase());
    }
    @Named("statusToString")
    protected String statusToString(Status status) {
        if (status == null) {
            return null;
        }
        return status.name();
    }
    /* --------------------------- */
    /* SALLE MAPPINGS              */
    /* --------------------------- */

    /**
     * Maps a Salle entity to SalleDTO.
     * Converts collections to lists of IDs.
     */
    @Mapping(target = "seanceIds", source = "seances", qualifiedByName = "mapSeancesToIds")
    public abstract SalleDTO toSalleDTO(Salle salle);
    /**
     * Maps a SalleDTO to Salle entity.
     * Converts lists of IDs to entity collections.
     */
    @Mapping(target = "seances", source = "seanceIds", qualifiedByName = "mapIdListToSeanceList")
    public abstract Salle toSalle(SalleDTO dto);

    /**
     * Updates an existing Salle entity with data from SalleDTO.
     */
    @Mapping(target = "seances", ignore = true) // Avoid updating collections directly
    public abstract void updateFromDto(SalleDTO dto, @MappingTarget Salle salle);

    /* --------------------------- */
    /* SEANCE MAPPINGS             */
    /* --------------------------- */

    /**
     * Maps a SeanceDTO to Seance entity.
     * Includes conversions for the new date/time types, entity references, and session type.
     */
    @Mapping(target = "jour", source = "jour", qualifiedByName = "stringToDayOfWeek")
    @Mapping(target = "heureDebut", source = "heureDebut", qualifiedByName = "stringToLocalTime")
    @Mapping(target = "heureFin", source = "heureFin", qualifiedByName = "stringToLocalTime")
    @Mapping(target = "frequence", source = "frequence", qualifiedByName = "stringToFrequenceType")
    @Mapping(target = "type", source = "type", qualifiedByName = "stringToSeanceType")
    @Mapping(target = "date", source = "date", qualifiedByName = "stringToLocalDate")
    @Mapping(target = "salle", source = "salleId", qualifiedByName = "idToSalle")
    @Mapping(target = "enseignant", source = "enseignantId", qualifiedByName = "idToEnseignant")
    @Mapping(target = "branches", source = "brancheIds", qualifiedByName = "mapIdListToBrancheList")
    @Mapping(target = "tds", source = "tdIds", qualifiedByName = "mapIdListToTDList")
    @Mapping(target = "tps", source = "tpIds", qualifiedByName = "mapIdListToTPList")
    public abstract Seance toSeance(SeanceDTO dto);

    /**
     * Maps a Seance entity to SeanceDTO.
     * Converts entity references to IDs, date/time types to strings, and session type to string.
     */
    @Mapping(target = "salleId", source = "salle.id")
    @Mapping(target = "enseignantId", source = "enseignant.id")
    @Mapping(target = "brancheIds", source = "branches", qualifiedByName = "mapBrancheListToIds")
    @Mapping(target = "tdIds", source = "tds", qualifiedByName = "mapTDListToIds")
    @Mapping(target = "tpIds", source = "tps", qualifiedByName = "mapTPListToIds")
    @Mapping(target = "jour", source = "jour", qualifiedByName = "dayOfWeekToString")
    @Mapping(target = "heureDebut", source = "heureDebut", qualifiedByName = "localTimeToString")
    @Mapping(target = "heureFin", source = "heureFin", qualifiedByName = "localTimeToString")
    @Mapping(target = "frequence", source = "frequence", qualifiedByName = "frequenceTypeToString")
    @Mapping(target = "type", source = "type", qualifiedByName = "seanceTypeToString")
    @Mapping(target = "date", source = "date", qualifiedByName = "localDateToString")
    public abstract SeanceDTO toSeanceDTO(Seance seance);

    /**
     * Converts a string to SeanceType enum value.
     */
    @Named("stringToSeanceType")
    protected SeanceType stringToSeanceType(String type) {
        if (type == null) {
            return null;
        }
        return SeanceType.valueOf(type.toUpperCase());
    }

    /**
     * Converts SeanceType enum to string.
     */
    @Named("seanceTypeToString")
    protected String seanceTypeToString(SeanceType type) {
        if (type == null) {
            return null;
        }
        return type.name();
    }



    /**
     * Updates an existing Seance entity with data from SeanceDTO.
     * Includes conversions for the new date/time types, entity references, and session type.
     */
    @Mapping(target = "jour", source = "jour", qualifiedByName = "stringToDayOfWeek")
    @Mapping(target = "heureDebut", source = "heureDebut", qualifiedByName = "stringToLocalTime")
    @Mapping(target = "heureFin", source = "heureFin", qualifiedByName = "stringToLocalTime")
    @Mapping(target = "frequence", source = "frequence", qualifiedByName = "stringToFrequenceType")
    @Mapping(target = "type", source = "type", qualifiedByName = "stringToSeanceType")
    @Mapping(target = "date", source = "date", qualifiedByName = "stringToLocalDate")
    @Mapping(target = "salle", source = "salleId", qualifiedByName = "idToSalle")
    @Mapping(target = "enseignant", source = "enseignantId", qualifiedByName = "idToEnseignant")
    @Mapping(target = "branches", source = "brancheIds", qualifiedByName = "mapIdListToBrancheList")
    @Mapping(target = "tds", source = "tdIds", qualifiedByName = "mapIdListToTDList")
    @Mapping(target = "tps", source = "tpIds", qualifiedByName = "mapIdListToTPList")
    public abstract void updateFromDto(SeanceDTO dto, @MappingTarget Seance seance);

    /* --------------------------- */
    /* CONFLICT MAPPINGS           */
    /* --------------------------- */

    /**
     * Maps a List of Object arrays from findConflictingSeancePairs to a List of SeanceConflictDTO.
     *
     * @param conflictPairs List of Object arrays containing conflict data
     * @return List of SeanceConflictDTO objects
     */
    public List<SeanceConflictDTO> toSeanceConflictDTOList(List<Object[]> conflictPairs) {
        return conflictPairs.stream()
                .map(this::toSeanceConflictDTO)
                .collect(Collectors.toList());
    }

    /**
     * Maps a single Object array from findConflictingSeancePairs to a SeanceConflictDTO.
     *
     * @param conflictPair Object array containing conflict data [seance1, seance2, conflictTypes]
     * @return SeanceConflictDTO representing the conflict
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
     * Maps a List of Object arrays from findConflictingByRooms to a List of SeanceRoomConflictDTO.
     *
     * @param roomConflicts List of Object arrays containing room conflict data
     * @return List of SeanceRoomConflictDTO objects
     */
    public List<SeanceRoomConflictDTO> toSeanceRoomConflictDTOList(List<Object[]> roomConflicts) {
        return roomConflicts.stream()
                .map(this::toSeanceRoomConflictDTO)
                .collect(Collectors.toList());
    }

    /**
     * Maps a single Object array from findConflictingByRooms to a SeanceRoomConflictDTO.
     *
     * @param roomConflict Object array containing room conflict data [seance1, seance2, conflictType]
     * @return SeanceRoomConflictDTO representing the room conflict
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
     * Maps a List of Object arrays from findRoomConflictsForSeance to a List of SingleSeanceConflictDTO.
     *
     * @param conflictsForSeance List of Object arrays containing conflicts for a specific seance
     * @return List of SingleSeanceConflictDTO objects
     */
    public List<SingleSeanceConflictDTO> toSingleSeanceConflictDTOList(List<Object[]> conflictsForSeance) {
        return conflictsForSeance.stream()
                .map(this::toSingleSeanceConflictDTO)
                .collect(Collectors.toList());
    }

    /**
     * Maps a single Object array from findRoomConflictsForSeance to a SingleSeanceConflictDTO.
     *
     * @param conflictForSeance Object array containing conflict data [seance, conflictTypes]
     * @return SingleSeanceConflictDTO representing the conflict
     */
    public SingleSeanceConflictDTO toSingleSeanceConflictDTO(Object[] conflictForSeance) {
        Seance seance = (Seance) conflictForSeance[0];
        String conflictTypes = (String) conflictForSeance[1];

        return new SingleSeanceConflictDTO(
                seance.getId(),
                List.of(conflictTypes.split("; "))
        );
    }

    /* --------------------------- */
    /* SIGNAL MAPPINGS             */
    /* --------------------------- */

    /**
     * Maps a Signal entity to SignalDTO.
     * Converts entity references to IDs.
     */
    @Mapping(target = "enseignantId", source = "enseignant.id")
    public abstract SignalDTO toSignalDTO(Signal signal);

    /**
     * Maps a SignalDTO to Signal entity.
     * Converts IDs to entity references.
     */
    @Mapping(target = "enseignant", source = "enseignantId", qualifiedByName = "idToEnseignant")
    public abstract Signal toSignal(SignalDTO dto);

    /**
     * Updates an existing Signal entity with data from SignalDTO.
     */
    @Mapping(target = "enseignant", source = "enseignantId", qualifiedByName = "idToEnseignant")
    public abstract void updateFromDto(SignalDTO dto, @MappingTarget Signal signal);

    /* --------------------------- */
    /* TD MAPPINGS                 */
    /* --------------------------- */

    /**
     * Maps a TD entity to TDDTO.
     * Converts entity references to IDs and collections to lists of IDs.
     */
    @Mapping(target = "brancheId", source = "branche.id")
    @Mapping(target = "tpIds", source = "tpList", qualifiedByName = "mapTPListToIds")
    @Mapping(target = "seanceIds", source = "seances", qualifiedByName = "mapSeancesToIds")
    public abstract TDDTO toTDDTO(TD td);

    /**
     * Maps a TDDTO to TD entity.
     * Converts IDs to entity references and lists of IDs to collections.
     */
    @Mapping(target = "branche", source = "brancheId", qualifiedByName = "idToBranche")
    @Mapping(target = "tpList", source = "tpIds", qualifiedByName = "mapIdListToTPList")
    @Mapping(target = "seances", source = "seanceIds", qualifiedByName = "mapIdListToSeanceList")
    public abstract TD toTD(TDDTO dto);

    /**
     * Updates an existing TD entity with data from TDDTO.
     */
    @Mapping(target = "branche", source = "brancheId", qualifiedByName = "idToBranche")
    @Mapping(target = "tpList", ignore = true) // Avoid updating collections directly
    @Mapping(target = "seances", ignore = true)
    public abstract void updateFromDto(TDDTO dto, @MappingTarget TD td);

    /* --------------------------- */
    /* TECHNICIEN MAPPINGS         */
    /* --------------------------- */

    /**
     * Maps a Technicien entity to TechnicienDTO.
     */
    @Mapping(target = "codeTechnicien", source = "codeTechnicien")
    public abstract TechnicienDTO toTechnicienDTO(Technicien technicien);

    /**
     * Maps a TechnicienDTO to Technicien entity.
     */
    public abstract Technicien toTechnicien(TechnicienDTO dto);

    /**
     * Updates an existing Technicien entity with data from TechnicienDTO.
     */
    @Mapping(target = "codeTechnicien", source = "codeTechnicien")
    public abstract void updateFromDto(TechnicienDTO dto, @MappingTarget Technicien technicien);

    /* --------------------------- */
    /* TP MAPPINGS                 */
    /* --------------------------- */

    /**
     * Maps a TP entity to TPDTO.
     * Converts entity references to IDs and collections to lists of IDs.
     */
    @Mapping(target = "tdId", source = "td.id")
    @Mapping(target = "etudiantIds", source = "etudiants", qualifiedByName = "mapEtudiantsToIds")
    @Mapping(target = "seanceIds", source = "seances", qualifiedByName = "mapSeancesToIds")
    public abstract TPDTO toTPDTO(TP tp);

    /**
     * Maps a TPDTO to TP entity.
     * Converts IDs to entity references and lists of IDs to collections.
     */
    @Mapping(target = "td", source = "tdId", qualifiedByName = "idToTD")
    @Mapping(target = "etudiants", source = "etudiantIds", qualifiedByName = "mapIdListToEtudiantList")
    @Mapping(target = "seances", source = "seanceIds", qualifiedByName = "mapIdListToSeanceList")
    public abstract TP toTP(TPDTO dto);

    /**
     * Updates an existing TP entity with data from TPDTO.
     */
    @Mapping(target = "td", source = "tdId", qualifiedByName = "idToTD")
    @Mapping(target = "etudiants", ignore = true) // Avoid updating collections directly
    @Mapping(target = "seances", ignore = true)
    public abstract void updateFromDto(TPDTO dto, @MappingTarget TP tp);

    /* --------------------------- */
    /* COLLECTION MAPPING HELPERS  */
    /* --------------------------- */

    /**
     * Maps a list of Seance entities to a list of their IDs.
     */
    @Named("mapSeancesToIds")
    public List<Long> mapSeancesToIds(List<Seance> seances) {
        return seances == null ? Collections.emptyList()
                : seances.stream().map(Seance::getId).collect(Collectors.toList());
    }

    /**
     * Maps a list of PropositionDeRattrapage entities to a list of their IDs.
     */
    @Named("mapPropositionsToIds")
    public List<Long> mapPropositionsToIds(List<PropositionDeRattrapage> propositions) {
        return propositions == null ? Collections.emptyList()
                : propositions.stream().map(PropositionDeRattrapage::getId).collect(Collectors.toList());
    }

    /**
     * Maps a list of Branche entities to a list of their IDs.
     */
    @Named("mapBrancheListToIds")
    public List<Long> mapBrancheListToIds(List<Branche> branches) {
        return branches == null ? Collections.emptyList()
                : branches.stream().map(Branche::getId).collect(Collectors.toList());
    }

    /**
     * Maps a list of TD entities to a list of their IDs.
     */
    @Named("mapTDListToIds")
    public List<Long> mapTDListToIds(List<TD> tds) {
        return tds == null ? Collections.emptyList()
                : tds.stream().map(TD::getId).collect(Collectors.toList());
    }

    /**
     * Maps a list of TP entities to a list of their IDs.
     */
    @Named("mapTPListToIds")
    public List<Long> mapTPListToIds(List<TP> tps) {
        return tps == null ? Collections.emptyList()
                : tps.stream().map(TP::getId).collect(Collectors.toList());
    }

    /**
     * Maps a list of Etudiant entities to a list of their IDs.
     */
    @Named("mapEtudiantsToIds")
    public List<Long> mapEtudiantsToIds(List<Etudiant> etudiants) {
        return etudiants == null ? Collections.emptyList()
                : etudiants.stream().map(Etudiant::getId).collect(Collectors.toList());
    }

    /**
     * Maps a list of Signal entities to a list of their IDs.
     */
    @Named("mapSignalsToIds")
    public List<Long> mapSignalsToIds(List<Signal> signals) {
        return signals == null ? Collections.emptyList()
                : signals.stream().map(Signal::getId).collect(Collectors.toList());
    }

    /**
     * Maps a list of Salle entities to a list of their IDs.
     */
    @Named("mapSallesToIds")
    public List<Long> mapSallesToIds(List<Salle> salles) {
        return salles == null ? Collections.emptyList()
                : salles.stream().map(Salle::getId).collect(Collectors.toList());
    }

    /* --------------------------- */
    /* ID TO ENTITY LOOKUPS        */
    /* --------------------------- */

    /**
     * Converts a Branche ID to a Branche entity by lookup.
     *
     * @param id Branche ID to lookup
     * @return Branche entity
     * @throws CustomException if the Branche with the given ID is not found
     */
    @Named("idToBranche")
    public Branche idToBranche(Long id) throws CustomException {
        if (id == null) return null;
        return brancheRepository.findById(id)
                .orElseThrow(() -> new CustomException("Branche not found with ID: " + id));
    }

    /**
     * Converts a TP ID to a TP entity by lookup.
     *
     * @param id TP ID to lookup
     * @return TP entity
     * @throws CustomException if the TP with the given ID is not found
     */
    @Named("idToTP")
    public TP idToTP(Long id) throws CustomException {
        if (id == null) return null;
        return tpRepository.findById(id)
                .orElseThrow(() -> new CustomException("TP not found with ID: " + id));
    }

    /**
     * Converts a Personne ID to a Personne entity by lookup.
     *
     * @param id Personne ID to lookup
     * @return Personne entity
     * @throws CustomException if the Personne with the given ID is not found
     */
    @Named("idToPersonne")
    public Personne idToPersonne(Long id) throws CustomException {
        if (id == null) return null;
        return personneRepository.findById(id)
                .orElseThrow(() -> new CustomException("Personne not found with ID: " + id));
    }

    /**
     * Converts an Enseignant ID to an Enseignant entity by lookup.
     *
     * @param id Enseignant ID to lookup
     * @return Enseignant entity
     * @throws CustomException if the Enseignant with the given ID is not found
     */
    @Named("idToEnseignant")
    public Enseignant idToEnseignant(Long id) throws CustomException {
        if (id == null) return null;
        return enseignantRepository.findById(id)
                .orElseThrow(() -> new CustomException("Enseignant not found with ID: " + id));
    }

    /**
     * Converts a Salle ID to a Salle entity by lookup.
     *
     * @param id Salle ID to lookup
     * @return Salle entity
     * @throws CustomException if the Salle with the given ID is not found
     */
    @Named("idToSalle")
    public Salle idToSalle(Long id) throws CustomException {
        if (id == null) return null;
        return salleRepository.findById(id)
                .orElseThrow(() -> new CustomException("Salle not found with ID: " + id));
    }

    /**
     * Converts a TD ID to a TD entity by lookup.
     *
     * @param id TD ID to lookup
     * @return TD entity
     * @throws CustomException if the TD with the given ID is not found
     */
    @Named("idToTD")
    public TD idToTD(Long id) throws CustomException {
        if (id == null) return null;
        return tdRepository.findById(id)
                .orElseThrow(() -> new CustomException("TD not found with ID: " + id));
    }

    /* --------------------------- */
    /* ID LIST TO ENTITY LIST      */
    /* --------------------------- */

    /**
     * Converts a list of Salle IDs to a list of Salle entities by lookup.
     *
     * @param ids List of Salle IDs to lookup
     * @return List of Salle entities
     * @throws CustomException if any Salle with the given IDs are not found
     */
    @Named("mapIdListToSalleList")
    public List<Salle> mapIdListToSalleList(List<Long> ids) throws CustomException {
        if (ids == null || ids.isEmpty()) return Collections.emptyList();
        List<Salle> salles = salleRepository.findAllById(ids);
        if (ids.size() != salles.size()){
            throw new CustomException("One or more Salles not found in the provided ID list.");
        }
        return salles;
    }

    /**
     * Converts a list of Seance IDs to a list of Seance entities by lookup.
     *
     * @param ids List of Seance IDs to lookup
     * @return List of Seance entities
     * @throws CustomException if any Seance with the given IDs are not found
     */
    @Named("mapIdListToSeanceList")
    public List<Seance> mapIdListToSeanceList(List<Long> ids) throws CustomException {
        if (ids == null || ids.isEmpty()) return Collections.emptyList();
        List<Seance> seances = seanceRepository.findAllById(ids);
        if (seances.size() != ids.size()) {
            throw new CustomException("One or more Seances not found in the provided ID list.");
        }
        return seances;
    }

    /**
     * Converts a list of PropositionDeRattrapage IDs to a list of PropositionDeRattrapage entities by lookup.
     *
     * @param ids List of PropositionDeRattrapage IDs to lookup
     * @return List of PropositionDeRattrapage entities
     * @throws CustomException if any PropositionDeRattrapage with the given IDs are not found
     */
    @Named("mapIdListToPropositionList")
    public List<PropositionDeRattrapage> mapIdListToPropositionList(List<Long> ids) throws CustomException {
        if (ids == null || ids.isEmpty()) return Collections.emptyList();
        List<PropositionDeRattrapage> propositions = propositionDeRattrapageRepository.findAllById(ids);
        if (propositions.size() != ids.size()) {
            throw new CustomException("One or more Propositions not found in the provided ID list.");
        }
        return propositions;
    }

    /**
     * Converts a list of Signal IDs to a list of Signal entities by lookup.
     *
     * @param ids List of Signal IDs to lookup
     * @return List of Signal entities
     * @throws CustomException if any Signal with the given IDs are not found
     */
    @Named("mapIdsToSignals")
    public List<Signal> mapIdsToSignals(List<Long> ids) throws CustomException {
        if (ids == null || ids.isEmpty()) return Collections.emptyList();
        List<Signal> signals = signalRepository.findAllById(ids);
        if (signals.size() != ids.size()) {
            throw new CustomException("One or more Signals not found in the provided ID list.");
        }
        return signals;
    }

    /**
     * Converts a list of Branche IDs to a list of Branche entities by lookup.
     *
     * @param ids List of Branche IDs to lookup
     * @return List of Branche entities
     * @throws CustomException if any Branche with the given IDs are not found
     */
    @Named("mapIdListToBrancheList")
    public List<Branche> mapIdListToBrancheList(List<Long> ids) throws CustomException {
        if (ids == null || ids.isEmpty()) return Collections.emptyList();
        List<Branche> branches = brancheRepository.findAllById(ids);
        if (branches.size() != ids.size()) {
            throw new CustomException("One or more Branches not found in the provided ID list.");
        }
        return branches;
    }

    /**
     * Converts a list of TD IDs to a list of TD entities by lookup.
     *
     * @param ids List of TD IDs to lookup
     * @return List of TD entities
     * @throws CustomException if any TD with the given IDs are not found
     */
    @Named("mapIdListToTDList")
    public List<TD> mapIdListToTDList(List<Long> ids) throws CustomException {
        if (ids == null || ids.isEmpty()) return Collections.emptyList();
        List<TD> tds = tdRepository.findAllById(ids);
        if (tds.size() != ids.size()) {
            throw new CustomException("One or more TDs not found in the provided ID list.");
        }
        return tds;
    }

    /**
     * Converts a list of TP IDs to a list of TP entities by lookup.
     *
     * @param ids List of TP IDs to lookup
     * @return List of TP entities
     * @throws CustomException if any TP with the given IDs are not found
     */
    @Named("mapIdListToTPList")
    public List<TP> mapIdListToTPList(List<Long> ids) throws CustomException {
        if (ids == null || ids.isEmpty()) return Collections.emptyList();
        List<TP> tps = tpRepository.findAllById(ids);
        if (tps.size() != ids.size()) {
            throw new CustomException("One or more TPs not found in the provided ID list.");
        }
        return tps;
    }

    /**
     * Converts a list of Etudiant IDs to a list of Etudiant entities by lookup.
     *
     * @param ids List of Etudiant IDs to lookup
     * @return List of Etudiant entities
     * @throws CustomException if any Etudiant with the given IDs are not found
     */
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