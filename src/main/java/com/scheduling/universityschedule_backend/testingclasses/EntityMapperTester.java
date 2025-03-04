package com.scheduling.universityschedule_backend.testingclasses;



import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.*;
import com.scheduling.universityschedule_backend.repository.*;
import com.scheduling.universityschedule_backend.util.CustomLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
public class EntityMapperTester {

    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private PersonneRepository personneRepository;
    @Autowired
    private EnseignantRepository enseignantRepository;
    @Autowired
    private BrancheRepository brancheRepository;
    @Autowired
    private SalleRepository salleRepository;
    @Autowired
    private SeanceRepository seanceRepository;
    @Autowired
    private TDRepository tdRepository;
    @Autowired
    private TPRepository tpRepository;
    @Autowired
    private SignalRepository signalRepository;
    @Autowired
    private PropositionDeRattrapageRepository propositionDeRattrapageRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private FichierExcelRepository fichierExcelRepository;

    public void testEntityMapper() throws CustomException {
        CustomLogger.logInfo("=================== Testing Entity Mapper (Both Ways) ====================");

        // Fetch entities from the database (assuming each repository returns a non-empty list)
        Personne personne = personneRepository.findAll().getFirst();
        Enseignant enseignant = enseignantRepository.findAll().getFirst();
        Branche branche = brancheRepository.findAll().getFirst();
        Salle salle = salleRepository.findAll().getFirst();
        Seance seance = seanceRepository.findAll().getFirst();
        TD td = tdRepository.findAll().getFirst();
        TP tp = tpRepository.findAll().getFirst();
        Signal signal = signalRepository.findAll().getFirst();
        PropositionDeRattrapage proposition = propositionDeRattrapageRepository.findAll().getFirst();
        Notification notification = notificationRepository.findAll().getFirst();
        FichierExcel fichierExcel = fichierExcelRepository.findAll().getFirst();

        // --- Mapping: Entity -> DTO ---
        PersonneDTO personneDTO = entityMapper.toPersonneDTO(personne);
        CustomLogger.logInfo("Entity -> DTO | PersonneDTO: " + personneDTO);

        EnseignantDTO enseignantDTO = entityMapper.toEnseignantDTO(enseignant);
        CustomLogger.logInfo("Entity -> DTO | EnseignantDTO: " + enseignantDTO);


        BrancheDTO brancheDTO = entityMapper.toBrancheDTO(branche);
        CustomLogger.logInfo("Entity -> DTO | BrancheDTO: " + brancheDTO);

        SalleDTO salleDTO = entityMapper.toSalleDTO(salle);
        CustomLogger.logInfo("Entity -> DTO | SalleDTO: " + salleDTO);

        SeanceDTO seanceDTO = entityMapper.toSeanceDTO(seance);
        CustomLogger.logInfo("Entity -> DTO | SeanceDTO: " + seanceDTO);

        TDDTO tdDTO = entityMapper.toTDDTO(td);
        CustomLogger.logInfo("Entity -> DTO | TDDTO: " + tdDTO);

        TPDTO tpDTO = entityMapper.toTPDTO(tp);
        CustomLogger.logInfo("Entity -> DTO | TPDTO: " + tpDTO);

        SignalDTO signalDTO = entityMapper.toSignalDTO(signal);
        CustomLogger.logInfo("Entity -> DTO | SignalDTO: " + signalDTO);

        PropositionDeRattrapageDTO propositionDTO = entityMapper.toPropositionDeRattrapageDTO(proposition);
        CustomLogger.logInfo("Entity -> DTO | PropositionDeRattrapageDTO: " + propositionDTO);

        NotificationDTO notificationDTO = entityMapper.toNotificationDTO(notification);
        CustomLogger.logInfo("Entity -> DTO | NotificationDTO: " + notificationDTO);

        FichierExcelDTO fichierExcelDTO = entityMapper.toFichierExcelDTO(fichierExcel);
        CustomLogger.logInfo("Entity -> DTO | FichierExcelDTO: " + fichierExcelDTO);

        CustomLogger.logInfo("Mapping from DTO to Entity========================================================");
        // --- Mapping: DTO -> Entity ---
        Personne mappedPersonne = entityMapper.toPersonne(personneDTO);
        CustomLogger.logInfo("DTO -> Entity | Mapped Personne: " + mappedPersonne);

        Enseignant mappedEnseignant = entityMapper.toEnseignant(enseignantDTO);
        CustomLogger.logInfo("DTO -> Entity | Mapped Enseignant: " + mappedEnseignant);
        mappedEnseignant.getSeances().forEach(seance1 -> CustomLogger.logInfo("examining datafetching " + seance1 + "\n"));
        Branche mappedBranche = entityMapper.toBranche(brancheDTO);
        CustomLogger.logInfo("DTO -> Entity | Mapped Branche: " + mappedBranche);

        Salle mappedSalle = entityMapper.toSalle(salleDTO);
        CustomLogger.logInfo("DTO -> Entity | Mapped Salle: " + mappedSalle);

        Seance mappedSeance = entityMapper.toSeance(seanceDTO);
        CustomLogger.logInfo("DTO -> Entity | Mapped Seance: " + mappedSeance);

        TD mappedTD = entityMapper.toTD(tdDTO);
        CustomLogger.logInfo("DTO -> Entity | Mapped TD: " + mappedTD);

        TP mappedTP = entityMapper.toTP(tpDTO);
        CustomLogger.logInfo("DTO -> Entity | Mapped TP: " + mappedTP);

        Signal mappedSignal = entityMapper.toSignal(signalDTO);
        CustomLogger.logInfo("DTO -> Entity | Mapped Signal: " + mappedSignal);

        PropositionDeRattrapage mappedProposition = entityMapper.toPropositionDeRattrapage(propositionDTO);
        CustomLogger.logInfo("DTO -> Entity | Mapped PropositionDeRattrapage: " + mappedProposition);

        Notification mappedNotification = entityMapper.toNotification(notificationDTO);
        CustomLogger.logInfo("DTO -> Entity | Mapped Notification: " + mappedNotification);

        FichierExcel mappedFichierExcel = entityMapper.toFichierExcel(fichierExcelDTO);
        CustomLogger.logInfo("DTO -> Entity | Mapped FichierExcel: " + mappedFichierExcel);

        CustomLogger.logInfo("=================== Finished Entity Mapping Test ===================");
    }

