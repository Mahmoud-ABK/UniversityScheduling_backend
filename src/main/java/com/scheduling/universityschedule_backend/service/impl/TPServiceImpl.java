package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.TPDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.TP;
import com.scheduling.universityschedule_backend.repository.TPRepository;
import com.scheduling.universityschedule_backend.service.TPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class TPServiceImpl implements TPService {

    private final TPRepository tpRepository;
    private final EntityMapper mapper;

    @Autowired
    public TPServiceImpl(TPRepository tpRepository, EntityMapper mapper) {
        this.tpRepository = tpRepository;
        this.mapper = mapper;
    }

    /**
     * Retrieves all practical sessions (TPs).
     */
    @Override
    public List<TPDTO> getAllTPs() throws CustomException {
        List<TP> tps = tpRepository.findAll();
        List<TPDTO> dtoList = new ArrayList<>();
        tps.forEach(tp -> dtoList.add(mapper.toTPDTO(tp)));
        return dtoList;
    }

    /**
     * Retrieves a TP by its ID.
     */
    @Override
    public TPDTO getTPById(Long id) throws CustomException {
        TP tp = tpRepository.findById(id)
                .orElseThrow(() -> new CustomException("TP not found"));
        return mapper.toTPDTO(tp);
    }

    /**
     * Creates a new TP.
     */
    @Override
    public TPDTO createTP(TPDTO tpDTO) throws CustomException {
        try {
            TP tp = mapper.toTP(tpDTO);
            tp = tpRepository.save(tp);
            return mapper.toTPDTO(tp);
        } catch (Exception e) {
            throw new CustomException("Failed to create TP", e);
        }
    }

    /**
     * Updates an existing TP.
     */
    @Override
    public TPDTO updateTP(Long id, TPDTO tpDTO) throws CustomException {
        TP tp = tpRepository.findById(id)
                .orElseThrow(() -> new CustomException("TP not found"));
        tp.setNb(tpDTO.getNb());
        tp = tpRepository.save(tp);
        return mapper.toTPDTO(tp);
    }

    /**
     * Deletes a TP by its ID.
     */
    @Override
    public void deleteTP(Long id) throws CustomException {
        if (!tpRepository.existsById(id)) {
            throw new CustomException("TP not found");
        }
        tpRepository.deleteById(id);
    }
}
