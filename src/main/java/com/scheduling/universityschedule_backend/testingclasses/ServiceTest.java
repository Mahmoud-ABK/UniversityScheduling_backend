package com.scheduling.universityschedule_backend.testingclasses;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.model.FrequenceType;
import com.scheduling.universityschedule_backend.model.Status;
import com.scheduling.universityschedule_backend.model.SeanceType;
import com.scheduling.universityschedule_backend.service.*;
import com.scheduling.universityschedule_backend.util.CustomLogger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
import java.util.*;
import java.util.function.Function;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public ServiceTest(AdministrateurService administrateurService, BrancheService brancheService, EtudiantService etudiantService, ExcelFileService excelFileService, SalleService salleService, SeanceService seanceService, EnseignantService enseignantService, TDService tdService, TPService tpService, NotificationService notificationService) {
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
            enseignant.setHeures((int)(Math.random() * 40) + 1);
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
            branche.setNbTD((int)(Math.random() * 5) + 1);
            branche.setDepartement(Math.random() < 0.33 ? "Technologie" : "informatique");
            branches.add(brancheService.create(branche));
        }
        CustomLogger.logInfo("Created Branches: " + branches.size());

        // 4. Create TDs
        List<TDDTO> tds = new ArrayList<>();
        for (int i = 0; i < Math.max(2, (sampleSize * 3) / 20); i++) {
            TDDTO td = new TDDTO();
            td.setNb((int)(Math.random() * 5) + 1);
            td.setNbTP((int)(Math.random() * 3) + 1);
            td.setBrancheId(branches.get(RANDOM.nextInt(branches.size())).getId());
            tds.add(tdService.create(td));
        }
        CustomLogger.logInfo("Created TDs: " + tds.size());

        // 5. Create TPs
        List<TPDTO> tps = new ArrayList<>();
        for (int i = 0; i < Math.max(3, sampleSize / 3); i++) {
            TPDTO tp = new TPDTO();
            tp.setNb((int)(Math.random() * 3) + 1);
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
            salle.setCapacite((int)(Math.random() * 150) + 50);
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
        int signalcount=0;
        for (int i = 0; i < Math.max(1, sampleSize / 3); i++) {
            try {
                SignalDTO signal = new SignalDTO();
                signal.setMessage("Signal Issue " + i);
                signal.setSeverity(Math.random() > 0.5 ? "High" : "Low");
                signal.setTimestamp(LocalDateTime.parse(LocalDateTime.now().toString()));

                Long teacherId = enseignants.get(RANDOM.nextInt(enseignants.size()-1)).getId();
                signal.setEnseignantId(String.valueOf(teacherId));
                CustomLogger.logInfo(signal.toString());

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
        Random RANDOM = new Random();

        // Testing AdministrateurService
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
                fetchedAdmin1.setNom("Updated Name");
                AdministrateurDTO updatedAdmin = administrateurService.update(fetchedAdmin1.getId(), fetchedAdmin1);

                // Test delete
                administrateurService.delete(admin2.getId());

                CustomLogger.logInfo("AdministrateurService RUD operations completed successfully");
            } else {
                CustomLogger.logInfo("Not enough administrators for RUD testing");
            }
        } catch (Exception e) {
            CustomLogger.logError("Error testing AdministrateurService: " + e.getMessage(), e);
        }

        // Testing BrancheService
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
                fetchedBranche1.setDepartement("Updated Department");
                BrancheDTO updatedBranche = brancheService.update(fetchedBranche1.getId(), fetchedBranche1);

                // Test delete
                brancheService.delete(branche2.getId());

                CustomLogger.logInfo("BrancheService RUD operations completed successfully");
            } else {
                CustomLogger.logInfo("Not enough branches for RUD testing");
            }
        } catch (Exception e) {
            CustomLogger.logError("Error testing BrancheService: " + e.getMessage(), e);
        }

        // Testing EnseignantService
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
                fetchedTeacher1.setHeures(fetchedTeacher1.getHeures() + 5);
                EnseignantDTO updatedTeacher = enseignantService.update(fetchedTeacher1.getId(), fetchedTeacher1);

                // Test delete
                enseignantService.delete(teacher2.getId());

                CustomLogger.logInfo("EnseignantService RUD operations completed successfully");
            } else {
                CustomLogger.logInfo("Not enough teachers for RUD testing");
            }
        } catch (Exception e) {
            CustomLogger.logError("Error testing EnseignantService: " + e.getMessage(), e);
        }

        // Testing EtudiantService
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
                fetchedStudent1.setEmail("updated.email" + RANDOM.nextInt(100) + "@example.com");
                EtudiantDTO updatedStudent = etudiantService.update(fetchedStudent1.getId(), fetchedStudent1);

                // Test delete
                etudiantService.delete(student2.getId());

                CustomLogger.logInfo("EtudiantService RUD operations completed successfully");
            } else {
                CustomLogger.logInfo("Not enough students for RUD testing");
            }
        } catch (Exception e) {
            CustomLogger.logError("Error testing EtudiantService: " + e.getMessage(), e);
        }

        // Testing SalleService
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
                fetchedRoom1.setCapacite(fetchedRoom1.getCapacite() + 10);
                SalleDTO updatedRoom = salleService.update(fetchedRoom1.getId(), fetchedRoom1);

                // Test delete
                salleService.delete(room2.getId());

                CustomLogger.logInfo("SalleService RUD operations completed successfully");
            } else {
                CustomLogger.logInfo("Not enough rooms for RUD testing");
            }
        } catch (Exception e) {
            CustomLogger.logError("Error testing SalleService: " + e.getMessage(), e);
        }

        // Testing SeanceService
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
                fetchedSession1.setMatiere("Updated Subject " + RANDOM.nextInt(100));
                SeanceDTO updatedSession = seanceService.update(fetchedSession1.getId(), fetchedSession1);

                // Test delete
                seanceService.delete(session2.getId());

                CustomLogger.logInfo("SeanceService RUD operations completed successfully");
            } else {
                CustomLogger.logInfo("Not enough sessions for RUD testing");
            }
        } catch (Exception e) {
            CustomLogger.logError("Error testing SeanceService: " + e.getMessage(), e);
        }

        // Testing TDService
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
                fetchedTD1.setNb(fetchedTD1.getNb() + 1);
                TDDTO updatedTD = tdService.update(fetchedTD1.getId(), fetchedTD1);

                // Test delete
                tdService.delete(td2.getId());

                CustomLogger.logInfo("TDService RUD operations completed successfully");
            } else {
                CustomLogger.logInfo("Not enough TDs for RUD testing");
            }
        } catch (Exception e) {
            CustomLogger.logError("Error testing TDService: " + e.getMessage(), e);
        }

        // Testing TPService
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
                fetchedTP1.setNb(fetchedTP1.getNb() + 1);
                TPDTO updatedTP = tpService.update(fetchedTP1.getId(), fetchedTP1);

                // Test delete
                tpService.delete(tp2.getId());

                CustomLogger.logInfo("TPService RUD operations completed successfully");
            } else {
                CustomLogger.logInfo("Not enough TPs for RUD testing");
            }
        } catch (Exception e) {
            CustomLogger.logError("Error testing TPService: " + e.getMessage(), e);
        }

        // Testing NotificationService
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
                fetchedNotification1.setMessage("Updated message " + RANDOM.nextInt(100));
                NotificationDTO updatedNotification = notificationService.update(fetchedNotification1.getId(), fetchedNotification1);

                // Test delete
                notificationService.delete(notification2.getId());

                CustomLogger.logInfo("NotificationService RUD operations completed successfully");
            } else {
                CustomLogger.logInfo("Not enough notifications for RUD testing");
            }
        } catch (Exception e) {
            CustomLogger.logError("Error testing NotificationService: " + e.getMessage(), e);
        }

        // Testing ExcelFileService
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
                fetchedFile1.setStatus("Updated Status");
                FichierExcelDTO updatedFile = excelFileService.update(fetchedFile1.getId(), fetchedFile1);

                // Test delete
                excelFileService.delete(file2.getId());

                CustomLogger.logInfo("ExcelFileService RUD operations completed successfully");
            } else {
                CustomLogger.logInfo("Not enough excel files for RUD testing");
            }
        } catch (Exception e) {
            CustomLogger.logError("Error testing ExcelFileService: " + e.getMessage(), e);
        }

        CustomLogger.logInfo("========== RUD Testing Complete ==========");
    }

}