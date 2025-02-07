package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.FichierExcelDTO;
import com.scheduling.universityschedule_backend.dto.SalleDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import java.util.List;
import java.util.Optional;

public interface ResourceService {
    // Salle operations
    SalleDTO createSalle(SalleDTO salle);
    Optional<SalleDTO> getSalleById(Long id);
    List<SalleDTO> getAllSalles();
    SalleDTO updateSalle(SalleDTO salle) throws CustomException;
    void deleteSalle(Long id) throws CustomException;

    // FichierExcel operations
    FichierExcelDTO importExcel(FichierExcelDTO fichierExcel);
    Optional<FichierExcelDTO> getFichierExcelById(Long id);
    List<FichierExcelDTO> getAllFichiersExcel();
    void deleteFichierExcel(Long id) throws CustomException;

    // Retrieve a list of available rooms for a given time slot
    List<SalleDTO> getAvailableSalles(String timeSlot) throws CustomException;
}
