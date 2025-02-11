package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.FichierExcelDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.FichierExcel;
import com.scheduling.universityschedule_backend.repository.FichierExcelRepository;
import com.scheduling.universityschedule_backend.service.FichierExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FichierExcelServiceImpl implements FichierExcelService {

    private final FichierExcelRepository fichierExcelRepository;
    private final EntityMapper mapper;

    @Autowired
    public FichierExcelServiceImpl(FichierExcelRepository fichierExcelRepository, EntityMapper mapper) {
        this.fichierExcelRepository = fichierExcelRepository;
        this.mapper = mapper;
    }

    /**
     * Imports an Excel file by saving it and returning the saved DTO.
     */
    @Override
    public FichierExcelDTO importerFichierExcel(FichierExcelDTO fichierExcelDTO) throws CustomException {
        try {
            FichierExcel fe = mapper.toFichierExcel(fichierExcelDTO);
            fe = fichierExcelRepository.save(fe);
            return mapper.toFichierExcelDTO(fe);
        } catch (Exception e) {
            throw new CustomException("Failed to import Excel file", e);
        }
    }
}
