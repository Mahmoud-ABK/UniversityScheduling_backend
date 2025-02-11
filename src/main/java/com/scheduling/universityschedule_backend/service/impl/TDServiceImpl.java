package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.TDDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.TD;
import com.scheduling.universityschedule_backend.repository.TDRepository;
import com.scheduling.universityschedule_backend.service.TDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class TDServiceImpl implements TDService {

    private final TDRepository tdRepository;
    private final EntityMapper mapper;

    @Autowired
    public TDServiceImpl(TDRepository tdRepository, EntityMapper mapper) {
        this.tdRepository = tdRepository;
        this.mapper = mapper;
    }

    /**
     * Retrieves all tutorial sessions (TDs).
     */
    @Override
    public List<TDDTO> getAllTDs() throws CustomException {
        List<TD> tds = tdRepository.findAll();
        List<TDDTO> dtoList = new ArrayList<>();
        tds.forEach(td -> dtoList.add(mapper.toTDDTO(td)));
        return dtoList;
    }

    /**
     * Retrieves a TD by its ID.
     */
    @Override
    public TDDTO getTDById(Long id) throws CustomException {
        TD td = tdRepository.findById(id)
                .orElseThrow(() -> new CustomException("TD not found"));
        return mapper.toTDDTO(td);
    }

    /**
     * Creates a new TD.
     */
    @Override
    public TDDTO createTD(TDDTO tdDTO) throws CustomException {
        try {
            TD td = mapper.toTD(tdDTO);
            td = tdRepository.save(td);
            return mapper.toTDDTO(td);
        } catch (Exception e) {
            throw new CustomException("Failed to create TD", e);
        }
    }

    /**
     * Updates an existing TD.
     */
    @Override
    public TDDTO updateTD(Long id, TDDTO tdDTO) throws CustomException {
        TD td = tdRepository.findById(id)
                .orElseThrow(() -> new CustomException("TD not found"));
        td.setNb(tdDTO.getNb());
        td.setNbTP(tdDTO.getNbTP());
        td = tdRepository.save(td);
        return mapper.toTDDTO(td);
    }

    /**
     * Deletes a TD by its ID.
     */
    @Override
    public void deleteTD(Long id) throws CustomException {
        if (!tdRepository.existsById(id)) {
            throw new CustomException("TD not found");
        }
        tdRepository.deleteById(id);
    }
}
