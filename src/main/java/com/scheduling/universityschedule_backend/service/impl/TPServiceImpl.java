package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.SeanceDTO;
import com.scheduling.universityschedule_backend.dto.TPDTO;
import com.scheduling.universityschedule_backend.dto.EtudiantDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.TP;
import com.scheduling.universityschedule_backend.repository.TPRepository;
import com.scheduling.universityschedule_backend.service.TPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TPServiceImpl implements TPService {

    @Autowired
    private TPRepository tpRepository;

    @Autowired
    private EntityMapper entityMapper;

    @Override
    public List<TPDTO> findAll() throws CustomException {
        List<TP> tps = tpRepository.findAll();
        return tps.stream()
                .map(entityMapper::toTPDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TPDTO findById(Long id) throws CustomException {
        TP tp = tpRepository.findById(id)
                .orElseThrow(() -> new CustomException("Practical session not found with ID: " + id));
        return entityMapper.toTPDTO(tp);
    }

    @Override
    public TPDTO create(TPDTO tpDTO) throws CustomException {
        TP tp = entityMapper.toTP(tpDTO);
        TP savedTP = tpRepository.save(tp);
        return entityMapper.toTPDTO(savedTP);
    }

    @Override
    public TPDTO update(Long id, TPDTO tpDTO) throws CustomException {
        TP existingTP = tpRepository.findById(id)
                .orElseThrow(() -> new CustomException("Practical session not found with ID: " + id));
        entityMapper.updateFromDto(tpDTO, existingTP);
        TP updatedTP = tpRepository.save(existingTP);
        return entityMapper.toTPDTO(updatedTP);
    }

    @Override
    public void delete(Long id) throws CustomException {
        if (!tpRepository.existsById(id)) {
            throw new CustomException("Practical session not found with ID: " + id);
        }
        tpRepository.deleteById(id);
    }

    @Override
    public List<EtudiantDTO> getStudents(Long tpId) throws CustomException {
        TP tp = tpRepository.findById(tpId)
                .orElseThrow(() -> new CustomException("Practical session not found with ID: " + tpId));
        return tp.getEtudiants().stream()
                .map(entityMapper::toEtudiantDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SeanceDTO> generateSchedule(Long id) throws CustomException {
        // Implement the logic to generate the schedule for the given practical session

        // Step 1: Retrieve the TP (Practical Session) entity from the repository
        TP tp = tpRepository.findById(id)
                .orElseThrow(() -> new CustomException("Practical session not found with ID: " + id));

        // Step 2: Convert the list of Seance entities (associated with TP) to SeanceDTOs
        return tp.getSeances().stream()
                .map(entityMapper::toSeanceDTO)  // Map each Seance entity to a SeanceDTO
                .collect(Collectors.toList());  // Collect the results into a List of SeanceDTOs
    }


    @Override
    public List<EtudiantDTO> getEtudiants(Long id) throws CustomException {
        TP tp = tpRepository.findById(id)
                .orElseThrow(() -> new CustomException("Practical session not found with ID: " + id));
        return tp.getEtudiants().stream()
                .map(entityMapper::toEtudiantDTO)
                .collect(Collectors.toList());
    }
}