package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.FichierExcelDTO;
import com.scheduling.universityschedule_backend.dto.SeanceDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.FichierExcel;
import com.scheduling.universityschedule_backend.model.Seance;
import com.scheduling.universityschedule_backend.repository.FichierExcelRepository;
import com.scheduling.universityschedule_backend.repository.SeanceRepository;
import com.scheduling.universityschedule_backend.service.ExcelFileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Service implementation for Excel file operations.
 * Handles file uploads, processing, and history tracking.
 */
@Service
@Transactional
public class ExcelFileServiceImpl implements ExcelFileService {

    /**
     * File status constants
     */
    private static final String STATUS_PROCESSING = "Processing";
    private static final String STATUS_COMPLETED = "Completed";
    private static final String STATUS_FAILED = "Failed";

    private final FichierExcelRepository fichierExcelRepository;
    private final SeanceRepository seanceRepository;
    private final EntityMapper entityMapper;

    /**
     * Constructor injection for dependencies
     */
    public ExcelFileServiceImpl(FichierExcelRepository fichierExcelRepository,
                                SeanceRepository seanceRepository,
                                EntityMapper entityMapper) {
        this.fichierExcelRepository = fichierExcelRepository;
        this.seanceRepository = seanceRepository;
        this.entityMapper = entityMapper;
    }

    @Override
    public FichierExcelDTO findById(Long id) throws CustomException {
        try {
            // Validate input
            if (id == null) {
                throw new CustomException("File ID cannot be null");
            }

            // Retrieve file
            FichierExcel fichierExcel = fichierExcelRepository.findById(id)
                    .orElseThrow(() -> new CustomException("File not found with ID: " + id));

            // Convert to DTO
            return entityMapper.toFichierExcelDTO(fichierExcel);
        } catch (CustomException e) {
            throw e; // Rethrow custom exceptions as-is
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve file with ID: " + id, e);
        }
    }

    @Override
    public List<FichierExcelDTO> findAll() throws CustomException {
        try {
            // Retrieve all files
            List<FichierExcel> fichiersExcel = fichierExcelRepository.findAll();

            // Convert to DTOs (JPA repositories typically return empty lists rather than null)
            return fichiersExcel.stream()
                    .filter(Objects::nonNull)
                    .map(entityMapper::toFichierExcelDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve all files", e);
        }
    }

    @Override
    public FichierExcelDTO create(FichierExcelDTO fichierExcelDTO) throws CustomException {
        try {
            // Validate input
            if (fichierExcelDTO == null) {
                throw new CustomException("File data cannot be null");
            }

            // Check for duplicate ID if provided
            if (fichierExcelDTO.getId() != null && fichierExcelRepository.existsById(fichierExcelDTO.getId())) {
                throw new CustomException("File with ID " + fichierExcelDTO.getId() + " already exists");
            }

            // Convert to entity
            FichierExcel fichierExcel = entityMapper.toFichierExcel(fichierExcelDTO);

            // Save entity
            FichierExcel savedFichierExcel = fichierExcelRepository.save(fichierExcel);

            // Convert back to DTO
            return entityMapper.toFichierExcelDTO(savedFichierExcel);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to create file", e);
        }
    }

    @Override
    public FichierExcelDTO update(Long id, FichierExcelDTO fichierExcelDTO) throws CustomException {
        try {
            // Validate inputs
            if (id == null) {
                throw new CustomException("File ID cannot be null");
            }

            if (fichierExcelDTO == null) {
                throw new CustomException("File data cannot be null");
            }

            // Find existing file
            FichierExcel existingFichierExcel = fichierExcelRepository.findById(id)
                    .orElseThrow(() -> new CustomException("File not found with ID: " + id));

            // Update entity with DTO data
            entityMapper.updateFromDto(fichierExcelDTO, existingFichierExcel);

            // Save updated entity
            FichierExcel updatedFichierExcel = fichierExcelRepository.save(existingFichierExcel);

            // Convert back to DTO
            return entityMapper.toFichierExcelDTO(updatedFichierExcel);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to update file with ID: " + id, e);
        }
    }

    @Override
    public void delete(Long id) throws CustomException {
        try {
            // Validate input
            if (id == null) {
                throw new CustomException("File ID cannot be null");
            }

            // Check if file exists
            if (!fichierExcelRepository.existsById(id)) {
                throw new CustomException("File not found with ID: " + id);
            }

            // Delete file
            fichierExcelRepository.deleteById(id);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to delete file with ID: " + id, e);
        }
    }

    @Override
    public void upload(FichierExcelDTO file, List<SeanceDTO> seanceDTOS) throws CustomException {
        FichierExcel fichierExcel = null;

        try {
            // Validate inputs
            if (file == null) {
                throw new CustomException("File data cannot be null");
            }

            if (seanceDTOS == null) {
                throw new CustomException("Sessions data cannot be null");
            }

            // Convert file to entity
            fichierExcel = entityMapper.toFichierExcel(file);

            // Set import date and initial status
            fichierExcel.setImportDate(LocalDateTime.now());
            fichierExcel.setStatus(STATUS_PROCESSING);

            // Save file with initial status
            fichierExcelRepository.save(fichierExcel);

            // Convert and save sessions
            List<Seance> seances = seanceDTOS.stream()
                    .filter(Objects::nonNull)
                    .map(entityMapper::toSeance)
                    .collect(Collectors.toList());

            seanceRepository.saveAll(seances);

            // Update file status to completed
            fichierExcel.setStatus(STATUS_COMPLETED);
            fichierExcelRepository.save(fichierExcel);
        } catch (Exception e) {
            // Update file status to failed if an error occurs
            if (fichierExcel != null) {
                try {
                    fichierExcel.setStatus(STATUS_FAILED);
                    fichierExcelRepository.save(fichierExcel);
                } catch (Exception saveException) {
                    // Log this exception but don't throw it, we want to throw the original exception
                    System.err.println("Failed to update file status to FAILED: " + saveException.getMessage());
                }
            }

            // Throw the original exception
            if (e instanceof CustomException) {
                throw (CustomException) e;
            } else {
                throw new CustomException("Failed to upload and process file", e);
            }
        }
    }

    @Override
    public List<FichierExcelDTO> getImportHistory() throws CustomException {
        try {
            // Reuse findAll method since they do the same thing
            return findAll();
        } catch (CustomException e) {
            // Just change the error message to be more specific to this operation
            throw new CustomException("Failed to retrieve import history", e.getCause());
        }
    }
}