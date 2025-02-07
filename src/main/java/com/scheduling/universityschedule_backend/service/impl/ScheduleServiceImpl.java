package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.model.Branche;
import com.scheduling.universityschedule_backend.model.Seance;
import com.scheduling.universityschedule_backend.model.TD;
import com.scheduling.universityschedule_backend.model.TP;
import com.scheduling.universityschedule_backend.repository.SeanceRepository;
import com.scheduling.universityschedule_backend.repository.TDRepository;
import com.scheduling.universityschedule_backend.repository.TPRepository;
import com.scheduling.universityschedule_backend.repository.BrancheRepository;
import com.scheduling.universityschedule_backend.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final SeanceRepository seanceRepository;
    private final TDRepository tdRepository;
    private final TPRepository tpRepository;
    private final BrancheRepository brancheRepository;

    @Autowired
    public ScheduleServiceImpl(SeanceRepository seanceRepository,
                               TDRepository tdRepository,
                               TPRepository tpRepository,
                               BrancheRepository brancheRepository) {
        this.seanceRepository = seanceRepository;
        this.tdRepository = tdRepository;
        this.tpRepository = tpRepository;
        this.brancheRepository = brancheRepository;
    }

    // Seance operations

    @Override
    public Seance createSeance(Seance seance) {
        return seanceRepository.save(seance);
    }

    @Override
    public Seance getSeanceById(Long id) throws CustomException {
        return seanceRepository.findById(id)
                .orElseThrow(() -> new CustomException("Seance not found with id: " + id));
    }

    @Override
    public List<Seance> getAllSeances() {
        return seanceRepository.findAll();
    }

    @Override
    public Seance updateSeance(Seance seance) throws CustomException {
        if (seance.getId() == null || !seanceRepository.existsById(seance.getId())) {
            throw new CustomException("Seance not found with id: " + seance.getId());
        }
        return seanceRepository.save(seance);
    }

    @Override
    public void updateSeance(Long seanceId, Seance seance) throws CustomException {
        Seance existing = seanceRepository.findById(seanceId)
                .orElseThrow(() -> new CustomException("Seance not found with id: " + seanceId));
        // Update the fields (except id)
        existing.setJour(seance.getJour());
        existing.setHeureDebut(seance.getHeureDebut());
        existing.setHeureFin(seance.getHeureFin());
        existing.setType(seance.getType());
        existing.setMatiere(seance.getMatiere());
        existing.setFrequence(seance.getFrequence());
        existing.setSalle(seance.getSalle());
        existing.setEnseignant(seance.getEnseignant());
        seanceRepository.save(existing);
    }

    @Override
    public void deleteSeance(Long id) throws CustomException {
        if (!seanceRepository.existsById(id)) {
            throw new CustomException("Seance not found with id: " + id);
        }
        seanceRepository.deleteById(id);
    }

    // TD operations

    @Override
    public TD createTD(TD td) {
        return tdRepository.save(td);
    }

    @Override
    public Optional<TD> getTDById(Long id) {
        return tdRepository.findById(id);
    }

    @Override
    public List<TD> getAllTDs() {
        return tdRepository.findAll();
    }

    @Override
    public TD updateTD(TD td) throws CustomException {
        if (td.getId() == null || !tdRepository.existsById(td.getId())) {
            throw new CustomException("TD not found with id: " + td.getId());
        }
        return tdRepository.save(td);
    }

    @Override
    public void updateTD(Long tdId, TD td) throws CustomException {
        TD existing = tdRepository.findById(tdId)
                .orElseThrow(() -> new CustomException("TD not found with id: " + tdId));
        // Update fields – here we update the nbTP and branche (add other fields if available)
        existing.setNbTP(td.getNbTP());
        existing.setBranche(td.getBranche());
        tdRepository.save(existing);
    }

    @Override
    public void deleteTD(Long id) throws CustomException {
        if (!tdRepository.existsById(id)) {
            throw new CustomException("TD not found with id: " + id);
        }
        tdRepository.deleteById(id);
    }

    // TP operations

    @Override
    public TP createTP(TP tp) {
        return tpRepository.save(tp);
    }

    @Override
    public Optional<TP> getTPById(Long id) {
        return tpRepository.findById(id);
    }

    @Override
    public List<TP> getAllTPs() {
        return tpRepository.findAll();
    }

    @Override
    public TP updateTP(TP tp) throws CustomException {
        if (tp.getId() == null || !tpRepository.existsById(tp.getId())) {
            throw new CustomException("TP not found with id: " + tp.getId());
        }
        return tpRepository.save(tp);
    }

    @Override
    public void updateTP(Long tpId, TP tp) throws CustomException {
        TP existing = tpRepository.findById(tpId)
                .orElseThrow(() -> new CustomException("TP not found with id: " + tpId));
        // Update fields – here we update the associated TD and the list of Etudiants (if provided)
        existing.setTd(tp.getTd());
        existing.setEtudiants(tp.getEtudiants());
        tpRepository.save(existing);
    }

    @Override
    public void deleteTP(Long id) throws CustomException {
        if (!tpRepository.existsById(id)) {
            throw new CustomException("TP not found with id: " + id);
        }
        tpRepository.deleteById(id);
    }

    // Query methods

    @Override
    public List<Seance> getSeancesForTeacher(Long enseignantId) {
        List<Seance> allSeances = seanceRepository.findAll();
        return allSeances.stream()
                .filter(seance -> seance.getEnseignant() != null &&
                        seance.getEnseignant().getId().equals(enseignantId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Seance> getSeancesForStudent(Long etudiantId) {
        // Assumption: lecture sessions (type "lecture") are common to all students.
        List<Seance> allSeances = seanceRepository.findAll();
        return allSeances.stream()
                .filter(seance -> "lecture".equalsIgnoreCase(seance.getType()))
                .collect(Collectors.toList());
    }

    @Override
    public int getTeachingHoursForTeacher(Long enseignantId) throws CustomException {
        List<Seance> teacherSeances = getSeancesForTeacher(enseignantId);
        int totalHours = 0;
        for (Seance seance : teacherSeances) {
            try {
                // Parse the start and end times (assumed to be in HH:mm format)
                LocalTime start = LocalTime.parse(seance.getHeureDebut());
                LocalTime end = LocalTime.parse(seance.getHeureFin());
                long minutes = Duration.between(start, end).toMinutes();
                totalHours += (int) (minutes / 60); // Integer division; adjust if you need more precision
            } catch (DateTimeParseException e) {
                throw new CustomException("Invalid time format for seance with id: " + seance.getId());
            }
        }
        return totalHours;
    }

    @Override
    public List<Seance> getSeancesByBranche(Long brancheId) throws CustomException {
        // Assumption: A seance is linked to a branch if its "matiere" equals the branch's specialite.
        Branche branche = brancheRepository.findById(brancheId)
                .orElseThrow(() -> new CustomException("Branche not found with id: " + brancheId));
        String specialite = branche.getSpecialite();
        List<Seance> allSeances = seanceRepository.findAll();
        return allSeances.stream()
                .filter(seance -> seance.getMatiere() != null &&
                        seance.getMatiere().equalsIgnoreCase(specialite))
                .collect(Collectors.toList());
    }
}
