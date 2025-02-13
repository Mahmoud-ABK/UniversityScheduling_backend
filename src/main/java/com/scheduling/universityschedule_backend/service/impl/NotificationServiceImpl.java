package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.NotificationDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.Notification;
import com.scheduling.universityschedule_backend.repository.NotificationRepository;
import com.scheduling.universityschedule_backend.service.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for notification management.
 * Handles system notifications and user alerts.
 */
@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final EntityMapper mapper;

    public NotificationServiceImpl(NotificationRepository notificationRepository, EntityMapper mapper) {
        this.notificationRepository = notificationRepository;
        this.mapper = mapper;
    }

    @Override
    public List<NotificationDTO> findAll() throws CustomException {
        try {
            return notificationRepository.findAll()
                    .stream()
                    .map(mapper::toNotificationDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve notifications: " + e.getMessage());
        }
    }

    @Override
    public void markAsRead(Long id) throws CustomException {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new CustomException("Notification not found with id " + id));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Override
    public List<NotificationDTO> getUnreadNotifications() throws CustomException {
        try {
            return notificationRepository.findAll()
                    .stream()
                    .filter(notification -> notification.getRead() != null && !notification.getRead())
                    .map(mapper::toNotificationDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve unread notifications: " + e.getMessage());
        }
    }

    @Override
    public void broadcastNotification(NotificationDTO notificationDTO) throws CustomException {
        try {
            Notification notification = mapper.toNotification(notificationDTO);
            notification.setDate(java.time.LocalDateTime.now());
            notification.setRead(false);
            notificationRepository.save(notification);
        } catch (Exception e) {
            throw new CustomException("Broadcast failed: " + e.getMessage());
        }
    }
}
