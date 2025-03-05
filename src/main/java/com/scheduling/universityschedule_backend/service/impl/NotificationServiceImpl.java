package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.Notification;
import com.scheduling.universityschedule_backend.repository.NotificationRepository;
import com.scheduling.universityschedule_backend.service.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        // Validate input
        if (notificationDTO == null) {
            throw new CustomException("Notification data cannot be null");
        }

        // For now, this method is unimplemented
        throw new CustomException("Method not implemented yet: broadcastNotification");
    }

    @Override
    public void sendNotificationToTeachers(NotificationDTO notificationDTO) throws CustomException {
        // Validate input
        if (notificationDTO == null) {
            throw new CustomException("Notification data cannot be null");
        }

        // For now, this method is unimplemented
        throw new CustomException("Method not implemented yet: sendNotificationToTeachers");
    }

    @Override
    public void sendNotificationToStudents(NotificationDTO notificationDTO) throws CustomException {
        // Validate input
        if (notificationDTO == null) {
            throw new CustomException("Notification data cannot be null");
        }

        // For now, this method is unimplemented
        throw new CustomException("Method not implemented yet: sendNotificationToStudents");
    }

    @Override
    public void sendNotificationToBranches(NotificationDTO notificationDTO, List<BrancheDTO> brancheDTOS) throws CustomException {
        // Validate inputs
        if (notificationDTO == null) {
            throw new CustomException("Notification data cannot be null");
        }

        if (brancheDTOS == null || brancheDTOS.isEmpty()) {
            throw new CustomException("Branch list cannot be null or empty");
        }

        // For now, this method is unimplemented
        throw new CustomException("Method not implemented yet: sendNotificationToBranches");
    }

    @Override
    public void sendNotificationToBranche(NotificationDTO notificationDTO, BrancheDTO brancheDTO) throws CustomException {
        // Validate inputs
        if (notificationDTO == null) {
            throw new CustomException("Notification data cannot be null");
        }

        if (brancheDTO == null) {
            throw new CustomException("Branch data cannot be null");
        }

        // For now, this method is unimplemented
        throw new CustomException("Method not implemented yet: sendNotificationToBranche");
    }

    @Override
    public void sendNotificationToTDs(NotificationDTO notificationDTO, List<TDDTO> tddtos) throws CustomException {
        // Validate inputs
        if (notificationDTO == null) {
            throw new CustomException("Notification data cannot be null");
        }

        if (tddtos == null || tddtos.isEmpty()) {
            throw new CustomException("Tutorial groups list cannot be null or empty");
        }

        // For now, this method is unimplemented
        throw new CustomException("Method not implemented yet: sendNotificationToTDs");
    }

    @Override
    public void sendNotificationToTD(NotificationDTO notificationDTO, TDDTO tddto) throws CustomException {
        // Validate inputs
        if (notificationDTO == null) {
            throw new CustomException("Notification data cannot be null");
        }

        if (tddto == null) {
            throw new CustomException("Tutorial group data cannot be null");
        }

        // For now, this method is unimplemented
        throw new CustomException("Method not implemented yet: sendNotificationToTD");
    }

    @Override
    public void sendNotificationToTPs(NotificationDTO notificationDTO, List<TPDTO> tpdtos) throws CustomException {
        // Validate inputs
        if (notificationDTO == null) {
            throw new CustomException("Notification data cannot be null");
        }

        if (tpdtos == null || tpdtos.isEmpty()) {
            throw new CustomException("Practical groups list cannot be null or empty");
        }

        // For now, this method is unimplemented
        throw new CustomException("Method not implemented yet: sendNotificationToTPs");
    }

    @Override
    public void sendNotificationToTP(NotificationDTO notificationDTO, TPDTO tpdto) throws CustomException {
        // Validate inputs
        if (notificationDTO == null) {
            throw new CustomException("Notification data cannot be null");
        }

        if (tpdto == null) {
            throw new CustomException("Practical group data cannot be null");
        }

        // For now, this method is unimplemented
        throw new CustomException("Method not implemented yet: sendNotificationToTP");
    }
}