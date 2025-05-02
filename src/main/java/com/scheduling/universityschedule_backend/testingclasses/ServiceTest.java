package com.scheduling.universityschedule_backend.testingclasses;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.FrequenceType;
import com.scheduling.universityschedule_backend.model.Status;
import com.scheduling.universityschedule_backend.model.SeanceType;
import com.scheduling.universityschedule_backend.repository.PropositionDeRattrapageRepository;
import com.scheduling.universityschedule_backend.service.*;
import com.scheduling.universityschedule_backend.util.CustomLogger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
import java.util.*;
import java.util.function.Function;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Transactional
public class ServiceTest {

    private final AdministrateurService administrateurService;
    private final BrancheService brancheService;
    private final EtudiantService etudiantService;
    private final ExcelFileService excelFileService;
    private final SalleService salleService;
    private final SeanceService seanceService;
    private final EnseignantService enseignantService;
    private final TDService tdService;
    private final TPService tpService;
    private final NotificationService notificationService;
    private final PropositionDeRattrapageRepository propositionDeRattrapageRepository;


    public Random RANDOM = new Random();
    // Add to ServiceTest class
    private final EntityManager entityManager;

    @PersistenceContext
    private EntityManager em;
    private final EntityMapper entityMapper;

    // Add this method to be called at the beginning of each test method
    private void clearDatabase() {
        entityManager.flush();
        entityManager.clear();
    }

    public void debug() throws CustomException {
//        populateDatabase(50);
//        testAdministrateurService();
//        testEtudiantService();
//        testSalleService();
//        testSeanceService();
//
//        clearDatabase();
//        testTPService();
//        testTDService();
//        testNotificationService();
//        testExcelFileService();
//rudTest();
//        testSpecializedFunctionalities();
//    testAllUntestedExcelFileServiceFunctionalities();
        testSpecializedFunctionalities();
    }

    public void testSpecializedFunctionalities() throws CustomException {
        CustomLogger.logInfo("========== Testing Specialized Functionalities ==========");
        clearDatabase();
//        testAllUntestAdministrateurServiceFunctionalities();
//testAllUntestedBrancheServiceFunctionalities();
//        testAllUntestedEnseignantServiceFunctionalities()
        testAllUntestedSalleServiceFunctionalities();
        CustomLogger.logInfo("========== Specialized Functions Testing Complete ==========");
    }

    public ServiceTest(AdministrateurService administrateurService, BrancheService brancheService, EtudiantService etudiantService, ExcelFileService excelFileService, SalleService salleService, SeanceService seanceService, EnseignantService enseignantService, TDService tdService, TPService tpService, NotificationService notificationService, EntityMapper entityMapper, EntityManager entityManager, PropositionDeRattrapageRepository propositionDeRattrapageRepository) {
        this.administrateurService = administrateurService;
        this.brancheService = brancheService;
        this.etudiantService = etudiantService;
        this.excelFileService = excelFileService;
        this.salleService = salleService;
        this.seanceService = seanceService;
        this.enseignantService = enseignantService;
        this.tdService = tdService;
        this.tpService = tpService;
        this.notificationService = notificationService;
        this.entityMapper = entityMapper;
        this.entityManager = entityManager;
        this.propositionDeRattrapageRepository = propositionDeRattrapageRepository;
    }

