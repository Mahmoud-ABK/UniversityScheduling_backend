package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.FichierExcelDTO;
import com.scheduling.universityschedule_backend.dto.NotificationDTO;
import com.scheduling.universityschedule_backend.dto.PropositionDeRattrapageDTO;
import com.scheduling.universityschedule_backend.dto.SeanceDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.FichierExcel;
import com.scheduling.universityschedule_backend.model.Notification;
import com.scheduling.universityschedule_backend.model.PropositionDeRattrapage;
import com.scheduling.universityschedule_backend.model.Seance;
import com.scheduling.universityschedule_backend.repository.FichierExcelRepository;
import com.scheduling.universityschedule_backend.repository.NotificationRepository;
import com.scheduling.universityschedule_backend.repository.PropositionDeRattrapageRepository;
import com.scheduling.universityschedule_backend.repository.SeanceRepository;
import com.scheduling.universityschedule_backend.service.AdministrateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdministrateurServiceImpl implements AdministrateurService {

    private final FichierExcelRepository fichierExcelRepository;
    private final SeanceRepository seanceRepository;
    private final PropositionDeRattrapageRepository propositionRepo;
    private final NotificationRepository notificationRepository;
    private final EntityMapper mapper;

    @Autowired
    public AdministrateurServiceImpl(FichierExcelRepository fichierExcelRepository,
                                     SeanceRepository seanceRepository,
                                     PropositionDeRattrapageRepository propositionRepo,
                                     NotificationRepository notificationRepository,
                                     EntityMapper mapper) {
        this.fichierExcelRepository = fichierExcelRepository;
        this.seanceRepository = seanceRepository;
        this.propositionRepo = propositionRepo;
        this.notificationRepository = notificationRepository;
        this.mapper = mapper;
    }

    /**
     * Imports the schedule (emploi du temps) from an Excel file.
     */
    @Override
    public FichierExcelDTO importEmploiDuTemps(FichierExcelDTO fichierExcelDTO) throws CustomException {
        try {
            FichierExcel fe = mapper.toFichierExcel(fichierExcelDTO);
            fe = fichierExcelRepository.save(fe);
            return mapper.toFichierExcelDTO(fe);
        } catch (Exception e) {
            throw new CustomException("Failed to import emploi du temps", e);
        }
    }

    /**
     * Generates the schedule by retrieving all sessions.
     */
    @Override
    public List<SeanceDTO> genererEmploiDuTemps() throws CustomException {
        try {
            List<Seance> seances = seanceRepository.findAll();
            List<SeanceDTO> seanceDTOList = new ArrayList<>();
            for (Seance seance : seances) {
                seanceDTOList.add(mapper.toSeanceDTO(seance));
            }
            return seanceDTOList;
        } catch (Exception e) {
            throw new CustomException("Failed to generate emploi du temps", e);
        }
    }

    /**
     * Processes a catch-up session proposal (demande de rattrapage) by updating its status.
     */
    @Override
    public PropositionDeRattrapageDTO traiterDemandeRattrapage(Long id, boolean approved) throws CustomException {
        PropositionDeRattrapage proposal = propositionRepo.findById(id)
                .orElseThrow(() -> new CustomException("Proposition de rattrapage not found"));
        proposal.setStatus(approved ? "approved" : "rejected");
        proposal = propositionRepo.save(proposal);
        return mapper.toPropositionDeRattrapageDTO(proposal);
    }

    /**
     * Diffuses (sends) a notification.
     */
    @Override
    public List<NotificationDTO> diffuserNotification(NotificationDTO notificationDTO) throws CustomException {
        try {
            Notification notif = mapper.toNotification(notificationDTO);
            notif = notificationRepository.save(notif);
            List<NotificationDTO> list = new ArrayList<>();
            list.add(mapper.toNotificationDTO(notif));
            return list;
        } catch (Exception e) {
            throw new CustomException("Failed to diffuse notification", e);
        }
    }
}
