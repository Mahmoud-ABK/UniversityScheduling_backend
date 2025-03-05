package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.EnseignantDTO;
import com.scheduling.universityschedule_backend.dto.PropositionDeRattrapageDTO;
import com.scheduling.universityschedule_backend.dto.SeanceDTO;
import com.scheduling.universityschedule_backend.dto.SignalDTO;
import com.scheduling.universityschedule_backend.dto.TPDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.Enseignant;
import com.scheduling.universityschedule_backend.repository.EnseignantRepository;
import com.scheduling.universityschedule_backend.repository.SeanceRepository;
import com.scheduling.universityschedule_backend.service.EnseignantService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Service implementation for teacher operations.
 * Manages teacher schedules, teaching hours, and communication.
 */
@Service
@Transactional
public class EnseignantServiceImpl implements EnseignantService {

    private final EnseignantRepository enseignantRepository;
    private final SeanceRepository seanceRepository;
    private final EntityMapper entityMapper;

    /**
     * Constructor injection for dependencies
     */
    public EnseignantServiceImpl(EnseignantRepository enseignantRepository,
                                 SeanceRepository seanceRepository,
                                 EntityMapper entityMapper) {
        this.enseignantRepository = enseignantRepository;
        this.seanceRepository = seanceRepository;
        this.entityMapper = entityMapper;
    }

    @Override
    public EnseignantDTO findById(Long id) throws CustomException {
        try {
            // Validate input
            if (id == null) {
                throw new CustomException("Teacher ID cannot be null");
            }

            // Retrieve teacher
            Enseignant enseignant = enseignantRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Teacher not found with ID: " + id));

            // Convert to DTO
            return entityMapper.toEnseignantDTO(enseignant);
        } catch (CustomException e) {
            throw e; // Rethrow custom exceptions as-is
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve teacher with ID: " + id, e);
        }
    }

    @Override
    public List<EnseignantDTO> findAll() throws CustomException {
        try {
            // Retrieve all teachers
            List<Enseignant> enseignants = enseignantRepository.findAll();

            // Convert to DTOs
            return enseignants.stream()
                    .map(entityMapper::toEnseignantDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve all teachers", e);
        }
    }

    @Override
    public EnseignantDTO create(EnseignantDTO enseignantDTO) throws CustomException {
        try {
            // Validate input
            if (enseignantDTO == null) {
                throw new CustomException("Teacher data cannot be null");
            }

            // Check for duplicate ID if provided
            if (enseignantDTO.getId() != null && enseignantRepository.existsById(enseignantDTO.getId())) {
                throw new CustomException("Teacher with ID " + enseignantDTO.getId() + " already exists");
            }

            // Convert to entity
            Enseignant enseignant = entityMapper.toEnseignant(enseignantDTO);

            // Save entity
            Enseignant savedEnseignant = enseignantRepository.save(enseignant);

            // Convert back to DTO
            return entityMapper.toEnseignantDTO(savedEnseignant);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to create teacher", e);
        }
    }

    @Override
    public EnseignantDTO update(Long id, EnseignantDTO enseignantDTO) throws CustomException {
        try {
            // Validate inputs
            if (id == null) {
                throw new CustomException("Teacher ID cannot be null");
            }

            if (enseignantDTO == null) {
                throw new CustomException("Teacher data cannot be null");
            }

            // Find existing teacher
            Enseignant existingEnseignant = enseignantRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Teacher not found with ID: " + id));

            // Update entity with DTO data
            entityMapper.updateFromDto(enseignantDTO, existingEnseignant);

            // Save updated entity
            Enseignant updatedEnseignant = enseignantRepository.save(existingEnseignant);

            // Convert back to DTO
            return entityMapper.toEnseignantDTO(updatedEnseignant);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to update teacher with ID: " + id, e);
        }
    }

    @Override
    public void delete(Long id) throws CustomException {
        try {
            // Validate input
            if (id == null) {
                throw new CustomException("Teacher ID cannot be null");
            }

            // Check if teacher exists
            if (!enseignantRepository.existsById(id)) {
                throw new CustomException("Teacher not found with ID: " + id);
            }

            // Delete teacher
            enseignantRepository.deleteById(id);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to delete teacher with ID: " + id, e);
        }
    }

    @Override
    public List<SeanceDTO> getSchedule(Long id) throws CustomException {
        try {
            // Validate input
            if (id == null) {
                throw new CustomException("Teacher ID cannot be null");
            }

            // Find teacher
            Enseignant enseignant = enseignantRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Teacher not found with ID: " + id));

            // Get sessions from teacher - using safe access pattern for collections
            if (enseignant.getSeances() == null) {
                return Collections.emptyList();
            }

            // Convert to DTOs
            return enseignant.getSeances().stream()
                    .filter(Objects::nonNull)
                    .map(entityMapper::toSeanceDTO)
                    .collect(Collectors.toList());
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve schedule for teacher with ID: " + id, e);
        }
    }

    @Override
    public int getTotalTeachingHours(Long teacherId, LocalDate startDate, LocalDate endDate) throws CustomException {
        // For now, we're leaving this unimplemented
        throw new CustomException("Method not implemented yet: getTotalTeachingHours");
    }

    @Override
    public PropositionDeRattrapageDTO submitMakeupRequest(Long id, PropositionDeRattrapageDTO proposition) throws CustomException {
        // For now, we're leaving this unimplemented
        throw new CustomException("Method not implemented yet: submitMakeupRequest");
    }

    @Override
    public SignalDTO submitSignal(Long id, SignalDTO signal) throws CustomException {
        // For now, we're leaving this unimplemented
        throw new CustomException("Method not implemented yet: submitSignal");
    }

    @Override
    public List<SignalDTO> getSignals(Long id) throws CustomException {
        try {
            // Validate input
            if (id == null) {
                throw new CustomException("Teacher ID cannot be null");
            }

            // Find teacher
            Enseignant enseignant = enseignantRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Teacher not found with ID: " + id));

            // Get signals from teacher - using safe access pattern for collections
            if (enseignant.getSignals() == null) {
                return Collections.emptyList();
            }

            // Convert to DTOs
            return enseignant.getSignals().stream()
                    .filter(Objects::nonNull)
                    .map(entityMapper::toSignalDTO)
                    .collect(Collectors.toList());
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve signals for teacher with ID: " + id, e);
        }
    }

    @Override
    public List<String> getSubjects(Long id) throws CustomException {
        // For now, we're leaving this unimplemented
        throw new CustomException("Method not implemented yet: getSubjects");
    }

    @Override
    public List<TPDTO> getStudentGroups(Long id) throws CustomException {
        // For now, we're leaving this unimplemented
        throw new CustomException("Method not implemented yet: getStudentGroups");
    }
}