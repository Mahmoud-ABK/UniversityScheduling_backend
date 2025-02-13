package com.scheduling.universityschedule_backend;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.service.AdministrateurService;
import com.scheduling.universityschedule_backend.service.BrancheService;
import com.scheduling.universityschedule_backend.service.EnseignantService;
import com.scheduling.universityschedule_backend.service.EtudiantService;
import com.scheduling.universityschedule_backend.service.ExcelFileService;
import com.scheduling.universityschedule_backend.service.NotificationService;
import com.scheduling.universityschedule_backend.service.SalleService;
import com.scheduling.universityschedule_backend.service.SeanceService;
import com.scheduling.universityschedule_backend.service.TDService;
import com.scheduling.universityschedule_backend.service.TPService;
import com.scheduling.universityschedule_backend.util.CustomLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
populateDatabase();
			}
	/**
	 * Populates the database with random data.
	 * Each call to this function will generate new random entities.
	 *
	 * @param numBranches        Number of branches to create
	 * @param numTeachers        Number of teachers to create
	 * @param numStudents        Number of students to create
	 * @param numRooms           Number of rooms to create
	 * @param numSessions        Number of teaching sessions to create
	 * @param numMakeupRequests  Number of makeup session requests to create
	 * @param numNotifications   Number of notifications to broadcast
	 */
	public void populateDatabase(int numBranches, int numTeachers, int numStudents,
								 int numRooms, int numSessions, int numMakeupRequests, int numNotifications) {
		Random random = new Random();

		// Lists to store created entities for associations.
		List<BrancheDTO> branchList = new ArrayList<>();
		List<EnseignantDTO> teacherList = new ArrayList<>();
		List<PersonneDTO> studentList = new ArrayList<>();
		List<SalleDTO> roomList = new ArrayList<>();

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

			// --- Create Random Teachers using UserService ---
			CustomLogger.logInfo("Populating " + numTeachers + " teachers...");
			for (int i = 0; i < numTeachers; i++) {
				PersonneDTO teacher = new PersonneDTO();
				teacher.setNom("Teacher" + (i + 1));
				teacher.setPrenom("LastName" + (i + 1));
				teacher.setEmail("teacher" + i + "@university.com");
				teacher.setTel("+216900000" + i);
				teacher.setAdresse("City-" + (random.nextInt(10) + 1));
				// Create teacher via UserService; assume it returns an EnseignantDTO
				PersonneDTO createdTeacher = userService.create(teacher);
				// Cast to EnseignantDTO (in a real app, ensure that the user is flagged as a teacher)
				teacherList.add((EnseignantDTO) createdTeacher);
				CustomLogger.logInfo("Created Teacher: " + createdTeacher);
			}

			// --- Create Random Students using UserService ---
			CustomLogger.logInfo("Populating " + numStudents + " students...");
			for (int i = 0; i < numStudents; i++) {
				PersonneDTO student = new PersonneDTO();
				student.setNom("Student" + (i + 1));
				student.setPrenom("LastName" + (i + 1));
				student.setEmail("student" + i + "@university.com");
				student.setTel("+216800000" + i);
				student.setAdresse("Dormitory-" + (i + 1));
				// Assign a random branch to student if available
				if (!branchList.isEmpty()) {
					// (Assuming EtudiantDTO has a field 'branche' of type BrancheDTO)
					// Here we simply add the branch info into the generic user.
					// In a full implementation, you'd convert to an EtudiantDTO.
					// For testing purposes, we use PersonneDTO.
					// You might need to adjust this if you have a dedicated method.
				}
				PersonneDTO createdStudent = userService.create(student);
				studentList.add(createdStudent);
				CustomLogger.logInfo("Created Student: " + createdStudent);
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

			// --- Create Random Sessions ---
			CustomLogger.logInfo("Populating " + numSessions + " sessions...");
			String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
			String[] subjects = {"Mathematics", "Physics", "Chemistry", "Biology", "Computer Science"};
			String[] sessionTypes = {"CR", "TD", "TP"};
			for (int i = 0; i < numSessions; i++) {
				SeanceDTO seance = new SeanceDTO();
				seance.setJour(days[random.nextInt(days.length)]);
				int startHour = 8 + random.nextInt(5); // start between 8 and 12
				seance.setHeureDebut(String.format("%02d:00", startHour));
				seance.setHeureFin(String.format("%02d:00", startHour + 1 + random.nextInt(2))); // duration 1 to 2 hours
				seance.setType(sessionTypes[random.nextInt(sessionTypes.length)]);
				seance.setMatiere(subjects[random.nextInt(subjects.length)]);
				seance.setFrequence(random.nextBoolean() ? "weekly" : "biweekly");
				// Assign a random room and teacher if available
				if (!roomList.isEmpty()) {
					seance.setSalle(roomList.get(random.nextInt(roomList.size())));
				}
				if (!teacherList.isEmpty()) {
					seance.setEnseignant(teacherList.get(random.nextInt(teacherList.size())));
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
				// Pick a random teacher to submit the request
				if (!teacherList.isEmpty()) {
					// Using enseignantService.submitMakeupRequest to create a makeup session
					PropositionDeRattrapageDTO createdRequest = enseignantService.submitMakeupRequest(
							Long.valueOf(teacherList.get(random.nextInt(teacherList.size())).getId()), makeupRequest);
					CustomLogger.logInfo("Created Makeup Request: " + createdRequest);
				}
			}

			// --- Simulate an Excel File Upload ---
			CustomLogger.logInfo("Simulating Excel file upload...");
			FichierExcelDTO fichierExcel = new FichierExcelDTO();
			fichierExcel.setFileName("random_schedule_" + random.nextInt(1000) + ".xlsx");
			fichierExcel.setStatus("imported");
			fichierExcel.setImportDate(LocalDateTime.now());
			// Note: Using AdministrateurService.importExcelSchedule is defined in the interface,
			// but here we use ExcelFileService.upload as our method.
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
			// Clear exception handling: log the error and rethrow if necessary.
			CustomLogger.logError("-=======================ERROR=======");
			CustomLogger.logError("Error while populating database: \n " + ce.getMessage(), ce);
		} catch (Exception e) {
			CustomLogger.logError("Unexpected error while populating database", e);
		}
	}

}
