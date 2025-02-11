package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.SignalDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.Signal;
import com.scheduling.universityschedule_backend.repository.SignalRepository;
import com.scheduling.universityschedule_backend.service.SignalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class SignalServiceImpl implements SignalService {

    private final SignalRepository signalRepository;
    private final EntityMapper mapper;

    @Autowired
    public SignalServiceImpl(SignalRepository signalRepository, EntityMapper mapper) {
        this.signalRepository = signalRepository;
        this.mapper = mapper;
    }

    /**
     * Submits a new signal.
     */
    @Override
    public SignalDTO submitSignal(SignalDTO signalDTO) throws CustomException {
        try {
            Signal signal = mapper.toSignal(signalDTO);
            signal = signalRepository.save(signal);
            return mapper.toSignalDTO(signal);
        } catch (Exception e) {
            throw new CustomException("Failed to submit signal", e);
        }
    }

    /**
     * Retrieves all signals.
     */
    @Override
    public List<SignalDTO> getAllSignals() throws CustomException {
        List<Signal> signals = signalRepository.findAll();
        List<SignalDTO> dtoList = new ArrayList<>();
        signals.forEach(s -> dtoList.add(mapper.toSignalDTO(s)));
        return dtoList;
    }
}
