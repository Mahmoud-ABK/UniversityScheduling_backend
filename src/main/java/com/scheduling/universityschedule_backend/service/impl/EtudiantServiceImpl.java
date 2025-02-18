package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.EtudiantDTO;
import com.scheduling.universityschedule_backend.dto.NotificationDTO;
import com.scheduling.universityschedule_backend.dto.SeanceDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.Etudiant;
import com.scheduling.universityschedule_backend.repository.EtudiantRepository;
import com.scheduling.universityschedule_backend.repository.NotificationRepository;
import com.scheduling.universityschedule_backend.service.EtudiantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EtudiantServiceImpl implements EtudiantService {

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private EntityMapper entityMapper;

    @Override
    public EtudiantDTO findById(Long id) throws CustomException {
        Etudiant etudiant = etudiantRepository.findById(id)
                .orElseThrow(() -> new CustomException("Student not found with ID: " + id));
        return entityMapper.toEtudiantDTO(etudiant);
    }

    @Override
    public List<EtudiantDTO> findAll() throws CustomException {
        List<Etudiant> etudiants = etudiantRepository.findAll();
        return etudiants.stream()
                .map(entityMapper::toEtudiantDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EtudiantDTO create(EtudiantDTO etudiantDTO) throws CustomException {
        Etudiant etudiant = entityMapper.toEtudiant(etudiantDTO);
        Etudiant savedEtudiant = etudiantRepository.save(etudiant);
        return entityMapper.toEtudiantDTO(savedEtudiant);
    }

    @Override
    public EtudiantDTO update(Long id, EtudiantDTO etudiantDTO) throws CustomException {
        Etudiant existingEtudiant = etudiantRepository.findById(id)
                .orElseThrow(() -> new CustomException("Student not found with ID: " + id));
        entityMapper.updateFromDto(etudiantDTO, existingEtudiant);
        Etudiant updatedEtudiant = etudiantRepository.save(existingEtudiant);
        return entityMapper.toEtudiantDTO(updatedEtudiant);
    }

    @Override
    public void delete(Long id) throws CustomException {
        if (!etudiantRepository.existsById(id)) {
            throw new CustomException("Student not found with ID: " + id);
        }
        etudiantRepository.deleteById(id);
    }

    @Override
    public List<SeanceDTO> getPersonalSchedule(Long id) throws CustomException {
        Etudiant etudiant = etudiantRepository.findById(id)
                .orElseThrow(() -> new CustomException("Student not found with ID: " + id));
        return etudiant.getTp().getSeances().stream()
                .map(entityMapper::toSeanceDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SeanceDTO> getBranchSchedule(Long brancheId) throws CustomException {
        // Implement the logic to retrieve the schedule for the specified branch
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public List<NotificationDTO> getNotifications(Long id) throws CustomException {
        if (!etudiantRepository.existsById(id)) {
            throw new CustomException("Student not found with ID: " + id);
        }
        return notificationRepository.findAllByPersonneId(id).stream()
                .map(entityMapper::toNotificationDTO)
                .collect(Collectors.toList());
    }
}