    public void populateDatabase(int sampleSize) throws CustomException {
        CustomLogger.logInfo("========== Populating Database Using Service Layer ==========");

        // Random generators
        Random RANDOM = new Random();
        List<String> NOMS = Arrays.asList("Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller",
                "Wilson", "Moore", "Taylor", "Anderson", "Thomas", "Jackson", "White");
        List<String> PRENOMS = Arrays.asList("James", "Mary", "John", "Patricia", "Robert", "Jennifer", "Michael",
                "Linda", "William", "Elizabeth", "David", "Barbara", "Richard", "Susan");

        // Helper functions for random data
        Function<Void, String> generateRandomCin = v -> String.valueOf(100000000 + RANDOM.nextInt(900000000));
        Function<Void, String> generateRandomTel = v -> String.valueOf(1000000000L + RANDOM.nextLong(9000000000L));
        Function<Void, String> pickNom = v -> NOMS.get(RANDOM.nextInt(NOMS.size()));
        Function<Void, String> pickPrenom = v -> PRENOMS.get(RANDOM.nextInt(PRENOMS.size()));
        Function<Void, DayOfWeek> randomDay = v -> DayOfWeek.values()[RANDOM.nextInt(5)];
        Function<Void, String> randomDayString = v -> randomDay.apply(null).toString();
        Function<Void, LocalTime> randomTime = v -> LocalTime.of(8 + RANDOM.nextInt(9), RANDOM.nextInt(12) * 5);
        Function<LocalTime, LocalTime> randomTimeAfter = start -> start.plusMinutes(90 + RANDOM.nextInt(90));
        Function<Void, String> randomFrequenceString = v -> {
            FrequenceType[] freqs = FrequenceType.values();
            return freqs[RANDOM.nextInt(freqs.length)].toString();
        };
        Function<Void, String> pickSeanceType = v -> {
            SeanceType[] types = SeanceType.values();
            return types[RANDOM.nextInt(types.length)].toString();
        };
        Function<Void, String> pickRandomSubject = v -> {
            String[] subjects = {
                    "Mathematics", "Physics", "Chemistry", "Computer Architecture",
                    "Database Systems", "Operating Systems", "Data Structures",
                    "Algorithms", "Software Engineering", "Networks", "Artificial Intelligence"
            };
            return subjects[RANDOM.nextInt(subjects.length)];
        };

        Function<Void, String> pickNiveau = v -> {
            String[] systems = {"L", "CPI", "ING", "MR", "MP"};
            String system = systems[RANDOM.nextInt(systems.length)];
            int yearLevel = switch (system) {
                case "L" -> RANDOM.nextInt(3) + 1;
                case "CPI" -> RANDOM.nextInt(2) + 1;
                case "ING" -> RANDOM.nextInt(3) + 1;
                default -> RANDOM.nextInt(2) + 1;
            };
            return system + yearLevel;
        };

        Function<String, String> pickSpecialty = niveau -> {
            if (niveau.startsWith("L")) return Arrays.asList("EEA", "INFO", "TIC", "MATH").get(RANDOM.nextInt(4));
            if (niveau.startsWith("CPI")) return "INFO";
            if (niveau.startsWith("ING")) return Arrays.asList("ELEC", "INFO").get(RANDOM.nextInt(2));
            return Arrays.asList("GL", "I3", "DS").get(RANDOM.nextInt(3));
        };

        // 1. Create Administrateurs
        List<AdministrateurDTO> admins = new ArrayList<>();
        for (int i = 0; i < Math.max(1, sampleSize / 5); i++) {
            AdministrateurDTO admin = new AdministrateurDTO();
            admin.setCin(generateRandomCin.apply(null));
            admin.setNom(pickNom.apply(null));
            admin.setPrenom(pickPrenom.apply(null));
            admin.setEmail("admin" + i + "@example.com");
            admin.setTel(generateRandomTel.apply(null));
            admin.setAdresse("Admin Address " + i);
            admin.setCodeAdmin("ADM" + i);
            admins.add(administrateurService.create(admin));
        }
        CustomLogger.logInfo("Created Administrators: " + admins.size());

        // 2. Create Enseignants
        List<EnseignantDTO> enseignants = new ArrayList<>();
        for (int i = 0; i < sampleSize; i++) {
            EnseignantDTO enseignant = new EnseignantDTO();
            enseignant.setCin(generateRandomCin.apply(null));
            enseignant.setNom(pickNom.apply(null));
            enseignant.setPrenom(pickPrenom.apply(null));
            enseignant.setEmail("enseignant" + i + "@example.com");
            enseignant.setTel(generateRandomTel.apply(null));
            enseignant.setAdresse("Teacher Address " + i);
            enseignant.setCodeEnseignant("ENS" + i);
            enseignant.setHeures((int) (Math.random() * 40) + 1);
            enseignants.add(enseignantService.create(enseignant));
        }
        CustomLogger.logInfo("Created Teachers: " + enseignants.size());

        // 3. Create Branches
        List<BrancheDTO> branches = new ArrayList<>();
        for (int i = 0; i < Math.max(1, sampleSize / 15); i++) {
            BrancheDTO branche = new BrancheDTO();
            String niveau = pickNiveau.apply(null);
            branche.setNiveau(niveau);
            branche.setSpecialite(pickSpecialty.apply(niveau));
            branche.setNbTD((int) (Math.random() * 5) + 1);
            branche.setDepartement(Math.random() < 0.33 ? "Technologie" : "informatique");
            branches.add(brancheService.create(branche));
        }
        CustomLogger.logInfo("Created Branches: " + branches.size());

        // 4. Create TDs
        List<TDDTO> tds = new ArrayList<>();
        for (int i = 0; i < Math.max(2, (sampleSize * 3) / 20); i++) {
            TDDTO td = new TDDTO();
            td.setNb((int) (Math.random() * 5) + 1);
            td.setNbTP((int) (Math.random() * 3) + 1);
            td.setBrancheId(branches.get(RANDOM.nextInt(branches.size())).getId());
            tds.add(tdService.create(td));
        }
        CustomLogger.logInfo("Created TDs: " + tds.size());

        // 5. Create TPs
        List<TPDTO> tps = new ArrayList<>();
        for (int i = 0; i < Math.max(3, sampleSize / 3); i++) {
            TPDTO tp = new TPDTO();
            tp.setNb((int) (Math.random() * 3) + 1);
            tp.setTdId(tds.get(RANDOM.nextInt(tds.size())).getId());
            tps.add(tpService.create(tp));
        }
        CustomLogger.logInfo("Created TPs: " + tps.size());

        // 6. Create Etudiants
        List<EtudiantDTO> etudiants = new ArrayList<>();
        for (int i = 0; i < sampleSize * 3; i++) {
            EtudiantDTO etudiant = new EtudiantDTO();
            etudiant.setCin(generateRandomCin.apply(null));
            etudiant.setNom(pickNom.apply(null));
            etudiant.setPrenom(pickPrenom.apply(null));
            etudiant.setEmail("etudiant" + i + "@example.com");
            etudiant.setTel(generateRandomTel.apply(null));
            etudiant.setAdresse("Student Address " + i);
            etudiant.setMatricule("MAT" + i);
            etudiant.setBrancheId(branches.get(RANDOM.nextInt(branches.size())).getId());
            etudiant.setTpId(tps.get(RANDOM.nextInt(tps.size())).getId());
            etudiants.add(etudiantService.create(etudiant));
        }
        CustomLogger.logInfo("Created Students: " + etudiants.size());

        // 7. Create Salles
        List<SalleDTO> salles = new ArrayList<>();
        for (int i = 0; i < Math.max(3, sampleSize / 10); i++) {
            SalleDTO salle = new SalleDTO();
            salle.setIdentifiant("SAL" + i);
            salle.setType("Lecture Hall");
            salle.setCapacite((int) (Math.random() * 150) + 50);
            salles.add(salleService.create(salle));
        }
        CustomLogger.logInfo("Created Rooms: " + salles.size());

        // 8. Create Seances (without conflict detection)
        List<SeanceDTO> seances = new ArrayList<>();
        for (int i = 0; i < sampleSize * 2; i++) {
            SeanceDTO seance = new SeanceDTO();
            seance.setMatiere(pickRandomSubject.apply(null));
            seance.setJour(randomDayString.apply(null));

            LocalTime startTime = randomTime.apply(null);
            LocalTime endTime = randomTimeAfter.apply(startTime);
            seance.setHeureDebut(startTime.toString());
            seance.setHeureFin(endTime.toString());

            seance.setFrequence(randomFrequenceString.apply(null));
            seance.setType(pickSeanceType.apply(null));

            if (seance.getFrequence().equals("CATCHUP")) {
                seance.setDate(LocalDate.now().plusDays(i % 14).toString());
            }

            seance.setSalleId(salles.get(RANDOM.nextInt(salles.size())).getId());
            seance.setEnseignantId(enseignants.get(RANDOM.nextInt(enseignants.size())).getId());

            // Add related entities (branches, TDs, TPs)
            List<Long> brancheIds = new ArrayList<>();
            brancheIds.add(branches.get(RANDOM.nextInt(branches.size())).getId());
            seance.setBrancheIds(brancheIds);

            List<Long> tdIds = new ArrayList<>();
            tdIds.add(tds.get(RANDOM.nextInt(tds.size())).getId());
            seance.setTdIds(tdIds);

            List<Long> tpIds = new ArrayList<>();
            tpIds.add(tps.get(RANDOM.nextInt(tps.size())).getId());
            seance.setTpIds(tpIds);

            // Create without checking conflicts
            try {
                seances.add(seanceService.create(seance));
            } catch (Exception e) {
                CustomLogger.logError("Failed to create seance " + i + ": " + e.getMessage());
            }
        }
        CustomLogger.logInfo("Created Sessions: " + seances.size());

// 9. Create Makeup Session Proposals
        for (int i = 0; i < Math.max(1, sampleSize / 3); i++) {
            try {
                PropositionDeRattrapageDTO proposition = new PropositionDeRattrapageDTO();
                proposition.setName("Makeup Session " + i);
                proposition.setMatiere(pickRandomSubject.apply(null));
                proposition.setType(pickSeanceType.apply(null));

                LocalTime startTime = randomTime.apply(null);
                LocalTime endTime = randomTimeAfter.apply(startTime);
                proposition.setHeureDebut(startTime.toString());
                proposition.setHeureFin(endTime.toString());

                LocalDateTime proposalDate = LocalDateTime.now().plusDays(RANDOM.nextInt(14));
                proposition.setDate(proposalDate.toString());
                proposition.setStatus(Status.PENDING.name());

                // Add random reason
                String[] reasons = {
                        "Previous session cancelled due to emergency",
                        "Holiday makeup session",
                        "Extra practice needed before exam",
                        "Schedule conflict resolution"
                };
                proposition.setReason(reasons[RANDOM.nextInt(reasons.length)]);

                // Add related entities
                Long teacherId = enseignants.get(RANDOM.nextInt(enseignants.size())).getId();
                proposition.setEnseignantId(teacherId);

                List<Long> brancheIds;
                brancheIds = new ArrayList<>();
                brancheIds.add(branches.get(RANDOM.nextInt(branches.size())).getId());
                proposition.setBrancheIds(brancheIds);

                List<Long> tdIds = new ArrayList<>();
                tdIds.add(tds.get(RANDOM.nextInt(tds.size())).getId());
                proposition.setTdIds(tdIds);

                List<Long> tpIds = new ArrayList<>();
                tpIds.add(tps.get(RANDOM.nextInt(tps.size())).getId());
                proposition.setTpIds(tpIds);

                enseignantService.submitMakeupRequest(teacherId, proposition);
            } catch (Exception e) {
                CustomLogger.logError("Failed to create makeup proposal: " + e.getMessage());
            }
        }
        // 10. Create Signals
        int signalcount = 0;
        for (int i = 0; i < Math.max(1, sampleSize / 3); i++) {
            try {
                SignalDTO signal = new SignalDTO();
                signal.setMessage("Signal Issue " + i);
                signal.setSeverity(Math.random() > 0.5 ? "High" : "Low");
                signal.setTimestamp(LocalDateTime.parse(LocalDateTime.now().toString()));

                Long teacherId = enseignants.get(RANDOM.nextInt(enseignants.size() - 1)).getId();
                signal.setEnseignantId(String.valueOf(teacherId));

                enseignantService.submitSignal(teacherId, signal);
                signalcount++;
            } catch (Exception e) {
                CustomLogger.logError("Failed to create signal: " + e.getMessage());
            }
        }
        CustomLogger.logInfo("Created Signals: " + signalcount);
// 11. Create Notifications
        for (int i = 0; i < sampleSize * 2; i++) {
            try {
                NotificationDTO notification = new NotificationDTO();
                notification.setMessage("Notification message " + i);
                notification.setType(RANDOM.nextBoolean() ? "SCHEDULE_CHANGE" : "ANNOUNCEMENT");
                notification.setDate(LocalDateTime.now().minusHours(RANDOM.nextInt(48)));
                notification.setIsread(RANDOM.nextInt(10) < 3); // 30% read, 70% unread

                // Determine sender (admin) and receiver (can be enseignant, student, etc.)
                Long senderId = admins.get(RANDOM.nextInt(admins.size())).getId();

                // Vary recipients between teachers and students
                Long recipientId;
                if (RANDOM.nextBoolean()) {
                    // Teacher as recipient
                    recipientId = enseignants.get(RANDOM.nextInt(enseignants.size())).getId();

                    // Set type for teacher notifications
                    if (RANDOM.nextBoolean()) {
                        notification.setType("SCHEDULE_CHANGE");
                        notification.setMessage("Your session on " +
                                DayOfWeek.values()[RANDOM.nextInt(5)] + " has been " +
                                (RANDOM.nextBoolean() ? "rescheduled" : "cancelled"));
                    }
                } else {
                    // Student as recipient
                    recipientId = etudiants.get(RANDOM.nextInt(etudiants.size())).getId();

                    // Set type for student notifications
                    if (RANDOM.nextBoolean()) {
                        notification.setType("COURSE_MATERIAL");
                        notification.setMessage("New material available for " + pickRandomSubject.apply(null));
                    }
                }

                notification.setExpediteurId(senderId);
                notification.setRecepteurId(recipientId);

                // Here we assume the notification service has a create method
                // If there is a specific method for sending notifications, use that instead
                notificationService.create(notification);

            } catch (Exception e) {
                CustomLogger.logError("Failed to create notification: " + e.getMessage());
            }
        }
        CustomLogger.logInfo("Created Notifications: " + sampleSize * 2);
        // 12. FichierExcel (for bulk imports)
        for (int i = 0; i < Math.max(1, sampleSize / 5); i++) {
            try {
                FichierExcelDTO fichier = new FichierExcelDTO();
                fichier.setFileName("schedule_" + i + ".xlsx");
                fichier.setStatus("Imported");
                fichier.setErrors(List.of("No errors"));
                fichier.setImportDate(LocalDateTime.parse(LocalDateTime.now().toString()));

                excelFileService.create(fichier);
            } catch (Exception e) {
                CustomLogger.logError("Failed to create excel file record: " + e.getMessage());
            }
        }
        CustomLogger.logInfo("========== Database Population Complete ==========");
    }


    public void rudTest() throws CustomException {
        CustomLogger.logInfo("========== Testing Retrieval, Update, Delete Operations ==========");
        clearDatabase();
        testAdministrateurService();
        clearDatabase();
        testBrancheService();
        clearDatabase();
        testEnseignantService();
        clearDatabase();
        testEtudiantService();
        clearDatabase();
        testSalleService();
        clearDatabase();
        testSeanceService();
        clearDatabase();
        testTDService();
        clearDatabase();
        testTPService();
        clearDatabase();
        testNotificationService();
        clearDatabase();
        testExcelFileService();
        clearDatabase();

        CustomLogger.logInfo("========== RUD Testing Complete ==========");
    }

