package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.EtudiantDTO;
import com.scheduling.universityschedule_backend.dto.NotificationDTO;
import com.scheduling.universityschedule_backend.dto.SeanceDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.Etudiant;
import com.scheduling.universityschedule_backend.model.Seance;
import com.scheduling.universityschedule_backend.model.TD;
import com.scheduling.universityschedule_backend.repository.EtudiantRepository;
import com.scheduling.universityschedule_backend.repository.NotificationRepository;
import com.scheduling.universityschedule_backend.service.EtudiantService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Service implementation for student operations.
 * Handles CRUD operations and student-related functionalities.
 */
@Service
@Transactional
public class EtudiantServiceImpl implements EtudiantService {

    private final EtudiantRepository etudiantRepository;
    private final NotificationRepository notificationRepository;
    private final EntityMapper entityMapper;

    /**
     * Constructor injection for dependencies
     */
    public EtudiantServiceImpl(EtudiantRepository etudiantRepository,
                               NotificationRepository notificationRepository,
                               EntityMapper entityMapper) {
        this.etudiantRepository = etudiantRepository;
        this.notificationRepository = notificationRepository;
        this.entityMapper = entityMapper;
    }

    @Override
    public EtudiantDTO findById(Long id) throws CustomException {
        try {
            // Validate input
            if (id == null) {
                throw new CustomException("Student ID cannot be null");
            }

            // Retrieve student
            Etudiant etudiant = etudiantRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Student not found with ID: " + id));

            // Convert to DTO
            return entityMapper.toEtudiantDTO(etudiant);
        } catch (CustomException e) {
            throw e; // Rethrow custom exceptions as-is
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve student with ID: " + id, e);
        }
    }

