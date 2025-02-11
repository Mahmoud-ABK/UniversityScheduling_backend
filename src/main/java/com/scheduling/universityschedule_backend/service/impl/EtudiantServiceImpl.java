package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.EtudiantDTO;
import com.scheduling.universityschedule_backend.dto.NotificationDTO;
import com.scheduling.universityschedule_backend.dto.SeanceDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.Etudiant;
import com.scheduling.universityschedule_backend.model.Notification;
import com.scheduling.universityschedule_backend.model.Seance;
import com.scheduling.universityschedule_backend.repository.EtudiantRepository;
import com.scheduling.universityschedule_backend.repository.NotificationRepository;
import com.scheduling.universityschedule_backend.service.EtudiantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class EtudiantServiceImpl implements EtudiantService {

    private final EtudiantRepository etudiantRepository;
    private final NotificationRepository notificationRepository;
    private final EntityMapper mapper;

    @Autowired
    public EtudiantServiceImpl(EtudiantRepository etudiantRepository,
                               NotificationRepository notificationRepository,
                               EntityMapper mapper) {
        this.etudiantRepository = etudiantRepository;
        this.notificationRepository = notificationRepository;
        this.mapper = mapper;
    }

    /**
     * Retrieves the personal schedule based on the student's practical session (TP).
     */
    @Override
    public List<SeanceDTO> getEmploiDuTempsPersonnel(Long id) throws CustomException {
        Etudiant etudiant = etudiantRepository.findById(id)
                .orElseThrow(() -> new CustomException("Etudiant not found"));
        List<SeanceDTO> dtoList = new ArrayList<>();
        if (etudiant.getTp() != null && etudiant.getTp().getSeances() != null) {
            etudiant.getTp().getSeances().forEach(seance -> dtoList.add(mapper.toSeanceDTO(seance)));
        }
        return dtoList;
    }

    /**
     * Retrieves the schedule for a given branch.
     * (For simplicity, this is not fully implemented.)
     */
    @Override
    public List<SeanceDTO> getEmploiDuTempsBranche(Long brancheId) throws CustomException {
        // Not implemented – would normally query the branche's seances.
        return new ArrayList<>();
    }

    /**
     * Retrieves notifications for the student.
     */
    @Override
    public List<NotificationDTO> getNotifications(Long id) throws CustomException {
        List<Notification> notifications = notificationRepository.findAll();
        List<NotificationDTO> dtoList = new ArrayList<>();
        // For simplicity, we are not filtering by recepteur (student) here.
        notifications.forEach(n -> dtoList.add(mapper.toNotificationDTO(n)));
        return dtoList;
    }
}
