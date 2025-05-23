package com.scheduling.universityschedule_backend.testingclasses;

import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.model.*;
import com.scheduling.universityschedule_backend.repository.*;
import com.scheduling.universityschedule_backend.util.CustomLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JPAtest {
    private static final List<String> NOMS = Arrays.asList(
            "Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller",
            "Wilson", "Moore", "Taylor", "Anderson", "Thomas", "Jackson", "White"
    );

    private static final List<String> PRENOMS = Arrays.asList(
            "James", "Mary", "John", "Patricia", "Robert", "Jennifer", "Michael",
            "Linda", "William", "Elizabeth", "David", "Barbara", "Richard", "Susan"
    );

    private static final Random RANDOM = new Random();

    @Autowired private PersonneRepository personneRepository;
    @Autowired private EnseignantRepository enseignantRepository;
    @Autowired private AdministrateurRepository administrateurRepository;
    @Autowired private TechnicienRepository technicienRepository;
    @Autowired private EtudiantRepository etudiantRepository;
    @Autowired private BrancheRepository brancheRepository;
    @Autowired private SalleRepository salleRepository;
    @Autowired private SeanceRepository seanceRepository;
    @Autowired private TDRepository tdRepository;
    @Autowired private TPRepository tpRepository;
    @Autowired private NotificationRepository notificationRepository;
    @Autowired private SignalRepository signalRepository;
    @Autowired private PropositionDeRattrapageRepository propositionDeRattrapageRepository;
    @Autowired private FichierExcelRepository fichierExcelRepository;

    public void populateDatabase(int sampleSize) throws CustomException {
        CustomLogger.logInfo("=============================Populating Database====================");

        // 1. Administrateurs
        List<Administrateur> admins = new ArrayList<>();
        for (int i = 0; i < sampleSize % 3; i++) {
            Administrateur admin = new Administrateur();
            admin.setCin(generateRandomCin());
            admin.setNom(pickNom());
            admin.setPrenom(pickPrenom());
            admin.setEmail("admin" + i + "@example.com");
            admin.setTel(generateRandomTel());
            admin.setAdresse("Admin Address " + i);
            admin.setCodeAdmin("ADM" + i);
            admins.add(admin);
        }
        administrateurRepository.saveAll(admins);
        CustomLogger.logInfo("Administrateur count: " + admins.size());

        // 2. Enseignants
        List<Enseignant> enseignants = new ArrayList<>();
        for (int i = 0; i < sampleSize; i++) {
            Enseignant ens = new Enseignant();
            ens.setCin(generateRandomCin());
            ens.setNom(pickNom());
            ens.setPrenom(pickPrenom());
            ens.setEmail("enseignant" + i + "@example.com");
            ens.setTel(generateRandomTel());
            ens.setAdresse("Teacher Address " + i);
            ens.setCodeEnseignant("ENS" + i);
            ens.setHeures((int)(Math.random() * 40) + 1);
            enseignants.add(ens);
        }
        enseignantRepository.saveAll(enseignants);
        CustomLogger.logInfo("Enseignant count: " + enseignants.size());

        // 3. Techniciens
        List<Technicien> techniciens = new ArrayList<>();
        for (int i = 0; i < sampleSize % 3; i++) {
            Technicien tech = new Technicien();
            tech.setCin(generateRandomCin());
            tech.setNom(pickNom());
            tech.setPrenom(pickPrenom());
            tech.setEmail("tech" + i + "@example.com");
            tech.setTel(generateRandomTel());
            tech.setAdresse("Tech Address " + i);
            tech.setCodeTechnicien("TECH" + i);
            techniciens.add(tech);
        }
        technicienRepository.saveAll(techniciens);
        CustomLogger.logInfo("Technicien count: " + techniciens.size());

        // 4. Branches
        List<Branche> branches = new ArrayList<>();
        for (int i = 0; i < sampleSize / 15; i++) {
            Branche branche = new Branche();
            branche.setNiveau(pickNiveau());
            branche.setSpecialite(pickSpecialty(branche.getNiveau()));
            branche.setNbTD((int)(Math.random() * 5) + 1);
            branche.setDepartement(Math.random() < 0.33 ? "Technologie" : "informatique");
            branches.add(branche);
        }
        brancheRepository.saveAll(branches);
        CustomLogger.logInfo("Branche count: " + branches.size());

        // 5. Salles
        List<Salle> salles = new ArrayList<>();
        for (int i = 0; i < sampleSize / 10; i++) {
            Salle salle = new Salle();
            salle.setIdentifiant("SAL" + i);
            salle.setType("Lecture Hall");
            salle.setCapacite((int)(Math.random() * 150) + 50);
            salles.add(salle);
        }
        salleRepository.saveAll(salles);
        CustomLogger.logInfo("Salle count: " + salles.size());

        // 6. TDs
        List<TD> tds = new ArrayList<>();
        for (int i = 0; i < (sampleSize * 3) / 20; i++) {
            TD td = new TD();
            td.setNb((int)(Math.random() * 5) + 1);
            td.setNbTP((int)(Math.random() * 3) + 1);
            td.setBranche(branches.get((int)(Math.random() * branches.size())));
            tds.add(td);
        }
        tdRepository.saveAll(tds);
        CustomLogger.logInfo("TD count: " + tds.size());

        // 7. TPs
        List<TP> tps = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            TP tp = new TP();
            tp.setNb((int)(Math.random() * 3) + 1);
            tp.setTd(tds.get((int)(Math.random() * tds.size())));
            tps.add(tp);
            if (i > 2 && Math.random() < 0.5) break;
        }
        tpRepository.saveAll(tps);
        CustomLogger.logInfo("TP count: " + tps.size());

        // 8. Etudiants
        List<Etudiant> etudiants = new ArrayList<>();
        for (int i = 0; i < sampleSize * 3; i++) {
            Etudiant etu = new Etudiant();
            etu.setCin(generateRandomCin());
            etu.setNom(pickNom());
            etu.setPrenom(pickPrenom());
            etu.setEmail("etudiant" + i + "@example.com");
            etu.setTel(generateRandomTel());
            etu.setAdresse("Student Address " + i);
            etu.setMatricule("MAT" + i);
            etu.setBranche(branches.get((int)(Math.random() * branches.size())));
            etu.setTp(tps.get((int)(Math.random() * tps.size())));
            etudiants.add(etu);
        }
        etudiantRepository.saveAll(etudiants);
        CustomLogger.logInfo("Etudiant count: " + etudiants.size());

        // 9. Seances
        List<Seance> seances = new ArrayList<>();
        for (int i = 0; i < sampleSize * 2; i++) {
            Seance seance = new Seance();
            seance.setName("Seance " + i);
            seance.setMatiere(pickRandomSubject());
            seance.setJour(randomDay());
            seance.setHeureDebut(randomTime());
            seance.setHeureFin(randomTimeAfter(seance.getHeureDebut()));
            seance.setFrequence(randomFrequence());
            seance.setType(pickRandomSeanceType());
            seance.setDate(randomFrequence() == FrequenceType.CATCHUP ? LocalDate.now().plusDays(i) : null);
            seance.setSalle(salles.get((int)(Math.random() * salles.size())));
            seance.setEnseignant(enseignants.get((int)(Math.random() * enseignants.size())));
            seance.setBranches(List.of(branches.get((int)(Math.random() * branches.size()))));
            seance.setTds(List.of(tds.get((int)(Math.random() * tds.size()))));
            seance.setTps(List.of(tps.get((int)(Math.random() * tps.size()))));
            seances.add(seance);
        }
        seanceRepository.saveAll(seances);
        CustomLogger.logInfo("Seance count: " + seances.size());

        // 10. Signals
        List<Signal> signals = new ArrayList<>();
        for (int i = 0; i < Math.max(1, sampleSize / 3); i++) {
            Signal signal = new Signal();
            signal.setMessage("Signal Issue " + i);
            signal.setSeverity(Math.random() > 0.5 ? "High" : "Low");
            signal.setTimestamp(LocalDateTime.now().plusMinutes((int)(Math.random() * 1000)));
            signal.setEnseignant(enseignants.get((int)(Math.random() * enseignants.size())));
            signals.add(signal);
        }
        signalRepository.saveAll(signals);
        CustomLogger.logInfo("Signal count: " + signals.size());

        // 11. PropositionsDeRattrapage
        List<PropositionDeRattrapage> propositions = new ArrayList<>();
        for (int i = 0; i < Math.max(1, sampleSize / 3); i++) {
            PropositionDeRattrapage prop = new PropositionDeRattrapage();

            // Basic info
            prop.setName("Makeup Session " + i);
            prop.setMatiere(pickRandomSubject());
            prop.setType(pickRandomSeanceType());

            // Time settings
            LocalTime startTime = randomTime();
            prop.setHeureDebut(startTime);
            prop.setHeureFin(randomTimeAfter(startTime));
            prop.setDate(LocalDateTime.now().plusDays(RANDOM.nextInt(14))); // Next 2 weeks

            // Status and reason
            prop.setStatus(Status.PENDING); // New propositions always start as PENDING
            prop.setReason(pickRandomReason());

            // Relationships
            prop.setEnseignant(enseignants.get(RANDOM.nextInt(enseignants.size())));
            prop.setBranches(pickRandomBranches(branches, 1, 3));  // 1-3 branches
            prop.setTds(pickRandomTDs(tds, 1, 2));                 // 1-2 TDs
            prop.setTps(pickRandomTPs(tps, 0, 2));                 // 0-2 TPs

            propositions.add(prop);
        }
        propositionDeRattrapageRepository.saveAll(propositions);
        CustomLogger.logInfo("PropositionDeRattrapage count: " + propositions.size());

        // 12. Notifications
        List<Notification> notifications = new ArrayList<>();
        for (int i = 0; i < Math.max(1, sampleSize / 2); i++) {
            Notification notif = new Notification();
            notif.setMessage("Notification " + i);
            notif.setDate(LocalDateTime.now().plusMinutes((int)(Math.random() * 1000)));
            notif.setType("Update");
            notif.setIsread(false);
            notif.setRecepteur(enseignants.get((int)(Math.random() * enseignants.size())));
            notif.setExpediteur(admins.isEmpty() ? null : admins.get((int)(Math.random() * admins.size())));
            notifications.add(notif);
        }
        notificationRepository.saveAll(notifications);
        CustomLogger.logInfo("Notification count: " + notifications.size());

        // 13. FichiersExcel
        List<FichierExcel> fichiers = new ArrayList<>();
        for (int i = 0; i < Math.max(1, sampleSize / 5); i++) {
            FichierExcel fichier = new FichierExcel();
            fichier.setFileName("schedule_" + i + ".xlsx");
            fichier.setStatus("Imported");
            fichier.setErrors(List.of("No errors"));
            fichier.setImportDate(LocalDateTime.now());
            fichiers.add(fichier);
        }
        fichierExcelRepository.saveAll(fichiers);
        CustomLogger.logInfo("FichierExcel count: " + fichiers.size());

        CustomLogger.logInfo("Database populated with sampleSize = " + sampleSize);
        CustomLogger.logInfo("========================Finished populating database===========");
    }

    // Helper methods

    private SeanceType pickRandomSeanceType() {
        SeanceType[] types = SeanceType.values();
        return types[RANDOM.nextInt(types.length)];
    }

    private String pickRandomSubject() {
        String[] subjects = {
                "Mathematics", "Physics", "Chemistry",
                "Computer Architecture", "Database Systems", "Operating Systems",
                "Data Structures", "Algorithms", "Software Engineering",
                "Networks", "Artificial Intelligence", "Web Development",
                "Mobile Development", "Cloud Computing", "Security",
                "Statistics", "Linear Algebra", "Calculus"
        };
        return subjects[RANDOM.nextInt(subjects.length)];
    }

    private Status pickRandomStatus() {
        Status[] statuses = Status.values();
        return statuses[RANDOM.nextInt(statuses.length)];
    }

    private String pickRandomReason() {
        String[] reasons = {
                "Previous session cancelled due to emergency",
                "Holiday makeup session",
                "Extra practice needed before exam",
                "Schedule conflict resolution",
                "Weather-related cancellation makeup",
                "Technical issues in previous session",
                "Extended topic coverage needed",
                "Preparatory session for upcoming exam",
                "Missed class coverage",
                "Student request for additional practice"
        };
        return reasons[RANDOM.nextInt(reasons.length)];
    }

    private List<Branche> pickRandomBranches(List<Branche> branches, int min, int max) {
        int count = min + RANDOM.nextInt(max - min + 1);
        List<Branche> selected = new ArrayList<>();
        for (int i = 0; i < count && i < branches.size(); i++) {
            selected.add(branches.get(RANDOM.nextInt(branches.size())));
        }
        return new ArrayList<>(new HashSet<>(selected)); // Remove duplicates
    }

    private List<TD> pickRandomTDs(List<TD> tds, int min, int max) {
        int count = min + RANDOM.nextInt(max - min + 1);
        List<TD> selected = new ArrayList<>();
        for (int i = 0; i < count && i < tds.size(); i++) {
            selected.add(tds.get(RANDOM.nextInt(tds.size())));
        }
        return new ArrayList<>(new HashSet<>(selected)); // Remove duplicates
    }

    private List<TP> pickRandomTPs(List<TP> tps, int min, int max) {
        int count = min + RANDOM.nextInt(max - min + 1);
        List<TP> selected = new ArrayList<>();
        for (int i = 0; i < count && i < tps.size(); i++) {
            selected.add(tps.get(RANDOM.nextInt(tps.size())));
        }
        return new ArrayList<>(new HashSet<>(selected)); // Remove duplicates
    }
    private DayOfWeek randomDay() {
        return DayOfWeek.values()[RANDOM.nextInt(5)];
    }

    private LocalTime randomTime() {
        return LocalTime.of(8 + RANDOM.nextInt(9), RANDOM.nextInt(12) * 5);
    }

    private LocalTime randomTimeAfter(LocalTime start) {
        return start.plusMinutes(90 + RANDOM.nextInt(90));
    }

    private FrequenceType randomFrequence() {
        FrequenceType[] freqs = FrequenceType.values();
        return freqs[RANDOM.nextInt(freqs.length)];
    }

    private String generateRandomCin() {
        return String.valueOf(100000000 + RANDOM.nextInt(900000000));
    }

    private String generateRandomTel() {
        return String.valueOf(1000000000L + RANDOM.nextLong(9000000000L));
    }

    private static String pickNom() {
        return NOMS.get(RANDOM.nextInt(NOMS.size()));
    }

    private static String pickPrenom() {
        return PRENOMS.get(RANDOM.nextInt(PRENOMS.size()));
    }

    private static String pickNiveau() {
        String[] systems = {"L", "CPI", "ING", "MR", "MP"};
        String system = systems[RANDOM.nextInt(systems.length)];
        int yearLevel = switch (system) {
            case "L" -> RANDOM.nextInt(3) + 1;
            case "CPI" -> RANDOM.nextInt(2) + 1;
            case "ING" -> RANDOM.nextInt(3) + 1;
            default -> RANDOM.nextInt(2) + 1;
        };
        return system + yearLevel;
    }

    private static String pickSpecialty(String niveau) {
        if (niveau.startsWith("L")) return Arrays.asList("EEA", "INFO", "TIC", "MATH").get(RANDOM.nextInt(4));
        if (niveau.startsWith("CPI")) return "INFO";
        if (niveau.startsWith("ING")) return Arrays.asList("ELEC", "INFO").get(RANDOM.nextInt(2));
        return Arrays.asList("GL", "I3", "DS").get(RANDOM.nextInt(3));
    }

    @Transactional
    public void testRetrieveEntities() {
        CustomLogger.logInfo("============================= Retrieving Entity Samples =====================");

        // 1. Administrateurs
        List<Administrateur> admins = administrateurRepository.findAll().stream().limit(2).toList();
        CustomLogger.logInfo("----- Administrateurs (" + admins.size() + ") -----");
        admins.forEach(admin -> CustomLogger.logInfo(
                "Code: " + admin.getCodeAdmin() +
                        " | Name: " + admin.getNom() + " " + admin.getPrenom() +
                        " | Email: " + admin.getEmail() +
                        " | CIN: " + admin.getCin()
        ));

        // 2. Enseignants
        List<Enseignant> teachers = enseignantRepository.findAll().stream().limit(3).toList();
        CustomLogger.logInfo("\n----- Enseignants (" + teachers.size() + ") -----");
        teachers.forEach(teacher -> CustomLogger.logInfo(
                "Code: " + teacher.getCodeEnseignant() +
                        " | Hours: " + teacher.getHeures() +
                        " | Tel: " + teacher.getTel() +
                        " | Seances: " + (teacher.getSeances() != null ? teacher.getSeances().size() : 0)
        ));

        // 3. Techniciens
        List<Technicien> techs = technicienRepository.findAll().stream().limit(2).toList();
        CustomLogger.logInfo("\n----- Techniciens (" + techs.size() + ") -----");
        techs.forEach(tech -> CustomLogger.logInfo(
                "Code: " + tech.getCodeTechnicien() +
                        " | Address: " + tech.getAdresse() +
                        " | Email: " + tech.getEmail()
        ));

        // 4. Etudiants
        List<Etudiant> students = etudiantRepository.findAll().stream().limit(3).toList();
        CustomLogger.logInfo("\n----- Etudiants (" + students.size() + ") -----");
        students.forEach(student -> CustomLogger.logInfo(
                "Matricule: " + student.getMatricule() +
                        " | Branch: " + (student.getBranche() != null ? student.getBranche().getSpecialite() : "N/A") +
                        " | TP Group: " + (student.getTp() != null ? student.getTp().getId() : "N/A")
        ));

        // 5. Branches
        List<Branche> branches = brancheRepository.findAll().stream().limit(2).toList();
        CustomLogger.logInfo("\n----- Branches (" + branches.size() + ") -----");
        branches.forEach(branch -> CustomLogger.logInfo(
                "Level: " + branch.getNiveau() +
                        " | Specialty: " + branch.getSpecialite() +
                        " | TDs: " + branch.getNbTD() +
                        " | Department: " + branch.getDepartement()
        ));

        // 6. Salles
        List<Salle> rooms = salleRepository.findAll().stream().limit(3).toList();
        CustomLogger.logInfo("\n----- Salles (" + rooms.size() + ") -----");
        rooms.forEach(room -> CustomLogger.logInfo(
                "ID: " + room.getIdentifiant() +
                        " | Type: " + room.getType() +
                        " | Capacity: " + room.getCapacite() +
                        " | Seances: " + (room.getSeances() != null ? room.getSeances().size() : 0)
        ));

        // 7. Seances
        List<Seance> sessions = seanceRepository.findAll().stream().limit(3).toList();
        CustomLogger.logInfo("\n----- Seances (" + sessions.size() + ") -----");
        sessions.forEach(session -> CustomLogger.logInfo(
                "Subject: " + session.getMatiere() +
                        " | Day: " + session.getJour() +
                        " | Time: " + session.getHeureDebut() + "-" + session.getHeureFin() +
                        " | Type: " + session.getFrequence()
        ));

        // 8. TDs
        List<TD> tds = tdRepository.findAll().stream().limit(2).toList();
        CustomLogger.logInfo("\n----- TDs (" + tds.size() + ") -----");
        tds.forEach(td -> CustomLogger.logInfo(
                "ID: " + td.getId() +
                        " | Group: " + td.getNb() +
                        " | TPs: " + td.getNbTP() +
                        " | Branch: " + (td.getBranche() != null ? td.getBranche().getNiveau() : "N/A")
        ));

        // 9. TPs
        List<TP> tps = tpRepository.findAll().stream().limit(2).toList();
        CustomLogger.logInfo("\n----- TPs (" + tps.size() + ") -----");
        tps.forEach(tp -> CustomLogger.logInfo(
                "ID: " + tp.getId() +
                        " | Group: " + tp.getNb() +
                        " | TD: " + (tp.getTd() != null ? tp.getTd().getId() : "N/A") +
                        " | Students: " + (tp.getEtudiants() != null ? tp.getEtudiants().size() : 0)
        ));

        // 10. Signals
        List<Signal> signals = signalRepository.findAll().stream().limit(2).toList();
        CustomLogger.logInfo("\n----- Signals (" + signals.size() + ") -----");
        signals.forEach(signal -> CustomLogger.logInfo(
                "ID: " + signal.getId() +
                        " | Severity: " + signal.getSeverity() +
                        " | Date: " + signal.getTimestamp() +
                        " | Teacher: " + (signal.getEnseignant() != null ? signal.getEnseignant().getCodeEnseignant() : "N/A")
        ));

        // 11. Propositions
        List<PropositionDeRattrapage> propositions = propositionDeRattrapageRepository.findAll().stream().limit(2).toList();
        CustomLogger.logInfo("\n----- Propositions (" + propositions.size() + ") -----");
        propositions.forEach(prop -> CustomLogger.logInfo(
                "ID: " + prop.getId() +
                        " | Date: " + prop.getDate() +
                        " | Status: " + prop.getStatus() +
                        " | Teacher: " + (prop.getEnseignant() != null ? prop.getEnseignant().getCodeEnseignant() : "N/A")
        ));

        // 12. Notifications
        List<Notification> notifications = notificationRepository.findAll().stream().limit(3).toList();
        CustomLogger.logInfo("\n----- Notifications (" + notifications.size() + ") -----");
        notifications.forEach(notif -> CustomLogger.logInfo(
                "ID: " + notif.getId() +
                        " | Type: " + notif.getType() +
                        " | Read: " + notif.isRead() +
                        " | Sender: " + (notif.getExpediteur() != null ? notif.getExpediteur().getCin() : "System") +
                        " | Receiver: " + (notif.getRecepteur() != null ? notif.getRecepteur().getNom() : "N/A")
        ));

        // 13. Fichiers
        List<FichierExcel> fichiers = fichierExcelRepository.findAll().stream().limit(2).toList();
        CustomLogger.logInfo("\n----- Fichiers (" + fichiers.size() + ") -----");
        fichiers.forEach(fichier -> CustomLogger.logInfo(
                "ID: " + fichier.getId() +
                        " | File: " + fichier.getFileName() +
                        " | Status: " + fichier.getStatus() +
                        " | Errors: " + String.join(", ", fichier.getErrors())
        ));

        CustomLogger.logInfo("============================= Retrieval Complete =====================");
    }


    @Transactional
    public void testRetrieveAndDelete() {
        CustomLogger.logInfo("=============================Deleting Sample Data====================");

        // Delete notifications
        List<Notification> notifications = notificationRepository.findAll().stream().limit(2).toList();
        notificationRepository.deleteAll(notifications);
        CustomLogger.logInfo("Deleted " + notifications.size() + " notifications");

        // Delete signals
        List<Signal> signals = signalRepository.findAll().stream().limit(2).toList();
        signalRepository.deleteAll(signals);
        CustomLogger.logInfo("Deleted " + signals.size() + " signals");

        // Delete fichiers
        List<FichierExcel> fichiers = fichierExcelRepository.findAll().stream().limit(1).toList();
        fichierExcelRepository.deleteAll(fichiers);
        CustomLogger.logInfo("Deleted " + fichiers.size() + " fichiers");
    }

    @Transactional
    public void testRetrieveAndModify() {
        CustomLogger.logInfo("==================== Modifying Sample Entities ====================");

        // 1. Modify Enseignants
        enseignantRepository.findAll().stream().limit(2).forEach(enseignant -> {
            CustomLogger.logInfo("\n----- Original Enseignant -----");
            CustomLogger.logInfo("Code: " + enseignant.getCodeEnseignant());
            CustomLogger.logInfo("Hours: " + enseignant.getHeures());
            CustomLogger.logInfo("Email: " + enseignant.getEmail());

            int newHours = enseignant.getHeures() + 5;
            enseignant.setHeures(newHours);
            enseignantRepository.save(enseignant);

            CustomLogger.logInfo("----- Modified Enseignant -----");
            CustomLogger.logInfo("New hours: " + enseignant.getHeures());
        });

        // 2. Modify Etudiants
        etudiantRepository.findAll().stream().limit(2).forEach(etudiant -> {
            CustomLogger.logInfo("\n----- Original Etudiant -----");
            CustomLogger.logInfo("Matricule: " + etudiant.getMatricule());
            CustomLogger.logInfo("Address: " + etudiant.getAdresse());

            String newAddress = "Updated Address " + LocalDateTime.now().getMinute();
            etudiant.setAdresse(newAddress);
            etudiantRepository.save(etudiant);

            CustomLogger.logInfo("----- Modified Etudiant -----");
            CustomLogger.logInfo("New address: " + etudiant.getAdresse());
        });

        // 3. Modify Seances
        seanceRepository.findAll().stream().limit(2).forEach(seance -> {
            CustomLogger.logInfo("\n----- Original Seance -----");
            CustomLogger.logInfo("Matiere: " + seance.getMatiere());
            CustomLogger.logInfo("Time: " + seance.getHeureDebut() + "-" + seance.getHeureFin());
            CustomLogger.logInfo("Salle: " + (seance.getSalle() != null ? seance.getSalle().getIdentifiant() : "None"));

            // Modify time
            LocalTime newStart = seance.getHeureDebut().plusMinutes(30);
            LocalTime newEnd = seance.getHeureFin().plusMinutes(30);
            seance.setHeureDebut(newStart);
            seance.setHeureFin(newEnd);
            seanceRepository.save(seance);

            CustomLogger.logInfo("----- Modified Seance -----");
            CustomLogger.logInfo("New time: " + seance.getHeureDebut() + "-" + seance.getHeureFin());
        });

        // 4. Modify Salles
        salleRepository.findAll().stream().limit(2).forEach(salle -> {
            CustomLogger.logInfo("\n----- Original Salle -----");
            CustomLogger.logInfo("ID: " + salle.getIdentifiant());
            CustomLogger.logInfo("Type: " + salle.getType());
            CustomLogger.logInfo("Capacity: " + salle.getCapacite());

            // Modify properties
            salle.setType("Modified Type");
            salle.setCapacite(salle.getCapacite() + 10);
            salleRepository.save(salle);

            CustomLogger.logInfo("----- Modified Salle -----");
            CustomLogger.logInfo("New type: " + salle.getType());
            CustomLogger.logInfo("New capacity: " + salle.getCapacite());
        });

        CustomLogger.logInfo("==================== Modification Complete ====================");
    }
    @Transactional
    public void testListFetch() {
        CustomLogger.logInfo("==================== Testing List Fetching ====================");

        // 1. Enseignant (Seances, Propositions, Signals)
        enseignantRepository.findAll().stream().limit(2).forEach(enseignant -> {
            CustomLogger.logInfo("\n----- Enseignant " + enseignant.getCodeEnseignant() + " -----");

            // Seances
            CustomLogger.logInfo("Seances (" + enseignant.getSeances().size() + "):");
            enseignant.getSeances().stream().limit(3).forEach(seance ->
                    CustomLogger.logInfo("- " + seance.getMatiere() + " | " +
                            seance.getHeureDebut() + "-" + seance.getHeureFin())
            );

            // Propositions
            CustomLogger.logInfo("Propositions (" + enseignant.getPropositionsDeRattrapage().size() + "):");
            enseignant.getPropositionsDeRattrapage().stream().limit(3).forEach(prop ->
                    CustomLogger.logInfo("- " + prop.getReason() + " | " + prop.getStatus())
            );

            // Signals
            CustomLogger.logInfo("Signals (" + enseignant.getSignals().size() + "):");
            enseignant.getSignals().stream().limit(3).forEach(signal ->
                    CustomLogger.logInfo("- " + signal.getMessage() + " | " + signal.getSeverity())
            );
        });

        // 2. Branche (Seances)
        brancheRepository.findAll().stream().limit(2).forEach(branche -> {
            CustomLogger.logInfo("\n----- Branche " + branche.getSpecialite() + " -----");

            CustomLogger.logInfo("Seances (" + branche.getSeances().size() + "):");
            branche.getSeances().stream().limit(3).forEach(seance ->
                    CustomLogger.logInfo("- "+ seance.getJour() +"- " + seance.getMatiere() + " | " +
                            seance.getHeureDebut() + "-" + seance.getHeureFin())
            );
        });

        // 3. Seance (Branches, TDs, TPs)
        seanceRepository.findAll().stream().limit(2).forEach(seance -> {
            CustomLogger.logInfo("\n----- Seance " + seance.getMatiere() + " -----");

            // Branches
            CustomLogger.logInfo("Branches (" + seance.getBranches().size() + "):");
            seance.getBranches().stream().limit(3).forEach(branche ->
                    CustomLogger.logInfo("- " + branche.getNiveau() + " " + branche.getSpecialite())
            );

            // TDs
            CustomLogger.logInfo("TDs (" + seance.getTds().size() + "):");
            seance.getTds().stream().limit(3).forEach(td ->
                    CustomLogger.logInfo("- TD" + td.getNb() + " | " + td.getBranche().getSpecialite())
            );

            // TPs
            CustomLogger.logInfo("TPs (" + seance.getTps().size() + "):");
            seance.getTps().stream().limit(3).forEach(tp ->
                    CustomLogger.logInfo("- TP" + tp.getNb() + " | " + tp.getTd().getNb())
            );
        });

        // 4. Salle (Seances)
        salleRepository.findAll().stream().limit(2).forEach(salle -> {
            CustomLogger.logInfo("\n----- Salle " + salle.getIdentifiant() + " -----");

            CustomLogger.logInfo("Seances (" + salle.getSeances().size() + "):");
            salle.getSeances().stream().limit(3).forEach(seance ->
                    CustomLogger.logInfo("- " + seance.getMatiere() + " | " +
                            seance.getHeureDebut() + "-" + seance.getHeureFin())
            );
        });

        // 5. TD (TPs, Seances)
        tdRepository.findAll().stream().limit(2).forEach(td -> {
            CustomLogger.logInfo("\n----- TD " + td.getNb() + " -----");

            // TPs
            CustomLogger.logInfo("TPs (" + td.getTpList().size() + "):");
            td.getTpList().stream().limit(3).forEach(tp ->
                    CustomLogger.logInfo("- TP" + tp.getNb() + " | Students: " + tp.getEtudiants().size())
            );

            // Seances
            CustomLogger.logInfo("Seances (" + td.getSeances().size() + "):");
            td.getSeances().stream().limit(3).forEach(seance ->
                    CustomLogger.logInfo("- " + seance.getMatiere() + " | " +
                            seance.getHeureDebut() + "-" + seance.getHeureFin())
            );
        });

        // 6. TP (Etudiants, Seances)
        tpRepository.findAll().stream().limit(2).forEach(tp -> {
            CustomLogger.logInfo("\n----- TP " + tp.getNb() + " -----");

            // Etudiants
            CustomLogger.logInfo("Etudiants (" + tp.getEtudiants().size() + "):");
            tp.getEtudiants().stream().limit(3).forEach(etudiant ->
                    CustomLogger.logInfo("- " + etudiant.getMatricule() + " | " +
                            etudiant.getNom() + " " + etudiant.getPrenom())
            );

            // Seances
            CustomLogger.logInfo("Seances (" + tp.getSeances().size() + "):");
            tp.getSeances().stream().limit(3).forEach(seance ->
                    CustomLogger.logInfo("- " + seance.getMatiere() + " | " +
                            seance.getHeureDebut() + "-" + seance.getHeureFin())
            );
        });

        // 7. FichierExcel (Errors)
        fichierExcelRepository.findAll().stream().limit(2).forEach(fichier -> {
            CustomLogger.logInfo("\n----- Fichier " + fichier.getFileName() + " -----");

            CustomLogger.logInfo("Errors (" + fichier.getErrors().size() + "):");
            fichier.getErrors().forEach(error ->
                    CustomLogger.logInfo("- " + error)
            );
        });

        CustomLogger.logInfo("==================== List Fetching Complete ====================");
    }

}