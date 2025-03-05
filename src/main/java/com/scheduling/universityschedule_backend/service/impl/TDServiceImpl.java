package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.SeanceDTO;
import com.scheduling.universityschedule_backend.dto.TDDTO;
import com.scheduling.universityschedule_backend.dto.EtudiantDTO;
import com.scheduling.universityschedule_backend.dto.TPDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.Etudiant;
import com.scheduling.universityschedule_backend.model.TD;
import com.scheduling.universityschedule_backend.repository.TDRepository;
import com.scheduling.universityschedule_backend.service.TDService;
import com.scheduling.universityschedule_backend.util.CustomLogger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service implementation for managing Tutorial Groups (TD).
 * Handles CRUD operations and related functionalities for TDs.
 */
@Service
@Transactional
public class TDServiceImpl implements TDService {

    private final TDRepository tdRepository;
    private final EntityMapper entityMapper;

    /**
     * Constructor injection for dependencies
     *
     * @param tdRepository The repository for TD entities
     * @param entityMapper The mapper for converting between entities and DTOs
     */
    public TDServiceImpl(TDRepository tdRepository, EntityMapper entityMapper) {
        this.tdRepository = tdRepository;
        this.entityMapper = entityMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TDDTO> findAll() throws CustomException {
        try {
            CustomLogger.logInfo("Fetching all tutorial groups");
            List<TD> tds = tdRepository.findAll();

            return tds.stream()
                    .filter(Objects::nonNull)
                    .map(entityMapper::toTDDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            CustomLogger.logError("Failed to retrieve all tutorial groups", e);
            throw new CustomException("Failed to retrieve all tutorial groups", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public TDDTO findById(Long id) throws CustomException {
        try {
            // Validate input
            if (id == null) {
                CustomLogger.logError("Tutorial group ID cannot be null");
                throw new CustomException("Tutorial group ID cannot be null");
            }

            CustomLogger.logInfo("Fetching tutorial group with ID: " + id);
            TD td = tdRepository.findById(id)
                    .orElseThrow(() -> {
                        CustomLogger.logError("Tutorial group not found with ID: " + id);
                        return new CustomException("Tutorial group not found with ID: " + id);
                    });

            return entityMapper.toTDDTO(td);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            CustomLogger.logError("Failed to retrieve tutorial group with ID: " + id, e);
            throw new CustomException("Failed to retrieve tutorial group with ID: " + id, e);
        }
    }

    @Override
    public TDDTO create(TDDTO tdDTO) throws CustomException {
        try {
            // Validate input
            if (tdDTO == null) {
                CustomLogger.logError("Tutorial group data cannot be null");
                throw new CustomException("Tutorial group data cannot be null");
            }

            // Check for duplicate ID if provided
            if (tdDTO.getId() != null && tdRepository.existsById(tdDTO.getId())) {
                CustomLogger.logError("Tutorial group with ID " + tdDTO.getId() + " already exists");
                throw new CustomException("Tutorial group with ID " + tdDTO.getId() + " already exists");
            }

            CustomLogger.logInfo("Creating new tutorial group");
            TD td = entityMapper.toTD(tdDTO);
            TD savedTD = tdRepository.save(td);

            CustomLogger.logInfo("Created tutorial group with ID: " + savedTD.getId());
            return entityMapper.toTDDTO(savedTD);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            CustomLogger.logError("Failed to create tutorial group", e);
            throw new CustomException("Failed to create tutorial group", e);
        }
    }

    @Override
    public TDDTO update(Long id, TDDTO tdDTO) throws CustomException {
        try {
            // Validate inputs
            if (id == null) {
                CustomLogger.logError("Tutorial group ID cannot be null");
                throw new CustomException("Tutorial group ID cannot be null");
            }

            if (tdDTO == null) {
                CustomLogger.logError("Tutorial group data cannot be null");
                throw new CustomException("Tutorial group data cannot be null");
            }

            CustomLogger.logInfo("Updating tutorial group with ID: " + id);
            TD existingTD = tdRepository.findById(id)
                    .orElseThrow(() -> {
                        CustomLogger.logError("Tutorial group not found with ID: " + id);
                        return new CustomException("Tutorial group not found with ID: " + id);
                    });

            // Set the ID in the DTO to ensure we're updating the correct entity
            tdDTO.setId(id);
            entityMapper.updateFromDto(tdDTO, existingTD);
            TD updatedTD = tdRepository.save(existingTD);

            CustomLogger.logInfo("Updated tutorial group with ID: " + id);
            return entityMapper.toTDDTO(updatedTD);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            CustomLogger.logError("Failed to update tutorial group with ID: " + id, e);
            throw new CustomException("Failed to update tutorial group with ID: " + id, e);
        }
    }

    @Override
    public void delete(Long id) throws CustomException {
        try {
            // Validate input
            if (id == null) {
                CustomLogger.logError("Tutorial group ID cannot be null");
                throw new CustomException("Tutorial group ID cannot be null");
            }

            CustomLogger.logInfo("Checking if tutorial group with ID: " + id + " exists");
            if (!tdRepository.existsById(id)) {
                CustomLogger.logError("Tutorial group not found with ID: " + id);
                throw new CustomException("Tutorial group not found with ID: " + id);
            }

            CustomLogger.logInfo("Deleting tutorial group with ID: " + id);
            tdRepository.deleteById(id);
            CustomLogger.logInfo("Deleted tutorial group with ID: " + id);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            CustomLogger.logError("Failed to delete tutorial group with ID: " + id, e);
            throw new CustomException("Failed to delete tutorial group with ID: " + id, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<TPDTO> getTPs(Long tdId) throws CustomException {
        try {
            // Validate input
            if (tdId == null) {
                CustomLogger.logError("Tutorial group ID cannot be null");
                throw new CustomException("Tutorial group ID cannot be null");
            }

            CustomLogger.logInfo("Fetching practical groups for tutorial group with ID: " + tdId);
            TD td = tdRepository.findById(tdId)
                    .orElseThrow(() -> {
                        CustomLogger.logError("Tutorial group not found with ID: " + tdId);
                        return new CustomException("Tutorial group not found with ID: " + tdId);
                    });

            // Safely handle potential null collection
            if (td.getTpList() == null) {
                CustomLogger.logInfo("No practical groups found for tutorial group with ID: " + tdId);
                return Collections.emptyList();
            }

            List<TPDTO> tpDTOs = td.getTpList().stream()
                    .filter(Objects::nonNull)
                    .map(entityMapper::toTPDTO)
                    .collect(Collectors.toList());

            CustomLogger.logInfo("Found " + tpDTOs.size() + " practical groups for tutorial group with ID: " + tdId);
            return tpDTOs;
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            CustomLogger.logError("Failed to retrieve practical groups for tutorial group with ID: " + tdId, e);
            throw new CustomException("Failed to retrieve practical groups for tutorial group with ID: " + tdId, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeanceDTO> generateSchedule(Long id) throws CustomException {
        try {
            // Validate input
            if (id == null) {
                CustomLogger.logError("Tutorial group ID cannot be null");
                throw new CustomException("Tutorial group ID cannot be null");
            }

            CustomLogger.logInfo("Generating schedule for tutorial group with ID: " + id);
            TD td = tdRepository.findById(id)
                    .orElseThrow(() -> {
                        CustomLogger.logError("Tutorial group not found with ID: " + id);
                        return new CustomException("Tutorial group not found with ID: " + id);
                    });

            // Safely handle potential null collection
            if (td.getSeances() == null) {
                CustomLogger.logInfo("No sessions found for tutorial group with ID: " + id);
                return Collections.emptyList();
            }

            List<SeanceDTO> sessionDTOs = td.getSeances().stream()
                    .filter(Objects::nonNull)
                    .map(entityMapper::toSeanceDTO)
                    .collect(Collectors.toList());

            CustomLogger.logInfo("Generated schedule with " + sessionDTOs.size() + " sessions for tutorial group with ID: " + id);
            return sessionDTOs;
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            CustomLogger.logError("Failed to generate schedule for tutorial group with ID: " + id, e);
            throw new CustomException("Failed to generate schedule for tutorial group with ID: " + id, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<EtudiantDTO> getEtudiants(Long id) throws CustomException {
        try {
            // Validate input
            if (id == null) {
                CustomLogger.logError("Tutorial group ID cannot be null");
                throw new CustomException("Tutorial group ID cannot be null");
            }

            CustomLogger.logInfo("Fetching students for tutorial group with ID: " + id);
            List<EtudiantDTO> students;

            // Check if there's a custom repository method for this query
            try {
                // This uses the repository's custom method
                CustomLogger.logInfo("Using repository method to fetch students directly");
                students = tdRepository.findAllEtudiantsByTdId(id).stream()
                        .filter(Objects::nonNull)
                        .map(entityMapper::toEtudiantDTO)
                        .collect(Collectors.toList());
            } catch (Exception ex) {
                // Fallback to relationship navigation if custom method isn't available
                CustomLogger.logInfo("Falling back to relationship navigation to get students");
                TD td = tdRepository.findById(id)
                        .orElseThrow(() -> {
                            CustomLogger.logError("Tutorial group not found with ID: " + id);
                            return new CustomException("Tutorial group not found with ID: " + id);
                        });

                // Safely handle potential null collection
                if (td.getTpList() == null) {
                    CustomLogger.logInfo("No practical groups found for tutorial group with ID: " + id);
                    return Collections.emptyList();
                }

                students = td.getTpList().stream()
                        .filter(Objects::nonNull)
                        .flatMap(tp -> {
                            if (tp.getEtudiants() == null) {
                                return Stream.<Etudiant>empty();
                            }
                            return tp.getEtudiants().stream();
                        })
                        .filter(Objects::nonNull)
                        .map(entityMapper::toEtudiantDTO)
                        .collect(Collectors.toList());
            }

            CustomLogger.logInfo("Found " + students.size() + " students for tutorial group with ID: " + id);
            return students;
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            CustomLogger.logError("Failed to retrieve students for tutorial group with ID: " + id, e);
            throw new CustomException("Failed to retrieve students for tutorial group with ID: " + id, e);
        }
    }
}