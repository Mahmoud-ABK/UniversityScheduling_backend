package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.* ;
import com.scheduling.universityschedule_backend.exception.CustomException;

import java.util.List;

/**
 * Service interface for managing TD groups
 */
public interface TDService {
    List<TDDTO> getAllTDs() throws CustomException;
    TDDTO getTDById(Long id) throws CustomException;
    TDDTO createTD(TDDTO tdDTO) throws CustomException;
    TDDTO updateTD(Long id, TDDTO tdDTO) throws CustomException;
    void deleteTD(Long id) throws CustomException;
}
