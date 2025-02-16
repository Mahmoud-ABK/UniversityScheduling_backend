package com.scheduling.universityschedule_backend.testingclasses;

import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.model.*;
import com.scheduling.universityschedule_backend.repository.*;
import com.scheduling.universityschedule_backend.util.CustomLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class DatabasePopulator {
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

    /**
     * Populates the database with random data. The sampleSize parameter determines the data density:
     *
     * - Administrateur: sampleSize mod 3
     * - Enseignant: sampleSize
     * - Technicien: sampleSize mod 3
     * - Etudiant: sampleSize * 3
     * - Branche: sampleSize // 15
     * - Salle: sampleSize // 10
     * - Seance: sampleSize * 2
     * - TD: (sampleSize * 3 ) // 20
     * - TP: 2 or 3 or 6
     * - Signal: sampleSize / 3
     * - PropositionDeRattrapage: sampleSize / 3
     * - Notification: sampleSize / 2
     * - FichierExcel: sampleSize / 5
     * After data generation, the method logs the number of entities created.
     *
     * @param sampleSize determines density of generated data.
     * @throws CustomException if any error occurs during population.
     */
    public void populateDatabase(int sampleSize) throws CustomException {
        CustomLogger.logInfo("=============================Populating Database====================");
        // --- 1. Administrateur(s) ---
        int adminCount = sampleSize % 3;
        List<Administrateur> admins = new ArrayList<>();
        for (int i = 0; i < adminCount; i++) {
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

// --- 2. Enseignant(s) ---
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

// --- 3. Technicien(s) ---
        int technicienCount = sampleSize % 3; // Fewer technicians
        List<Technicien> techniciens = new ArrayList<>();
        for (int i = 0; i < technicienCount; i++) {
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

        // --- 4. Branche(s) ---
        int brancheCount = sampleSize / 15;
        List<Branche> branches = new ArrayList<>();
        for (int i = 0; i < brancheCount; i++) {
            Branche branche = new Branche();
            branche.setNiveau(pickNiveau());
            branche.setSpecialite(pickSpecialty(branche.getNiveau()));
            branche.setNbTD((int)(Math.random() * 5) + 1);
            branche.setDepartement(Math.random() < 0.33 ? "Technologie"  : Math.random() < 0.66 ? "informatique": "mathematiques");
            branches.add(branche);
        }
        brancheRepository.saveAll(branches);
        CustomLogger.logInfo("Branche count: " + branches.size());

        // --- 5. Salle(s) ---
        int salleCount = sampleSize / 10;
        List<Salle> salles = new ArrayList<>();
        for (int i = 0; i < salleCount; i++) {
            Salle salle = new Salle();
            salle.setIdentifiant("SAL" + i);
            salle.setType("Lecture Hall");
            salle.setCapacite((int)(Math.random() * 150) + 50);
            salle.setDisponibilite(List.of("08:00-12:00", "13:00-17:00"));
            salles.add(salle);
        }
        salleRepository.saveAll(salles);
        CustomLogger.logInfo("Salle count: " + salles.size());

        // --- 6. TD(s) ---
        int tdCount = (sampleSize * 3 ) / 20;
        List<TD> tds = new ArrayList<>();
        for (int i = 0; i < tdCount; i++) {
            TD td = new TD();
            td.setNb((int)(Math.random() * 5) + 1);
            td.setNbTP((int)(Math.random() * 3) + 1);
            td.setBranche(branches.get((int)(Math.random() * branches.size())));
            tds.add(td);
        }
        tdRepository.saveAll(tds);
        CustomLogger.logInfo("TD count: " + tds.size());

        // --- 7. TP(s) ---
        List<TP> tps = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            TP tp = new TP();
            tp.setNb((int)(Math.random() * 3) + 1);
            tp.setTd(tds.get((int)(Math.random() * tds.size())));
            tps.add(tp);
            if (i>2 && Math.random() < 0.5 ) {
                break;
            }
        }
        tpRepository.saveAll(tps);
        CustomLogger.logInfo("TP count: " + tps.size());

        // --- 8. Etudiant(s) ---
        // Assume each student gets assigned one Branche and one TP.
        // Generate more students to simulate a larger student body.
        int studentCount = sampleSize * 3;
        List<Etudiant> etudiants = new ArrayList<>();
        for (int i = 0; i < studentCount; i++) {
            Etudiant etu = new Etudiant();
            etu.setCin(generateRandomCin());
            etu.setNom(pickNom());
            etu.setPrenom(pickPrenom());
            etu.setEmail("etudiant" + i + "@example.com");
            etu.setTel(generateRandomTel());
            etu.setAdresse("Student Address " + i);
            etu.setMatricule("MAT" + i);
            etu.setBranche(branches.get((int)(Math.random() * branches.size())));
            // Randomly assign a TP (in a real scenario, students are enrolled in one practical session)
            etu.setTp(tps.get((int)(Math.random() * tps.size())));
            etudiants.add(etu);
        }
        etudiantRepository.saveAll(etudiants);
        CustomLogger.logInfo("Etudiant count: " + etudiants.size());

        // --- 9. Seance(s) ---
        // Sessions (seances) are created by the Administrator.
        // We generate a denser set of sessions.
        int seanceCount = sampleSize * 3;
        List<Seance> seances = new ArrayList<>();
        for (int i = 0; i < seanceCount; i++) {
            Seance seance = new Seance();
            seance.setJour(randomDay());
            String start = randomTime();
            seance.setHeureDebut(start);
            seance.setHeureFin(randomTimeAfter(start));
            seance.setType(randomSeanceType());
            seance.setMatiere("Matiere" + i);
            seance.setFrequence(randomFrequence());
            // Randomly assign a Salle and an Enseignant
            seance.setSalle(salles.get((int)(Math.random() * salles.size())));
            seance.setEnseignant(enseignants.get((int)(Math.random() * enseignants.size())));
            // For simplicity, each session gets one Branche, one TD, and one TP (from the available lists)
            seance.setBranches(List.of(branches.get((int)(Math.random() * branches.size()))));
            seance.setTds(List.of(tds.get((int)(Math.random() * tds.size()))));
            seance.setTps(List.of(tps.get((int)(Math.random() * tps.size()))));
            seances.add(seance);
        }
        seanceRepository.saveAll(seances);
        CustomLogger.logInfo("Seance count: " + seances.size());

        // --- 10. Signal(s) ---
        int signalCount = Math.max(1, sampleSize / 3);
        List<Signal> signals = new ArrayList<>();
        for (int i = 0; i < signalCount; i++) {
            Signal signal = new Signal();
            signal.setMessage("Signal Issue " + i);
            signal.setSeverity(Math.random() > 0.5 ? "High" : "Low");
            signal.setTimestamp(LocalDateTime.now().plusMinutes((int)(Math.random() * 1000)));
            // Randomly assign an Enseignant for the signal
            signal.setEnseignant(enseignants.get((int)(Math.random() * enseignants.size())));
            signals.add(signal);
        }
        signalRepository.saveAll(signals);
        CustomLogger.logInfo("Signal count: " + signals.size());

        // --- 11. PropositionDeRattrapage(s) ---
        int propCount = Math.max(1, sampleSize / 3);
        List<PropositionDeRattrapage> propositions = new ArrayList<>();
        for (int i = 0; i < propCount; i++) {
            PropositionDeRattrapage prop = new PropositionDeRattrapage();
            prop.setDate(LocalDateTime.now().plusDays((int)(Math.random() * 10)));
            prop.setReason("Rattrapage for session " + i);
            prop.setStatus("Pending");
            // Randomly assign an Enseignant for the proposition
            prop.setEnseignant(enseignants.get((int)(Math.random() * enseignants.size())));
            propositions.add(prop);
        }
        propositionDeRattrapageRepository.saveAll(propositions);
        CustomLogger.logInfo("PropositionDeRattrapage count: " + propositions.size());

        // --- 12. Notification(s) ---
        int notifCount = Math.max(1, sampleSize / 2);
        List<Notification> notifications = new ArrayList<>();
        for (int i = 0; i < notifCount; i++) {
            Notification notif = new Notification();
            notif.setMessage("Notification " + i);
            notif.setDate(LocalDateTime.now().plusMinutes((int)(Math.random() * 1000)));
            notif.setType("Update");
            notif.setIsread(false);
            // Randomly choose a recepteur and expediteur from the pool of Personne (or Etudiant/Enseignant)
            notif.setRecepteur(enseignants.get((int)(Math.random() * enseignants.size())));
            notif.setExpediteur(admins.get((int)(Math.random() * (admins.size()-1))));
            notifications.add(notif);
        }
        notificationRepository.saveAll(notifications);
        CustomLogger.logInfo("Notification count: " + notifications.size());

        // --- 13. FichierExcel(s) ---
        int fichierCount = Math.max(1, sampleSize / 5);
        List<FichierExcel> fichiers = new ArrayList<>();
        for (int i = 0; i < fichierCount; i++) {
            FichierExcel fichier = new FichierExcel();
            fichier.setFileName("schedule_" + i + ".xlsx");
            fichier.setStatus("Imported");
            fichier.setErrors(List.of("No errors"));
            fichier.setImportDate(LocalDateTime.now());
            fichiers.add(fichier);
        }
        fichierExcelRepository.saveAll(fichiers);
        CustomLogger.logInfo("FichierExcel count: " + fichiers.size());

        CustomLogger.logInfo("Database populated with random sample data based on sampleSize = " + sampleSize);
        CustomLogger.logInfo("========================Finished populating database===========");
    }

    // --- Helper Methods for Random Data Generation ---

    private String generateRandomCin() {
        return String.valueOf(100000000 + (int)(Math.random() * 900000000));
    }

    private String generateRandomTel() {
        return String.valueOf(1000000000L + (long)(Math.random() * 9000000000L));
    }

    private String randomDay() {
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        return days[(int)(Math.random() * days.length)];
    }

    private String randomTime() {
        int hour = 8 + (int)(Math.random() * 9); // hours between 8 and 16
        int minute = (int)(Math.random() * 7) * 5; // multiples of 5 between 0 and 30

        // Ensure minute is set to 0 if the hour is 16 and minute exceeds 30
        if (hour == 16 && minute > 30) {
            minute = 30;
        }

        return String.format("%02d:%02d", hour, minute);
    }


    private String randomTimeAfter(String time) {
        // Split the provided time into hour and minute components
        String[] parts = time.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);

        // Randomly add 1 hour and 30 minutes or 2 hours
        int randomIncrement = (Math.random() < 0.5) ? 90 : 120; // 90 minutes (1 hour 30 minutes) or 120 minutes (2 hours)

        // Calculate the new time
        int newHour = hour + randomIncrement / 60;
        int newMinute = minute + randomIncrement % 60;

        // Handle minute overflow
        if (newMinute >= 60) {
            newHour++;
            newMinute -= 60;
        }

        // Limit the hour to 18 or less (adjust as needed)
        if (newHour >= 18) {
            newHour = 17;
            newMinute = 59; // Optional: set to the latest possible time in the day
        }

        // Return the new time as a formatted string
        return String.format("%02d:%02d", newHour, newMinute);
    }


    private String randomSeanceType() {
        String[] types = {"CR", "CI", "TD", "TP"};
        return types[(int)(Math.random() * types.length)];
    }

    private String randomFrequence() {
        String[] freqs = {"", "1/15", "catch-up"};
        return freqs[(int)(Math.random() * freqs.length)];
    }
    private static String pickNom() {
        return NOMS.get(RANDOM.nextInt(NOMS.size()));
    }

    private static String pickPrenom() {
        return PRENOMS.get(RANDOM.nextInt(PRENOMS.size()));
    }

    // Method to pick a niveau
    public static String pickNiveau() {
        String[] systems = {"L", "CPI", "ING", "MR", "MP"};
        String system = systems[RANDOM.nextInt(systems.length)];

        int yearLevel = switch (system) {
            case "L" -> RANDOM.nextInt(3) + 1; // L1, L2, L3
            case "CPI" -> RANDOM.nextInt(2) + 1; // CPI1, CPI2
            case "ING" -> RANDOM.nextInt(3) + 1; // ING1, ING2, ING3
            case "MR", "MP" -> RANDOM.nextInt(2) + 1; // MR1, MR2 or MP1, MP2
            default -> throw new IllegalStateException("Unexpected value: " + system);
        };

        return system + yearLevel;
    }

    // Method to pick a specialty based on the niveau
    public static String pickSpecialty(String niveau) {
        List<String> specialties = new ArrayList<>();
        if (niveau.startsWith("L")) {
            specialties = Arrays.asList("EEA", "INFO", "TIC", "MATH");
        } else if (niveau.startsWith("CPI")) {
            specialties = List.of("INFO");
        } else if (niveau.startsWith("ING")) {
            specialties = Arrays.asList("ELEC", "INFO");
        } else if (niveau.startsWith("MR") || niveau.startsWith("MP")) {
            specialties = Arrays.asList("GL", "I3", "DS");
        }

        return specialties.get(RANDOM.nextInt(specialties.size()));
    }
}
