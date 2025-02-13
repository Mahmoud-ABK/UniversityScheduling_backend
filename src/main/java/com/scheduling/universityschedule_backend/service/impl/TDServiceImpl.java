package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.TDDTO;
import com.scheduling.universityschedule_backend.dto.TPDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.TD;
import com.scheduling.universityschedule_backend.model.TP;
import com.scheduling.universityschedule_backend.repository.TDRepository;
import com.scheduling.universityschedule_backend.repository.TPRepository;
import com.scheduling.universityschedule_backend.service.TDService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service implementation for tutorial group management.
 * Handles operations related to tutorial sessions.
 */
@Service
@Transactional
public class TDServiceImpl implements TDService {

    private final TDRepository tdRepository;
    private final TPRepository tpRepository;
    private final EntityMapper mapper;

    public TDServiceImpl(TDRepository tdRepository, TPRepository tpRepository, EntityMapper mapper) {
        this.tdRepository = tdRepository;
        this.tpRepository = tpRepository;
        this.mapper = mapper;
    }

    @Override
    public List<TDDTO> findAll() throws CustomException {
        try {
            return tdRepository.findAll().stream().map(mapper::toTDDTO).collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve tutorial groups: " + e.getMessage());
        }
    }

    @Override
    public TDDTO findById(Long id) throws CustomException {
        Optional<TD> tdOpt = tdRepository.findById(id);
        if (tdOpt.isEmpty()) {
            throw new CustomException("Tutorial group not found with id " + id);
        }
        return mapper.toTDDTO(tdOpt.get());
    }

    @Override
    public List<TPDTO> getTPs(Long tdId) throws CustomException {
        TD td = tdRepository.findById(tdId)
                .orElseThrow(() -> new CustomException("Tutorial group not found with id " + tdId));
        List<TP> tps = td.getTpList();
        return tps.stream().map(mapper::toTPDTO).collect(Collectors.toList());
    }
}
