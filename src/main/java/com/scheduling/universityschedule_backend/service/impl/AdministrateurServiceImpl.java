package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.AdministrateurDTO;
import com.scheduling.universityschedule_backend.dto.PropositionDeRattrapageDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.*;
import com.scheduling.universityschedule_backend.repository.AdministrateurRepository;
import com.scheduling.universityschedule_backend.repository.PropositionDeRattrapageRepository;
import com.scheduling.universityschedule_backend.repository.SalleRepository;
import com.scheduling.universityschedule_backend.repository.SeanceRepository;
import com.scheduling.universityschedule_backend.service.AdministrateurService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for administrator operations.
 * Handles schedule management, makeup sessions, and system-wide notifications.
 */
@Service
@Transactional
public class AdministrateurServiceImpl implements AdministrateurService {

    /**
     * Status constants for makeup session proposals
     */
    private static final String STATUS_APPROVED = "approved";
    private static final String STATUS_REJECTED = "rejected";

    private final AdministrateurRepository administrateurRepository;
    private final PropositionDeRattrapageRepository propositionDeRattrapageRepository;
    private final EntityMapper entityMapper;
    private final SalleRepository salleRepository;
    private final SeanceRepository seanceRepository;


    /**
     * Constructor injection for dependencies
     */
    public AdministrateurServiceImpl(AdministrateurRepository administrateurRepository,
                                     PropositionDeRattrapageRepository propositionDeRattrapageRepository,
                                     EntityMapper entityMapper, SalleRepository salleRepository,SeanceRepository seanceRepository) {
        this.administrateurRepository = administrateurRepository;
        this.propositionDeRattrapageRepository = propositionDeRattrapageRepository;
        this.entityMapper = entityMapper;
        this.salleRepository = salleRepository;
        this.seanceRepository = seanceRepository;

    }

    @Override
    public AdministrateurDTO findById(Long id) throws CustomException {
        try {
            // Validate input
            if (id == null) {
                throw new CustomException("Administrator ID cannot be null");
            }

            // Retrieve administrator
            Administrateur administrateur = administrateurRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Administrator not found with ID: " + id));

            // Convert to DTO
            return entityMapper.toAdministrateurDTO(administrateur);
        } catch (CustomException e) {
            throw e; // Rethrow custom exceptions as-is
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve administrator with ID: " + id, e);
        }
    }