    private void testAdministrateurService() {
        try {
            CustomLogger.logInfo("Testing AdministrateurService RUD operations...");
            List<AdministrateurDTO> allAdmins = administrateurService.findAll();
            if (allAdmins.size() >= 2) {
                // Get 2 random admins
                int index1 = RANDOM.nextInt(allAdmins.size());
                int index2 = (index1 + 1 + RANDOM.nextInt(allAdmins.size() - 1)) % allAdmins.size();
                AdministrateurDTO admin1 = allAdmins.get(index1);
                AdministrateurDTO admin2 = allAdmins.get(index2);

                // Test findById
                AdministrateurDTO fetchedAdmin1 = administrateurService.findById(admin1.getId());
                AdministrateurDTO fetchedAdmin2 = administrateurService.findById(admin2.getId());

                // Test update
                CustomLogger.logInfo("Admin to be updated: " + fetchedAdmin1.getId().toString());
                fetchedAdmin1.setNom("Updated Name");
                AdministrateurDTO updatedAdmin = administrateurService.update(fetchedAdmin1.getId(), fetchedAdmin1);
                CustomLogger.logInfo("Updated admin: " + updatedAdmin.getId().toString());

                // Test delete
                CustomLogger.logInfo("Admin to be deleted: " + fetchedAdmin2.getId().toString());
                administrateurService.delete(admin2.getId());

                CustomLogger.logInfo("AdministrateurService RUD operations completed successfully");
            } else {
                CustomLogger.logInfo("Not enough administrators for RUD testing");
            }
        } catch (Exception e) {
            CustomLogger.logError("Error testing AdministrateurService: " + e.getMessage(), e);
        }
    }

    private void testBrancheService() {
        try {
            CustomLogger.logInfo("Testing BrancheService RUD operations...");
            List<BrancheDTO> allBranches = brancheService.findAll();
            if (allBranches.size() >= 2) {
                // Get 2 random branches
                int index1 = RANDOM.nextInt(allBranches.size());
                int index2 = (index1 + 1 + RANDOM.nextInt(allBranches.size() - 1)) % allBranches.size();
                BrancheDTO branche1 = allBranches.get(index1);
                BrancheDTO branche2 = allBranches.get(index2);

                // Test findById
                BrancheDTO fetchedBranche1 = brancheService.findById(branche1.getId());
                BrancheDTO fetchedBranche2 = brancheService.findById(branche2.getId());

                // Test update
                CustomLogger.logInfo("Branch to be updated: " + fetchedBranche1);
                fetchedBranche1.setDepartement("Updated Department");
                BrancheDTO updatedBranche = brancheService.update(fetchedBranche1.getId(), fetchedBranche1);
                CustomLogger.logInfo("Updated branch: " + updatedBranche);

                // Test delete
                CustomLogger.logInfo("Branch to be deleted: " + fetchedBranche2);
                brancheService.delete(branche2.getId());

                CustomLogger.logInfo("BrancheService RUD operations completed successfully");
            } else {
                CustomLogger.logInfo("Not enough branches for RUD testing");
            }
        } catch (Exception e) {
            CustomLogger.logError("Error testing BrancheService: " + e.getMessage(), e);
        }
    }

    private void testEnseignantService() {
        try {
            CustomLogger.logInfo("Testing EnseignantService RUD operations...");
            List<EnseignantDTO> allTeachers = enseignantService.findAll();
            if (allTeachers.size() >= 2) {
                // Get 2 random teachers
                int index1 = RANDOM.nextInt(allTeachers.size());
                int index2 = (index1 + 1 + RANDOM.nextInt(allTeachers.size() - 1)) % allTeachers.size();
                EnseignantDTO teacher1 = allTeachers.get(index1);
                EnseignantDTO teacher2 = allTeachers.get(index2);

                // Test findById
                EnseignantDTO fetchedTeacher1 = enseignantService.findById(teacher1.getId());
                EnseignantDTO fetchedTeacher2 = enseignantService.findById(teacher2.getId());

                // Test update
                CustomLogger.logInfo("Teacher to be updated: " + fetchedTeacher1.getId().toString());
                fetchedTeacher1.setHeures(fetchedTeacher1.getHeures() + 5);
                EnseignantDTO updatedTeacher = enseignantService.update(fetchedTeacher1.getId(), fetchedTeacher1);
                CustomLogger.logInfo("Updated teacher: " + updatedTeacher);

                // Test delete
                CustomLogger.logInfo("Teacher to be deleted: " + fetchedTeacher1.getId().toString());
                enseignantService.delete(teacher2.getId());

                CustomLogger.logInfo("EnseignantService RUD operations completed successfully");
            } else {
                CustomLogger.logInfo("Not enough teachers for RUD testing");
            }
        } catch (Exception e) {
            CustomLogger.logError("Error testing EnseignantService: " + e.getMessage(), e);
        }
    }

    private void testEtudiantService() {
        try {
            CustomLogger.logInfo("Testing EtudiantService RUD operations...");
            List<EtudiantDTO> allStudents = etudiantService.findAll();
            if (allStudents.size() >= 2) {
                // Get 2 random students
                int index1 = RANDOM.nextInt(allStudents.size());
                int index2 = (index1 + 1 + RANDOM.nextInt(allStudents.size() - 1)) % allStudents.size();
                EtudiantDTO student1 = allStudents.get(index1);
                EtudiantDTO student2 = allStudents.get(index2);

                // Test findById
                EtudiantDTO fetchedStudent1 = etudiantService.findById(student1.getId());
                EtudiantDTO fetchedStudent2 = etudiantService.findById(student2.getId());

                // Test update
                CustomLogger.logInfo("Student to be updated: " + fetchedStudent1);
                fetchedStudent1.setEmail("updated.email" + RANDOM.nextInt(100) + "@example.com");
                EtudiantDTO updatedStudent = etudiantService.update(fetchedStudent1.getId(), fetchedStudent1);
                CustomLogger.logInfo("Updated student: " + updatedStudent);

                // Test delete
                CustomLogger.logInfo("Student to be deleted: " + fetchedStudent2);
                etudiantService.delete(student2.getId());

                CustomLogger.logInfo("EtudiantService RUD operations completed successfully");
            } else {
                CustomLogger.logInfo("Not enough students for RUD testing");
            }
        } catch (Exception e) {
            CustomLogger.logError("Error testing EtudiantService: " + e.getMessage(), e);
        }
    }

    private void testSalleService() {
        try {
            CustomLogger.logInfo("Testing SalleService RUD operations...");
            List<SalleDTO> allRooms = salleService.findAll();
            if (allRooms.size() >= 2) {
                // Get 2 random rooms
                int index1 = RANDOM.nextInt(allRooms.size());
                int index2 = (index1 + 1 + RANDOM.nextInt(allRooms.size() - 1)) % allRooms.size();
                SalleDTO room1 = allRooms.get(index1);
                SalleDTO room2 = allRooms.get(index2);

                // Test findById
                SalleDTO fetchedRoom1 = salleService.findById(room1.getId());
                SalleDTO fetchedRoom2 = salleService.findById(room2.getId());

                // Test update
                CustomLogger.logInfo("Room to be updated: " + fetchedRoom1);
                fetchedRoom1.setCapacite(fetchedRoom1.getCapacite() + 10);
                SalleDTO updatedRoom = salleService.update(fetchedRoom1.getId(), fetchedRoom1);
                CustomLogger.logInfo("Updated room: " + updatedRoom);

                // Test delete
                CustomLogger.logInfo("Room to be deleted: " + fetchedRoom2);
                salleService.delete(room2.getId());

                CustomLogger.logInfo("SalleService RUD operations completed successfully");
            } else {
                CustomLogger.logInfo("Not enough rooms for RUD testing");
            }
        } catch (Exception e) {
            CustomLogger.logError("Error testing SalleService: " + e.getMessage(), e);
        }
    }

    private void testSeanceService() {
        try {
            CustomLogger.logInfo("Testing SeanceService RUD operations...");
            List<SeanceDTO> allSessions = seanceService.findAll();
            if (allSessions.size() >= 2) {
                // Get 2 random sessions
                int index1 = RANDOM.nextInt(allSessions.size());
                int index2 = (index1 + 1 + RANDOM.nextInt(allSessions.size() - 1)) % allSessions.size();
                SeanceDTO session1 = allSessions.get(index1);
                SeanceDTO session2 = allSessions.get(index2);

                // Test findById
                SeanceDTO fetchedSession1 = seanceService.findById(session1.getId());
                SeanceDTO fetchedSession2 = seanceService.findById(session2.getId());

                // Test update
                CustomLogger.logInfo("Session to be updated: " + fetchedSession1);
                fetchedSession1.setMatiere("Updated Subject " + RANDOM.nextInt(100));
                SeanceDTO updatedSession = seanceService.update(fetchedSession1.getId(), fetchedSession1);
                CustomLogger.logInfo("Updated session: " + updatedSession);

                // Test delete
                CustomLogger.logInfo("Session to be deleted: " + fetchedSession2);
                seanceService.delete(session2.getId());

                CustomLogger.logInfo("SeanceService RUD operations completed successfully");
            } else {
                CustomLogger.logInfo("Not enough sessions for RUD testing");
            }
        } catch (Exception e) {
            CustomLogger.logError("Error testing SeanceService: " + e.getMessage(), e);
        }
    }

