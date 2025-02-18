package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.SalleDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.Salle;
import com.scheduling.universityschedule_backend.repository.SalleRepository;
import com.scheduling.universityschedule_backend.service.SalleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SalleServiceImpl implements SalleService {

    @Autowired
    private SalleRepository salleRepository;

    @Autowired
    private EntityMapper entityMapper;

    @Override
    public SalleDTO findById(Long id) throws CustomException {
        Salle salle = salleRepository.findById(id)
                .orElseThrow(() -> new CustomException("Room not found with ID: " + id));
        return entityMapper.toSalleDTO(salle);
    }

    @Override
    public List<SalleDTO> findAll() throws CustomException {
        List<Salle> salles = salleRepository.findAll();
        return salles.stream()
                .map(entityMapper::toSalleDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SalleDTO create(SalleDTO salleDTO) throws CustomException {
        Salle salle = entityMapper.toSalle(salleDTO);
        Salle savedSalle = salleRepository.save(salle);
        return entityMapper.toSalleDTO(savedSalle);
    }

    @Override
    public SalleDTO update(Long id, SalleDTO salleDTO) throws CustomException {
        Salle existingSalle = salleRepository.findById(id)
                .orElseThrow(() -> new CustomException("Room not found with ID: " + id));
        entityMapper.updateFromDto(salleDTO, existingSalle);
        Salle updatedSalle = salleRepository.save(existingSalle);
        return entityMapper.toSalleDTO(updatedSalle);
    }

    @Override
    public void delete(Long id) throws CustomException {
        if (!salleRepository.existsById(id)) {
            throw new CustomException("Room not found with ID: " + id);
        }
        salleRepository.deleteById(id);
    }

    @Override
    public List<SalleDTO> getAvailableRooms(String date, String day, String startTime, String endTime) throws CustomException {
        // Implement the logic to find available rooms based on the provided parameters
        // This method might require custom queries in the SalleRepository to find available rooms
        throw new UnsupportedOperationException("Method not implemented yet");
    }
}