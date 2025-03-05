package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.*;
import com.scheduling.universityschedule_backend.repository.*;
import com.scheduling.universityschedule_backend.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Service implementation for notification operations.
 * Handles notification creation, updates, and various distribution methods.
 */
@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final EntityMapper entityMapper;
    @Autowired
    private PersonneRepository personneRepository;
    @Autowired
    private EtudiantRepository etudiantRepository;
    @Autowired
    private EnseignantRepository enseignantRepository;
    @Autowired
    private TDRepository tdRepository;

    /**
     * Constructor injection for dependencies
     */
    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   EntityMapper entityMapper) {
        this.notificationRepository = notificationRepository;
        this.entityMapper = entityMapper;
    }

    @Override
    public NotificationDTO findById(Long id) throws CustomException {
        try {
            // Validate input
            if (id == null) {
                throw new CustomException("Notification ID cannot be null");
            }

            // Retrieve notification
            Notification notification = notificationRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Notification not found with ID: " + id));

            // Convert to DTO
            return entityMapper.toNotificationDTO(notification);
        } catch (CustomException e) {
            throw e; // Rethrow custom exceptions as-is
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve notification with ID: " + id, e);
        }
    }

    @Override
    public List<NotificationDTO> findAll() throws CustomException {
        try {
            // Retrieve all notifications
            List<Notification> notifications = notificationRepository.findAll();

            // Convert to DTOs (JPA repositories typically return empty lists rather than null)
            return notifications.stream()
                    .filter(Objects::nonNull)
                    .map(entityMapper::toNotificationDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve all notifications", e);
        }
    }

    @Override
    public NotificationDTO create(NotificationDTO notificationDTO) throws CustomException {
        try {
            // Validate input
            if (notificationDTO == null) {
                throw new CustomException("Notification data cannot be null");
            }

            // Check for duplicate ID if provided
            if (notificationDTO.getId() != null && notificationRepository.existsById(notificationDTO.getId())) {
                throw new CustomException("Notification with ID " + notificationDTO.getId() + " already exists");
            }

            // Convert to entity
            Notification notification = entityMapper.toNotification(notificationDTO);

            // Save entity
            Notification savedNotification = notificationRepository.save(notification);

            // Convert back to DTO
            return entityMapper.toNotificationDTO(savedNotification);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to create notification", e);
        }
    }

    @Override
    public NotificationDTO update(Long id, NotificationDTO notificationDTO) throws CustomException {
        try {
            // Validate inputs
            if (id == null) {
                throw new CustomException("Notification ID cannot be null");
            }

            if (notificationDTO == null) {
                throw new CustomException("Notification data cannot be null");
            }

            // Find existing notification
            Notification existingNotification = notificationRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Notification not found with ID: " + id));

            // Update entity with DTO data
            entityMapper.updateFromDto(notificationDTO, existingNotification);

            // Save updated entity
            Notification updatedNotification = notificationRepository.save(existingNotification);

            // Convert back to DTO
            return entityMapper.toNotificationDTO(updatedNotification);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to update notification with ID: " + id, e);
        }
    }

    @Override
    public void delete(Long id) throws CustomException {
        try {
            // Validate input
            if (id == null) {
                throw new CustomException("Notification ID cannot be null");
            }

            // Check if notification exists
            if (!notificationRepository.existsById(id)) {
                throw new CustomException("Notification not found with ID: " + id);
            }

            // Delete notification
            notificationRepository.deleteById(id);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to delete notification with ID: " + id, e);
        }
    }

    @Override
    public void markAsRead(Long id) throws CustomException {
        try {
            // Validate input
            if (id == null) {
                throw new CustomException("Notification ID cannot be null");
            }

            // Find notification
            Notification notification = notificationRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Notification not found with ID: " + id));

            // Check if already read (skip update if already marked as read)
            if (Boolean.TRUE.equals(notification.getIsread())) {
                return; // Already read, no need to update
            }

            // Mark as read
            notification.setIsread(true);

            // Save updated notification
            notificationRepository.save(notification);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to mark notification as read: " + id, e);
        }
    }
    @Override
    public List<NotificationDTO> getUnreadNotifications() throws CustomException {
        try {
            // Retrieve unread notifications
            List<Notification> unreadNotifications = notificationRepository.findByIsreadFalse();

            // Convert to DTOs (JPA repositories typically return empty lists rather than null)
            return unreadNotifications.stream()
                    .filter(Objects::nonNull)
                    .map(entityMapper::toNotificationDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve unread notifications", e);
        }
    }

    @Override
    public void broadcastNotification(NotificationDTO notificationDTO) throws CustomException {
        try {
            // Validate input
            if (notificationDTO == null) {
                throw new CustomException("Notification data cannot be null");
            }

            // Validate message content
            if (notificationDTO.getMessage() == null || notificationDTO.getMessage().trim().isEmpty()) {
                throw new CustomException("Notification message cannot be empty");
            }

            // Create base notification
            Notification baseNotification = entityMapper.toNotification(notificationDTO);
            baseNotification.setDate(LocalDateTime.now());
            baseNotification.setIsread(false);
            baseNotification.setType("BROADCAST");

            // Get all users (Personne entities) using repository
            List<Personne> allUsers = personneRepository.findAll();

            // Create and save individual notifications for each user
            List<Notification> notifications = allUsers.stream()
                    .filter(Objects::nonNull)
                    .map(user -> {
                        Notification notification = new Notification();
                        notification.setMessage(baseNotification.getMessage());
                        notification.setDate(baseNotification.getDate());
                        notification.setType(baseNotification.getType());
                        notification.setIsread(false);
                        notification.setRecepteur(user);
                        notification.setExpediteur(baseNotification.getExpediteur());
                        return notification;
                    })
                    .collect(Collectors.toList());

            // Save all notifications in batch
            notificationRepository.saveAll(notifications);

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to broadcast notification: " + e.getMessage(), e);
        }
    }

    @Override
    public void sendNotificationToTeachers(NotificationDTO notificationDTO) throws CustomException {
        try {
            // Validate input
            if (notificationDTO == null) {
                throw new CustomException("Notification data cannot be null");
            }

            // Validate message content
            if (notificationDTO.getMessage() == null || notificationDTO.getMessage().trim().isEmpty()) {
                throw new CustomException("Notification message cannot be empty");
            }

            // Create base notification
            Notification baseNotification = entityMapper.toNotification(notificationDTO);
            baseNotification.setDate(LocalDateTime.now());
            baseNotification.setIsread(false);
            baseNotification.setType("TEACHER_BROADCAST");

            // Get all teachers using repository
            List<Enseignant> allTeachers = enseignantRepository.findAll();

            // Create and save individual notifications for each teacher
            List<Notification> notifications = allTeachers.stream()
                    .filter(Objects::nonNull)
                    .map(teacher -> {
                        Notification notification = new Notification();
                        notification.setMessage(baseNotification.getMessage());
                        notification.setDate(baseNotification.getDate());
                        notification.setType(baseNotification.getType());
                        notification.setIsread(false);
                        notification.setRecepteur(teacher);
                        notification.setExpediteur(baseNotification.getExpediteur());
                        return notification;
                    })
                    .collect(Collectors.toList());

            // Save all notifications in batch
            notificationRepository.saveAll(notifications);

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to send notification to teachers: " + e.getMessage(), e);
        }
    }

    @Override
    public void sendNotificationToStudents(NotificationDTO notificationDTO) throws CustomException {
        try {
            // Validate input
            if (notificationDTO == null) {
                throw new CustomException("Notification data cannot be null");
            }

            // Validate message content
            if (notificationDTO.getMessage() == null || notificationDTO.getMessage().trim().isEmpty()) {
                throw new CustomException("Notification message cannot be empty");
            }

            // Create base notification
            Notification baseNotification = entityMapper.toNotification(notificationDTO);
            baseNotification.setDate(LocalDateTime.now());
            baseNotification.setIsread(false);
            baseNotification.setType("STUDENT_BROADCAST");

            // Get all students using repository
            List<Etudiant> allStudents = etudiantRepository.findAll();

            // Create and save individual notifications for each student
            List<Notification> notifications = allStudents.stream()
                    .filter(Objects::nonNull)
                    .map(student -> {
                        Notification notification = new Notification();
                        notification.setMessage(baseNotification.getMessage());
                        notification.setDate(baseNotification.getDate());
                        notification.setType(baseNotification.getType());
                        notification.setIsread(false);
                        notification.setRecepteur(student);
                        notification.setExpediteur(baseNotification.getExpediteur());
                        return notification;
                    })
                    .collect(Collectors.toList());

            // Save all notifications in batch
            notificationRepository.saveAll(notifications);

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to send notification to students: " + e.getMessage(), e);
        }
    }
    @Override
    public void sendNotificationToBranches(NotificationDTO notificationDTO, List<BrancheDTO> brancheDTOS) throws CustomException {
        try {
            // Validate inputs
            if (notificationDTO == null) {
                throw new CustomException("Notification data cannot be null");
            }

            if (brancheDTOS == null || brancheDTOS.isEmpty()) {
                throw new CustomException("Branch list cannot be null or empty");
            }

            // Convert branch DTOs to entities
            List<Branche> branches = brancheDTOS.stream()
                    .filter(Objects::nonNull)
                    .map(entityMapper::toBranche)
                    .toList();

            // Create base notification
            Notification baseNotification = entityMapper.toNotification(notificationDTO);
            baseNotification.setDate(LocalDateTime.now());
            baseNotification.setIsread(false);
            baseNotification.setType("BRANCH_NOTIFICATION");

            // Get all students from these branches using repository
            List<Notification> notifications = new ArrayList<>();

            // For each branch, get its TDs and their students
            for (Branche branch : branches) {
                List<TD> tds = tdRepository.getAllTDbyBrancheId(branch.getId());
                for (TD td : tds) {
                    if (td.getTpList() != null) {
                        for (TP tp : td.getTpList()) {
                            if (tp.getEtudiants() != null) {
                                for (Etudiant student : tp.getEtudiants()) {
                                    Notification notification = new Notification();
                                    notification.setMessage(baseNotification.getMessage());
                                    notification.setDate(baseNotification.getDate());
                                    notification.setType(baseNotification.getType());
                                    notification.setIsread(false);
                                    notification.setRecepteur(student);
                                    notification.setExpediteur(baseNotification.getExpediteur());
                                    notifications.add(notification);
                                }
                            }
                        }
                    }
                }
            }

            // Save all notifications in batch
            notificationRepository.saveAll(notifications);

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to send notifications to branches: " + e.getMessage(), e);
        }
    }

    @Override
    public void sendNotificationToBranche(NotificationDTO notificationDTO, BrancheDTO brancheDTO) throws CustomException {
        try {
            // Validate inputs
            if (notificationDTO == null) {
                throw new CustomException("Notification data cannot be null");
            }

            if (brancheDTO == null) {
                throw new CustomException("Branch data cannot be null");
            }

            // Convert branch DTO to entity
            Branche branch = entityMapper.toBranche(brancheDTO);

            // Create base notification
            Notification baseNotification = entityMapper.toNotification(notificationDTO);
            baseNotification.setDate(LocalDateTime.now());
            baseNotification.setIsread(false);
            baseNotification.setType("SINGLE_BRANCH_NOTIFICATION");

            // Get all students from this branch using repository
            List<TD> tds = tdRepository.getAllTDbyBrancheId(branch.getId());
            List<Notification> notifications = new ArrayList<>();

            // For each TD, get its TPs and their students
            for (TD td : tds) {
                if (td.getTpList() != null) {
                    for (TP tp : td.getTpList()) {
                        if (tp.getEtudiants() != null) {
                            for (Etudiant student : tp.getEtudiants()) {
                                Notification notification = new Notification();
                                notification.setMessage(baseNotification.getMessage());
                                notification.setDate(baseNotification.getDate());
                                notification.setType(baseNotification.getType());
                                notification.setIsread(false);
                                notification.setRecepteur(student);
                                notification.setExpediteur(baseNotification.getExpediteur());
                                notifications.add(notification);
                            }
                        }
                    }
                }
            }

            // Save all notifications in batch
            notificationRepository.saveAll(notifications);

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to send notification to branch: " + e.getMessage(), e);
        }
    }

    @Override
    public void sendNotificationToTDs(NotificationDTO notificationDTO, List<TDDTO> tddtos) throws CustomException {
        try {
            // Validate inputs
            if (notificationDTO == null) {
                throw new CustomException("Notification data cannot be null");
            }

            if (tddtos == null || tddtos.isEmpty()) {
                throw new CustomException("Tutorial groups list cannot be null or empty");
            }

            // Convert TD DTOs to entities
            List<TD> tds = tddtos.stream()
                    .filter(Objects::nonNull)
                    .map(entityMapper::toTD)
                    .toList();

            // Create base notification
            Notification baseNotification = entityMapper.toNotification(notificationDTO);
            baseNotification.setDate(LocalDateTime.now());
            baseNotification.setIsread(false);
            baseNotification.setType("TD_GROUP_NOTIFICATION");

            List<Notification> notifications = new ArrayList<>();

            // For each TD, get its TPs and their students
            for (TD td : tds) {
                if (td.getTpList() != null) {
                    for (TP tp : td.getTpList()) {
                        if (tp.getEtudiants() != null) {
                            for (Etudiant student : tp.getEtudiants()) {
                                Notification notification = new Notification();
                                notification.setMessage(baseNotification.getMessage());
                                notification.setDate(baseNotification.getDate());
                                notification.setType(baseNotification.getType());
                                notification.setIsread(false);
                                notification.setRecepteur(student);
                                notification.setExpediteur(baseNotification.getExpediteur());
                                notifications.add(notification);
                            }
                        }
                    }
                }
            }

            // Save all notifications in batch
            notificationRepository.saveAll(notifications);

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to send notifications to tutorial groups: " + e.getMessage(), e);
        }
    }

    @Override
    public void sendNotificationToTD(NotificationDTO notificationDTO, TDDTO tddto) throws CustomException {
        try {
            // Validate inputs
            if (notificationDTO == null) {
                throw new CustomException("Notification data cannot be null");
            }

            if (tddto == null) {
                throw new CustomException("Tutorial group data cannot be null");
            }

            // Convert TD DTO to entity
            TD td = entityMapper.toTD(tddto);

            // Create base notification
            Notification baseNotification = entityMapper.toNotification(notificationDTO);
            baseNotification.setDate(LocalDateTime.now());
            baseNotification.setIsread(false);
            baseNotification.setType("SINGLE_TD_NOTIFICATION");

            List<Notification> notifications = new ArrayList<>();

            // Get all students from TPs in this TD
            if (td.getTpList() != null) {
                for (TP tp : td.getTpList()) {
                    if (tp.getEtudiants() != null) {
                        for (Etudiant student : tp.getEtudiants()) {
                            Notification notification = new Notification();
                            notification.setMessage(baseNotification.getMessage());
                            notification.setDate(baseNotification.getDate());
                            notification.setType(baseNotification.getType());
                            notification.setIsread(false);
                            notification.setRecepteur(student);
                            notification.setExpediteur(baseNotification.getExpediteur());
                            notifications.add(notification);
                        }
                    }
                }
            }

            // Save all notifications in batch
            notificationRepository.saveAll(notifications);

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to send notification to tutorial group: " + e.getMessage(), e);
        }
    }
    @Override
    public void sendNotificationToTPs(NotificationDTO notificationDTO, List<TPDTO> tpdtos) throws CustomException {
        try {
            // Validate inputs
            if (notificationDTO == null) {
                throw new CustomException("Notification data cannot be null");
            }

            if (tpdtos == null || tpdtos.isEmpty()) {
                throw new CustomException("Practical groups list cannot be null or empty");
            }

            // Convert TP DTOs to entities for processing
            List<TP> tps = tpdtos.stream()
                    .filter(Objects::nonNull)
                    .map(entityMapper::toTP)
                    .toList();

            // Create base notification
            Notification baseNotification = entityMapper.toNotification(notificationDTO);
            baseNotification.setDate(LocalDateTime.now());
            baseNotification.setIsread(false);
            baseNotification.setType("TP_GROUP_NOTIFICATION");

            List<Notification> notifications = new ArrayList<>();

            // For each TP, create notifications for its students
            for (TP tp : tps) {
                if (tp.getEtudiants() != null) {
                    for (Etudiant student : tp.getEtudiants()) {
                        Notification notification = new Notification();
                        notification.setMessage(baseNotification.getMessage());
                        notification.setDate(baseNotification.getDate());
                        notification.setType(baseNotification.getType());
                        notification.setIsread(false);
                        notification.setRecepteur(student);
                        notification.setExpediteur(baseNotification.getExpediteur());
                        notifications.add(notification);
                    }
                }
            }

            // Save all notifications in batch
            notificationRepository.saveAll(notifications);

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to send notifications to practical groups: " + e.getMessage(), e);
        }
    }

    @Override
    public void sendNotificationToTP(NotificationDTO notificationDTO, TPDTO tpdto) throws CustomException {
        try {
            // Validate inputs
            if (notificationDTO == null) {
                throw new CustomException("Notification data cannot be null");
            }

            if (tpdto == null) {
                throw new CustomException("Practical group data cannot be null");
            }

            // Convert TP DTO to entity
            TP tp = entityMapper.toTP(tpdto);

            // Create base notification
            Notification baseNotification = entityMapper.toNotification(notificationDTO);
            baseNotification.setDate(LocalDateTime.now());
            baseNotification.setIsread(false);
            baseNotification.setType("SINGLE_TP_NOTIFICATION");

            List<Notification> notifications = new ArrayList<>();

            // Create notifications for all students in the TP
            if (tp.getEtudiants() != null) {
                for (Etudiant student : tp.getEtudiants()) {
                    Notification notification = new Notification();
                    notification.setMessage(baseNotification.getMessage());
                    notification.setDate(baseNotification.getDate());
                    notification.setType(baseNotification.getType());
                    notification.setIsread(false);
                    notification.setRecepteur(student);
                    notification.setExpediteur(baseNotification.getExpediteur());
                    notifications.add(notification);
                }
            }

            // Save all notifications in batch
            notificationRepository.saveAll(notifications);

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to send notification to practical group: " + e.getMessage(), e);
        }
    }
}