package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.model.Notification;
import com.scheduling.universityschedule_backend.repository.NotificationRepository;
import com.scheduling.universityschedule_backend.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Notification createNotification(Notification notification) {
        // Ensure new notifications are marked as unread.
        notification.setRead(false);
        return notificationRepository.save(notification);
    }

    @Override
    public Optional<Notification> getNotificationById(Long id) {
        return notificationRepository.findById(id);
    }

    @Override
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    @Override
    public Notification updateNotification(Notification notification) throws CustomException {
        if (notification.getId() == null || !notificationRepository.existsById(notification.getId())) {
            throw new CustomException("Notification not found with id: " + notification.getId());
        }
        return notificationRepository.save(notification);
    }

    @Override
    public void deleteNotification(Long id) throws CustomException {
        if (!notificationRepository.existsById(id)) {
            throw new CustomException("Notification not found with id: " + id);
        }
        notificationRepository.deleteById(id);
    }

    @Override
    public void sendNotification(Notification notification) {
        // In this basic implementation, sending a notification is equivalent to saving it.
        // Additional logic (e.g., push/email) could be added here if needed.
        createNotification(notification);
    }

    @Override
    public List<Notification> getUnreadNotifications(Long userId) throws CustomException {
        // Retrieve all notifications and filter those where:
        // - The recepteur is not null
        // - The recepteur's id matches the provided userId
        // - The notification is not marked as read.
        List<Notification> allNotifications = notificationRepository.findAll();
        return allNotifications.stream()
                .filter(n -> n.getRecepteur() != null
                        && n.getRecepteur().getId().equals(userId)
                        && !n.isRead())
                .collect(Collectors.toList());
    }
}
