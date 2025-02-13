package com.scheduling.universityschedule_backend;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.service.*;
import com.scheduling.universityschedule_backend.util.CustomLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class UniversityScheduleBackendApplication implements CommandLineRunner {

	// Autowire the service layer beans
	@Autowired
	private AdministrateurService administrateurService;

	@Autowired
	private BrancheService brancheService;

	@Autowired
	private EnseignantService enseignantService;

	@Autowired
	private EtudiantService etudiantService;

	@Autowired
	private FichierExcelService fichierExcelService;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private PropositionDeRattrapageService propositionDeRattrapageService;

	@Autowired
	private SalleService salleService;

	@Autowired
	private SeanceService seanceService;

	@Autowired
	private SignalService signalService;

	@Autowired
	private TDService tdService;

	@Autowired
	private TechnicienService technicienService;

	@Autowired
	private TPService tpService;

	public static void main(String[] args) {
		SpringApplication.run(UniversityScheduleBackendApplication.class, args);
	}

	@Override
	public void run(String... args) {
		try {
			CustomLogger.logInfo("===== Testing FichierExcelService =====");
			FichierExcelDTO fichierExcelDTO = new FichierExcelDTO();
			fichierExcelDTO.setFileName("schedule.xlsx");
			fichierExcelDTO.setStatus("imported");
			fichierExcelDTO.setImportDate(LocalDateTime.now());
			FichierExcelDTO importedFile = fichierExcelService.importerFichierExcel(fichierExcelDTO);
			CustomLogger.logInfo("Imported FichierExcel: " + importedFile);

			CustomLogger.logInfo("===== Testing AdministrateurService =====");
			List<SeanceDTO> seances = administrateurService.genererEmploiDuTemps();
			CustomLogger.logInfo("Generated Emploi du Temps: " + seances);
			try {
				PropositionDeRattrapageDTO processedProposal = administrateurService.traiterDemandeRattrapage(1L, true);
				CustomLogger.logInfo("Processed Proposition de Rattrapage: " + processedProposal);
			} catch (CustomException ce) {
				CustomLogger.logInfo("No proposition found with ID 1: " + ce.getMessage());
			}
			NotificationDTO notifDTO = new NotificationDTO();
			notifDTO.setMessage("Schedule updated");
			notifDTO.setDate(LocalDateTime.now());
			notifDTO.setType("update");
			notifDTO.setRead(false);
			List<NotificationDTO> diffusedNotifs = administrateurService.diffuserNotification(notifDTO);
			CustomLogger.logInfo("Diffused Notification: " + diffusedNotifs);

			CustomLogger.logInfo("===== Testing BrancheService =====");
			BrancheDTO brancheDTO = new BrancheDTO();
			brancheDTO.setNiveau("Undergraduate");
			brancheDTO.setSpecialite("Computer Science");
			brancheDTO.setNbTD(4);
			brancheDTO.setDepartement("Engineering");
			BrancheDTO createdBranche = brancheService.createBranche(brancheDTO);
			CustomLogger.logInfo("Created Branche: " + createdBranche);
			BrancheDTO fetchedBranche = brancheService.getBrancheById(createdBranche.getId());
			CustomLogger.logInfo("Fetched Branche: " + fetchedBranche);
			brancheDTO.setSpecialite("Software Engineering");
			BrancheDTO updatedBranche = brancheService.updateBranche(createdBranche.getId(), brancheDTO);
			CustomLogger.logInfo("Updated Branche: " + updatedBranche);
			brancheService.deleteBranche(createdBranche.getId());
			CustomLogger.logInfo("Deleted Branche with ID: " + createdBranche.getId());

			CustomLogger.logInfo("===== Testing EnseignantService =====");
			try {
				List<SeanceDTO> teacherSeances = enseignantService.getEmploiDuTemps(1L);
				CustomLogger.logInfo("Teacher's Emploi du Temps: " + teacherSeances);
			} catch (CustomException ce) {
				CustomLogger.logInfo("Enseignant with ID 1 not found: " + ce.getMessage());
			}
			try {
				int heures = enseignantService.getHeuresEnseignees(1L);
				CustomLogger.logInfo("Heures Enseignees: " + heures);
			} catch (CustomException ce) {
				CustomLogger.logInfo("Enseignant with ID 1 not found: " + ce.getMessage());
			}
			PropositionDeRattrapageDTO demandeDTO = new PropositionDeRattrapageDTO();
			demandeDTO.setReason("Class cancelled");
			try {
				PropositionDeRattrapageDTO propositionResult = enseignantService.soumettreDemandeRattrapage(1L, demandeDTO);
				CustomLogger.logInfo("Submitted Demande de Rattrapage: " + propositionResult);
			} catch (CustomException ce) {
				CustomLogger.logInfo("Failed to submit demande de rattrapage: " + ce.getMessage());
			}
			SignalDTO signalDTO = new SignalDTO();
			signalDTO.setMessage("Need more time between sessions");
			signalDTO.setSeverity("medium");
			try {
				SignalDTO submittedSignal = enseignantService.soumettreSuggestion(1L, signalDTO);
				CustomLogger.logInfo("Submitted Signal: " + submittedSignal);
			} catch (CustomException ce) {
				CustomLogger.logInfo("Failed to submit signal: " + ce.getMessage());
			}
			try {
				List<SignalDTO> signals = enseignantService.getSignalisations(1L);
				CustomLogger.logInfo("Retrieved Signalisations: " + signals);
			} catch (CustomException ce) {
				CustomLogger.logInfo("Failed to retrieve signalisations: " + ce.getMessage());
			}

			CustomLogger.logInfo("===== Testing EtudiantService =====");
			try {
				List<SeanceDTO> etudiantSeances = etudiantService.getEmploiDuTempsPersonnel(1L);
				CustomLogger.logInfo("Etudiant's Emploi du Temps Personnel: " + etudiantSeances);
			} catch (CustomException ce) {
				CustomLogger.logInfo("Etudiant with ID 1 not found: " + ce.getMessage());
			}
			try {
				List<NotificationDTO> etudiantNotifs = etudiantService.getNotifications(1L);
				CustomLogger.logInfo("Etudiant Notifications: " + etudiantNotifs);
			} catch (CustomException ce) {
				CustomLogger.logInfo("Failed to get notifications for Etudiant with ID 1: " + ce.getMessage());
			}

			CustomLogger.logInfo("===== Testing NotificationService =====");
			try {
				List<NotificationDTO> userNotifs = notificationService.getNotificationsForUser(1L);
				CustomLogger.logInfo("User Notifications: " + userNotifs);
				if (!userNotifs.isEmpty()) {
					notificationService.markAsRead(userNotifs.get(0).getId());
					CustomLogger.logInfo("Marked notification as read for ID: " + userNotifs.get(0).getId());
				}
			} catch (CustomException ce) {
				CustomLogger.logInfo("Failed to process notifications: " + ce.getMessage());
			}

			CustomLogger.logInfo("===== Testing PropositionDeRattrapageService =====");
			PropositionDeRattrapageDTO proposalDTO = new PropositionDeRattrapageDTO();
			proposalDTO.setReason("Extra session needed");
			try {
				PropositionDeRattrapageDTO submittedProposal = propositionDeRattrapageService.submitProposal(proposalDTO);
				CustomLogger.logInfo("Submitted Proposal: " + submittedProposal);
				PropositionDeRattrapageDTO processedProposal = propositionDeRattrapageService.approveOrRejectProposal(submittedProposal.getId(), true);
				CustomLogger.logInfo("Processed Proposal: " + processedProposal);
			} catch (CustomException ce) {
				CustomLogger.logInfo("Failed to process proposal: " + ce.getMessage());
			}

			CustomLogger.logInfo("===== Testing SalleService =====");
			SalleDTO salleDTO = new SalleDTO();
			salleDTO.setIdentifiant("A101");
			salleDTO.setType("Lecture Hall");
			salleDTO.setCapacite(100);
			salleDTO.setDisponibilite(new ArrayList<>(Arrays.asList("08:00-10:00", "10:00-12:00")));
			try {
				SalleDTO createdSalle = salleService.createSalle(salleDTO);
				CustomLogger.logInfo("Created Salle: " + createdSalle);
				SalleDTO fetchedSalle = salleService.getSalleById(createdSalle.getId());
				CustomLogger.logInfo("Fetched Salle: " + fetchedSalle);
				salleDTO.setCapacite(120);
				SalleDTO updatedSalle = salleService.updateSalle(createdSalle.getId(), salleDTO);
				CustomLogger.logInfo("Updated Salle: " + updatedSalle);
				salleService.deleteSalle(createdSalle.getId());
				CustomLogger.logInfo("Deleted Salle with ID: " + createdSalle.getId());
			} catch (CustomException ce) {
				CustomLogger.logInfo("Salle Service test failed: " + ce.getMessage());
			}

			CustomLogger.logInfo("===== Testing SeanceService =====");
			SeanceDTO seanceDTO = new SeanceDTO();
			seanceDTO.setJour("Monday");
			seanceDTO.setHeureDebut("09:00");
			seanceDTO.setHeureFin("10:30");
			seanceDTO.setType("CR");
			seanceDTO.setMatiere("Mathematics");
			seanceDTO.setFrequence("weekly");
			try {
				SeanceDTO createdSeance = seanceService.createSeance(seanceDTO);
				CustomLogger.logInfo("Created Seance: " + createdSeance);
				SeanceDTO fetchedSeance = seanceService.getSeanceById(createdSeance.getId());
				CustomLogger.logInfo("Fetched Seance: " + fetchedSeance);
				seanceDTO.setMatiere("Advanced Mathematics");
				SeanceDTO updatedSeance = seanceService.updateSeance(createdSeance.getId(), seanceDTO);
				CustomLogger.logInfo("Updated Seance: " + updatedSeance);
				List<SeanceConflictDTO> conflicts = seanceService.detectConflicts(createdSeance.getId());
				CustomLogger.logInfo("Detected Conflicts: " + conflicts);
				seanceService.deleteSeance(createdSeance.getId());
				CustomLogger.logInfo("Deleted Seance with ID: " + createdSeance.getId());
			} catch (CustomException ce) {
				CustomLogger.logInfo("Seance Service test failed: " + ce.getMessage());
			}

			CustomLogger.logInfo("===== Testing SignalService =====");
			SignalDTO sigDTO = new SignalDTO();
			sigDTO.setMessage("Test signal");
			sigDTO.setSeverity("high");
			try {
				SignalDTO submittedSignal = signalService.submitSignal(sigDTO);
				CustomLogger.logInfo("Submitted Signal: " + submittedSignal);
				List<SignalDTO> allSignals = signalService.getAllSignals();
				CustomLogger.logInfo("All Signals: " + allSignals);
			} catch (CustomException ce) {
				CustomLogger.logInfo("Signal Service test failed: " + ce.getMessage());
			}

			CustomLogger.logInfo("===== Testing TDService =====");
			TDDTO tdDTO = new TDDTO();
			tdDTO.setNb(2);
			tdDTO.setNbTP(3);
			try {
				TDDTO createdTD = tdService.createTD(tdDTO);
				CustomLogger.logInfo("Created TD: " + createdTD);
				TDDTO fetchedTD = tdService.getTDById(createdTD.getId());
				CustomLogger.logInfo("Fetched TD: " + fetchedTD);
				tdDTO.setNb(3);
				TDDTO updatedTD = tdService.updateTD(createdTD.getId(), tdDTO);
				CustomLogger.logInfo("Updated TD: " + updatedTD);
				tdService.deleteTD(createdTD.getId());
				CustomLogger.logInfo("Deleted TD with ID: " + createdTD.getId());
			} catch (CustomException ce) {
				CustomLogger.logInfo("TD Service test failed: " + ce.getMessage());
			}

			CustomLogger.logInfo("===== Testing TechnicienService =====");
			TechnicienDTO techDTO = new TechnicienDTO();
			techDTO.setCin("T123456");
			techDTO.setNom("Doe");
			techDTO.setPrenom("John");
			techDTO.setEmail("john.doe@example.com");
			techDTO.setTel("1234567890");
			techDTO.setAdresse("123 Tech Street");
			techDTO.setCodeTechnicien("TECH001");
			try {
				TechnicienDTO createdTech = technicienService.createTechnicien(techDTO);
				CustomLogger.logInfo("Created Technicien: " + createdTech);
				TechnicienDTO fetchedTech = technicienService.getTechnicienById(createdTech.getId());
				CustomLogger.logInfo("Fetched Technicien: " + fetchedTech);
				techDTO.setNom("DoeUpdated");
				TechnicienDTO updatedTech = technicienService.updateTechnicien(createdTech.getId(), techDTO);
				CustomLogger.logInfo("Updated Technicien: " + updatedTech);
				technicienService.deleteTechnicien(createdTech.getId());
				CustomLogger.logInfo("Deleted Technicien with ID: " + createdTech.getId());
			} catch (CustomException ce) {
				CustomLogger.logInfo("Technicien Service test failed: " + ce.getMessage());
			}

			CustomLogger.logInfo("===== Testing TPService =====");
			TPDTO tpDTO = new TPDTO();
			tpDTO.setNb(1);
			try {
				TPDTO createdTP = tpService.createTP(tpDTO);
				CustomLogger.logInfo("Created TP: " + createdTP);
				TPDTO fetchedTP = tpService.getTPById(createdTP.getId());
				CustomLogger.logInfo("Fetched TP: " + fetchedTP);
				tpDTO.setNb(2);
				TPDTO updatedTP = tpService.updateTP(createdTP.getId(), tpDTO);
				CustomLogger.logInfo("Updated TP: " + updatedTP);
				tpService.deleteTP(createdTP.getId());
				CustomLogger.logInfo("Deleted TP with ID: " + createdTP.getId());
			} catch (CustomException ce) {
				CustomLogger.logInfo("TP Service test failed: " + ce.getMessage());
			}
		} catch (Exception e) {
			CustomLogger.logError("Exception during service layer testing", e);
		}
	}
}
