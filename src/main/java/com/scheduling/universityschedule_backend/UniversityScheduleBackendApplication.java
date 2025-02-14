package com.scheduling.universityschedule_backend;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.service.*;
import com.scheduling.universityschedule_backend.util.CustomLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class UniversityScheduleBackendApplication implements CommandLineRunner {

	@Autowired
	private AdministrateurService administrateurService;

	@Autowired
	private BrancheService brancheService;

	@Autowired
	private EnseignantService enseignantService;

	@Autowired
	private EtudiantService etudiantService;

	@Autowired
	private ExcelFileService fichierExcelService;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private SalleService salleService;

	@Autowired
	private SeanceService seanceService;

	@Autowired
	private TDService tdService;

	@Autowired
	private TPService tpService;

	public static void main(String[] args) {
		SpringApplication.run(UniversityScheduleBackendApplication.class, args);
	}

	@Override
	public void run(String... args) {
		populateDatabase(3, 3, 7, 2, 2);
	}

	/**
	 * Populates the database with random data.
	 * Each call to this function will generate new random entities.
	 *
	 * @param numBranches        Number of branches to create
	 * @param numRooms           Number of rooms to create
	 * @param numSessions        Number of teaching sessions to create
	 * @param numMakeupRequests  Number of makeup session requests to create
	 * @param numNotifications   Number of notifications to broadcast
	 */
	public void populateDatabase(int numBranches, int numRooms, int numSessions,
								 int numMakeupRequests, int numNotifications) {
		Random random = new Random();

		// Lists to store created branches and rooms for later associations.
		List<BrancheDTO> branchList = new ArrayList<>();
		List<SalleDTO> roomList = new ArrayList<>();
		List<EnseignantDTO> teacherList = new ArrayList<>();
		List<EtudiantDTO> studentList = new ArrayList<>();
		List<TDDTO> tdList = new ArrayList<>();
		List<TPDTO> tpList = new ArrayList<>();

		try {
			// --- Create Random Branches ---
			CustomLogger.logInfo("Populating " + numBranches + " branches...");
			for (int i = 0; i < numBranches; i++) {
				BrancheDTO branche = new BrancheDTO();
				branche.setNiveau(random.nextBoolean() ? "Undergraduate" : "Graduate");
				branche.setSpecialite("Specialty-" + (random.nextInt(100) + 1));
				branche.setNbTD(random.nextInt(6) + 1); // between 1 and 6
				branche.setDepartement("Department-" + (random.nextInt(10) + 1));
				BrancheDTO createdBranche = brancheService.create(branche);
				branchList.add(createdBranche);
				CustomLogger.logInfo("Created Branche: " + createdBranche);
			}

			// --- Create Random Rooms ---
			CustomLogger.logInfo("Populating " + numRooms + " rooms...");
			String[] roomTypes = {"Lecture Hall", "Lab", "Seminar Room"};
			for (int i = 0; i < numRooms; i++) {
				SalleDTO salle = new SalleDTO();
				salle.setIdentifiant("Room-" + (random.nextInt(500) + 1));
				salle.setType(roomTypes[random.nextInt(roomTypes.length)]);
				salle.setCapacite(random.nextInt(150) + 50); // capacity between 50 and 200
				List<String> disponibilite = new ArrayList<>();
				disponibilite.add(String.format("%02d:00-%02d:00", 8 + random.nextInt(3), 10 + random.nextInt(3)));
				salle.setDisponibilite(disponibilite);
				SalleDTO createdSalle = salleService.create(salle);
				roomList.add(createdSalle);
				CustomLogger.logInfo("Created Room: " + createdSalle);
			}

			// --- Create Random Teachers ---
			CustomLogger.logInfo("Populating random teachers...");
			for (int i = 0; i < 5; i++) {
				EnseignantDTO enseignant = new EnseignantDTO();
				enseignant.setCodeEnseignant("TeacherCode-" + (random.nextInt(1000) + 1));
				enseignant.setHeures(random.nextInt(20) + 10);

				PersonneDTO personne = new PersonneDTO();
				personne.setCin("CIN-" + (random.nextInt(1000) + 1));
				personne.setNom("LastName-" + (random.nextInt(100) + 1));
				personne.setPrenom("FirstName-" + (random.nextInt(100) + 1));
				personne.setEmail("email" + (random.nextInt(1000) + 1) + "@example.com");
				personne.setTel("000-000-" + (random.nextInt(10000) + 1));
				personne.setAdresse("Address-" + (random.nextInt(100) + 1));

				enseignant.setId(personne.getId());
				enseignant.setCin(personne.getCin());
				enseignant.setNom(personne.getNom());
				enseignant.setPrenom(personne.getPrenom());
				enseignant.setEmail(personne.getEmail());
				enseignant.setTel(personne.getTel());
				enseignant.setAdresse(personne.getAdresse());

				teacherList.add(enseignant);
				CustomLogger.logInfo("Created Teacher: " + enseignant);
			}

			// --- Create Random Students ---
			CustomLogger.logInfo("Populating random students...");
			for (int i = 0; i < 10; i++) {
				EtudiantDTO etudiant = new EtudiantDTO();
				etudiant.setMatricule("StudentID-" + (random.nextInt(1000) + 1));

				PersonneDTO personne = new PersonneDTO();
				personne.setCin("CIN-" + (random.nextInt(1000) + 1));
				personne.setNom("LastName-" + (random.nextInt(100) + 1));
				personne.setPrenom("FirstName-" + (random.nextInt(100) + 1));
				personne.setEmail("email" + (random.nextInt(1000) + 1) + "@example.com");
				personne.setTel("000-000-" + (random.nextInt(10000) + 1));
				personne.setAdresse("Address-" + (random.nextInt(100) + 1));

				etudiant.setId(personne.getId());
				etudiant.setCin(personne.getCin());
				etudiant.setNom(personne.getNom());
				etudiant.setPrenom(personne.getPrenom());
				etudiant.setEmail(personne.getEmail());
				etudiant.setTel(personne.getTel());
				etudiant.setAdresse(personne.getAdresse());

				etudiant.setBrancheId(branchList.get(random.nextInt(branchList.size())).getId());
				etudiant.setTpId(null); // Will be set later

				studentList.add(etudiant);
				CustomLogger.logInfo("Created Student: " + etudiant);
			}

			// --- Create Random TDs and TPs ---
			CustomLogger.logInfo("Populating random TDs and TPs...");
			for (BrancheDTO branche : branchList) {
				TDDTO td = new TDDTO();
				td.setNb(random.nextInt(10) + 1);
				td.setNbTP(random.nextInt(5) + 1);
				td.setBrancheId(branche.getId());

				List<Long> tpIds = new ArrayList<>();
				for (int i = 0; i < td.getNbTP(); i++) {
					TPDTO tp = new TPDTO();
					tp.setNb(random.nextInt(10) + 1);
					tp.setTdId(td.getId());

					List<Long> studentIds = new ArrayList<>();
					for (EtudiantDTO student : studentList) {
						if (student.getBrancheId().equals(branche.getId())) {
							studentIds.add(student.getId());
							student.setTpId(tp.getId());
						}
					}
					tp.setEtudiantIds(studentIds);

					TPDTO createdTP = tpService.create(tp);
					tpIds.add(createdTP.getId());
					tpList.add(createdTP);
					CustomLogger.logInfo("Created TP: " + createdTP);
				}

				td.setTpIds(tpIds);
				TDDTO createdTD = tdService.create(td);
				tdList.add(createdTD);
				CustomLogger.logInfo("Created TD: " + createdTD);
			}

			// --- Create Random Sessions ---
			CustomLogger.logInfo("Populating " + numSessions + " sessions...");
			String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
			String[] subjects = {"Mathematics", "Physics", "Chemistry", "Biology", "Computer Science"};
			String[] sessionTypes = {"CR", "TD", "TP"};
			for (int i = 0; i < numSessions; i++) {
				SeanceDTO seance = new SeanceDTO();
				seance.setJour(days[random.nextInt(days.length)]);
				int startHour = 8 + random.nextInt(5); // between 8 and 12
				seance.setHeureDebut(String.format("%02d:00", startHour));
				seance.setHeureFin(String.format("%02d:00", startHour + 1 + random.nextInt(2))); // duration 1-2 hours
				seance.setType(sessionTypes[random.nextInt(sessionTypes.length)]);
				seance.setMatiere(subjects[random.nextInt(subjects.length)]);
				seance.setFrequence(random.nextBoolean() ? "weekly" : "biweekly");

				if (!roomList.isEmpty()) {
					seance.setSalleId(roomList.get(random.nextInt(roomList.size())).getId());
				}

				if (!teacherList.isEmpty()) {
					seance.setEnseignantId(teacherList.get(random.nextInt(teacherList.size())).getId());
				}

				if (!branchList.isEmpty()) {
					List<Long> brancheIds = new ArrayList<>();
					brancheIds.add(branchList.get(random.nextInt(branchList.size())).getId());
					seance.setBrancheIds(brancheIds);
				}

				if (!tdList.isEmpty()) {
					List<Long> tdIds = new ArrayList<>();
					tdIds.add(tdList.get(random.nextInt(tdList.size())).getId());
					seance.setTdIds(tdIds);
				}

				if (!tpList.isEmpty()) {
					List<Long> tpIds = new ArrayList<>();
					tpIds.add(tpList.get(random.nextInt(tpList.size())).getId());
					seance.setTpIds(tpIds);
				}

				SeanceDTO createdSeance = seanceService.create(seance);
				CustomLogger.logInfo("Created Session: " + createdSeance);
			}

			// --- Create Random Makeup Session Requests ---
			CustomLogger.logInfo("Populating " + numMakeupRequests + " makeup session requests...");
			for (int i = 0; i < numMakeupRequests; i++) {
				PropositionDeRattrapageDTO makeupRequest = new PropositionDeRattrapageDTO();
				makeupRequest.setDate(LocalDateTime.now().plusDays(random.nextInt(10)));
				makeupRequest.setReason("Makeup for " + subjects[random.nextInt(subjects.length)]);
				makeupRequest.setStatus("Pending");

				if (!teacherList.isEmpty()) {
					makeupRequest.setEnseignantId(teacherList.get(random.nextInt(teacherList.size())).getId());
				}

				PropositionDeRattrapageDTO createdRequest = enseignantService.submitMakeupRequest(makeupRequest.getEnseignantId(), makeupRequest);
				CustomLogger.logInfo("Created Makeup Request: " + createdRequest);
			}

			// --- Simulate an Excel File Upload ---
			CustomLogger.logInfo("Simulating Excel file upload...");
			FichierExcelDTO fichierExcel = new FichierExcelDTO();
			fichierExcel.setFileName("random_schedule_" + random.nextInt(1000) + ".xlsx");
			fichierExcel.setStatus("imported");
			fichierExcel.setImportDate(LocalDateTime.now());
			fichierExcelService.upload(fichierExcel);
			CustomLogger.logInfo("Uploaded Excel file: " + fichierExcel);

			// --- Broadcast Random Notifications ---
			CustomLogger.logInfo("Broadcasting " + numNotifications + " notifications...");
			for (int i = 0; i < numNotifications; i++) {
				NotificationDTO notif = new NotificationDTO();
				notif.setMessage("System populated at " + LocalDateTime.now());
				notif.setDate(LocalDateTime.now());
				notif.setType("info");
				notif.setRead(false);
				notificationService.broadcastNotification(notif);
				CustomLogger.logInfo("Broadcasted Notification: " + notif);
			}

		} catch (CustomException ce) {
			CustomLogger.logError("Error while populating database: " + ce.getMessage(), ce);
		} catch (Exception e) {
			CustomLogger.logError("Unexpected error while populating database", e);
		}
	}
}