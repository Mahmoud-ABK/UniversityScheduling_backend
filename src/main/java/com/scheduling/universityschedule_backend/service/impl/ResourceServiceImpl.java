package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.model.FichierExcel;
import com.scheduling.universityschedule_backend.model.Salle;
import com.scheduling.universityschedule_backend.repository.FichierExcelRepository;
import com.scheduling.universityschedule_backend.repository.SalleRepository;
import com.scheduling.universityschedule_backend.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

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
    public Salle updateSalle(Salle salle) {
        return salleRepository.save(salle);
    }

    @Override
    public void deleteSalle(Long id) {
        salleRepository.deleteById(id);
    }

    // FichierExcel operations
    @Override
    public FichierExcel importExcel(FichierExcel fichierExcel) {
        // Add any custom processing logic here if needed.
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
    public void deleteFichierExcel(Long id) {
        fichierExcelRepository.deleteById(id);
    }
}
