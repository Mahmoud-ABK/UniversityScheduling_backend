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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BrancheServiceImpl implements BrancheService {

    @Autowired
    private BrancheRepository brancheRepository;

    @Autowired
    private TDRepository tdRepository;

    @Autowired
    private EntityMapper entityMapper;

    @Override
    public BrancheDTO findById(Long id) throws CustomException {
        Branche branche = brancheRepository.findById(id)
                .orElseThrow(() -> new CustomException("Branch not found with ID: " + id));
        return entityMapper.toBrancheDTO(branche);
    }

    @Override
    public List<BrancheDTO> findAll() throws CustomException {
        List<Branche> branches = brancheRepository.findAll();
        return branches.stream()
                .map(entityMapper::toBrancheDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BrancheDTO create(BrancheDTO brancheDTO) throws CustomException {
        Branche branche = entityMapper.toBranche(brancheDTO);
        Branche savedBranche = brancheRepository.save(branche);
        return entityMapper.toBrancheDTO(savedBranche);
    }

    @Override
    public BrancheDTO update(Long id, BrancheDTO brancheDTO) throws CustomException {
        Branche existingBranche = brancheRepository.findById(id)
                .orElseThrow(() -> new CustomException("Branch not found with ID: " + id));
        Branche updatedBranche = brancheRepository.save(entityMapper.toBranche(brancheDTO));
        return entityMapper.toBrancheDTO(updatedBranche);
    }

    @Override
    public void delete(Long id) throws CustomException {
        if (!brancheRepository.existsById(id)) {
            throw new CustomException("Branch not found with ID: " + id);
        }
        brancheRepository.deleteById(id);
    }

    @Override
    public List<SeanceDTO> getSchedule(Long branchId) throws CustomException {
        Branche branche = brancheRepository.findById(branchId)
                .orElseThrow(() -> new CustomException("Branch not found with ID: " + branchId));
        List<Seance> seances = branche.getSeances();
        return seances.stream()
                .map(entityMapper::toSeanceDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EtudiantDTO> getEtudiants(Long id) throws CustomException {
        List<Etudiant> etudiants = tdRepository.findAllEtudiantsByBrancheId(id);
        return etudiants.stream()
                .map(entityMapper::toEtudiantDTO)
                .collect(Collectors.toList());
    }
}