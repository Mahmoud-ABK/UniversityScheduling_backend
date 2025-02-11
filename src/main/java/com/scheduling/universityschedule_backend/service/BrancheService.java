package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.* ;

import com.scheduling.universityschedule_backend.exception.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for managing branches/programs
 */
public interface BrancheService {
    Page<BrancheDTO> getAllBranches(Pageable pageable);
    BrancheDTO getBrancheById(Long id) throws CustomException;
    BrancheDTO createBranche(BrancheDTO brancheDTO) throws CustomException ;
    BrancheDTO updateBranche(Long id, BrancheDTO brancheDTO) throws CustomException ;
    void deleteBranche(Long id) throws CustomException ;
}