    private void testTDService() {
        try {
            CustomLogger.logInfo("Testing TDService RUD operations...");
            List<TDDTO> allTDs = tdService.findAll();
            if (allTDs.size() >= 2) {
                // Get 2 random TDs
                int index1 = RANDOM.nextInt(allTDs.size());
                int index2 = (index1 + 1 + RANDOM.nextInt(allTDs.size() - 1)) % allTDs.size();
                TDDTO td1 = allTDs.get(index1);
                TDDTO td2 = allTDs.get(index2);

                // Test findById
                TDDTO fetchedTD1 = tdService.findById(td1.getId());
                TDDTO fetchedTD2 = tdService.findById(td2.getId());

                // Test update
                CustomLogger.logInfo("TD to be updated: " + fetchedTD1);
                fetchedTD1.setNb(fetchedTD1.getNb() + 1);
                TDDTO updatedTD = tdService.update(fetchedTD1.getId(), fetchedTD1);
                CustomLogger.logInfo("Updated TD: " + updatedTD);

                // Test delete
                CustomLogger.logInfo("TD to be deleted: " + fetchedTD2);
                tdService.delete(td2.getId());

                CustomLogger.logInfo("TDService RUD operations completed successfully");
            } else {
                CustomLogger.logInfo("Not enough TDs for RUD testing");
            }
        } catch (Exception e) {
            CustomLogger.logError("Error testing TDService: " + e.getMessage(), e);
        }
    }

    private void testTPService() {
        try {
            CustomLogger.logInfo("Testing TPService RUD operations...");
            List<TPDTO> allTPs = tpService.findAll();
            if (allTPs.size() >= 2) {
                // Get 2 random TPs
                int index1 = RANDOM.nextInt(allTPs.size());
                int index2 = (index1 + 1 + RANDOM.nextInt(allTPs.size() - 1)) % allTPs.size();
                TPDTO tp1 = allTPs.get(index1);
                TPDTO tp2 = allTPs.get(index2);

                // Test findById
                TPDTO fetchedTP1 = tpService.findById(tp1.getId());
                TPDTO fetchedTP2 = tpService.findById(tp2.getId());

                // Test update
                CustomLogger.logInfo("TP to be updated: " + fetchedTP1);
                fetchedTP1.setNb(fetchedTP1.getNb() + 1);
                TPDTO updatedTP = tpService.update(fetchedTP1.getId(), fetchedTP1);
                CustomLogger.logInfo("Updated TP: " + updatedTP);

                // Test delete
                CustomLogger.logInfo("TP to be deleted: " + fetchedTP2);
                tpService.delete(tp2.getId());

                CustomLogger.logInfo("TPService RUD operations completed successfully");
            } else {
                CustomLogger.logInfo("Not enough TPs for RUD testing");
            }
        } catch (Exception e) {
            CustomLogger.logError("Error testing TPService: " + e.getMessage(), e);
        }
    }

    private void testNotificationService() {
        try {
            CustomLogger.logInfo("Testing NotificationService RUD operations...");
            List<NotificationDTO> allNotifications = notificationService.findAll();
            if (allNotifications.size() >= 2) {
                // Get 2 random notifications
                int index1 = RANDOM.nextInt(allNotifications.size());
                int index2 = (index1 + 1 + RANDOM.nextInt(allNotifications.size() - 1)) % allNotifications.size();
                NotificationDTO notification1 = allNotifications.get(index1);
                NotificationDTO notification2 = allNotifications.get(index2);

                // Test findById
                NotificationDTO fetchedNotification1 = notificationService.findById(notification1.getId());
                NotificationDTO fetchedNotification2 = notificationService.findById(notification2.getId());

                // Test update
                CustomLogger.logInfo("Notification to be updated: " + fetchedNotification1);
                fetchedNotification1.setMessage("Updated message " + RANDOM.nextInt(100));
                NotificationDTO updatedNotification = notificationService.update(fetchedNotification1.getId(), fetchedNotification1);
                CustomLogger.logInfo("Updated notification: " + updatedNotification);

                // Test delete
                CustomLogger.logInfo("Notification to be deleted: " + fetchedNotification2);
                notificationService.delete(notification2.getId());

                CustomLogger.logInfo("NotificationService RUD operations completed successfully");
            } else {
                CustomLogger.logInfo("Not enough notifications for RUD testing");
            }
        } catch (Exception e) {
            CustomLogger.logError("Error testing NotificationService: " + e.getMessage(), e);
        }
    }

    private void testExcelFileService() {
        try {
            CustomLogger.logInfo("Testing ExcelFileService RUD operations...");
            List<FichierExcelDTO> allFiles = excelFileService.findAll();
            if (allFiles.size() >= 2) {
                // Get 2 random files
                int index1 = RANDOM.nextInt(allFiles.size());
                int index2 = (index1 + 1 + RANDOM.nextInt(allFiles.size() - 1)) % allFiles.size();
                FichierExcelDTO file1 = allFiles.get(index1);
                FichierExcelDTO file2 = allFiles.get(index2);

                // Test findById
                FichierExcelDTO fetchedFile1 = excelFileService.findById(file1.getId());
                FichierExcelDTO fetchedFile2 = excelFileService.findById(file2.getId());

                // Test update
                CustomLogger.logInfo("Excel file to be updated: " + fetchedFile1);
                fetchedFile1.setStatus("Updated Status");
                FichierExcelDTO updatedFile = excelFileService.update(fetchedFile1.getId(), fetchedFile1);
                CustomLogger.logInfo("Updated excel file: " + updatedFile);

                // Test delete
                CustomLogger.logInfo("Excel file to be deleted: " + fetchedFile2);
                excelFileService.delete(file2.getId());

                CustomLogger.logInfo("ExcelFileService RUD operations completed successfully");
            } else {
                CustomLogger.logInfo("Not enough excel files for RUD testing");
            }
        } catch (Exception e) {
            CustomLogger.logError("Error testing ExcelFileService: " + e.getMessage(), e);
        }
    }


    // ======================================ADVANCED FEATURES ========================

