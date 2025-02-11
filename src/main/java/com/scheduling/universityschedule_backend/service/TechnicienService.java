package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.* ;
import com.scheduling.universityschedule_backend.exception.CustomException;

import java.util.List;

/**
 * Service interface for managing technicians
 */
public interface TechnicienService {
    List<TechnicienDTO> getAllTechniciens() throws CustomException;
    TechnicienDTO getTechnicienById(Long id) throws CustomException;
    TechnicienDTO createTechnicien(TechnicienDTO technicienDTO) throws CustomException;
    TechnicienDTO updateTechnicien(Long id, TechnicienDTO technicienDTO) throws CustomException;
    void deleteTechnicien(Long id) throws CustomException;
}