    public void testConflictMappings() throws CustomException {
        CustomLogger.logInfo("=========== testing conflict mapping ==================");
        // Fetch conflicting seance pairs
        List<Object[]> conflictPairs = seanceRepository.findConflictingSeancePairs(FrequenceType.BIWEEKLY,FrequenceType.CATCHUP);
        List<SeanceConflictDTO> seanceConflictDTOs = entityMapper.toSeanceConflictDTOList(conflictPairs);
        CustomLogger.logInfo("SeanceConflictDTOs: ------------------" );
        seanceConflictDTOs.forEach(seanceConflictDTO -> {CustomLogger.logInfo( seanceConflictDTO + "\n" );} );

        // Fetch room conflicts
        List<Object[]> roomConflicts = seanceRepository.findConflictingByRooms(FrequenceType.BIWEEKLY,FrequenceType.CATCHUP);
        List<SeanceRoomConflictDTO> seanceRoomConflictDTOs = entityMapper.toSeanceRoomConflictDTOList(roomConflicts);
        CustomLogger.logInfo("SeanceRoomConflictDTOs: --------------------" );
        seanceRoomConflictDTOs.forEach(seanceRoomConflictDTO -> {CustomLogger.logInfo( seanceRoomConflictDTO + "\n" );} );

        // Fetch conflicts for a specific seance
        Long seanceId = seanceRepository.findAll().getFirst().getId();
        List<Object[]> conflictsForSeance = seanceRepository.findRoomConflictsForSeanceById(seanceId,FrequenceType.BIWEEKLY,FrequenceType.CATCHUP);
        List<SingleSeanceConflictDTO> singleSeanceConflictDTOs = entityMapper.toSingleSeanceConflictDTOList(conflictsForSeance);
        CustomLogger.logInfo("SingleSeanceConflictDTOs: ------------" );
        singleSeanceConflictDTOs.forEach(seanceConflictDTO -> {CustomLogger.logInfo( seanceConflictDTO + "\n" );} );
        CustomLogger.logInfo("===================FINISHED ---------------------------------");
    }
}