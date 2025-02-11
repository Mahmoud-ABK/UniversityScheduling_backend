package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.TechnicienDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.Technicien;
import com.scheduling.universityschedule_backend.repository.TechnicienRepository;
import com.scheduling.universityschedule_backend.service.TechnicienService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class TechnicienServiceImpl implements TechnicienService {

    private final TechnicienRepository technicienRepository;
    private final EntityMapper mapper;

    @Autowired
    public TechnicienServiceImpl(TechnicienRepository technicienRepository, EntityMapper mapper) {
        this.technicienRepository = technicienRepository;
        this.mapper = mapper;
    }

    /**
     * Retrieves all technicians.
     */
    @Override
    public List<TechnicienDTO> getAllTechniciens() throws CustomException {
        List<Technicien> techs = technicienRepository.findAll();
        List<TechnicienDTO> dtoList = new ArrayList<>();
        techs.forEach(t -> dtoList.add(mapper.toTechnicienDTO(t)));
        return dtoList;
    }

    /**
     * Retrieves a technician by its ID.
     */
    @Override
    public TechnicienDTO getTechnicienById(Long id) throws CustomException {
        Technicien tech = technicienRepository.findById(id)
                .orElseThrow(() -> new CustomException("Technicien not found"));
        return mapper.toTechnicienDTO(tech);
    }

    /**
     * Creates a new technician.
     */
    @Override
    public TechnicienDTO createTechnicien(TechnicienDTO technicienDTO) throws CustomException {
        try {
            Technicien tech = mapper.toTechnicien(technicienDTO);
            tech = technicienRepository.save(tech);
            return mapper.toTechnicienDTO(tech);
        } catch (Exception e) {
            throw new CustomException("Failed to create Technicien", e);
        }
    }

    /**
     * Updates an existing technician.
     */
    @Override
    public TechnicienDTO updateTechnicien(Long id, TechnicienDTO technicienDTO) throws CustomException {
        Technicien tech = technicienRepository.findById(id)
                .orElseThrow(() -> new CustomException("Technicien not found"));
        tech.setCin(technicienDTO.getCin());
        tech.setNom(technicienDTO.getNom());
        tech.setPrenom(technicienDTO.getPrenom());
        tech.setEmail(technicienDTO.getEmail());
        tech.setTel(technicienDTO.getTel());
        tech.setAdresse(technicienDTO.getAdresse());
        tech.setCodeTechnicien(technicienDTO.getCodeTechnicien());
        tech = technicienRepository.save(tech);
        return mapper.toTechnicienDTO(tech);
    }

    /**
     * Deletes a technician by its ID.
     */
    @Override
    public void deleteTechnicien(Long id) throws CustomException {
        if (!technicienRepository.existsById(id)) {
            throw new CustomException("Technicien not found");
        }
        technicienRepository.deleteById(id);
    }
}
