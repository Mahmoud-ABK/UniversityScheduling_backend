package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.* ;
import com.scheduling.universityschedule_backend.exception.CustomException;

import java.util.List;

/**
 * Service interface for managing rooms
 */
public interface SalleService {
    List<SalleDTO> getAllSalles() throws CustomException;
    SalleDTO getSalleById(Long id) throws CustomException;
    SalleDTO createSalle(SalleDTO salleDTO) throws CustomException;
    SalleDTO updateSalle(Long id, SalleDTO salleDTO) throws CustomException;
    void deleteSalle(Long id) throws CustomException;
}
