package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.SeanceDTO;
import com.scheduling.universityschedule_backend.dto.TPDTO;
import com.scheduling.universityschedule_backend.dto.EtudiantDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.TP;
import com.scheduling.universityschedule_backend.repository.TPRepository;
import com.scheduling.universityschedule_backend.service.TPService;
import com.scheduling.universityschedule_backend.util.CustomLogger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Service implementation for managing Practical Groups (TP).
 * Handles CRUD operations and related functionalities for TPs.
 *
 * @author Mahmoud-ABK
 * @version 1.0
 * @since 2025-03-05
 */
@Service
@Transactional
public class TPServiceImpl implements TPService {

    private final TPRepository tpRepository;
    private final EntityMapper entityMapper;

    /**
     * Constructor injection for dependencies
     *
     * @param tpRepository The repository for TP entities
     * @param entityMapper The mapper for converting between entities and DTOs
     */
    public TPServiceImpl(TPRepository tpRepository, EntityMapper entityMapper) {
        this.tpRepository = tpRepository;
        this.entityMapper = entityMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TPDTO> findAll() throws CustomException {
        try {
            CustomLogger.logInfo("Fetching all practical groups");
            List<TP> tps = tpRepository.findAll();

            List<TPDTO> tpDTOs = tps.stream()
                    .filter(Objects::nonNull)
                    .map(entityMapper::toTPDTO)
                    .collect(Collectors.toList());

            CustomLogger.logInfo("Found " + tpDTOs.size() + " practical groups");
            return tpDTOs;
        } catch (Exception e) {
            CustomLogger.logError("Failed to retrieve all practical groups", e);
            throw new CustomException("Failed to retrieve all practical groups", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TPDTO findById(Long id) throws CustomException {
        try {
            // Validate input
            if (id == null) {
                CustomLogger.logError("Practical group ID cannot be null");
                throw new CustomException("Practical group ID cannot be null");
            }

            CustomLogger.logInfo("Fetching practical group with ID: " + id);
            TP tp = tpRepository.findById(id)
                    .orElseThrow(() -> {
                        CustomLogger.logError("Practical group not found with ID: " + id);
                        return new CustomException("Practical group not found with ID: " + id);
                    });

            return entityMapper.toTPDTO(tp);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            CustomLogger.logError("Failed to retrieve practical group with ID: " + id, e);
            throw new CustomException("Failed to retrieve practical group with ID: " + id, e);
        }
    }

    @Override
    public TPDTO create(TPDTO tpDTO) throws CustomException {
        try {
            // Validate input
            if (tpDTO == null) {
                CustomLogger.logError("Practical group data cannot be null");
                throw new CustomException("Practical group data cannot be null");
            }

            // Check for duplicate ID if provided
            if (tpDTO.getId() != null && tpRepository.existsById(tpDTO.getId())) {
                CustomLogger.logError("Practical group with ID " + tpDTO.getId() + " already exists");
                throw new CustomException("Practical group with ID " + tpDTO.getId() + " already exists");
            }

            CustomLogger.logInfo("Creating new practical group");
            TP tp = entityMapper.toTP(tpDTO);
            TP savedTP = tpRepository.save(tp);

            CustomLogger.logInfo("Created practical group with ID: " + savedTP.getId());
            return entityMapper.toTPDTO(savedTP);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            CustomLogger.logError("Failed to create practical group", e);
            throw new CustomException("Failed to create practical group", e);
        }
    }

    @Override
    public TPDTO update(Long id, TPDTO tpDTO) throws CustomException {
        try {
            // Validate inputs
            if (id == null) {
                CustomLogger.logError("Practical group ID cannot be null");
                throw new CustomException("Practical group ID cannot be null");
            }

            if (tpDTO == null) {
                CustomLogger.logError("Practical group data cannot be null");
                throw new CustomException("Practical group data cannot be null");
            }

            CustomLogger.logInfo("Updating practical group with ID: " + id);
            TP existingTP = tpRepository.findById(id)
                    .orElseThrow(() -> {
                        CustomLogger.logError("Practical group not found with ID: " + id);
                        return new CustomException("Practical group not found with ID: " + id);
                    });

            // Set the ID in the DTO to ensure we're updating the correct entity
            tpDTO.setId(id);
            entityMapper.updateFromDto(tpDTO, existingTP);
            TP updatedTP = tpRepository.save(existingTP);

            CustomLogger.logInfo("Updated practical group with ID: " + id);
            return entityMapper.toTPDTO(updatedTP);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            CustomLogger.logError("Failed to update practical group with ID: " + id, e);
            throw new CustomException("Failed to update practical group with ID: " + id, e);
        }
    }

    @Override
    public void delete(Long id) throws CustomException {
        try {
            // Validate input
            if (id == null) {
                CustomLogger.logError("Practical group ID cannot be null");
                throw new CustomException("Practical group ID cannot be null");
            }

            CustomLogger.logInfo("Checking if practical group with ID: " + id + " exists");
            if (!tpRepository.existsById(id)) {
                CustomLogger.logError("Practical group not found with ID: " + id);
                throw new CustomException("Practical group not found with ID: " + id);
            }

            CustomLogger.logInfo("Deleting practical group with ID: " + id);
            tpRepository.deleteById(id);
            CustomLogger.logInfo("Deleted practical group with ID: " + id);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            CustomLogger.logError("Failed to delete practical group with ID: " + id, e);
            throw new CustomException("Failed to delete practical group with ID: " + id, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<EtudiantDTO> getStudents(Long tpId) throws CustomException {
        CustomLogger.logInfo("Redirecting getStudents to getEtudiants method for practical group: " + tpId);
        return getEtudiants(tpId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeanceDTO> generateSchedule(Long id) throws CustomException {
        try {
            // Validate input
            if (id == null) {
                CustomLogger.logError("Practical group ID cannot be null");
                throw new CustomException("Practical group ID cannot be null");
            }

            CustomLogger.logInfo("Generating schedule for practical group with ID: " + id);
            TP tp = tpRepository.findById(id)
                    .orElseThrow(() -> {
                        CustomLogger.logError("Practical group not found with ID: " + id);
                        return new CustomException("Practical group not found with ID: " + id);
                    });

            // Safely handle potential null collection
            if (tp.getSeances() == null) {
                CustomLogger.logInfo("No sessions found for practical group with ID: " + id);
                return Collections.emptyList();
            }

            List<SeanceDTO> sessionDTOs = tp.getSeances().stream()
                    .filter(Objects::nonNull)
                    .map(entityMapper::toSeanceDTO)
                    .collect(Collectors.toList());

            CustomLogger.logInfo("Generated schedule with " + sessionDTOs.size() + " sessions for practical group with ID: " + id);
            return sessionDTOs;
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            CustomLogger.logError("Failed to generate schedule for practical group with ID: " + id, e);
            throw new CustomException("Failed to generate schedule for practical group with ID: " + id, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<EtudiantDTO> getEtudiants(Long id) throws CustomException {
        try {
            // Validate input
            if (id == null) {
                CustomLogger.logError("Practical group ID cannot be null");
                throw new CustomException("Practical group ID cannot be null");
            }

            CustomLogger.logInfo("Fetching students for practical group with ID: " + id);
            TP tp = tpRepository.findById(id)
                    .orElseThrow(() -> {
                        CustomLogger.logError("Practical group not found with ID: " + id);
                        return new CustomException("Practical group not found with ID: " + id);
                    });

            // Safely handle potential null collection
            if (tp.getEtudiants() == null) {
                CustomLogger.logInfo("No students found for practical group with ID: " + id);
                return Collections.emptyList();
            }

            List<EtudiantDTO> students = tp.getEtudiants().stream()
                    .filter(Objects::nonNull)
                    .map(entityMapper::toEtudiantDTO)
                    .collect(Collectors.toList());

            CustomLogger.logInfo("Found " + students.size() + " students for practical group with ID: " + id);
            return students;
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            CustomLogger.logError("Failed to retrieve students for practical group with ID: " + id, e);
            throw new CustomException("Failed to retrieve students for practical group with ID: " + id, e);
        }
    }


}