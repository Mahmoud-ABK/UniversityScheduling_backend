package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.FichierExcelDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.FichierExcel;
import com.scheduling.universityschedule_backend.repository.FichierExcelRepository;
import com.scheduling.universityschedule_backend.service.ExcelFileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for Excel file management.
 * Handles upload and tracking of Excel schedule imports.
 */
@Service
@Transactional
public class ExcelFileServiceImpl implements ExcelFileService {

    private final FichierExcelRepository fichierExcelRepository;
    private final EntityMapper mapper;

    public ExcelFileServiceImpl(FichierExcelRepository fichierExcelRepository, EntityMapper mapper) {
        this.fichierExcelRepository = fichierExcelRepository;
        this.mapper = mapper;
    }

    @Override
    public void upload(FichierExcelDTO file) throws CustomException {
        try {
            FichierExcel fichier = mapper.toFichierExcel(file);
            fichier.setImportDate(java.time.LocalDateTime.now());
            fichier.setStatus("Processing");
            fichierExcelRepository.save(fichier);
            fichier.setStatus("Successful");
            fichierExcelRepository.save(fichier);
        } catch (Exception e) {
            throw new CustomException("Excel file upload failed: " + e.getMessage());
        }
    }

    @Override
    public List<FichierExcelDTO> getImportHistory() throws CustomException {
        try {
            return fichierExcelRepository.findAll()
                    .stream()
                    .map(mapper::toFichierExcelDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve import history: " + e.getMessage());
        }
    }
}
