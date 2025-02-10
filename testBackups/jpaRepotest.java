package com.scheduling.universityschedule_backend;

import com.scheduling.universityschedule_backend.model.*;
import com.scheduling.universityschedule_backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class UniversityScheduleBackendApplication implements CommandLineRunner {

	@Autowired
	private AdministrateurRepository administrateurRepository;

	@Autowired
	private BrancheRepository brancheRepository;

	@Autowired
	private EnseignantRepository enseignantRepository;

	@Autowired
	private EtudiantRepository etudiantRepository;

	@Autowired
	private FichierExcelRepository fichierExcelRepository;

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private SalleRepository salleRepository;

	@Autowired
	private SeanceRepository seanceRepository;

	@Autowired
	private SignalRepository signalRepository;

	@Autowired
	private TDRepository tdRepository;

	@Autowired
	private TechnicienRepository technicienRepository;

	@Autowired
	private TPRepository tpRepository;

	public static void main(String[] args) throws FileNotFoundException {

		SpringApplication.run(UniversityScheduleBackendApplication.class, args);
	}

	// Populate the database using no-argument constructors and setters
	public void populateDatabase() {
		// Create Branches
		Branche branche1 = new Branche();
		branche1.setNiveau("Informatique");
		branche1.setSpecialite("Licence");
		branche1.setNbTD(3);
		branche1.setDepartement("Informatique");
		branche1.setSeances(new ArrayList<>());

		Branche branche2 = new Branche();
		branche2.setNiveau("Mathematiques");
		branche2.setSpecialite("Master");
		branche2.setNbTD(2);
		branche2.setDepartement("Mathematiques");
		branche2.setSeances(new ArrayList<>());

		Branche branche3 = new Branche();
		branche3.setNiveau("Physique");
		branche3.setSpecialite("Master");
		branche3.setNbTD(1);
		branche3.setDepartement("Physique");
		branche3.setSeances(new ArrayList<>());

		Branche branche4 = new Branche();
		branche4.setNiveau("Chimie");
		branche4.setSpecialite("Licence");
		branche4.setNbTD(2);
		branche4.setDepartement("Chimie");
		branche4.setSeances(new ArrayList<>());

		Branche branche5 = new Branche();
		branche5.setNiveau("Biologie");
		branche5.setSpecialite("Licence");
		branche5.setNbTD(2);
		branche5.setDepartement("Biologie");
		branche5.setSeances(new ArrayList<>());

		brancheRepository.saveAll(Arrays.asList(branche1, branche2, branche3, branche4, branche5));

		// Create Administrators
		Administrateur admin1 = new Administrateur();
		admin1.setCin("CIN_ADMIN_001");
		admin1.setNom("John");
		admin1.setPrenom("Doe");
		admin1.setEmail("john.doe@university.com");
		admin1.setTel("1234567890");
		admin1.setAdresse("Admin Street");
		admin1.setCodeAdmin("admin123");

		Administrateur admin2 = new Administrateur();
		admin2.setCin("CIN_ADMIN_002");
		admin2.setNom("Jane");
		admin2.setPrenom("Doe");
		admin2.setEmail("jane.doe@university.com");
		admin2.setTel("0987654321");
		admin2.setAdresse("Admin Lane");
		admin2.setCodeAdmin("admin124");

		administrateurRepository.saveAll(Arrays.asList(admin1, admin2));

		// Create Technicians
		Technicien tech1 = new Technicien();
		tech1.setCin("CIN_TECH_001");
		tech1.setNom("Sarah");
		tech1.setPrenom("Connor");
		tech1.setEmail("sarah.connor@university.com");
		tech1.setTel("0987654321");
		tech1.setAdresse("Tech Street");
		tech1.setCodeTechnicien("tech123");

		Technicien tech2 = new Technicien();
		tech2.setCin("CIN_TECH_002");
		tech2.setNom("Kyle");
		tech2.setPrenom("Reese");
		tech2.setEmail("kyle.reese@university.com");
		tech2.setTel("1234567890");
		tech2.setAdresse("Tech Avenue");
		tech2.setCodeTechnicien("tech124");

		technicienRepository.saveAll(Arrays.asList(tech1, tech2));

		// Create Teachers (Enseignant)
		Enseignant enseignant1 = new Enseignant();
		enseignant1.setCin("CIN_ENS_001");
		enseignant1.setNom("Alice");
		enseignant1.setPrenom("Smith");
		enseignant1.setEmail("alice.smith@university.com");
		enseignant1.setTel("0987654321");
		enseignant1.setAdresse("Teacher Street");
		enseignant1.setCodeEnseignant("ens123");
		enseignant1.setHeures(12);
		enseignant1.setSeances(new ArrayList<>());
		enseignant1.setPropositionsDeRattrapage(new ArrayList<>());

		Enseignant enseignant2 = new Enseignant();
		enseignant2.setCin("CIN_ENS_002");
		enseignant2.setNom("Bob");
		enseignant2.setPrenom("Brown");
		enseignant2.setEmail("bob.brown@university.com");
		enseignant2.setTel("1234567890");
		enseignant2.setAdresse("Teacher Lane");
		enseignant2.setCodeEnseignant("ens124");
		enseignant2.setHeures(15);
		enseignant2.setSeances(new ArrayList<>());
		enseignant2.setPropositionsDeRattrapage(new ArrayList<>());

		Enseignant enseignant3 = new Enseignant();
		enseignant3.setCin("CIN_ENS_003");
		enseignant3.setNom("Catherine");
		enseignant3.setPrenom("Johnson");
		enseignant3.setEmail("catherine.johnson@university.com");
		enseignant3.setTel("2233445566");
		enseignant3.setAdresse("Teacher Boulevard");
		enseignant3.setCodeEnseignant("ens125");
		enseignant3.setHeures(10);
		enseignant3.setSeances(new ArrayList<>());
		enseignant3.setPropositionsDeRattrapage(new ArrayList<>());

		Enseignant enseignant4 = new Enseignant();
		enseignant4.setCin("CIN_ENS_004");
		enseignant4.setNom("David");
		enseignant4.setPrenom("Wilson");
		enseignant4.setEmail("david.wilson@university.com");
		enseignant4.setTel("3344556677");
		enseignant4.setAdresse("Teacher Road");
		enseignant4.setCodeEnseignant("ens126");
		enseignant4.setHeures(8);
		enseignant4.setSeances(new ArrayList<>());
		enseignant4.setPropositionsDeRattrapage(new ArrayList<>());

		enseignantRepository.saveAll(Arrays.asList(enseignant1, enseignant2, enseignant3, enseignant4));

		// Create Students (Etudiant)
		Etudiant etudiant1 = new Etudiant();
		etudiant1.setCin("CIN_ETU_001");
		etudiant1.setNom("Bob");
		etudiant1.setPrenom("Johnson");
		etudiant1.setEmail("bob.johnson@university.com");
		etudiant1.setTel("1122334455");
		etudiant1.setAdresse("Student Street");
		etudiant1.setMatricule("student123");
		etudiant1.setBranche(branche1);
		etudiant1.setTp(null);

		Etudiant etudiant2 = new Etudiant();
		etudiant2.setCin("CIN_ETU_002");
		etudiant2.setNom("Charlie");
		etudiant2.setPrenom("Davis");
		etudiant2.setEmail("charlie.davis@university.com");
		etudiant2.setTel("2233445566");
		etudiant2.setAdresse("Student Avenue");
		etudiant2.setMatricule("student124");
		etudiant2.setBranche(branche2);
		etudiant2.setTp(null);

		Etudiant etudiant3 = new Etudiant();
		etudiant3.setCin("CIN_ETU_003");
		etudiant3.setNom("Emily");
		etudiant3.setPrenom("Martinez");
		etudiant3.setEmail("emily.martinez@university.com");
		etudiant3.setTel("3344556677");
		etudiant3.setAdresse("Student Crescent");
		etudiant3.setMatricule("student125");
		etudiant3.setBranche(branche3);
		etudiant3.setTp(null);

		Etudiant etudiant4 = new Etudiant();
		etudiant4.setCin("CIN_ETU_004");
		etudiant4.setNom("Lucas");
		etudiant4.setPrenom("Lopez");
		etudiant4.setEmail("lucas.lopez@university.com");
		etudiant4.setTel("4455667788");
		etudiant4.setAdresse("Student Parkway");
		etudiant4.setMatricule("student126");
		etudiant4.setBranche(branche4);
		etudiant4.setTp(null);

		Etudiant etudiant5 = new Etudiant();
		etudiant5.setCin("CIN_ETU_005");
		etudiant5.setNom("Sophia");
		etudiant5.setPrenom("Gonzalez");
		etudiant5.setEmail("sophia.gonzalez@university.com");
		etudiant5.setTel("5566778899");
		etudiant5.setAdresse("Student Circle");
		etudiant5.setMatricule("student127");
		etudiant5.setBranche(branche5);
		etudiant5.setTp(null);

		etudiantRepository.saveAll(Arrays.asList(etudiant1, etudiant2, etudiant3, etudiant4, etudiant5));

		// Create Rooms (Salle)
		Salle salle1 = new Salle();
		salle1.setIdentifiant("A101");
		salle1.setType("Lecture Room");
		salle1.setCapacite(50);
		salle1.setSeances(new ArrayList<>());
		salle1.setDisponibilite(Arrays.asList("08:00-10:00", "10:30-12:30", "14:00-16:00"));

		Salle salle2 = new Salle();
		salle2.setIdentifiant("B202");
		salle2.setType("Lab");
		salle2.setCapacite(20);
		salle2.setSeances(new ArrayList<>());
		salle2.setDisponibilite(Arrays.asList("08:00-10:00", "13:00-15:00", "15:30-17:30"));

		Salle salle3 = new Salle();
		salle3.setIdentifiant("C303");
		salle3.setType("Seminar Room");
		salle3.setCapacite(30);
		salle3.setSeances(new ArrayList<>());
		salle3.setDisponibilite(Arrays.asList("09:00-11:00", "11:30-13:30"));

		salleRepository.saveAll(Arrays.asList(salle1, salle2, salle3));

		// Create Sessions (Seance)
		Seance seance1 = new Seance();
		seance1.setJour("Monday");
		seance1.setHeureDebut("08:00");
		seance1.setHeureFin("10:00");
		seance1.setType("Lecture");
		seance1.setMatiere("Math");
		seance1.setFrequence("1/15");
		seance1.setSalle(salle1);
		seance1.setEnseignant(enseignant1);
		seance1.setBranches(new ArrayList<>());
		seance1.setTds(new ArrayList<>());
		seance1.setTps(new ArrayList<>());

		Seance seance2 = new Seance();
		seance2.setJour("Monday");
		seance2.setHeureDebut("10:30");
		seance2.setHeureFin("12:30");
		seance2.setType("Lecture");
		seance2.setMatiere("Informatics");
		seance2.setFrequence("1/15");
		seance2.setSalle(salle1);
		seance2.setEnseignant(enseignant2);
		seance2.setBranches(new ArrayList<>());
		seance2.setTds(new ArrayList<>());
		seance2.setTps(new ArrayList<>());

		Seance seance3 = new Seance();
		seance3.setJour("Tuesday");
		seance3.setHeureDebut("08:00");
		seance3.setHeureFin("10:00");
		seance3.setType("Lab");
		seance3.setMatiere("Informatics");
		seance3.setFrequence("2/15");
		seance3.setSalle(salle2);
		seance3.setEnseignant(enseignant2);
		seance3.setBranches(new ArrayList<>());
		seance3.setTds(new ArrayList<>());
		seance3.setTps(new ArrayList<>());

		Seance seance4 = new Seance();
		seance4.setJour("Tuesday");
		seance4.setHeureDebut("10:30");
		seance4.setHeureFin("12:30");
		seance4.setType("Lecture");
		seance4.setMatiere("Physics");
		seance4.setFrequence("1/10");
		seance4.setSalle(salle3);
		seance4.setEnseignant(enseignant3);
		seance4.setBranches(new ArrayList<>());
		seance4.setTds(new ArrayList<>());
		seance4.setTps(new ArrayList<>());

		Seance seance5 = new Seance();
		seance5.setJour("Wednesday");
		seance5.setHeureDebut("08:00");
		seance5.setHeureFin("10:00");
		seance5.setType("Seminar");
		seance5.setMatiere("Chemistry");
		seance5.setFrequence("1/12");
		seance5.setSalle(salle3);
		seance5.setEnseignant(enseignant4);
		seance5.setBranches(new ArrayList<>());
		seance5.setTds(new ArrayList<>());
		seance5.setTps(new ArrayList<>());

		seanceRepository.saveAll(Arrays.asList(seance1, seance2, seance3, seance4, seance5));

		// Create Signals
		Signal signal1 = new Signal();
		signal1.setMessage("Room A101 is too small for the class.");
		signal1.setSeverity("HIGH");
		signal1.setTimestamp(LocalDateTime.now());

		Signal signal2 = new Signal();
		signal2.setMessage("Equipment failure in Lab B202.");
		signal2.setSeverity("MEDIUM");
		signal2.setTimestamp(LocalDateTime.now());

		Signal signal3 = new Signal();
		signal3.setMessage("Seminar Room C303 lacks proper lighting.");
		signal3.setSeverity("LOW");
		signal3.setTimestamp(LocalDateTime.now());

		signalRepository.saveAll(Arrays.asList(signal1, signal2, signal3));

		// Create Excel File Records (FichierExcel)
		FichierExcel fichierExcel1 = new FichierExcel();
		fichierExcel1.setFileName("schedule_import_01.xlsx");
		fichierExcel1.setStatus("success");
		fichierExcel1.setErrors(Arrays.asList("No issues"));
		fichierExcel1.setImportDate(LocalDateTime.now());

		FichierExcel fichierExcel2 = new FichierExcel();
		fichierExcel2.setFileName("schedule_import_02.xlsx");
		fichierExcel2.setStatus("failure");
		fichierExcel2.setErrors(Arrays.asList("Missing room information"));
		fichierExcel2.setImportDate(LocalDateTime.now());

		fichierExcelRepository.saveAll(Arrays.asList(fichierExcel1, fichierExcel2));

		// Create Notifications
		Notification notification1 = new Notification();
		notification1.setMessage("Your session has been updated.");
		notification1.setDate(LocalDateTime.now());
		notification1.setType("INFO");
		notification1.setRead(false);
		notification1.setRecepteur(etudiant1);
		notification1.setExpediteur(admin1);

		Notification notification2 = new Notification();
		notification2.setMessage("New schedule available.");
		notification2.setDate(LocalDateTime.now());
		notification2.setType("INFO");
		notification2.setRead(false);
		notification2.setRecepteur(etudiant2);
		notification2.setExpediteur(admin2);

		notificationRepository.saveAll(Arrays.asList(notification1, notification2));

		// Create Tutorial Groups (TD)
		TD td1 = new TD();
		td1.setNb(5);
		td1.setNbTP(2);
		td1.setBranche(branche1);
		td1.setTpList(new ArrayList<>());
		td1.setSeances(new ArrayList<>());

		TD td2 = new TD();
		td2.setNb(4);
		td2.setNbTP(2);
		td2.setBranche(branche2);
		td2.setTpList(new ArrayList<>());
		td2.setSeances(new ArrayList<>());

		TD td3 = new TD();
		td3.setNb(3);
		td3.setNbTP(1);
		td3.setBranche(branche3);
		td3.setTpList(new ArrayList<>());
		td3.setSeances(new ArrayList<>());

		TD td4 = new TD();
		td4.setNb(6);
		td4.setNbTP(3);
		td4.setBranche(branche4);
		td4.setTpList(new ArrayList<>());
		td4.setSeances(new ArrayList<>());

		TD td5 = new TD();
		td5.setNb(4);
		td5.setNbTP(2);
		td5.setBranche(branche5);
		td5.setTpList(new ArrayList<>());
		td5.setSeances(new ArrayList<>());

		tdRepository.saveAll(Arrays.asList(td1, td2, td3, td4, td5));

		// Create Practical Groups (TP)
		TP tp1 = new TP();
		tp1.setNb(1);
		tp1.setTd(td1);
		tp1.setEtudiants(new ArrayList<>());
		tp1.setSeances(new ArrayList<>());

		TP tp2 = new TP();
		tp2.setNb(1);
		tp2.setTd(td2);
		tp2.setEtudiants(new ArrayList<>());
		tp2.setSeances(new ArrayList<>());

		TP tp3 = new TP();
		tp3.setNb(1);
		tp3.setTd(td3);
		tp3.setEtudiants(new ArrayList<>());
		tp3.setSeances(new ArrayList<>());

		TP tp4 = new TP();
		tp4.setNb(1);
		tp4.setTd(td4);
		tp4.setEtudiants(new ArrayList<>());
		tp4.setSeances(new ArrayList<>());

		TP tp5 = new TP();
		tp5.setNb(1);
		tp5.setTd(td5);
		tp5.setEtudiants(new ArrayList<>());
		tp5.setSeances(new ArrayList<>());

		tpRepository.saveAll(Arrays.asList(tp1, tp2, tp3, tp4, tp5));

		// Create Makeup Session Proposals (PropositionDeRattrapage)
		PropositionDeRattrapage prop1 = new PropositionDeRattrapage();
		prop1.setDate(LocalDateTime.now().plusDays(3));
		prop1.setReason("Missed session due to illness");
		prop1.setStatus("Pending");
		prop1.setEnseignant(enseignant1);

		PropositionDeRattrapage prop2 = new PropositionDeRattrapage();
		prop2.setDate(LocalDateTime.now().plusDays(5));
		prop2.setReason("Missed session due to travel");
		prop2.setStatus("Confirmed");
		prop2.setEnseignant(enseignant2);

		// Add proposals to the respective teacher's list
		enseignant1.getPropositionsDeRattrapage().add(prop1);
		enseignant2.getPropositionsDeRattrapage().add(prop2);
		enseignantRepository.saveAll(Arrays.asList(enseignant1, enseignant2));

		System.out.println("Database populated with sample data using no-argument constructors and setters!");
	}
	public void populatewithconflicts(){

		// Create Rooms (Salle)
		Salle salle1 = new Salle();
		salle1.setIdentifiant("C303");
		salle1.setType("Classroom");
		salle1.setCapacite(40);
		salle1.setDisponibilite(Arrays.asList("08:00-10:00", "10:00-12:00", "14:00-16:00"));
		salleRepository.save(salle1);

		Salle salle2 = new Salle();
		salle2.setIdentifiant("D404");
		salle2.setType("Lecture Hall");
		salle2.setCapacite(60);
		salle2.setDisponibilite(Arrays.asList("08:00-10:00", "10:00-12:00", "14:00-16:00"));
		salleRepository.save(salle2);

		// Create Enseignant (teacher) for the conflicting seances
		Enseignant enseignant1 = new Enseignant();
		enseignant1.setCin("CIN_ENS_003");
		enseignant1.setNom("John");
		enseignant1.setPrenom("Doe");
		enseignant1.setEmail("john.doe@university.com");
		enseignant1.setTel("123987654");
		enseignant1.setAdresse("Science Street");
		enseignant1.setCodeEnseignant("ENS003");
		enseignantRepository.save(enseignant1);

		Enseignant enseignant2 = new Enseignant();
		enseignant2.setCin("CIN_ENS_004");
		enseignant2.setNom("Jane");
		enseignant2.setPrenom("Doe");
		enseignant2.setEmail("jane.doe@university.com");
		enseignant2.setTel("456321789");
		enseignant2.setAdresse("Math Avenue");
		enseignant2.setCodeEnseignant("ENS004");
		enseignantRepository.save(enseignant2);

		// Create Conflicting Seances (new set with different times/rooms)
		Seance seance1 = new Seance();
		seance1.setJour("Wednesday");
		seance1.setHeureDebut("08:00");
		seance1.setHeureFin("10:00");
		seance1.setType("Lecture");
		seance1.setMatiere("Data Structures");
		seance1.setFrequence("Weekly");
		seance1.setSalle(salle1);  // Room C303
		seance1.setEnseignant(enseignant1);
		seanceRepository.save(seance1);

		Seance seance2 = new Seance();
		seance2.setJour("Wednesday");
		seance2.setHeureDebut("08:00");
		seance2.setHeureFin("10:00");
		seance2.setType("Lecture");
		seance2.setMatiere("Algorithms");
		seance2.setFrequence("Weekly");
		seance2.setSalle(salle1);  // Same room as seance1, causing conflict
		seance2.setEnseignant(enseignant2);
		seanceRepository.save(seance2);

		Seance seance3 = new Seance();
		seance3.setJour("Thursday");
		seance3.setHeureDebut("14:00");
		seance3.setHeureFin("16:00");
		seance3.setType("Workshop");
		seance3.setMatiere("Computer Networks");
		seance3.setFrequence("Biweekly");
		seance3.setSalle(salle2);  // Room D404
		seance3.setEnseignant(enseignant1);
		seanceRepository.save(seance3);

		Seance seance4 = new Seance();
		seance4.setJour("Thursday");
		seance4.setHeureDebut("14:00");
		seance4.setHeureFin("16:00");
		seance4.setType("Workshop");
		seance4.setMatiere("Operating Systems");
		seance4.setFrequence("Biweekly");
		seance4.setSalle(salle2);  // Same room as seance3, causing conflict
		seance4.setEnseignant(enseignant2);
		seanceRepository.save(seance4);

	}

	/**
	 * Tests basic JPA repository functions (except for save, which was already tested during population).
	 */
	private void testJpaFunctions() {
		System.out.println("----- Testing Basic JPA Functions -----");

		// Assuming at least one Administrateur was already saved during population
		List<Administrateur> admins = administrateurRepository.findAll();
		if (!admins.isEmpty()) {
			Administrateur admin = admins.get(0);
			Long adminId = admin.getId();

			// Test findById
			Optional<Administrateur> foundAdminOpt = administrateurRepository.findById(adminId);
			System.out.println("findById (" + adminId + "): " + foundAdminOpt.orElse(null));

			// Test findAll
			List<Administrateur> allAdmins = administrateurRepository.findAll();
			System.out.println("findAll: " + allAdmins);

			// Test existsById
			boolean exists = administrateurRepository.existsById(adminId);
			System.out.println("existsById (" + adminId + "): " + exists);

			// Test count
			long count = administrateurRepository.count();
			System.out.println("count: " + count);

			// Test flush (force pending changes to be written to the database)
			administrateurRepository.flush();
			System.out.println("flush: Repository changes flushed");

			// Test findAllById (for example, for two IDs)
			List<Administrateur> adminsByIds = administrateurRepository.findAllById(Arrays.asList(adminId, adminId + 1));
			System.out.println("findAllById ([ " + adminId + ", " + (adminId + 1) + " ]): " + adminsByIds);

			// Test deleteById by creating a temporary Administrateur, then deleting it.
			Administrateur tempAdmin = new Administrateur();
			tempAdmin.setCin("TEMP_CIN");
			tempAdmin.setNom("Temp");
			tempAdmin.setPrenom("User");
			tempAdmin.setEmail("temp@university.com");
			tempAdmin.setTel("0000000000");
			tempAdmin.setAdresse("Temp Street");
			tempAdmin.setCodeAdmin("temp001");
			Administrateur savedTemp = administrateurRepository.save(tempAdmin);
			Long tempId = savedTemp.getId();
			System.out.println("Temporary admin saved: " + savedTemp);
			administrateurRepository.deleteById(tempId);
			System.out.println("Temporary admin with id " + tempId + " deleted.");
		} else {
			System.out.println("No Administrateur found to test JPA functions.");
		}
	}

	/**
	 * Tests custom conflict-related JPA functions defined in SeanceRepository.
	 */
	private void testConflictFunctions() {
		System.out.println("----- Testing Conflict-Related Functions -----");

		// Use your custom query to retrieve conflicting seance pairs.
		// This query returns a list of Object arrays, where each array contains two conflicting Seance objects.
		List<Object[]> conflictPairs = seanceRepository.findConflictingSeancePairs();
		if (!conflictPairs.isEmpty()) {
			System.out.println("Conflicting Seance Pairs:");
			for (Object[] pair : conflictPairs) {
				Seance s1 = (Seance) pair[0];
				Seance s2 = (Seance) pair[1];
				String salleInfo = (s1.getSalle() != null ? s1.getSalle().getIdentifiant() : "unknown");
				System.out.println("Conflict: " + s1.getMatiere() + " (Room " + salleInfo + ") and " + s2.getMatiere());
			}
		} else {
			System.out.println("No conflicting seances found using findConflictingSeancePairs.");
		}

		// Test another custom query, e.g., conflicts by room
		List<Object[]> roomConflicts = seanceRepository.findConflictingByRooms();
		if (!roomConflicts.isEmpty()) {
			System.out.println("Room Conflict Pairs:");
			for (Object[] pair : roomConflicts) {
				Seance s1 = (Seance) pair[0];
				Seance s2 = (Seance) pair[1];
				String salleIdent = (s1.getSalle() != null ? s1.getSalle().getIdentifiant() : "unknown");
				System.out.println("Room " + salleIdent + " conflict: " + s1.getMatiere() + " vs " + s2.getMatiere());
			}
		} else {
			System.out.println("No room conflicts found using findConflictingByRooms.");
		}

		// Optionally, test the findRoomConflictsForSeance method for one seance.
		// For this example, pick one seance from the repository.
		Optional<Seance> optionalSeance = seanceRepository.findAll().stream().findFirst();
		if (optionalSeance.isPresent()) {
			Seance testSeance = optionalSeance.get();
			if (testSeance.getSalle() != null) {
				List<Seance> conflictsForTest = seanceRepository.findRoomConflictsForSeance(
						testSeance.getId(),
						testSeance.getJour(),
						testSeance.getSalle().getId(),
						testSeance.getHeureDebut(),
						testSeance.getHeureFin()
				);
				System.out.println("Room conflicts for seance (ID " + testSeance.getId() + "): " + conflictsForTest);
			}
		}
	}


	@Override
	public void run(String... args) throws Exception {

	}
}
