package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.model.FichierExcel;
import com.scheduling.universityschedule_backend.model.Salle;
import java.util.List;
import java.util.Optional;

public interface ResourceService {
    // Salle operations
    Salle createSalle(Salle salle);
    Optional<Salle> getSalleById(Long id);
    List<Salle> getAllSalles();
    Salle updateSalle(Salle salle) throws CustomException;
    void deleteSalle(Long id) throws CustomException;

    // FichierExcel operations
    FichierExcel importExcel(FichierExcel fichierExcel);
    Optional<FichierExcel> getFichierExcelById(Long id);
    List<FichierExcel> getAllFichiersExcel();
    void deleteFichierExcel(Long id) throws CustomException;
    // **New Method:**
    // Retrieve a list of available rooms for a given time slot
    List<Salle> getAvailableSalles(String timeSlot) throws CustomException;
}