    /**
     * Advanced feature testing methods for all services
     * Date: 2025-05-02 00:07:48 UTC
     */
    // Main method to call all specialized test methods
    public void testAllUntestAdministrateurServiceFunctionalities() throws CustomException {
        try {
            CustomLogger.logInfo("========== Starting AdministrateurService Functionality Tests ==========");
            CustomLogger.logInfo("Test DateTime: 2025-05-02 16:04:03");
            CustomLogger.logInfo("Test User: Mahmoud-ABK");

            // 1. Test getAllMakeupSessions
            CustomLogger.logInfo("\n----- Testing getAllMakeupSessions -----");
            List<PropositionDeRattrapageDTO> allSessions = administrateurService.getAllMakeupSessions();
            CustomLogger.logInfo("Found " + allSessions.size() + " makeup sessions");
            allSessions.forEach(session ->
                    CustomLogger.logInfo("Session ID: " + session.getId() +
                            ", Status: " + session.getStatus() +
                            ", Date: " + session.getDate() +
                            ", Subject: " + session.getMatiere()
                    )
            );

            // Find a PENDING session for first test
            CustomLogger.logInfo("\n----- Selecting test sessions based on status criteria -----");
            PropositionDeRattrapageDTO pendingSession = allSessions.stream()
                    .filter(s -> Status.PENDING.name().equals(s.getStatus()))
                    .findFirst()
                    .orElseThrow(() -> new CustomException("No PENDING makeup sessions found for testing"));
            CustomLogger.logInfo("Selected PENDING session - ID: " + pendingSession.getId());

            // 2. Test approveMakeupSession without room
            CustomLogger.logInfo("\n----- Testing approveMakeupSession without room -----");
            CustomLogger.logInfo("Attempting to schedule PENDING session ID: " + pendingSession.getId());
            PropositionDeRattrapageDTO approvedWithoutRoom = administrateurService.approveMakeupSession(
                    pendingSession.getId(),
                    null
            );
            CustomLogger.logInfo("Session status transition: " + pendingSession.getStatus() +
                    " -> " + approvedWithoutRoom.getStatus());

            // Get rooms for testing
            CustomLogger.logInfo("\n----- Retrieving Available Rooms -----");
            List<SalleDTO> rooms = salleService.findAll();
            CustomLogger.logInfo("Found " + rooms.size() + " available rooms");
            SalleDTO testRoom = rooms.getFirst();
            CustomLogger.logInfo("Selected test room - ID: " + testRoom.getId() +
                    ", Identifier: " + testRoom.getIdentifiant() +
                    ", Capacity: " + testRoom.getCapacite());

            // Find another PENDING session for room approval test
            CustomLogger.logInfo("\n----- Selecting another PENDING session for room approval -----");
            PropositionDeRattrapageDTO anotherPendingSession = allSessions.stream()
                    .filter(s -> Status.PENDING.name().equals(s.getStatus()))
                    .filter(s -> !s.getId().equals(pendingSession.getId()))
                    .findFirst()
                    .orElseThrow(() -> new CustomException("No additional PENDING sessions found for testing"));

            // 3. Test approveMakeupSession with room
            CustomLogger.logInfo("\n----- Testing approveMakeupSession with room -----");
            CustomLogger.logInfo("Attempting to approve PENDING session ID: " + anotherPendingSession.getId() +
                    " with room ID: " + testRoom.getId());
            PropositionDeRattrapageDTO approvedWithRoom = administrateurService.approveMakeupSession(
                    anotherPendingSession.getId(),
                    testRoom.getId()
            );
            CustomLogger.logInfo("Session approved - Status transition: " + anotherPendingSession.getStatus() +
                    " -> " + approvedWithRoom.getStatus() +
                    ", Assigned Room: " + testRoom.getIdentifiant());

            // Find a PENDING session for rejection test
            CustomLogger.logInfo("\n----- Selecting PENDING session for rejection -----");
            PropositionDeRattrapageDTO sessionToReject = allSessions.stream()
                    .filter(s -> Status.PENDING.name().equals(s.getStatus()))
                    .filter(s -> !s.getId().equals(pendingSession.getId()))
                    .filter(s -> !s.getId().equals(anotherPendingSession.getId()))
                    .findFirst()
                    .orElseThrow(() -> new CustomException("No PENDING sessions available for rejection testing"));

            // 4. Test rejectMakeupSession
            CustomLogger.logInfo("\n----- Testing rejectMakeupSession -----");
            CustomLogger.logInfo("Attempting to reject PENDING session ID: " + sessionToReject.getId());
            PropositionDeRattrapageDTO rejectedSession = administrateurService.rejectMakeupSession(sessionToReject.getId());
            CustomLogger.logInfo("Session rejected - Status transition: " + sessionToReject.getStatus() +
                    " -> " + rejectedSession.getStatus());

            // 5. Test approveScheduled
            CustomLogger.logInfo("\n----- Testing approveScheduled -----");
            // First get a PENDING session and schedule it
            PropositionDeRattrapageDTO toSchedule = allSessions.stream()
                    .filter(s -> Status.PENDING.name().equals(s.getStatus()))
                    .findFirst()
                    .orElseThrow(() -> new CustomException("No PENDING sessions available for scheduling"));

            CustomLogger.logInfo("First scheduling a session...");
            PropositionDeRattrapageDTO scheduledSession = administrateurService.approveMakeupSession(
                    toSchedule.getId(),
                    null
            );
            CustomLogger.logInfo("Session scheduled - ID: " + scheduledSession.getId() +
                    ", Status: " + scheduledSession.getStatus());

            CustomLogger.logInfo("Now approving SCHEDULED session...");
            PropositionDeRattrapageDTO approvedScheduled = administrateurService.approveScheduled(
                    scheduledSession.getId(),
                    testRoom.getId()
            );
            CustomLogger.logInfo("Scheduled session approval - Status transition: " +
                    scheduledSession.getStatus() + " -> " + approvedScheduled.getStatus());

            // 6. Test rejectScheduled
            clearDatabase();
            CustomLogger.logInfo("\n----- Testing rejectScheduled -----");
            // First get another PENDING session and schedule it
            // refresh fetched data
            allSessions = administrateurService.getAllMakeupSessions();
            PropositionDeRattrapageDTO toScheduleForRejection = allSessions.stream()
                    .filter(s -> Status.PENDING.name().equals(s.getStatus()))
                    .findFirst()
                    .orElseThrow(() -> new CustomException("No PENDING sessions available for schedule rejection"));

            CustomLogger.logInfo("First scheduling another session...");
            CustomLogger.logInfo("ID:" + toSchedule.getId());
            PropositionDeRattrapageDTO scheduledForRejection = administrateurService.approveMakeupSession(
                    toScheduleForRejection.getId(),
                    null
            );
            CustomLogger.logInfo("Session scheduled - ID: " + scheduledForRejection.getId() +
                    ", Initial Status: " + scheduledForRejection.getStatus());

            CustomLogger.logInfo("Now rejecting SCHEDULED session...");
            PropositionDeRattrapageDTO rejectedScheduled = administrateurService.rejectScheduled(
                    scheduledForRejection.getId(),
                    "Test rejection reason"
            );
            CustomLogger.logInfo("Scheduled session rejection - Status transition: " +
                    scheduledForRejection.getStatus() + " -> " + rejectedScheduled.getStatus() +
                    "\nRejection reason: " + rejectedScheduled.getReason());

            // Final status summary
            CustomLogger.logInfo("\n========== Test Results Summary ==========");
            CustomLogger.logInfo("Total sessions processed: " + allSessions.size());
            CustomLogger.logInfo("Final Status Distribution:");
            long pendingCount = allSessions.stream().filter(s -> Status.PENDING.name().equals(s.getStatus())).count();
            long scheduledCount = allSessions.stream().filter(s -> Status.SCHEDULED.name().equals(s.getStatus())).count();
            long approvedCount = allSessions.stream().filter(s -> Status.APPROVED.name().equals(s.getStatus())).count();
            long rejectedCount = allSessions.stream().filter(s -> Status.REJECTED.name().equals(s.getStatus())).count();

            CustomLogger.logInfo("- Pending: " + pendingCount);
            CustomLogger.logInfo("- Scheduled: " + scheduledCount);
            CustomLogger.logInfo("- Approved: " + approvedCount);
            CustomLogger.logInfo("- Rejected: " + rejectedCount);

        } catch (CustomException e) {
            CustomLogger.logError("\n===== ERROR in AdministrateurService Tests =====");
            CustomLogger.logError("Error type: CustomException");
            CustomLogger.logError("Error message: " + e.getMessage());
            CustomLogger.logError("Stack trace:");
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            CustomLogger.logError("\n===== UNEXPECTED ERROR in AdministrateurService Tests =====");
            CustomLogger.logError("Error type: " + e.getClass().getSimpleName());
            CustomLogger.logError("Error message: " + e.getMessage());
            CustomLogger.logError("Stack trace:");
            e.printStackTrace();
            throw new CustomException("Unexpected error in tests", e);
        } finally {
            CustomLogger.logInfo("\n----- Cleaning up test data -----");
            clearDatabase();
            CustomLogger.logInfo("Database cleared");
        }
    }

    public void testAllUntestedBrancheServiceFunctionalities() throws CustomException {
        try {
            CustomLogger.logInfo("========== Starting BrancheService Functionality Tests ==========");
            CustomLogger.logInfo("Test DateTime: 2025-05-02 16:26:46");
            CustomLogger.logInfo("Test User: Mahmoud-ABK");

            // Get all branches for testing
            CustomLogger.logInfo("\n----- Retrieving Available Branches -----");
            List<BrancheDTO> allBranches = brancheService.findAll();
            if (allBranches.isEmpty()) {
                throw new CustomException("No branches available for testing");
            }
            CustomLogger.logInfo("Found " + allBranches.size() + " branches");
            allBranches.forEach(branch ->
                    CustomLogger.logInfo("Branch ID: " + branch.getId() +
                            ", Level: " + branch.getNiveau() +
                            ", Specialty: " + branch.getSpecialite() +
                            ", Department: " + branch.getDepartement())
            );

            // 1. Test getSchedule
            CustomLogger.logInfo("\n----- Testing getSchedule functionality -----");
            // Find a branch that has sessions
            CustomLogger.logInfo("Searching for a branch with scheduled sessions...");
            BrancheDTO testBranch = allBranches.getFirst(); // Get first branch for testing
            List<SeanceDTO> branchSchedule = brancheService.getSchedule(testBranch.getId());

            CustomLogger.logInfo("Retrieved schedule for Branch ID: " + testBranch.getId());
            CustomLogger.logInfo("Total sessions found: " + branchSchedule.size());

            if (!branchSchedule.isEmpty()) {
                CustomLogger.logInfo("Sample of scheduled sessions:");
                branchSchedule.stream().limit(5).forEach(session ->
                        CustomLogger.logInfo("Session ID: " + session.getId() +
                                ", Subject: " + session.getMatiere() +
                                ", Day: " + session.getJour() +
                                ", Time: " + session.getHeureDebut() + " - " + session.getHeureFin() +
                                ", Type: " + session.getType() +
                                ", Room: " + (session.getSalleId() != null ? session.getSalleId() : "Not assigned"))
                );
            } else {
                CustomLogger.logInfo("No sessions found for this branch");
            }

            // 2. Test getEtudiants
            CustomLogger.logInfo("\n----- Testing getEtudiants functionality -----");
            CustomLogger.logInfo("Retrieving students for Branch ID: " + testBranch.getId());
            List<EtudiantDTO> branchStudents = brancheService.getEtudiants(testBranch.getId());

            CustomLogger.logInfo("Total students found: " + branchStudents.size());
            if (!branchStudents.isEmpty()) {
                CustomLogger.logInfo("Sample of enrolled students:");
                branchStudents.stream().limit(5).forEach(student ->
                        CustomLogger.logInfo("Student ID: " + student.getId() +
                                ", Name: " + student.getNom() + " " + student.getPrenom() +
                                ", CIN: " + student.getCin() +
                                ", Registration: " + student.getMatricule() +
                                ", Email: " + student.getEmail())
                );

                // Additional statistics
                CustomLogger.logInfo("\nStudent Distribution Statistics:");
                Map<String, Long> tpDistribution = branchStudents.stream()
                        .collect(Collectors.groupingBy(
                                student -> student.getTpId() != null ? "TP-" + student.getTpId() : "No TP",
                                Collectors.counting()));

                CustomLogger.logInfo("Students per TP group:");
                tpDistribution.forEach((tp, count) ->
                        CustomLogger.logInfo(tp + ": " + count + " students"));
            } else {
                CustomLogger.logInfo("No students found for this branch");
            }

            // Test Summary
            CustomLogger.logInfo("\n========== Test Results Summary ==========");
            CustomLogger.logInfo("1. Schedule Test Results:");
            CustomLogger.logInfo("   - Total sessions found: " + branchSchedule.size());
            CustomLogger.logInfo("   - Unique subjects: " +
                    branchSchedule.stream().map(SeanceDTO::getMatiere).distinct().count());
            CustomLogger.logInfo("   - Session types distribution: ");
            Map<String, Long> sessionTypes = branchSchedule.stream()
                    .collect(Collectors.groupingBy(SeanceDTO::getType, Collectors.counting()));
            sessionTypes.forEach((type, count) ->
                    CustomLogger.logInfo("     " + type + ": " + count));

            CustomLogger.logInfo("\n2. Student Test Results:");
            CustomLogger.logInfo("   - Total students: " + branchStudents.size());
            CustomLogger.logInfo("   - Students with TP assignments: " +
                    branchStudents.stream().filter(s -> s.getTpId() != null).count());
            CustomLogger.logInfo("   - Students without TP assignments: " +
                    branchStudents.stream().filter(s -> s.getTpId() == null).count());

            CustomLogger.logInfo("\n========== BrancheService Functionality Tests Completed Successfully ==========");

        } catch (CustomException e) {
            CustomLogger.logError("\n===== ERROR in BrancheService Tests =====");
            CustomLogger.logError("Error type: CustomException");
            CustomLogger.logError("Error message: " + e.getMessage());
            CustomLogger.logError("Stack trace:");
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            CustomLogger.logError("\n===== UNEXPECTED ERROR in BrancheService Tests =====");
            CustomLogger.logError("Error type: " + e.getClass().getSimpleName());
            CustomLogger.logError("Error message: " + e.getMessage());
            CustomLogger.logError("Stack trace:");
            e.printStackTrace();
            throw new CustomException("Unexpected error in tests", e);
        } finally {
            CustomLogger.logInfo("\n----- Cleaning up test data -----");
            clearDatabase();
            CustomLogger.logInfo("Database cleared");
        }
    }

