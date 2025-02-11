package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.SalleDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.Salle;
import com.scheduling.universityschedule_backend.repository.SalleRepository;
import com.scheduling.universityschedule_backend.service.SalleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class SalleServiceImpl implements SalleService {

    private final SalleRepository salleRepository;
    private final EntityMapper mapper;

    @Autowired
    public SalleServiceImpl(SalleRepository salleRepository, EntityMapper mapper) {
        this.salleRepository = salleRepository;
        this.mapper = mapper;
    }

    /**
     * Retrieves all rooms.
     */
    @Override
    public List<SalleDTO> getAllSalles() throws CustomException {
        List<Salle> salles = salleRepository.findAll();
        List<SalleDTO> dtoList = new ArrayList<>();
        salles.forEach(salle -> dtoList.add(mapper.toSalleDTO(salle)));
        return dtoList;
    }

    /**
     * Retrieves a room by its ID.
     */
    @Override
    public SalleDTO getSalleById(Long id) throws CustomException {
        Salle salle = salleRepository.findById(id)
                .orElseThrow(() -> new CustomException("Salle not found"));
        return mapper.toSalleDTO(salle);
    }

    /**
     * Creates a new room.
     */
    @Override
    public SalleDTO createSalle(SalleDTO salleDTO) throws CustomException {
        try {
            Salle salle = mapper.toSalle(salleDTO);
            salle = salleRepository.save(salle);
            return mapper.toSalleDTO(salle);
        } catch (Exception e) {
            throw new CustomException("Failed to create Salle", e);
        }
    }

    /**
     * Updates an existing room.
     */
    @Override
    public SalleDTO updateSalle(Long id, SalleDTO salleDTO) throws CustomException {
        Salle salle = salleRepository.findById(id)
                .orElseThrow(() -> new CustomException("Salle not found"));
        salle.setIdentifiant(salleDTO.getIdentifiant());
        salle.setType(salleDTO.getType());
        salle.setCapacite(salleDTO.getCapacite());
        salle.setDisponibilite(salleDTO.getDisponibilite());
        salle = salleRepository.save(salle);
        return mapper.toSalleDTO(salle);
    }

    /**
     * Deletes a room by its ID.
     */
    @Override
    public void deleteSalle(Long id) throws CustomException {
        if (!salleRepository.existsById(id)) {
            throw new CustomException("Salle not found");
        }
        salleRepository.deleteById(id);
    }
}
