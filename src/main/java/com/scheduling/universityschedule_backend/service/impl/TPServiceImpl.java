package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.EtudiantDTO;
import com.scheduling.universityschedule_backend.dto.TPDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.TP;
import com.scheduling.universityschedule_backend.repository.TPRepository;
import com.scheduling.universityschedule_backend.service.TPService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service implementation for practical session management.
 * Handles operations related to lab sessions.
 */
@Service
@Transactional
public class TPServiceImpl implements TPService {

    private final TPRepository tpRepository;
    private final EntityMapper mapper;

    public TPServiceImpl(TPRepository tpRepository, EntityMapper mapper) {
        this.tpRepository = tpRepository;
        this.mapper = mapper;
    }

    @Override
    public List<TPDTO> findAll() throws CustomException {
        try {
            return tpRepository.findAll().stream().map(mapper::toTPDTO).collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve practical sessions: " + e.getMessage());
        }
    }

    @Override
    public TPDTO findById(Long id) throws CustomException {
        Optional<TP> tpOpt = tpRepository.findById(id);
        if (tpOpt.isEmpty()) {
            throw new CustomException("Practical session not found with id " + id);
        }
        return mapper.toTPDTO(tpOpt.get());
    }

    @Override
    public List<EtudiantDTO> getStudents(Long tpId) throws CustomException {
        TP tp = tpRepository.findById(tpId)
                .orElseThrow(() -> new CustomException("Practical session not found with id " + tpId));
        return tp.getEtudiants().stream()
                .map(mapper::toEtudiantDTO)
                .collect(Collectors.toList());
    }
}
