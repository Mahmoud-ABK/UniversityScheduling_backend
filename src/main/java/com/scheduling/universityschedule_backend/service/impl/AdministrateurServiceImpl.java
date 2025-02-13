package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.Administrateur;
import com.scheduling.universityschedule_backend.model.Notification;
import com.scheduling.universityschedule_backend.model.PropositionDeRattrapage;
import com.scheduling.universityschedule_backend.repository.AdministrateurRepository;
import com.scheduling.universityschedule_backend.repository.FichierExcelRepository;
import com.scheduling.universityschedule_backend.repository.NotificationRepository;
import com.scheduling.universityschedule_backend.repository.PropositionDeRattrapageRepository;
import com.scheduling.universityschedule_backend.service.AdministrateurService;
import com.scheduling.universityschedule_backend.service.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service implementation for administrator operations.
 * Contains business logic for schedule management, makeup sessions,
 * and system-wide notifications.
 */
@Service
@Transactional
public class AdministrateurServiceImpl implements AdministrateurService {

    private final AdministrateurRepository administrateurRepository;
    private final FichierExcelRepository fichierExcelRepository;
    private final PropositionDeRattrapageRepository propositionRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;
    private final EntityMapper mapper;

    public AdministrateurServiceImpl(AdministrateurRepository administrateurRepository,
                                     FichierExcelRepository fichierExcelRepository,
                                     PropositionDeRattrapageRepository propositionRepository,
                                     NotificationRepository notificationRepository,
                                     NotificationService notificationService,
                                     EntityMapper mapper) {
        this.administrateurRepository = administrateurRepository;
        this.fichierExcelRepository = fichierExcelRepository;
        this.propositionRepository = propositionRepository;
        this.notificationRepository = notificationRepository;
        this.notificationService = notificationService;
        this.mapper = mapper;
    }

    @Override
    public AdministrateurDTO findById(Long id) throws CustomException {
        Optional<Administrateur> adminOpt = administrateurRepository.findById(id);
        if (adminOpt.isEmpty()) {
            throw new CustomException("Administrateur not found with id " + id);
        }
        return mapper.toAdministrateurDTO(adminOpt.get());
    }

    @Override
    public void importExcelSchedule(FichierExcelDTO fichier) throws CustomException {
        // Convert DTO to entity and simulate file import processing
        try {
            // For simulation purposes, we set the status and current import date.
            fichier.setStatus("Processing");
            fichier.setImportDate(LocalDateTime.now());
            // Save the import record (simulate import)
            fichierExcelRepository.save(mapper.toFichierExcel(fichier));
            // Assume after processing, status becomes "Successful"
            fichier.setStatus("Successful");
        } catch (Exception e) {
            throw new CustomException("Failed to import Excel schedule: " + e.getMessage());
        }
    }

    @Override
    public void generateSchedule() throws CustomException {
        // Simulate schedule generation based on constraints.
        // In a real system, this method would contain the scheduling algorithm.
        try {
            // For example, iterate over sessions and assign available rooms and teachers.
            // After generating, notify all affected users.
            // This is just a simulation.
            Notification notification = new Notification();
            notification.setMessage("New schedule has been generated.");
            notification.setDate(LocalDateTime.now());
            notification.setType("update");
            notification.setRead(false);
            // Save notification so that NotificationService can broadcast it
            notificationRepository.save(notification);
        } catch (Exception e) {
            throw new CustomException("Failed to generate schedule: " + e.getMessage());
        }
    }

    @Override
    public List<PropositionDeRattrapageDTO> getAllMakeupSessions() throws CustomException {
        try {
            return propositionRepository.findAll()
                    .stream()
                    .map(mapper::toPropositionDeRattrapageDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve makeup sessions: " + e.getMessage());
        }
    }

    @Override
    public void approveMakeupSession(Long id) throws CustomException {
        PropositionDeRattrapage proposition = propositionRepository.findById(id)
                .orElseThrow(() -> new CustomException("Makeup session not found with id " + id));
        proposition.setStatus("approved");
        propositionRepository.save(proposition);
        // Notify the teacher that his/her proposal was approved.
        Notification notification = new Notification();
        notification.setMessage("Your makeup session proposal (ID " + id + ") has been approved.");
        notification.setDate(LocalDateTime.now());
        notification.setType("alert");
        notification.setRead(false);
        if (proposition.getEnseignant() != null) {
            notification.setRecepteur(proposition.getEnseignant());
        }
        notificationRepository.save(notification);
        notificationService.broadcastNotification(mapper.toNotificationDTO(notification));
    }

    @Override
    public void rejectMakeupSession(Long id) throws CustomException {
        PropositionDeRattrapage proposition = propositionRepository.findById(id)
                .orElseThrow(() -> new CustomException("Makeup session not found with id " + id));
        proposition.setStatus("rejected");
        propositionRepository.save(proposition);
        // Notify the teacher that his/her proposal was rejected.
        Notification notification = new Notification();
        notification.setMessage("Your makeup session proposal (ID " + id + ") has been rejected.");
        notification.setDate(LocalDateTime.now());
        notification.setType("alert");
        notification.setRead(false);
        if (proposition.getEnseignant() != null) {
            notification.setRecepteur(proposition.getEnseignant());
        }
        notificationRepository.save(notification);
        notificationService.broadcastNotification(mapper.toNotificationDTO(notification));
    }

    @Override
    public void broadcastNotification(NotificationDTO notificationDTO) throws CustomException {
        try {
            // For demonstration, broadcast means saving a notification record.
            Notification notification = mapper.toNotification(notificationDTO);
            notification.setDate(LocalDateTime.now());
            notification.setRead(false);
            notificationRepository.save(notification);
        } catch (Exception e) {
            throw new CustomException("Failed to broadcast notification: " + e.getMessage());
        }
    }
}
