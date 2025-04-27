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
        for (int i = 0; i < Math.max(1, sampleSize / 3); i++) {
            try {
                SignalDTO signal = new SignalDTO();
                signal.setMessage("Signal Issue " + i);
                signal.setSeverity(Math.random() > 0.5 ? "High" : "Low");
                signal.setTimestamp(LocalDateTime.parse(LocalDateTime.now().toString()));

                Long teacherId = enseignants.get(RANDOM.nextInt(enseignants.size())).getId();
                signal.setEnseignantId(String.valueOf(teacherId));

                enseignantService.submitSignal(teacherId, signal);
            } catch (Exception e) {
                CustomLogger.logError("Failed to create signal: " + e.getMessage());
            }
        }
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
}