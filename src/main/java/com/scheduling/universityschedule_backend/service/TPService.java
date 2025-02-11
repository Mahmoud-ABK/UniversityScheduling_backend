package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.* ;
import com.scheduling.universityschedule_backend.exception.CustomException;

import java.util.List;

/**
 * Service interface for managing TP groups
 */
public interface TPService {
    List<TPDTO> getAllTPs() throws CustomException;
    TPDTO getTPById(Long id) throws CustomException;
    TPDTO createTP(TPDTO tpDTO) throws CustomException;
    TPDTO updateTP(Long id, TPDTO tpDTO) throws CustomException;
    void deleteTP(Long id) throws CustomException;
}
