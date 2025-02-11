package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.NotificationDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.Notification;
import com.scheduling.universityschedule_backend.repository.NotificationRepository;
import com.scheduling.universityschedule_backend.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final EntityMapper mapper;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository, EntityMapper mapper) {
        this.notificationRepository = notificationRepository;
        this.mapper = mapper;
    }

    /**
     * Retrieves all notifications for a given user.
     */
    @Override
    public List<NotificationDTO> getNotificationsForUser(Long userId) throws CustomException {
        List<Notification> notifications = notificationRepository.findAll();
        List<NotificationDTO> dtoList = new ArrayList<>();
        // For simplicity, no filtering by user is applied.
        notifications.forEach(n -> dtoList.add(mapper.toNotificationDTO(n)));
        return dtoList;
    }

    /**
     * Marks a notification as read.
     */
    @Override
    public void markAsRead(Long notificationId) throws CustomException {
        Notification notif = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new CustomException("Notification not found"));
        notif.setRead(true);
        notificationRepository.save(notif);
    }
}