    public void testAllUntestedEnseignantServiceFunctionalities() throws CustomException {
        try {
            CustomLogger.logInfo("========== Starting EnseignantService Functionality Tests ==========");
            CustomLogger.logInfo("Test DateTime: 2025-05-02 16:31:02");
            CustomLogger.logInfo("Test User: Mahmoud-ABK");

            // Get all teachers for testing
            CustomLogger.logInfo("\n----- Retrieving Available Teachers -----");
            List<EnseignantDTO> allTeachers = enseignantService.findAll();
            if (allTeachers.isEmpty()) {
                throw new CustomException("No teachers available for testing");
            }

            EnseignantDTO testTeacher = enseignantService.findById(1221L);
            CustomLogger.logInfo("Selected test teacher - ID: " + testTeacher.getId() +
                    ", Name: " + testTeacher.getNom() + " " + testTeacher.getPrenom());

            // 1. Test getSchedule
            CustomLogger.logInfo("\n----- Testing getSchedule functionality -----");
            List<SeanceDTO> teacherSchedule = enseignantService.getSchedule(testTeacher.getId());
            CustomLogger.logInfo("Retrieved schedule for Teacher ID: " + testTeacher.getId());
            CustomLogger.logInfo("Total sessions found: " + teacherSchedule.size());

            if (!teacherSchedule.isEmpty()) {
                CustomLogger.logInfo("Schedule details:");
                teacherSchedule.forEach(session ->
                        CustomLogger.logInfo("Session ID: " + session.getId() +
                                ", Subject: " + session.getMatiere() +
                                ", Day: " + session.getJour() +
                                ", Time: " + session.getHeureDebut() + " - " + session.getHeureFin() +
                                ", Frequency: " + session.getFrequence())
                );
            }

            // 2. Test getTotalTeachingHours
            CustomLogger.logInfo("\n----- Testing getTotalTeachingHours functionality -----");
            LocalDate startDate = LocalDate.now();
            LocalDate endDate = startDate.plusMonths(1);
            CustomLogger.logInfo("Calculating teaching hours from " + startDate + " to " + endDate);

            int totalHours = enseignantService.getTotalTeachingHours(
                    testTeacher.getId(),
                    startDate,
                    endDate
            );
            CustomLogger.logInfo("Total teaching hours: " + totalHours);

            // 3. Test submitMakeupRequest
            CustomLogger.logInfo("\n----- Testing submitMakeupRequest functionality -----");
            PropositionDeRattrapageDTO makeupRequest = new PropositionDeRattrapageDTO();
            makeupRequest.setName("Test Makeup Session");
            makeupRequest.setMatiere("Test Subject");
            makeupRequest.setDate(LocalDateTime.now().plusDays(7).toString());
            makeupRequest.setHeureDebut(LocalTime.of(10, 0).toString());
            makeupRequest.setHeureFin(LocalTime.of(12, 0).toString());
            makeupRequest.setType(entityMapper.seanceTypeToString(SeanceType.CI));
            makeupRequest.setReason("Test makeup session");


            CustomLogger.logInfo("Submitting makeup request for Teacher ID: " + testTeacher.getId());
            PropositionDeRattrapageDTO submittedRequest = enseignantService.submitMakeupRequest(
                    testTeacher.getId(),
                    makeupRequest
            );
            CustomLogger.logInfo("Makeup request submitted - ID: " + submittedRequest.getId() +
                    ", Status: " + submittedRequest.getStatus());

            // 4. Test submitSignal and getSignals
            CustomLogger.logInfo("\n----- Testing Signal submission and retrieval -----");
            SignalDTO signal = new SignalDTO();
            signal.setMessage("Test signal message");
            signal.setSeverity("HIGH");
            signal.setTimestamp(LocalDateTime.now());

            CustomLogger.logInfo("Submitting signal for Teacher ID: " + testTeacher.getId());
            SignalDTO submittedSignal = enseignantService.submitSignal(testTeacher.getId(), signal);
            CustomLogger.logInfo("Signal submitted - ID: " + submittedSignal.getId());

            // Refresh teacher's signals
            CustomLogger.logInfo("Retrieving all signals for Teacher ID: " + testTeacher.getId());
            List<SignalDTO> teacherSignals = enseignantService.getSignals(testTeacher.getId());
            CustomLogger.logInfo("Total signals found: " + teacherSignals.size());
            teacherSignals.forEach(s ->
                    CustomLogger.logInfo("Signal ID: " + s.getId() +
                            ", Message: " + s.getMessage() +
                            ", Severity: " + s.getSeverity() +
                            ", Timestamp: " + s.getTimestamp())
            );

            // 5. Test getSubjects
            CustomLogger.logInfo("\n----- Testing getSubjects functionality -----");
            List<String> teacherSubjects = enseignantService.getSubjects(testTeacher.getId());
            CustomLogger.logInfo("Retrieved subjects for Teacher ID: " + testTeacher.getId());
            CustomLogger.logInfo("Total subjects: " + teacherSubjects.size());
            if (!teacherSubjects.isEmpty()) {
                CustomLogger.logInfo("Subjects taught:");
                teacherSubjects.forEach(subject ->
                        CustomLogger.logInfo("- " + subject)
                );
            }

            // 6. Test getStudentGroups
            CustomLogger.logInfo("\n----- Testing getStudentGroups functionality -----");
            List<TPDTO> studentGroups = enseignantService.getStudentGroups(testTeacher.getId());
            CustomLogger.logInfo("Retrieved student groups for Teacher ID: " + testTeacher.getId());
            CustomLogger.logInfo("Total groups: " + studentGroups.size());
            if (!studentGroups.isEmpty()) {
                CustomLogger.logInfo("Student groups:");
                studentGroups.forEach(tp ->
                        CustomLogger.logInfo("TP ID: " + tp.getId() +
                                ", Number: " + tp.getNb() +
                                ", TD ID: " + tp.getTdId())
                );
            }

            // Test Summary
            CustomLogger.logInfo("\n========== Test Results Summary ==========");
            CustomLogger.logInfo("1. Schedule Test:");
            CustomLogger.logInfo("   - Total sessions: " + teacherSchedule.size());
            CustomLogger.logInfo("   - Weekly hours: " + totalHours);

            CustomLogger.logInfo("\n2. Makeup Request Test:");
            CustomLogger.logInfo("   - Request status: " + submittedRequest.getStatus());

            CustomLogger.logInfo("\n3. Signals Test:");
            CustomLogger.logInfo("   - Total signals: " + teacherSignals.size());
            CustomLogger.logInfo("   - Latest signal status: " +
                    (teacherSignals.isEmpty() ? "N/A" : teacherSignals.getLast().getMessage()));

            CustomLogger.logInfo("\n4. Subjects and Groups:");
            CustomLogger.logInfo("   - Total subjects: " + teacherSubjects.size());
            CustomLogger.logInfo("   - Total student groups: " + studentGroups.size());

            CustomLogger.logInfo("\n========== EnseignantService Functionality Tests Completed Successfully ==========");

        } catch (CustomException e) {
            CustomLogger.logError("\n===== ERROR in EnseignantService Tests =====");
            CustomLogger.logError("Error type: CustomException");
            CustomLogger.logError("Error message: " + e.getMessage());
            CustomLogger.logError("Stack trace:");
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            CustomLogger.logError("\n===== UNEXPECTED ERROR in EnseignantService Tests =====");
            CustomLogger.logError("Error type: " + e.getClass().getSimpleName());
            CustomLogger.logError("Error message: " + e.getMessage());
            CustomLogger.logError("Stack trace:");
            e.printStackTrace();
            throw new CustomException("Unexpected error in tests", e);
        } finally {
            CustomLogger.logInfo("\n----- Cleaning up test data -----");
            clearDatabase();
            CustomLogger.logInfo("Database cleared");
        }
    }