    @Override
    public List<EtudiantDTO> findAll() throws CustomException {
        try {
            // Retrieve all students
            List<Etudiant> etudiants = etudiantRepository.findAll();

            // Convert to DTOs (JPA repositories typically return empty lists rather than null)
            return etudiants.stream()
                    .filter(Objects::nonNull)
                    .map(entityMapper::toEtudiantDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve all students", e);
        }
    }

    @Override
    public EtudiantDTO create(EtudiantDTO etudiantDTO) throws CustomException {
        try {
            // Validate input
            if (etudiantDTO == null) {
                throw new CustomException("Student data cannot be null");
            }

            // Check for duplicate ID if provided
            if (etudiantDTO.getId() != null && etudiantRepository.existsById(etudiantDTO.getId())) {
                throw new CustomException("Student with ID " + etudiantDTO.getId() + " already exists");
            }

            // Convert to entity
            Etudiant etudiant = entityMapper.toEtudiant(etudiantDTO);

            // Save entity
            Etudiant savedEtudiant = etudiantRepository.save(etudiant);

            // Convert back to DTO
            return entityMapper.toEtudiantDTO(savedEtudiant);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to create student", e);
        }
    }

    @Override
    public EtudiantDTO update(Long id, EtudiantDTO etudiantDTO) throws CustomException {
        try {
            // Validate inputs
            if (id == null) {
                throw new CustomException("Student ID cannot be null");
            }

            if (etudiantDTO == null) {
                throw new CustomException("Student data cannot be null");
            }

            // Find existing student
            Etudiant existingEtudiant = etudiantRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Student not found with ID: " + id));

            // Update entity with DTO data
            entityMapper.updateFromDto(etudiantDTO, existingEtudiant);

            // Save updated entity
            Etudiant updatedEtudiant = etudiantRepository.save(existingEtudiant);

            // Convert back to DTO
            return entityMapper.toEtudiantDTO(updatedEtudiant);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to update student with ID: " + id, e);
        }
    }

    @Override
    public void delete(Long id) throws CustomException {
        try {
            // Validate input
            if (id == null) {
                throw new CustomException("Student ID cannot be null");
            }

            // Check if student exists
            if (!etudiantRepository.existsById(id)) {
                throw new CustomException("Student not found with ID: " + id);
            }

            // Delete student
            etudiantRepository.deleteById(id);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to delete student with ID: " + id, e);
        }
    }

    @Override
    public List<SeanceDTO> getPersonalSchedule(Long id) throws CustomException {
        try {
            // Validate input
            if (id == null) {
                throw new CustomException("Student ID cannot be null");
            }

            // Find student
            Etudiant etudiant = etudiantRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Student not found with ID: " + id));

            // Check if student is assigned to a practical group
            if (etudiant.getTp() == null) {
                throw new CustomException("Student with ID: " + id + " is not assigned to any practical group (TP)");
            }

            // Get sessions from TP
            if (etudiant.getTp().getSeances() == null) {
                return Collections.emptyList();
            }

            // Convert to DTOs
            return etudiant.getTp().getSeances().stream()
                    .filter(Objects::nonNull)
                    .map(entityMapper::toSeanceDTO)
                    .collect(Collectors.toList());
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve personal schedule for student with ID: " + id, e);
        }
    }

    // EtudiantServiceImpl.java
    /**
     * Retrieves schedule for a student's tutorial group (TD).
     * @param id Student's unique identifier
     * @return List of scheduled sessions for TD
     * @throws CustomException if schedule retrieval fails
     */
    @Override
    public List<SeanceDTO> getTDSchedule(Long id) throws CustomException {
        try {
            // Validate input
            if (id == null) {
                throw new CustomException("Student ID cannot be null");
            }

            // Find student
            Etudiant etudiant = etudiantRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Student not found with ID: " + id));

            // Get student's TP
            if (etudiant.getTp() == null) {
                throw new CustomException("Student with ID: " + id + " is not assigned to any practical group (TP)");
            }

            // Get student's TD through TP
            TD td = etudiant.getTp().getTd();
            if (td == null) {
                throw new CustomException("Student's practical group is not associated with any tutorial group (TD)");
            }

            // Get TD sessions
            List<Seance> tdSessions = td.getSeances();
            if (tdSessions == null) {
                return Collections.emptyList();
            }

            // Convert to DTOs
            return tdSessions.stream()
                    .filter(Objects::nonNull)
                    .map(entityMapper::toSeanceDTO)
                    .collect(Collectors.toList());
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve TD schedule for student with ID: " + id, e);
        }
    }
    // EtudiantServiceImpl.java
    /**
     * Retrieves schedule for a specific branch.
     * @param id Student's unique identifier
     * @return List of scheduled sessions for branch
     * @throws CustomException if schedule retrieval fails
     */
    @Override
    public List<SeanceDTO> getBranchSchedule(Long id) throws CustomException {
        try {
            // Validate input
            if (id == null) {
                throw new CustomException("Student ID cannot be null");
            }

            // Find student
            Etudiant etudiant = etudiantRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Student not found with ID: " + id));

            // Get student's branch
            if (etudiant.getBranche() == null) {
                throw new CustomException("Student with ID: " + id + " is not assigned to any branch");
            }

            // Get branch sessions
            List<Seance> branchSessions = etudiant.getBranche().getSeances();
            if (branchSessions == null) {
                return Collections.emptyList();
            }

            // Convert to DTOs
            return branchSessions.stream()
                    .filter(Objects::nonNull)
                    .map(entityMapper::toSeanceDTO)
                    .collect(Collectors.toList());
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve branch schedule for student with ID: " + id, e);
        }
    }

    @Override
    public List<NotificationDTO> getNotifications(Long id) throws CustomException {
        try {
            // Validate input
            if (id == null) {
                throw new CustomException("Student ID cannot be null");
            }

            // Check if student exists
            if (!etudiantRepository.existsById(id)) {
                throw new CustomException("Student not found with ID: " + id);
            }

            // Get notifications for student

            return notificationRepository.findAllByPersonneId(id).stream()
                    .filter(Objects::nonNull)
                    .map(entityMapper::toNotificationDTO)
                    .collect(Collectors.toList());
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve notifications for student with ID: " + id, e);
        }
    }
}