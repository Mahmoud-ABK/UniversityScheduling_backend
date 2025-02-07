package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.model.FichierExcel;
import com.scheduling.universityschedule_backend.model.Salle;
import com.scheduling.universityschedule_backend.repository.FichierExcelRepository;
import com.scheduling.universityschedule_backend.repository.SalleRepository;
import com.scheduling.universityschedule_backend.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResourceServiceImpl implements ResourceService {

    private final SalleRepository salleRepository;
    private final FichierExcelRepository fichierExcelRepository;

    @Autowired
    public ResourceServiceImpl(SalleRepository salleRepository, FichierExcelRepository fichierExcelRepository) {
        this.salleRepository = salleRepository;
        this.fichierExcelRepository = fichierExcelRepository;
    }

    // Salle operations

    @Override
    public Salle createSalle(Salle salle) {
        return salleRepository.save(salle);
    }

    @Override
    public Optional<Salle> getSalleById(Long id) {
        return salleRepository.findById(id);
    }

    @Override
    public List<Salle> getAllSalles() {
        return salleRepository.findAll();
    }

    @Override
    public Salle updateSalle(Salle salle) throws CustomException {
        if (salle.getId() == null || !salleRepository.existsById(salle.getId())) {
            throw new CustomException("Salle not found with id: " + salle.getId());
        }
        // Retrieve the existing salle, update its fields, and save
        Salle existingSalle = salleRepository.findById(salle.getId()).get();
        existingSalle.setIdentifiant(salle.getIdentifiant());
        existingSalle.setType(salle.getType());
        existingSalle.setCapacite(salle.getCapacite());
        existingSalle.setDisponibilite(salle.getDisponibilite());
        return salleRepository.save(existingSalle);
    }

    @Override
    public void deleteSalle(Long id) throws CustomException {
        if (!salleRepository.existsById(id)) {
            throw new CustomException("Salle not found with id: " + id);
        }
        salleRepository.deleteById(id);
    }

    // FichierExcel operations

    @Override
    public FichierExcel importExcel(FichierExcel fichierExcel) {
        return fichierExcelRepository.save(fichierExcel);
    }

    @Override
    public Optional<FichierExcel> getFichierExcelById(Long id) {
        return fichierExcelRepository.findById(id);
    }

    @Override
    public List<FichierExcel> getAllFichiersExcel() {
        return fichierExcelRepository.findAll();
    }

    @Override
    public void deleteFichierExcel(Long id) throws CustomException {
        if (!fichierExcelRepository.existsById(id)) {
            throw new CustomException("FichierExcel not found with id: " + id);
        }
        fichierExcelRepository.deleteById(id);
    }

    // New method: Retrieve a list of available rooms for a given time slot

    @Override
    public List<Salle> getAvailableSalles(String timeSlot) throws CustomException {
        if (timeSlot == null || timeSlot.trim().isEmpty()) {
            throw new CustomException("Time slot must be provided");
        }
        List<Salle> allSalles = salleRepository.findAll();
        return allSalles.stream()
                .filter(salle -> salle.getDisponibilite() != null && salle.getDisponibilite().contains(timeSlot))
                .collect(Collectors.toList());
    }
}
