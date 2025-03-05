package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.BrancheDTO;
import com.scheduling.universityschedule_backend.dto.EtudiantDTO;
import com.scheduling.universityschedule_backend.dto.SeanceDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.Branche;
import com.scheduling.universityschedule_backend.model.Etudiant;
import com.scheduling.universityschedule_backend.model.Seance;
import com.scheduling.universityschedule_backend.repository.BrancheRepository;
import com.scheduling.universityschedule_backend.repository.TDRepository;
import com.scheduling.universityschedule_backend.service.BrancheService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Service implementation for branch management.
 * Handles CRUD operations and branch-related functionalities.
 */
@Service
@Transactional
public class BrancheServiceImpl implements BrancheService {

    private final BrancheRepository brancheRepository;
    private final TDRepository tdRepository;
    private final EntityMapper entityMapper;

    /**
     * Constructor injection for dependencies
     */
    public BrancheServiceImpl(BrancheRepository brancheRepository,
                              TDRepository tdRepository,
                              EntityMapper entityMapper) {
        this.brancheRepository = brancheRepository;
        this.tdRepository = tdRepository;
        this.entityMapper = entityMapper;
    }

    @Override
    public BrancheDTO findById(Long id) throws CustomException {
        try {
            // Validate input
            if (id == null) {
                throw new CustomException("Branch ID cannot be null");
            }

            // Retrieve branch
            Branche branche = brancheRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Branch not found with ID: " + id));

            // Convert to DTO
            return entityMapper.toBrancheDTO(branche);
        } catch (CustomException e) {
            throw e; // Rethrow custom exceptions as-is
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve branch with ID: " + id, e);
        }
    }

    @Override
    public List<BrancheDTO> findAll() throws CustomException {
        try {
            // Retrieve all branches
            List<Branche> branches = brancheRepository.findAll();

            // Convert to DTOs (no need to check if list is null - JPA returns empty list)
            return branches.stream()
                    .filter(Objects::nonNull)
                    .map(entityMapper::toBrancheDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve all branches", e);
        }
    }

    @Override
    public BrancheDTO create(BrancheDTO brancheDTO) throws CustomException {
        try {
            // Validate input
            if (brancheDTO == null) {
                throw new CustomException("Branch data cannot be null");
            }

            // Check for duplicate ID if provided
            if (brancheDTO.getId() != null && brancheRepository.existsById(brancheDTO.getId())) {
                throw new CustomException("Branch with ID " + brancheDTO.getId() + " already exists");
            }

            // Convert to entity
            Branche branche = entityMapper.toBranche(brancheDTO);

            // Save entity
            Branche savedBranche = brancheRepository.save(branche);

            // Convert back to DTO
            return entityMapper.toBrancheDTO(savedBranche);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to create branch", e);
        }
    }

    @Override
    public BrancheDTO update(Long id, BrancheDTO brancheDTO) throws CustomException {
        try {
            // Validate inputs
            if (id == null) {
                throw new CustomException("Branch ID cannot be null");
            }

            if (brancheDTO == null) {
                throw new CustomException("Branch data cannot be null");
            }

            // Find existing branch
            Branche existingBranche = brancheRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Branch not found with ID: " + id));

            // Update entity with DTO data
            entityMapper.updateFromDto(brancheDTO, existingBranche);

            // Save updated entity
            Branche updatedBranche = brancheRepository.save(existingBranche);

            // Convert back to DTO
            return entityMapper.toBrancheDTO(updatedBranche);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to update branch with ID: " + id, e);
        }
    }

    @Override
    public void delete(Long id) throws CustomException {
        try {
            // Validate input
            if (id == null) {
                throw new CustomException("Branch ID cannot be null");
            }

            // Check if branch exists
            if (!brancheRepository.existsById(id)) {
                throw new CustomException("Branch not found with ID: " + id);
            }

            // Delete branch
            brancheRepository.deleteById(id);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to delete branch with ID: " + id, e);
        }
    }

    @Override
    public List<SeanceDTO> getSchedule(Long branchId) throws CustomException {
        try {
            // Validate input
            if (branchId == null) {
                throw new CustomException("Branch ID cannot be null");
            }

            // Find branch
            Branche branche = brancheRepository.findById(branchId)
                    .orElseThrow(() -> new CustomException("Branch not found with ID: " + branchId));

            // Get sessions from branch - handle null case properly
            List<Seance> seances = branche.getSeances();
            if (seances == null) {
                return Collections.emptyList();
            }

            // Convert to DTOs
            return seances.stream()
                    .filter(Objects::nonNull)
                    .map(entityMapper::toSeanceDTO)
                    .collect(Collectors.toList());
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve schedule for branch with ID: " + branchId, e);
        }
    }

    @Override
    public List<EtudiantDTO> getEtudiants(Long id) throws CustomException {
        try {
            // Validate input
            if (id == null) {
                throw new CustomException("Branch ID cannot be null");
            }

            // Check if branch exists
            if (!brancheRepository.existsById(id)) {
                throw new CustomException("Branch not found with ID: " + id);
            }

            // Get students using TD repository
            List<Etudiant> etudiants = tdRepository.findAllEtudiantsByBrancheId(id);

            // JPA repositories typically return empty lists rather than null,
            // but we'll be defensive just in case
            if (etudiants == null) {
                return Collections.emptyList();
            }

            // Convert to DTOs
            return etudiants.stream()
                    .filter(Objects::nonNull)
                    .map(entityMapper::toEtudiantDTO)
                    .collect(Collectors.toList());
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve students for branch with ID: " + id, e);
        }
    }
}