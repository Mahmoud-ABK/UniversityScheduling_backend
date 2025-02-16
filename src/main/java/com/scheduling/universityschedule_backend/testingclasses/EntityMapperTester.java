package com.scheduling.universityschedule_backend.testingclasses;



import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.*;
import com.scheduling.universityschedule_backend.repository.*;
import com.scheduling.universityschedule_backend.util.CustomLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
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
        // Fetch entities from the database
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

        // Test Personne mapping
        PersonneDTO personneDTO = entityMapper.toPersonneDTO(personne);
        CustomLogger.logInfo("PersonneDTO: " + personneDTO);

        // Test Enseignant mapping
        EnseignantDTO enseignantDTO = entityMapper.toEnseignantDTO(enseignant);
        CustomLogger.logInfo("EnseignantDTO: " + enseignantDTO);

        // Test Branche mapping
        BrancheDTO brancheDTO = entityMapper.toBrancheDTO(branche);
        CustomLogger.logInfo("BrancheDTO: " + brancheDTO);

        // Test Salle mapping
        SalleDTO salleDTO = entityMapper.toSalleDTO(salle);
        CustomLogger.logInfo("SalleDTO: " + salleDTO);

        // Test Seance mapping
        SeanceDTO seanceDTO = entityMapper.toSeanceDTO(seance);
        CustomLogger.logInfo("SeanceDTO: " + seanceDTO);

        // Test TD mapping
        TDDTO tdDTO = entityMapper.toTDDTO(td);
        CustomLogger.logInfo("TDDTO: " + tdDTO);

        // Test TP mapping
        TPDTO tpDTO = entityMapper.toTPDTO(tp);
        CustomLogger.logInfo("TPDTO: " + tpDTO);

        // Test Signal mapping
        SignalDTO signalDTO = entityMapper.toSignalDTO(signal);
        CustomLogger.logInfo("SignalDTO: " + signalDTO);

        // Test PropositionDeRattrapage mapping
        PropositionDeRattrapageDTO propositionDTO = entityMapper.toPropositionDeRattrapageDTO(proposition);
        CustomLogger.logInfo("PropositionDeRattrapageDTO: " + propositionDTO);

        // Test Notification mapping
        NotificationDTO notificationDTO = entityMapper.toNotificationDTO(notification);
        CustomLogger.logInfo("NotificationDTO: " + notificationDTO);

        // Test FichierExcel mapping
        FichierExcelDTO fichierExcelDTO = entityMapper.toFichierExcelDTO(fichierExcel);
        CustomLogger.logInfo("FichierExcelDTO: " + fichierExcelDTO);

        // Test reverse mappings
        Personne mappedPersonne = entityMapper.toPersonne(personneDTO);
        CustomLogger.logInfo("Mapped Personne: " + mappedPersonne);

        Enseignant mappedEnseignant = entityMapper.toEnseignant(enseignantDTO);
        CustomLogger.logInfo("Mapped Enseignant: " + mappedEnseignant);

        Branche mappedBranche = entityMapper.toBranche(brancheDTO);
        CustomLogger.logInfo("Mapped Branche: " + mappedBranche);

        Salle mappedSalle = entityMapper.toSalle(salleDTO);
        CustomLogger.logInfo("Mapped Salle: " + mappedSalle);

        Seance mappedSeance = entityMapper.toSeance(seanceDTO);
        CustomLogger.logInfo("Mapped Seance: " + mappedSeance);

        TD mappedTD = entityMapper.toTD(tdDTO);
        CustomLogger.logInfo("Mapped TD: " + mappedTD);

        TP mappedTP = entityMapper.toTP(tpDTO);
        CustomLogger.logInfo("Mapped TP: " + mappedTP);

        Signal mappedSignal = entityMapper.toSignal(signalDTO);
        CustomLogger.logInfo("Mapped Signal: " + mappedSignal);

        PropositionDeRattrapage mappedProposition = entityMapper.toPropositionDeRattrapage(propositionDTO);
        CustomLogger.logInfo("Mapped PropositionDeRattrapage: " + mappedProposition);

        Notification mappedNotification = entityMapper.toNotification(notificationDTO);
        CustomLogger.logInfo("Mapped Notification: " + mappedNotification);

        FichierExcel mappedFichierExcel = entityMapper.toFichierExcel(fichierExcelDTO);
        CustomLogger.logInfo("Mapped FichierExcel: " + mappedFichierExcel);
    }
    public void testConflictMappings() throws CustomException {
        // Fetch conflicting seance pairs
        List<Object[]> conflictPairs = seanceRepository.findConflictingSeancePairs();
        List<SeanceConflictDTO> seanceConflictDTOs = entityMapper.toSeanceConflictDTOList(conflictPairs);
        CustomLogger.logInfo("SeanceConflictDTOs: " + seanceConflictDTOs);

        // Fetch room conflicts
        List<Object[]> roomConflicts = seanceRepository.findConflictingByRooms();
        List<SeanceRoomConflictDTO> seanceRoomConflictDTOs = entityMapper.toSeanceRoomConflictDTOList(roomConflicts);
        CustomLogger.logInfo("SeanceRoomConflictDTOs: " + seanceRoomConflictDTOs);

        // Fetch conflicts for a specific seance
        Long seanceId = seanceRepository.findAll().getFirst().getId();
        List<Object[]> conflictsForSeance = seanceRepository.findRoomConflictsForSeance(seanceId);
        List<SingleSeanceConflictDTO> singleSeanceConflictDTOs = entityMapper.toSingleSeanceConflictDTOList(conflictsForSeance);
        CustomLogger.logInfo("SingleSeanceConflictDTOs: " + singleSeanceConflictDTOs);
    }
}