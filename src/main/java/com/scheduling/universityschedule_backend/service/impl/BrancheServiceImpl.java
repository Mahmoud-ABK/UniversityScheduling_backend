package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.BrancheDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.Branche;
import com.scheduling.universityschedule_backend.repository.BrancheRepository;
import com.scheduling.universityschedule_backend.service.BrancheService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service implementation for branch management.
 * Handles CRUD operations for academic branches.
 */
@Service
@Transactional
public class BrancheServiceImpl implements BrancheService {

    private final BrancheRepository brancheRepository;
    private final EntityMapper mapper;

    public BrancheServiceImpl(BrancheRepository brancheRepository, EntityMapper mapper) {
        this.brancheRepository = brancheRepository;
        this.mapper = mapper;
    }

    @Override
    public List<BrancheDTO> findAll() throws CustomException {
        try {
            return brancheRepository.findAll()
                    .stream()
                    .map(mapper::toBrancheDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve branches: " + e.getMessage());
        }
    }

    @Override
    public BrancheDTO findById(Long id) throws CustomException {
        Optional<Branche> brancheOpt = brancheRepository.findById(id);
        if (brancheOpt.isEmpty()) {
            throw new CustomException("Branche not found with id " + id);
        }
        return mapper.toBrancheDTO(brancheOpt.get());
    }

    @Override
    public BrancheDTO create(BrancheDTO brancheDTO) throws CustomException {
        try {
            Branche branche = mapper.toBranche(brancheDTO);
            branche = brancheRepository.save(branche);
            return mapper.toBrancheDTO(branche);
        } catch (Exception e) {
            throw new CustomException("Failed to create branch: " + e.getMessage());
        }
    }

    @Override
    public BrancheDTO update(Long id, BrancheDTO brancheDTO) throws CustomException {
        Branche existing = brancheRepository.findById(id)
                .orElseThrow(() -> new CustomException("Branche not found with id " + id));
        existing.setNiveau(brancheDTO.getNiveau());
        existing.setSpecialite(brancheDTO.getSpecialite());
        existing.setNbTD(brancheDTO.getNbTD());
        existing.setDepartement(brancheDTO.getDepartement());
        brancheRepository.save(existing);
        return mapper.toBrancheDTO(existing);
    }

    @Override
    public void delete(Long id) throws CustomException {
        if (!brancheRepository.existsById(id)) {
            throw new CustomException("Branche not found with id " + id);
        }
        brancheRepository.deleteById(id);
    }
}
