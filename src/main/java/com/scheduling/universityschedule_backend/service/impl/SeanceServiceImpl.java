package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.SeanceDTO;
import com.scheduling.universityschedule_backend.dto.SeanceConflictDTO;
import com.scheduling.universityschedule_backend.dto.SeanceRoomConflictDTO;
import com.scheduling.universityschedule_backend.dto.SingleSeanceConflictDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.Seance;
import com.scheduling.universityschedule_backend.model.FrequenceType;
import com.scheduling.universityschedule_backend.repository.SeanceRepository;
import com.scheduling.universityschedule_backend.service.SeanceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Service implementation for session operations.
 * Handles session creation, scheduling, conflict detection, and related operations.
 */
@Service
@Transactional
public class SeanceServiceImpl implements SeanceService {

    /**
     * Frequency type constants
     */
    private static final FrequenceType BIWEEKLY = FrequenceType.BIWEEKLY;
    private static final FrequenceType CATCHUP = FrequenceType.CATCHUP;
    private static final FrequenceType WEEKLY = FrequenceType.WEEKLY;

    private final SeanceRepository seanceRepository;
    private final EntityMapper entityMapper;

    /**
     * Constructor injection for dependencies
     */
    public SeanceServiceImpl(SeanceRepository seanceRepository,
                             EntityMapper entityMapper) {
        this.seanceRepository = seanceRepository;
        this.entityMapper = entityMapper;
    }

    @Override
    public SeanceDTO findById(Long id) throws CustomException {
        try {
            // Validate input
            if (id == null) {
                throw new CustomException("Session ID cannot be null");
            }

            // Retrieve session
            Seance seance = seanceRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Session not found with ID: " + id));

            // Convert to DTO
            return entityMapper.toSeanceDTO(seance);
        } catch (CustomException e) {
            throw e; // Rethrow custom exceptions as-is
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve session with ID: " + id, e);
        }
    }

