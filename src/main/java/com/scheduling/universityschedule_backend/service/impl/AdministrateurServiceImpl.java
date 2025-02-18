package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.AdministrateurDTO;
import com.scheduling.universityschedule_backend.dto.PropositionDeRattrapageDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.Administrateur;
import com.scheduling.universityschedule_backend.model.PropositionDeRattrapage;
import com.scheduling.universityschedule_backend.repository.AdministrateurRepository;
import com.scheduling.universityschedule_backend.repository.PropositionDeRattrapageRepository;
import com.scheduling.universityschedule_backend.service.AdministrateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdministrateurServiceImpl implements AdministrateurService {

    @Autowired
    private AdministrateurRepository administrateurRepository;

    @Autowired
    private PropositionDeRattrapageRepository propositionDeRattrapageRepository;

    @Autowired
    private EntityMapper entityMapper;

    @Override
    public AdministrateurDTO findById(Long id) throws CustomException {
        Administrateur administrateur = administrateurRepository.findById(id)
                .orElseThrow(() -> new CustomException("Administrator not found with ID: " + id));
        return entityMapper.toAdministrateurDTO(administrateur);
    }

    @Override
    public List<AdministrateurDTO> findAll() throws CustomException {
        List<Administrateur> administrateurs = administrateurRepository.findAll();
        return administrateurs.stream()
                .map(entityMapper::toAdministrateurDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AdministrateurDTO create(AdministrateurDTO administrateurDTO) throws CustomException {
        Administrateur administrateur = entityMapper.toAdministrateur(administrateurDTO);
        Administrateur savedAdministrateur = administrateurRepository.save(administrateur);
        return entityMapper.toAdministrateurDTO(savedAdministrateur);
    }

    @Override
    public AdministrateurDTO update(Long id, AdministrateurDTO administrateurDTO) throws CustomException {
        Administrateur existingAdministrateur = administrateurRepository.findById(id)
                .orElseThrow(() -> new CustomException("Administrator not found with ID: " + id));
        Administrateur updatedAdministrateur = administrateurRepository.save(entityMapper.toAdministrateur(administrateurDTO));
        return entityMapper.toAdministrateurDTO(updatedAdministrateur);
    }

    @Override
    public void delete(Long id) throws CustomException {
        if (!administrateurRepository.existsById(id)) {
            throw new CustomException("Administrator not found with ID: " + id);
        }
        administrateurRepository.deleteById(id);
    }

    @Override
    public List<PropositionDeRattrapageDTO> getAllMakeupSessions() throws CustomException {
        List<PropositionDeRattrapage> propositions = propositionDeRattrapageRepository.findAll();
        return propositions.stream()
                .map(entityMapper::toPropositionDeRattrapageDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void approveMakeupSession(Long id) throws CustomException {
        PropositionDeRattrapage proposition = propositionDeRattrapageRepository.findById(id)
                .orElseThrow(() -> new CustomException("Makeup session proposal not found with ID: " + id));
        proposition.setStatus("approved");
        propositionDeRattrapageRepository.save(proposition);
    }

    @Override
    public void rejectMakeupSession(Long id) throws CustomException {
        PropositionDeRattrapage proposition = propositionDeRattrapageRepository.findById(id)
                .orElseThrow(() -> new CustomException("Makeup session proposal not found with ID: " + id));
        proposition.setStatus("rejected");
        propositionDeRattrapageRepository.save(proposition);
    }
}