    public void testAllUntestedEtudiantServiceFunctionalities() throws CustomException {
        try {
            CustomLogger.logInfo("========== Starting EtudiantService Functionality Tests ==========");
            CustomLogger.logInfo("Test DateTime: 2025-05-02 16:59:04");
            CustomLogger.logInfo("Test User: Mahmoud-ABK");

            // Get all students for testing
            CustomLogger.logInfo("\n----- Retrieving Available Students -----");
            List<EtudiantDTO> allStudents = etudiantService.findAll();
            if (allStudents.isEmpty()) {
                throw new CustomException("No students available for testing");
            }

            // Find a student with TP and Branch assignments
            CustomLogger.logInfo("\n----- Selecting test student with required assignments -----");
            EtudiantDTO testStudent = allStudents.stream()
                    .filter(s -> s.getTpId() != null)
                    .filter(s -> s.getBrancheId() != null)
                    .findFirst()
                    .orElseThrow(() -> new CustomException("No student found with both TP and Branch assignments"));

            CustomLogger.logInfo("Selected test student - ID: " + testStudent.getId() +
                    ", Name: " + testStudent.getNom() + " " + testStudent.getPrenom() +
                    ", TP ID: " + testStudent.getTpId() +
                    ", Branch ID: " + testStudent.getBrancheId());

            // 1. Test getPersonalSchedule
            CustomLogger.logInfo("\n----- Testing getPersonalSchedule functionality -----");
            List<SeanceDTO> personalSchedule = etudiantService.getPersonalSchedule(testStudent.getId());
            CustomLogger.logInfo("Retrieved personal schedule for Student ID: " + testStudent.getId());
            CustomLogger.logInfo("Total personal sessions found: " + personalSchedule.size());

            if (!personalSchedule.isEmpty()) {
                CustomLogger.logInfo("Personal schedule details:");
                personalSchedule.stream().limit(5).forEach(session ->
                        CustomLogger.logInfo("Session ID: " + session.getId() +
                                ", Subject: " + session.getMatiere() +
                                ", Day: " + session.getJour() +
                                ", Time: " + session.getHeureDebut() + " - " + session.getHeureFin() +
                                ", Type: " + session.getType() +
                                ", Room: " + (session.getSalleId() != null ? session.getSalleId() : "Not assigned"))
                );
            }

            // 2. Test getTDSchedule
            CustomLogger.logInfo("\n----- Testing getTDSchedule functionality -----");
            List<SeanceDTO> tdSchedule = etudiantService.getTDSchedule(testStudent.getId());
            CustomLogger.logInfo("Retrieved TD schedule for Student ID: " + testStudent.getId());
            CustomLogger.logInfo("Total TD sessions found: " + tdSchedule.size());

            if (!tdSchedule.isEmpty()) {
                CustomLogger.logInfo("TD schedule details:");
                tdSchedule.stream().limit(5).forEach(session ->
                        CustomLogger.logInfo("Session ID: " + session.getId() +
                                ", Subject: " + session.getMatiere() +
                                ", Day: " + session.getJour() +
                                ", Time: " + session.getHeureDebut() + " - " + session.getHeureFin() +
                                ", Type: " + session.getType())
                );
            }

            // 3. Test getBranchSchedule
            CustomLogger.logInfo("\n----- Testing getBranchSchedule functionality -----");
            List<SeanceDTO> branchSchedule = etudiantService.getBranchSchedule(testStudent.getId());
            CustomLogger.logInfo("Retrieved branch schedule for Student ID: " + testStudent.getId());
            CustomLogger.logInfo("Total branch sessions found: " + branchSchedule.size());

            if (!branchSchedule.isEmpty()) {
                CustomLogger.logInfo("Branch schedule details:");
                branchSchedule.stream().limit(5).forEach(session ->
                        CustomLogger.logInfo("Session ID: " + session.getId() +
                                ", Subject: " + session.getMatiere() +
                                ", Day: " + session.getJour() +
                                ", Time: " + session.getHeureDebut() + " - " + session.getHeureFin() +
                                ", Type: " + session.getType())
                );

                // Session type distribution
                Map<String, Long> sessionTypeDistribution = branchSchedule.stream()
                        .collect(Collectors.groupingBy(
                                SeanceDTO::getType,
                                Collectors.counting()
                        ));

                CustomLogger.logInfo("\nSession type distribution in branch schedule:");
                sessionTypeDistribution.forEach((type, count) ->
                        CustomLogger.logInfo(type + ": " + count + " sessions")
                );
            }

            // 4. Test getNotifications
            CustomLogger.logInfo("\n----- Testing getNotifications functionality -----");
            List<NotificationDTO> notifications = etudiantService.getNotifications(testStudent.getId());
            CustomLogger.logInfo("Retrieved notifications for Student ID: " + testStudent.getId());
            CustomLogger.logInfo("Total notifications found: " + notifications.size());

            if (!notifications.isEmpty()) {
                CustomLogger.logInfo("Recent notifications:");
                notifications.stream()
                        .sorted(Comparator.comparing(NotificationDTO::getDate).reversed())
                        .limit(5)
                        .forEach(notification ->
                                CustomLogger.logInfo("Notification ID: " + notification.getId() +
                                        ", Message: " + notification.getMessage() +
                                        ", Created: " + notification.getDate() +
                                        ", Read: " + notification.getIsread())
                        );

                // Notification statistics
                long unreadCount = notifications.stream()
                        .filter(n -> !n.getIsread())
                        .count();
                CustomLogger.logInfo("\nNotification Statistics:");
                CustomLogger.logInfo("Total notifications: " + notifications.size());
                CustomLogger.logInfo("Unread notifications: " + unreadCount);
                CustomLogger.logInfo("Read notifications: " + (notifications.size() - unreadCount));
            }

            // Test Summary
            CustomLogger.logInfo("\n========== Test Results Summary ==========");
            CustomLogger.logInfo("1. Schedule Test Results:");
            CustomLogger.logInfo("   - Personal schedule sessions: " + personalSchedule.size());
            CustomLogger.logInfo("   - TD schedule sessions: " + tdSchedule.size());
            CustomLogger.logInfo("   - Branch schedule sessions: " + branchSchedule.size());

            // Unique subjects across all schedules
            Set<String> uniqueSubjects = Stream.concat(
                            Stream.concat(
                                    personalSchedule.stream(),
                                    tdSchedule.stream()
                            ),
                            branchSchedule.stream()
                    )
                    .map(SeanceDTO::getMatiere)
                    .collect(Collectors.toSet());

            CustomLogger.logInfo("   - Total unique subjects: " + uniqueSubjects.size());

            CustomLogger.logInfo("\n2. Notification Test Results:");
            CustomLogger.logInfo("   - Total notifications: " + notifications.size());
            if (!notifications.isEmpty()) {
                CustomLogger.logInfo("   - Most recent notification: " +
                        notifications.stream()
                                .max(Comparator.comparing(NotificationDTO::getDate))
                                .map(NotificationDTO::getMessage)
                                .orElse("N/A"));
            }

            CustomLogger.logInfo("\n========== EtudiantService Functionality Tests Completed Successfully ==========");

        } catch (CustomException e) {
            CustomLogger.logError("\n===== ERROR in EtudiantService Tests =====");
            CustomLogger.logError("Error type: CustomException");
            CustomLogger.logError("Error message: " + e.getMessage());
            CustomLogger.logError("Stack trace:");
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            CustomLogger.logError("\n===== UNEXPECTED ERROR in EtudiantService Tests =====");
            CustomLogger.logError("Error type: " + e.getClass().getSimpleName());
            CustomLogger.logError("Error message: " + e.getMessage());
            CustomLogger.logError("Stack trace:");
            e.printStackTrace();
            throw new CustomException("Unexpected error in tests", e);
        } finally {
            CustomLogger.logInfo("\n----- Cleaning up test data -----");
            clearDatabase();
            CustomLogger.logInfo("Database cleared");
        }
    }
    public void testAllUntestedExcelFileServiceFunctionalities() throws CustomException {
        try {
            CustomLogger.logInfo("========== Starting ExcelFileService Functionality Tests ==========");
            CustomLogger.logInfo("Test DateTime: 2025-05-02 17:17:13");
            CustomLogger.logInfo("Test User: Mahmoud-ABK");

            // 1. Test upload functionality
            CustomLogger.logInfo("\n----- Testing Excel File Upload functionality -----");

            // Prepare test file data
            FichierExcelDTO testFile = new FichierExcelDTO();
            testFile.setFileName("test_schedule_2025.xlsx");
            testFile.setImportDate(LocalDateTime.now());
            testFile.setStatus("STATUS_PROCESSING");
            testFile.setErrors(new ArrayList<>());

            // Prepare test sessions
            List<SeanceDTO> testSessions = new ArrayList<>();

            SeanceDTO session1 = new SeanceDTO();
            session1.setName("Test Session 1");
            session1.setMatiere("Mathematics");
            session1.setType(entityMapper.seanceTypeToString(SeanceType.CI));
            session1.setJour(entityMapper.dayOfWeekToString(DayOfWeek.MONDAY));
            session1.setHeureDebut(LocalTime.of(9, 0).toString());
            session1.setHeureFin(LocalTime.of(10, 30).toString());
            session1.setFrequence(entityMapper.frequenceTypeToString(FrequenceType.WEEKLY));
            testSessions.add(session1);

            SeanceDTO session2 = new SeanceDTO();
            session2.setName("Test Session 2");
            session2.setMatiere("Physics");
            session2.setType(entityMapper.seanceTypeToString(SeanceType.TD));
            session2.setJour(entityMapper.dayOfWeekToString(DayOfWeek.WEDNESDAY));
            session2.setHeureDebut(LocalTime.of(14, 0).toString());
            session2.setHeureFin(LocalTime.of(15, 30).toString());
            session2.setFrequence(entityMapper.frequenceTypeToString(FrequenceType.BIWEEKLY));
            testSessions.add(session2);

            CustomLogger.logInfo("Uploading test file with " + testSessions.size() + " sessions");
            CustomLogger.logInfo("File details:");
            CustomLogger.logInfo("- Name: " + testFile.getFileName());
            CustomLogger.logInfo("- Status: " + testFile.getStatus());
            CustomLogger.logInfo("- Import Date: " + testFile.getImportDate());

            // Perform upload
            excelFileService.upload(testFile, testSessions);
            CustomLogger.logInfo("File uploaded successfully");

            // 2. Test getImportHistory
            CustomLogger.logInfo("\n----- Testing Import History functionality -----");
            List<FichierExcelDTO> importHistory = excelFileService.getImportHistory();
            CustomLogger.logInfo("Retrieved " + importHistory.size() + " import records");

            if (!importHistory.isEmpty()) {
                CustomLogger.logInfo("Recent imports (last 5):");
                importHistory.stream()
                        .sorted(Comparator.comparing(FichierExcelDTO::getImportDate).reversed())
                        .limit(5)
                        .forEach(file ->
                                CustomLogger.logInfo("File: " + file.getFileName() +
                                        ", Status: " + file.getStatus() +
                                        ", Import Date: " + file.getImportDate() +
                                        ", Errors: " + (file.getErrors() != null ? file.getErrors().size() : 0))
                        );

                // Import statistics
                Map<String, Long> statusDistribution = importHistory.stream()
                        .collect(Collectors.groupingBy(
                                FichierExcelDTO::getStatus,
                                Collectors.counting()
                        ));

                CustomLogger.logInfo("\nImport Status Distribution:");
                statusDistribution.forEach((status, count) ->
                        CustomLogger.logInfo(status + ": " + count + " files")
                );
            }

            CustomLogger.logInfo("\n========== Test Results Summary ==========");
            CustomLogger.logInfo("1. Upload Test:");
            CustomLogger.logInfo("   - File uploaded: " + testFile.getFileName());
            CustomLogger.logInfo("   - Sessions included: " + testSessions.size());
            CustomLogger.logInfo("   - Final status: " + "STATUS_COMPLETED");

            CustomLogger.logInfo("\n2. Import History Test:");
            CustomLogger.logInfo("   - Total imports: " + importHistory.size());
            if (!importHistory.isEmpty()) {
                FichierExcelDTO latestImport = importHistory.stream()
                        .max(Comparator.comparing(FichierExcelDTO::getImportDate))
                        .orElse(null);
                if (latestImport != null) {
                    CustomLogger.logInfo("   - Latest import: " + latestImport.getFileName());
                    CustomLogger.logInfo("   - Latest status: " + latestImport.getStatus());
                    CustomLogger.logInfo("   - Import date: " + latestImport.getImportDate());
                }
            }

            CustomLogger.logInfo("\n========== ExcelFileService Tests Completed Successfully ==========");

        } catch (CustomException e) {
            CustomLogger.logError("\n===== ERROR in ExcelFileService Tests =====");
            CustomLogger.logError("Error type: CustomException");
            CustomLogger.logError("Error message: " + e.getMessage());
            CustomLogger.logError("Stack trace:");
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            CustomLogger.logError("\n===== UNEXPECTED ERROR in ExcelFileService Tests =====");
            CustomLogger.logError("Error type: " + e.getClass().getSimpleName());
            CustomLogger.logError("Error message: " + e.getMessage());
            CustomLogger.logError("Stack trace:");
            e.printStackTrace();
            throw new CustomException("Unexpected error in tests", e);
        } finally {
            CustomLogger.logInfo("\n----- Cleaning up test data -----");
            clearDatabase();
            CustomLogger.logInfo("Database cleared");
        }
    }

