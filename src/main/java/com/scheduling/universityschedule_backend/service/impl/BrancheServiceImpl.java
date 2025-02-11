package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.BrancheDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.Branche;
import com.scheduling.universityschedule_backend.repository.BrancheRepository;
import com.scheduling.universityschedule_backend.service.BrancheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BrancheServiceImpl implements BrancheService {

    private final BrancheRepository brancheRepository;
    private final EntityMapper mapper;

    @Autowired
    public BrancheServiceImpl(BrancheRepository brancheRepository, EntityMapper mapper) {
        this.brancheRepository = brancheRepository;
        this.mapper = mapper;
    }

    /**
     * Retrieves all branches with pagination.
     */
    @Override
    public Page<BrancheDTO> getAllBranches(Pageable pageable) {
        return brancheRepository.findAll(pageable)
                .map(mapper::toBrancheDTO);
    }

    /**
     * Retrieves a branch by its ID.
     */
    @Override
    public BrancheDTO getBrancheById(Long id) throws CustomException {
        Branche branche = brancheRepository.findById(id)
                .orElseThrow(() -> new CustomException("Branche not found"));
        return mapper.toBrancheDTO(branche);
    }

    /**
     * Creates a new branch.
     */
    @Override
    public BrancheDTO createBranche(BrancheDTO brancheDTO) throws CustomException {
        try {
            Branche branche = mapper.toBranche(brancheDTO);
            branche = brancheRepository.save(branche);
            return mapper.toBrancheDTO(branche);
        } catch (Exception e) {
            throw new CustomException("Failed to create branche", e);
        }
    }

    /**
     * Updates an existing branch.
     */
    @Override
    public BrancheDTO updateBranche(Long id, BrancheDTO brancheDTO) throws CustomException {
        Branche branche = brancheRepository.findById(id)
                .orElseThrow(() -> new CustomException("Branche not found"));
        branche.setNiveau(brancheDTO.getNiveau());
        branche.setSpecialite(brancheDTO.getSpecialite());
        branche.setNbTD(brancheDTO.getNbTD());
        branche.setDepartement(brancheDTO.getDepartement());
        branche = brancheRepository.save(branche);
        return mapper.toBrancheDTO(branche);
    }

    /**
     * Deletes a branch by its ID.
     */
    @Override
    public void deleteBranche(Long id) throws CustomException {
        if (!brancheRepository.existsById(id)) {
            throw new CustomException("Branche not found");
        }
        brancheRepository.deleteById(id);
    }
}
