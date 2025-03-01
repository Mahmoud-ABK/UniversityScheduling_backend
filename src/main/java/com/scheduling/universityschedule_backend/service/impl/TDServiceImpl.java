package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.SeanceDTO;
import com.scheduling.universityschedule_backend.dto.TDDTO;
import com.scheduling.universityschedule_backend.dto.EtudiantDTO;
import com.scheduling.universityschedule_backend.dto.TPDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.TD;
import com.scheduling.universityschedule_backend.repository.TDRepository;
import com.scheduling.universityschedule_backend.service.TDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TDServiceImpl implements TDService {

    @Autowired
    private TDRepository tdRepository;

    @Autowired
    private EntityMapper entityMapper;

    @Override
    public List<TDDTO> findAll() throws CustomException {
        List<TD> tds = tdRepository.findAll();
        return tds.stream()
                .map(entityMapper::toTDDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TDDTO findById(Long id) throws CustomException {
        TD td = tdRepository.findById(id)
                .orElseThrow(() -> new CustomException("Tutorial group not found with ID: " + id));
        return entityMapper.toTDDTO(td);
    }

    @Override
    public TDDTO create(TDDTO tdDTO) throws CustomException {
        TD td = entityMapper.toTD(tdDTO);
        TD savedTD = tdRepository.save(td);
        return entityMapper.toTDDTO(savedTD);
    }

    @Override
    public TDDTO update(Long id, TDDTO tdDTO) throws CustomException {
        TD existingTD = tdRepository.findById(id)
                .orElseThrow(() -> new CustomException("Tutorial group not found with ID: " + id));
        entityMapper.updateFromDto(tdDTO, existingTD);
        TD updatedTD = tdRepository.save(existingTD);
        return entityMapper.toTDDTO(updatedTD);
    }

    @Override
    public void delete(Long id) throws CustomException {
        if (!tdRepository.existsById(id)) {
            throw new CustomException("Tutorial group not found with ID: " + id);
        }
        tdRepository.deleteById(id);
    }

    @Override
    public List<TPDTO> getTPs(Long tdId) throws CustomException {
        TD td = tdRepository.findById(tdId)
                .orElseThrow(() -> new CustomException("Tutorial group not found with ID: " + tdId));
        return td.getTpList().stream()
                .map(entityMapper::toTPDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SeanceDTO> generateSchedule(Long id) throws CustomException {
        // Step 1: Retrieve the TD (Practical Session for TD) entity from the repository
        TD td = tdRepository.findById(id)
                .orElseThrow(() -> new CustomException("TD session not found with ID: " + id));

        // Step 2: Convert the list of Seance entities (associated with TD) to SeanceDTOs
        return td.getSeances().stream()
                .map(entityMapper::toSeanceDTO)  // Map each Seance entity to a SeanceDTO
                .collect(Collectors.toList());  // Collect the results into a List of SeanceDTOs
    }


    @Override
    public List<EtudiantDTO> getEtudiants(Long id) throws CustomException {
        TD td = tdRepository.findById(id)
                .orElseThrow(() -> new CustomException("Tutorial group not found with ID: " + id));
        return td.getTpList().stream()
                .flatMap(tp -> tp.getEtudiants().stream())
                .map(entityMapper::toEtudiantDTO)
                .collect(Collectors.toList());
    }
}