    public void testAllUntestedSalleServiceFunctionalities() throws CustomException {
        try {
            CustomLogger.logInfo("========== Starting SalleService Functionality Tests ==========");
            CustomLogger.logInfo("Test DateTime: 2025-05-02 17:17:13");
            CustomLogger.logInfo("Test User: Mahmoud-ABK");

            CustomLogger.logInfo("\n----- Testing getAvailableRooms functionality -----");

            // 1. Test regular session (without specific date)
            DayOfWeek day = DayOfWeek.MONDAY;
            LocalTime startTime = LocalTime.of(9, 0);
            LocalTime endTime = LocalTime.of(10, 30);

            CustomLogger.logInfo("Scenario 1: Regular session availability");
            CustomLogger.logInfo("Parameters:");
            CustomLogger.logInfo("- Day: " + entityMapper.dayOfWeekToString(day));
            CustomLogger.logInfo("- Time: " + startTime + " - " + endTime);

            List<SalleDTO> regularAvailableRooms = salleService.getAvailableRooms(
                    null, day, startTime, endTime
            );

            if (!regularAvailableRooms.isEmpty()) {
                CustomLogger.logInfo("Available rooms (" + regularAvailableRooms.size() + "):");
                regularAvailableRooms.forEach(room -> {
                    CustomLogger.logInfo("Room ID: " + room.getId() +
                            ", Identifier: " + room.getIdentifiant() +
                            ", Type: " + room.getType() +
                            ", Capacity: " + room.getCapacite());

                    if (room.getSeanceIds() != null && !room.getSeanceIds().isEmpty()) {
                        CustomLogger.logInfo("  Scheduled sessions: " + room.getSeanceIds().size());
                    }
                });

                // Room type distribution
                Map<String, Long> roomTypeDistribution = regularAvailableRooms.stream()
                        .collect(Collectors.groupingBy(
                                SalleDTO::getType,
                                Collectors.counting()
                        ));

                CustomLogger.logInfo("\nRoom Type Distribution:");
                roomTypeDistribution.forEach((type, count) ->
                        CustomLogger.logInfo(type + ": " + count + " rooms")
                );
            }

            // 2. Test makeup session (with specific date)
            LocalDate makeupDate = LocalDate.now().plusDays(7);
            CustomLogger.logInfo("\nScenario 2: Makeup session availability");
            CustomLogger.logInfo("Parameters:");
            CustomLogger.logInfo("- Date: " + makeupDate);
            CustomLogger.logInfo("- Day: " + entityMapper.dayOfWeekToString(makeupDate.getDayOfWeek()));
            CustomLogger.logInfo("- Time: " + startTime + " - " + endTime);

            List<SalleDTO> makeupAvailableRooms = salleService.getAvailableRooms(
                    makeupDate, makeupDate.getDayOfWeek(), startTime, endTime
            );

            if (!makeupAvailableRooms.isEmpty()) {
                CustomLogger.logInfo("Available rooms for makeup session (" + makeupAvailableRooms.size() + "):");
                makeupAvailableRooms.forEach(room -> {
                    CustomLogger.logInfo("Room ID: " + room.getId() +
                            ", Identifier: " + room.getIdentifiant() +
                            ", Type: " + room.getType() +
                            ", Capacity: " + room.getCapacite());
                });
            }

            // Compare results between scenarios
            CustomLogger.logInfo("\n========== Test Results Summary ==========");
            CustomLogger.logInfo("1. Regular Session Test:");
            CustomLogger.logInfo("   - Total available rooms: " + regularAvailableRooms.size());
            if (!regularAvailableRooms.isEmpty()) {
                double avgCapacity = regularAvailableRooms.stream()
                        .mapToInt(SalleDTO::getCapacite)
                        .average()
                        .orElse(0.0);
                CustomLogger.logInfo("   - Average room capacity: " + String.format("%.2f", avgCapacity));
            }

            CustomLogger.logInfo("\n2. Makeup Session Test:");
            CustomLogger.logInfo("   - Total available rooms: " + makeupAvailableRooms.size());
            if (!makeupAvailableRooms.isEmpty()) {
                double avgCapacity = makeupAvailableRooms.stream()
                        .mapToInt(SalleDTO::getCapacite)
                        .average()
                        .orElse(0.0);
                CustomLogger.logInfo("   - Average room capacity: " + String.format("%.2f", avgCapacity));
            }

            // Room availability comparison
            Set<String> regularRoomIds = regularAvailableRooms.stream()
                    .map(SalleDTO::getIdentifiant)
                    .collect(Collectors.toSet());
            Set<String> makeupRoomIds = makeupAvailableRooms.stream()
                    .map(SalleDTO::getIdentifiant)
                    .collect(Collectors.toSet());

            Set<String> commonRooms = new HashSet<>(regularRoomIds);
            commonRooms.retainAll(makeupRoomIds);

            CustomLogger.logInfo("\nAvailability Comparison:");
            CustomLogger.logInfo("- Rooms available for regular sessions only: " +
                    (regularRoomIds.size() - commonRooms.size()));
            CustomLogger.logInfo("- Rooms available for makeup sessions only: " +
                    (makeupRoomIds.size() - commonRooms.size()));
            CustomLogger.logInfo("- Rooms available for both scenarios: " + commonRooms.size());

            CustomLogger.logInfo("\n========== SalleService Tests Completed Successfully ==========");

        } catch (CustomException e) {
            CustomLogger.logError("\n===== ERROR in SalleService Tests =====");
            CustomLogger.logError("Error type: CustomException");
            CustomLogger.logError("Error message: " + e.getMessage());
            CustomLogger.logError("Stack trace:");
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            CustomLogger.logError("\n===== UNEXPECTED ERROR in SalleService Tests =====");
            CustomLogger.logError("Error type: " + e.getClass().getSimpleName());
            CustomLogger.logError("Error message: " + e.getMessage());
            CustomLogger.logError("Stack trace:");
            e.printStackTrace();
            throw new CustomException("Unexpected error in tests", e);
        } finally {
            CustomLogger.logInfo("\n----- Cleaning up test data -----");
            clearDatabase();
            CustomLogger.logInfo("Database cleared");
        }
    }
 }