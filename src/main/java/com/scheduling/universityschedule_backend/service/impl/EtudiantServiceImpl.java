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
import com.scheduling.universityschedule_backend.repository.SeanceRepository;
import com.scheduling.universityschedule_backend.service.EtudiantService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service implementation for student operations.
 * Manages student schedules and notifications.
 */
@Service
@Transactional
public class EtudiantServiceImpl implements EtudiantService {

    private final EtudiantRepository etudiantRepository;
    private final SeanceRepository seanceRepository;
    private final NotificationRepository notificationRepository;
    private final EntityMapper mapper;

    public EtudiantServiceImpl(EtudiantRepository etudiantRepository,
                               SeanceRepository seanceRepository,
                               NotificationRepository notificationRepository,
                               EntityMapper mapper) {
        this.etudiantRepository = etudiantRepository;
        this.seanceRepository = seanceRepository;
        this.notificationRepository = notificationRepository;
        this.mapper = mapper;
    }

    @Override
    public EtudiantDTO findById(Long id) throws CustomException {
        Optional<Etudiant> etudiantOpt = etudiantRepository.findById(id);
        if (etudiantOpt.isEmpty()) {
            throw new CustomException("Etudiant not found with id " + id);
        }
        return mapper.toEtudiantDTO(etudiantOpt.get());
    }

    @Override
    public List<SeanceDTO> getPersonalSchedule(Long id) throws CustomException {
        Etudiant etudiant = etudiantRepository.findById(id)
                .orElseThrow(() -> new CustomException("Etudiant not found with id " + id));
        if (etudiant.getTp() == null) {
            throw new CustomException("No practical session associated for student id " + id);
        }
        List<Seance> sessions = etudiant.getTp().getSeances();
        return sessions.stream().map(mapper::toSeanceDTO).collect(Collectors.toList());
    }

    @Override
    public List<SeanceDTO> getBranchSchedule(Long brancheId) throws CustomException {
        List<Seance> sessions = seanceRepository.findAll();
        List<Seance> filtered = sessions.stream()
                .filter(seance -> seance.getBranches() != null &&
                        seance.getBranches().stream().anyMatch(branche -> branche.getId().equals(brancheId)))
                .toList();
        return filtered.stream().map(mapper::toSeanceDTO).collect(Collectors.toList());
    }

    @Override
    public List<NotificationDTO> getNotifications(Long id) throws CustomException {
        List<Notification> notifications = notificationRepository.findAll()
                .stream()
                .filter(n -> n.getRecepteur() != null && n.getRecepteur().getId().equals(id))
                .toList();
        return notifications.stream().map(mapper::toNotificationDTO).collect(Collectors.toList());
    }
}