    @Override
    public List<AdministrateurDTO> findAll() throws CustomException {
        try {
            // Retrieve all administrators
            List<Administrateur> administrateurs = administrateurRepository.findAll();

            // Handle empty list case
            if (administrateurs.isEmpty()) {
                return Collections.emptyList();
            }

            // Convert to DTOs
            return administrateurs.stream()
                    .map(entityMapper::toAdministrateurDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve all administrators", e);
        }
    }

    @Override
    public AdministrateurDTO create(AdministrateurDTO administrateurDTO) throws CustomException {
        try {
            // Validate input
            if (administrateurDTO == null) {
                throw new CustomException("Administrator data cannot be null");
            }

            // Check for duplicate ID if provided
            if (administrateurDTO.getId() != null && administrateurRepository.existsById(administrateurDTO.getId())) {
                throw new CustomException("Administrator with ID " + administrateurDTO.getId() + " already exists");
            }

            // Convert to entity
            Administrateur administrateur = entityMapper.toAdministrateur(administrateurDTO);

            // Save entity
            Administrateur savedAdministrateur = administrateurRepository.save(administrateur);

            // Convert back to DTO
            return entityMapper.toAdministrateurDTO(savedAdministrateur);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to create administrator", e);
        }
    }

    @Override
    public AdministrateurDTO update(Long id, AdministrateurDTO administrateurDTO) throws CustomException {
        try {
            // Validate inputs
            if (id == null) {
                throw new CustomException("Administrator ID cannot be null");
            }

            if (administrateurDTO == null) {
                throw new CustomException("Administrator data cannot be null");
            }

            // Find existing administrator
            Administrateur existingAdministrateur = administrateurRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Administrator not found with ID: " + id));

            // Update entity with DTO data
            entityMapper.updateFromDto(administrateurDTO, existingAdministrateur);

            // Save updated entity
            Administrateur updatedAdministrateur = administrateurRepository.save(existingAdministrateur);

            // Convert back to DTO
            return entityMapper.toAdministrateurDTO(updatedAdministrateur);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to update administrator with ID: " + id, e);
        }
    }

    @Override
    public void delete(Long id) throws CustomException {
        try {
            // Validate input
            if (id == null) {
                throw new CustomException("Administrator ID cannot be null");
            }

            // Check if administrator exists
            if (!administrateurRepository.existsById(id)) {
                throw new CustomException("Administrator not found with ID: " + id);
            }

            // Delete administrator
            administrateurRepository.deleteById(id);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to delete administrator with ID: " + id, e);
        }
    }

    @Override
    public List<PropositionDeRattrapageDTO> getAllMakeupSessions() throws CustomException {
        try {
            // Retrieve all makeup session proposals
            List<PropositionDeRattrapage> propositions = propositionDeRattrapageRepository.findAll();

            // Handle empty list case
            if (propositions.isEmpty()) {
                return Collections.emptyList();
            }

            // Convert to DTOs
            return propositions.stream()
                    .map(entityMapper::toPropositionDeRattrapageDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve makeup session proposals", e);
        }
    }
    // still needs refinement
    // In AdministrateurServiceImpl.java
    @Override
    public PropositionDeRattrapageDTO approveMakeupSession(Long id, Long salleId) throws CustomException {
        try {
            // Validate input
            if (id == null) {
                throw new CustomException("Makeup session proposal ID cannot be null");
            }

            // Find existing makeup session proposal
            PropositionDeRattrapage proposition = propositionDeRattrapageRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Makeup session proposal not found with ID: " + id));

            if (proposition.getStatus() == Status.APPROVED) {
                throw new CustomException("Makeup session proposal is already approved");
            }

            if (salleId == null) {
                // Set status to SCHEDULED if no room is assigned
                proposition.setStatus(Status.SCHEDULED);
                PropositionDeRattrapage savedProposition = propositionDeRattrapageRepository.save(proposition);
                return entityMapper.toPropositionDeRattrapageDTO(savedProposition);
            } else {
                // Find the room
                Salle salle = salleRepository.findById(salleId)
                        .orElseThrow(() -> new CustomException("Room not found with ID: " + salleId));

                // Create new Seance from proposition
                Seance newSeance = new Seance();
                newSeance.setName(proposition.getName());
                newSeance.setMatiere(proposition.getMatiere());
                newSeance.setType(proposition.getType());
                newSeance.setHeureDebut(proposition.getHeureDebut());
                newSeance.setHeureFin(proposition.getHeureFin());
                newSeance.setDate(proposition.getDate().toLocalDate());
                newSeance.setJour(proposition.getDate().getDayOfWeek());
                newSeance.setFrequence(FrequenceType.CATCHUP);
                newSeance.setSalle(salle);
                newSeance.setEnseignant(proposition.getEnseignant());
                newSeance.setBranches(new ArrayList<>(proposition.getBranches()));
                newSeance.setTds(new ArrayList<>(proposition.getTds()));
                newSeance.setTps(new ArrayList<>(proposition.getTps()));

                // Save the new Seance
                seanceRepository.save(newSeance);

                // Update proposition status
                proposition.setStatus(Status.APPROVED);
                PropositionDeRattrapage savedProposition = propositionDeRattrapageRepository.save(proposition);

                return entityMapper.toPropositionDeRattrapageDTO(savedProposition);
            }
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to process makeup session proposal with ID: " + id, e);
        }
    }

    @Override
    public PropositionDeRattrapageDTO rejectMakeupSession(Long id) throws CustomException {
        try {
            // Validate input
            if (id == null) {
                throw new CustomException("Makeup session proposal ID cannot be null");
            }

            // Find existing makeup session proposal
            PropositionDeRattrapage proposition = propositionDeRattrapageRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Makeup session proposal not found with ID: " + id));

            // Validate current status
            if (proposition.getStatus() == Status.REJECTED) {
                return entityMapper.toPropositionDeRattrapageDTO(proposition); // Already rejected, no need to update
            }

            if (proposition.getStatus() == Status.APPROVED) {
                throw new CustomException("Cannot reject: makeup session proposal already approved");
            }

            // Update status to rejected
            proposition.setStatus(Status.REJECTED);
            propositionDeRattrapageRepository.save(proposition);
            return entityMapper.toPropositionDeRattrapageDTO(proposition);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to reject makeup session proposal with ID: " + id, e);
        }
    }

}