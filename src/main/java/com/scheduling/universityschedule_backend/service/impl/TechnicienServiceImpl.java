package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.TechnicienDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.Technicien;
import com.scheduling.universityschedule_backend.repository.TechnicienRepository;
import com.scheduling.universityschedule_backend.service.TechnicienService;
import com.scheduling.universityschedule_backend.util.CustomLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of TechnicienService interface.
 * Handles technician-related operations and resource management.
 *
 * @author Mahmoud-ABK
 * @version 1.0
 * @since 2025-05-04
 */
@Service
public class TechnicienServiceImpl implements TechnicienService {

    private final TechnicienRepository technicienRepository;
    private final EntityMapper entityMapper;

    /**
     * Constructor injection for dependencies
     *
     * @param technicienRepository Repository for Technician entities
     * @param entityMapper Mapper for converting between entities and DTOs
     */
    @Autowired
    public TechnicienServiceImpl(TechnicienRepository technicienRepository, EntityMapper entityMapper) {
        this.technicienRepository = technicienRepository;
        this.entityMapper = entityMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public TechnicienDTO findById(Long id) throws CustomException {
        try {
            // Validate input
            if (id == null) {
                CustomLogger.logError("Technician ID cannot be null");
                throw new CustomException("Technician ID cannot be null");
            }

            CustomLogger.logInfo("Fetching technician with ID: " + id);
            Technicien technicien = technicienRepository.findById(id)
                    .orElseThrow(() -> {
                        CustomLogger.logError("Technician not found with ID: " + id);
                        return new CustomException("Technician not found with ID: " + id);
                    });

            return entityMapper.toTechnicienDTO(technicien);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            CustomLogger.logError("Failed to retrieve technician with ID: " + id, e);
            throw new CustomException("Failed to retrieve technician with ID: " + id, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<TechnicienDTO> findAll() throws CustomException {
        try {
            CustomLogger.logInfo("Fetching all technicians");
            List<Technicien> techniciens = technicienRepository.findAll();

            // Handle empty list case
            if (techniciens.isEmpty()) {
                CustomLogger.logInfo("No technicians found in the system");
                return Collections.emptyList();
            }

            List<TechnicienDTO> technicienDTOs = techniciens.stream()
                    .map(entityMapper::toTechnicienDTO)
                    .collect(Collectors.toList());

            CustomLogger.logInfo("Found " + technicienDTOs.size() + " technicians");
            return technicienDTOs;
        } catch (Exception e) {
            CustomLogger.logError("Failed to retrieve all technicians", e);
            throw new CustomException("Failed to retrieve all technicians", e);
        }
    }

    @Override
    @Transactional
    public TechnicienDTO create(TechnicienDTO technicienDTO) throws CustomException {
        try {
            // Validate input
            if (technicienDTO == null) {
                CustomLogger.logError("Technician data cannot be null");
                throw new CustomException("Technician data cannot be null");
            }

            // Check for duplicate ID if provided
            if (technicienDTO.getId() != null && technicienRepository.existsById(technicienDTO.getId())) {
                CustomLogger.logError("Technician with ID " + technicienDTO.getId() + " already exists");
                throw new CustomException("Technician with ID " + technicienDTO.getId() + " already exists");
            }

            CustomLogger.logInfo("Creating new technician");
            Technicien technicien = entityMapper.toTechnicien(technicienDTO);
            Technicien savedTechnicien = technicienRepository.save(technicien);

            CustomLogger.logInfo("Created technician with ID: " + savedTechnicien.getId());
            return entityMapper.toTechnicienDTO(savedTechnicien);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            CustomLogger.logError("Failed to create technician", e);
            throw new CustomException("Failed to create technician", e);
        }
    }

    @Override
    @Transactional
    public TechnicienDTO update(Long id, TechnicienDTO technicienDTO) throws CustomException {
        try {
            // Validate inputs
            if (id == null) {
                CustomLogger.logError("Technician ID cannot be null");
                throw new CustomException("Technician ID cannot be null");
            }

            if (technicienDTO == null) {
                CustomLogger.logError("Technician data cannot be null");
                throw new CustomException("Technician data cannot be null");
            }

            CustomLogger.logInfo("Updating technician with ID: " + id);
            Technicien existingTechnicien = technicienRepository.findById(id)
                    .orElseThrow(() -> {
                        CustomLogger.logError("Technician not found with ID: " + id);
                        return new CustomException("Technician not found with ID: " + id);
                    });

            // Set the ID in the DTO to ensure we're updating the correct entity
            technicienDTO.setId(id);
            entityMapper.updateFromDto(technicienDTO, existingTechnicien);
            Technicien updatedTechnicien = technicienRepository.save(existingTechnicien);

            CustomLogger.logInfo("Updated technician with ID: " + id);
            return entityMapper.toTechnicienDTO(updatedTechnicien);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            CustomLogger.logError("Failed to update technician with ID: " + id, e);
            throw new CustomException("Failed to update technician with ID: " + id, e);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) throws CustomException {
        try {
            // Validate input
            if (id == null) {
                CustomLogger.logError("Technician ID cannot be null");
                throw new CustomException("Technician ID cannot be null");
            }

            CustomLogger.logInfo("Checking if technician with ID: " + id + " exists");
            if (!technicienRepository.existsById(id)) {
                CustomLogger.logError("Technician not found with ID: " + id);
                throw new CustomException("Technician not found with ID: " + id);
            }

            CustomLogger.logInfo("Deleting technician with ID: " + id);
            technicienRepository.deleteById(id);
            CustomLogger.logInfo("Deleted technician with ID: " + id);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            CustomLogger.logError("Failed to delete technician with ID: " + id, e);
            throw new CustomException("Failed to delete technician with ID: " + id, e);
        }
    }
}