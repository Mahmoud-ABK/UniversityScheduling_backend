package com.scheduling.universityschedule_backend.testingclasses;



import com.scheduling.universityschedule_backend.model.*;
import com.scheduling.universityschedule_backend.repository.*;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.util.CustomLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DatabasePopulator {

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

    public void populateDatabase() throws CustomException {
        // Create and save a Personne
        Personne personne = new Personne();
        personne.setCin("123456789");
        personne.setNom("Doe");
        personne.setPrenom("John");
        personne.setEmail("john.doe@example.com");
        personne.setTel("1234567890");
        personne.setAdresse("123 Main St");
        personneRepository.save(personne);

        // Create and save an Enseignant
        Enseignant enseignant = new Enseignant();
        enseignant.setCin("987654321");
        enseignant.setNom("Smith");
        enseignant.setPrenom("Jane");
        enseignant.setEmail("jane.smith@example.com");
        enseignant.setTel("0987654321");
        enseignant.setAdresse("456 Elm St");
        enseignant.setCodeEnseignant("ENS001");
        enseignant.setHeures(20);
        enseignantRepository.save(enseignant);

        // Create and save a Branche
        Branche branche = new Branche();
        branche.setNiveau("Undergraduate");
        branche.setSpecialite("Computer Science");
        branche.setNbTD(5);
        branche.setDepartement("CS");
        brancheRepository.save(branche);

        // Create and save a Salle
        Salle salle = new Salle();
        salle.setIdentifiant("SAL001");
        salle.setType("Lecture Hall");
        salle.setCapacite(100);
        salle.setDisponibilite(List.of("09:00-11:00", "13:00-15:00"));
        salleRepository.save(salle);

        // Create and save a Seance
        Seance seance = new Seance();
        seance.setJour("Monday");
        seance.setHeureDebut("09:00");
        seance.setHeureFin("11:00");
        seance.setType("CR");
        seance.setMatiere("Algorithms");
        seance.setFrequence("Weekly");
        seance.setSalle(salle);
        seance.setEnseignant(enseignant);
        seance.setBranches(List.of(branche));
        seanceRepository.save(seance);

        // Create and save a TD
        TD td = new TD();
        td.setNb(1);
        td.setNbTP(2);
        td.setBranche(branche);
        tdRepository.save(td);

        // Create and save a TP
        TP tp = new TP();
        tp.setNb(1);
        tp.setTd(td);
        tpRepository.save(tp);

        // Create and save a Signal
        Signal signal = new Signal();
        signal.setMessage("Issue with schedule");
        signal.setSeverity("High");
        signal.setTimestamp(LocalDateTime.now());
        signal.setEnseignant(enseignant);
        signalRepository.save(signal);

        // Create and save a PropositionDeRattrapage
        PropositionDeRattrapage proposition = new PropositionDeRattrapage();
        proposition.setDate(LocalDateTime.now().plusDays(1));
        proposition.setReason("Missed session");
        proposition.setStatus("Pending");
        proposition.setEnseignant(enseignant);
        propositionDeRattrapageRepository.save(proposition);

        // Create and save a Notification
        Notification notification = new Notification();
        notification.setMessage("Schedule updated");
        notification.setDate(LocalDateTime.now());
        notification.setType("Update");
        notification.setRead(false);
        notification.setRecepteur(personne);
        notification.setExpediteur(personne);
        notificationRepository.save(notification);

        // Create and save a FichierExcel
        FichierExcel fichierExcel = new FichierExcel();
        fichierExcel.setFileName("schedule.xlsx");
        fichierExcel.setStatus("Imported");
        fichierExcel.setErrors(List.of("No errors"));
        fichierExcel.setImportDate(LocalDateTime.now());
        fichierExcelRepository.save(fichierExcel);

        CustomLogger.logInfo("Database populated with sample data.");
    }
}