    @Override
    public List<SeanceDTO> findAll() throws CustomException {
        try {
            // Retrieve all sessions
            List<Seance> seances = seanceRepository.findAll();

            // Convert to DTOs
            return seances.stream()
                    .filter(Objects::nonNull)
                    .map(entityMapper::toSeanceDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve all sessions", e);
        }
    }

    @Override
    public SeanceDTO create(SeanceDTO seanceDTO) throws CustomException {
        try {
            // Validate input
            if (seanceDTO == null) {
                throw new CustomException("Session data cannot be null");
            }

            // Note: We're allowing creation even with conflicts as per instructions

            // Convert DTO to entity
            Seance seance = entityMapper.toSeance(seanceDTO);

            // Save entity
            Seance savedSeance = seanceRepository.save(seance);

            // Convert back to DTO
            return entityMapper.toSeanceDTO(savedSeance);
        } catch (DateTimeParseException e) {
            throw new CustomException("Invalid date/time format: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new CustomException("Failed to create session: " + e.getMessage(), e);
        }
    }

    @Override
    public SeanceDTO update(Long id, SeanceDTO seanceDTO) throws CustomException {
        try {
            // Validate inputs
            if (id == null) {
                throw new CustomException("Session ID cannot be null");
            }

            if (seanceDTO == null) {
                throw new CustomException("Session data cannot be null");
            }

            // Find existing session
            Seance existingSeance = seanceRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Session not found with ID: " + id));

            // Note: We're allowing updates even with conflicts as per instructions

            // Set ID in DTO to ensure we're updating the right entity
            seanceDTO.setId(id);

            // Update entity
            entityMapper.updateFromDto(seanceDTO, existingSeance);

            // Save updated entity
            Seance updatedSeance = seanceRepository.save(existingSeance);

            // Convert back to DTO
            return entityMapper.toSeanceDTO(updatedSeance);
        } catch (CustomException e) {
            throw e;
        } catch (DateTimeParseException e) {
            throw new CustomException("Invalid date/time format: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new CustomException("Failed to update session with ID: " + id, e);
        }
    }

    @Override
    public void delete(Long id) throws CustomException {
        try {
            // Validate input
            if (id == null) {
                throw new CustomException("Session ID cannot be null");
            }

            // Check if session exists
            if (!seanceRepository.existsById(id)) {
                throw new CustomException("Session not found with ID: " + id);
            }

            // Delete session
            seanceRepository.deleteById(id);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to delete session with ID: " + id, e);
        }
    }

    @Override
    public List<SeanceConflictDTO> getAllConflicts() throws CustomException {
        try {
            // Directly call repository method to find all conflicts
            List<Object[]> conflicts = seanceRepository.findConflictingSeancePairs(BIWEEKLY, CATCHUP);

            // Map conflicts to DTOs
            return entityMapper.toSeanceConflictDTOList(conflicts);
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve all session conflicts", e);
        }
    }

    @Override
    public List<SeanceRoomConflictDTO> getRoomConflicts() throws CustomException {
        try {
            // Directly call repository method to find room conflicts
            List<Object[]> conflicts = seanceRepository.findConflictingByRooms(BIWEEKLY, CATCHUP);

            // Map conflicts to DTOs
            return entityMapper.toSeanceRoomConflictDTOList(conflicts);
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve room conflicts", e);
        }
    }

    @Override
    public List<SingleSeanceConflictDTO> getConflictsForSession(Long seanceId) throws CustomException {
        try {
            // Validate input
            if (seanceId == null) {
                throw new CustomException("Session ID cannot be null");
            }

            // Directly call repository method to find conflicts for specific session
            List<Object[]> conflicts = seanceRepository.findRoomConflictsForSeanceById(seanceId, BIWEEKLY, CATCHUP);

            // Map conflicts to DTOs
            return entityMapper.toSingleSeanceConflictDTOList(conflicts);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve conflicts for session with ID: " + seanceId, e);
        }
    }

    @Override
    public List<SingleSeanceConflictDTO> getConflictsForSession(SeanceDTO seanceDTO) throws CustomException {
        try {
            // Validate input
            if (seanceDTO == null) {
                throw new CustomException("Session data cannot be null");
            }

            // Parse time strings to LocalTime objects
            LocalTime startTime = null;
            LocalTime endTime = null;
            try {
                if (seanceDTO.getHeureDebut() != null) {
                    startTime = LocalTime.parse(seanceDTO.getHeureDebut());
                }
                if (seanceDTO.getHeureFin() != null) {
                    endTime = LocalTime.parse(seanceDTO.getHeureFin());
                }
            } catch (DateTimeParseException e) {
                throw new CustomException("Invalid time format: " + e.getMessage(), e);
            }

            // Parse day of week
            DayOfWeek dayOfWeek = null;
            if (seanceDTO.getJour() != null) {
                try {
                    dayOfWeek = DayOfWeek.valueOf(seanceDTO.getJour());
                } catch (IllegalArgumentException e) {
                    throw new CustomException("Invalid day of week: " + seanceDTO.getJour(), e);
                }
            }

            // Parse frequency type
            FrequenceType frequenceType =null ;
            if (seanceDTO.getFrequence() != null) {
                try {
                    frequenceType = FrequenceType.valueOf(seanceDTO.getFrequence());
                } catch (IllegalArgumentException e) {
                    throw new CustomException("Invalid frequency type: " + seanceDTO.getFrequence(), e);
                }
            } else {
                // Default to weekly if not specified
                frequenceType = WEEKLY;
            }

            // Parse date (optional, used for catch-up sessions)
            LocalDate date = null;
            if (seanceDTO.getDate() != null && !seanceDTO.getDate().isEmpty()) {
                try {
                    date = LocalDate.parse(seanceDTO.getDate());
                } catch (DateTimeParseException e) {
                    throw new CustomException("Invalid date format: " + e.getMessage(), e);
                }
            }

            // Directly call repository method to find conflicts
            List<Object[]> conflicts = seanceRepository.findConflictsForSeance(
                    seanceDTO.getSalleId(),
                    seanceDTO.getEnseignantId(),
                    seanceDTO.getTpIds(),
                    seanceDTO.getTdIds(),
                    seanceDTO.getBrancheIds(),
                    dayOfWeek,
                    startTime,
                    endTime,
                    frequenceType,
                    date,
                    BIWEEKLY,
                    CATCHUP
            );

            // Map conflicts to DTOs
            return entityMapper.toSingleSeanceConflictDTOList(conflicts);

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to check for session conflicts: " + e.getMessage(), e);
        }
    }
}