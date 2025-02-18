package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.FichierExcelDTO;
import com.scheduling.universityschedule_backend.dto.SeanceDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.FichierExcel;
import com.scheduling.universityschedule_backend.model.Seance;
import com.scheduling.universityschedule_backend.repository.FichierExcelRepository;
import com.scheduling.universityschedule_backend.repository.SeanceRepository;
import com.scheduling.universityschedule_backend.service.ExcelFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ExcelFileServiceImpl implements ExcelFileService {

    @Autowired
    private FichierExcelRepository fichierExcelRepository;

    @Autowired
    private SeanceRepository seanceRepository;

    @Autowired
    private EntityMapper entityMapper;

    @Override
    public FichierExcelDTO findById(Long id) throws CustomException {
        FichierExcel fichierExcel = fichierExcelRepository.findById(id)
                .orElseThrow(() -> new CustomException("File not found with ID: " + id));
        return entityMapper.toFichierExcelDTO(fichierExcel);
    }

    @Override
    public List<FichierExcelDTO> findAll() throws CustomException {
        try {
            List<FichierExcel> fichiersExcel = fichierExcelRepository.findAll();
            return fichiersExcel.stream()
                    .map(entityMapper::toFichierExcelDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve all files", e);
        }
    }

    @Override
    public FichierExcelDTO create(FichierExcelDTO fichierExcelDTO) throws CustomException {
        try {
            FichierExcel fichierExcel = entityMapper.toFichierExcel(fichierExcelDTO);
            FichierExcel savedFichierExcel = fichierExcelRepository.save(fichierExcel);
            return entityMapper.toFichierExcelDTO(savedFichierExcel);
        } catch (Exception e) {
            throw new CustomException("Failed to create file", e);
        }
    }

    @Override
    public FichierExcelDTO update(Long id, FichierExcelDTO fichierExcelDTO) throws CustomException {
        FichierExcel existingFichierExcel = fichierExcelRepository.findById(id)
                .orElseThrow(() -> new CustomException("File not found with ID: " + id));
        entityMapper.updateFromDto(fichierExcelDTO, existingFichierExcel);
        try {
            FichierExcel updatedFichierExcel = fichierExcelRepository.save(existingFichierExcel);
            return entityMapper.toFichierExcelDTO(updatedFichierExcel);
        } catch (Exception e) {
            throw new CustomException("Failed to update file", e);
        }
    }

    @Override
    public void delete(Long id) throws CustomException {
        if (!fichierExcelRepository.existsById(id)) {
            throw new CustomException("File not found with ID: " + id);
        }
        try {
            fichierExcelRepository.deleteById(id);
        } catch (Exception e) {
            throw new CustomException("Failed to delete file", e);
        }
    }

    @Override
    public void upload(FichierExcelDTO file, List<SeanceDTO> seanceDTOS) throws CustomException {
        try {
            FichierExcel fichierExcel = entityMapper.toFichierExcel(file);
            fichierExcel.setImportDate(LocalDateTime.now());
            fichierExcel.setStatus("Processing");
            fichierExcelRepository.save(fichierExcel);

            List<Seance> seances = seanceDTOS.stream()
                    .map(entityMapper::toSeance)
                    .collect(Collectors.toList());
            seanceRepository.saveAll(seances);

            fichierExcel.setStatus("Completed");
            fichierExcelRepository.save(fichierExcel);
        } catch (Exception e) {
            throw new CustomException("Failed to upload and process file", e);
        }
    }

    @Override
    public List<FichierExcelDTO> getImportHistory() throws CustomException {
        try {
            List<FichierExcel> fichiersExcel = fichierExcelRepository.findAll();
            return fichiersExcel.stream()
                    .map(entityMapper::toFichierExcelDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve import history", e);
        }
    }
}