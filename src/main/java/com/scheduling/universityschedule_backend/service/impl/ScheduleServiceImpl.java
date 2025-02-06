package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.model.Seance;
import com.scheduling.universityschedule_backend.model.TD;
import com.scheduling.universityschedule_backend.model.TP;
import com.scheduling.universityschedule_backend.repository.SeanceRepository;
import com.scheduling.universityschedule_backend.repository.TDRepository;
import com.scheduling.universityschedule_backend.repository.TPRepository;
import com.scheduling.universityschedule_backend.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final SeanceRepository seanceRepository;
    private final TDRepository tdRepository;
    private final TPRepository tpRepository;

    @Autowired
    public ScheduleServiceImpl(SeanceRepository seanceRepository, TDRepository tdRepository, TPRepository tpRepository) {
        this.seanceRepository = seanceRepository;
        this.tdRepository = tdRepository;
        this.tpRepository = tpRepository;
    }

    // Seance operations
    @Override
    public Seance createSeance(Seance seance) {
        return seanceRepository.save(seance);
    }

    @Override
    public Optional<Seance> getSeanceById(Long id) {
        return seanceRepository.findById(id);
    }

    @Override
    public List<Seance> getAllSeances() {
        return seanceRepository.findAll();
    }

    @Override
    public Seance updateSeance(Seance seance) {
        return seanceRepository.save(seance);
    }

    @Override
    public void deleteSeance(Long id) {
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
    public TD updateTD(TD td) {
        return tdRepository.save(td);
    }

    @Override
    public void deleteTD(Long id) {
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
    public TP updateTP(TP tp) {
        return tpRepository.save(tp);
    }

    @Override
    public void deleteTP(Long id) {
        tpRepository.deleteById(id);
    }
}
