package com.scheduling.universityschedule_backend.service;

import com.scheduling.universityschedule_backend.dto.* ;
import com.scheduling.universityschedule_backend.exception.CustomException;

import java.util.List;

/**
 * Service interface for managing issue reports
 */
public interface SignalService {
    SignalDTO submitSignal(SignalDTO signalDTO) throws CustomException;
    List<SignalDTO> getAllSignals() throws CustomException;
}
