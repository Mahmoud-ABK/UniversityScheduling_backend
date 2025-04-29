package com.scheduling.universityschedule_backend.service.impl;

import com.scheduling.universityschedule_backend.dto.EnseignantDTO;
import com.scheduling.universityschedule_backend.dto.PropositionDeRattrapageDTO;
import com.scheduling.universityschedule_backend.dto.SeanceDTO;
import com.scheduling.universityschedule_backend.dto.SignalDTO;
import com.scheduling.universityschedule_backend.dto.TPDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.*;
import com.scheduling.universityschedule_backend.repository.EnseignantRepository;
import com.scheduling.universityschedule_backend.repository.PropositionDeRattrapageRepository;
import com.scheduling.universityschedule_backend.repository.SeanceRepository;
import com.scheduling.universityschedule_backend.repository.SignalRepository;
import com.scheduling.universityschedule_backend.service.EnseignantService;
import com.scheduling.universityschedule_backend.util.CustomLogger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service implementation for teacher operations.
 * Manages teacher schedules, teaching hours, and communication.
 */
@Service
@Transactional
public class EnseignantServiceImpl implements EnseignantService {

    private final EnseignantRepository enseignantRepository;
    private final SeanceRepository seanceRepository;
    private final EntityMapper entityMapper;
    private final PropositionDeRattrapageRepository propositionDeRattrapageRepository;
    private final SignalRepository signalRepository;

    /**
     * Constructor injection for dependencies
     */
    public EnseignantServiceImpl(EnseignantRepository enseignantRepository,
                                 SeanceRepository seanceRepository,
                                 EntityMapper entityMapper, PropositionDeRattrapageRepository propositionDeRattrapageRepository, SignalRepository signalRepository) {
        this.enseignantRepository = enseignantRepository;
        this.seanceRepository = seanceRepository;
        this.entityMapper = entityMapper;
        this.propositionDeRattrapageRepository = propositionDeRattrapageRepository;
        this.signalRepository = signalRepository;
    }

    @Override
    public EnseignantDTO findById(Long id) throws CustomException {
        try {
            // Validate input
            if (id == null) {
                throw new CustomException("Teacher ID cannot be null");
            }

            // Retrieve teacher
            Enseignant enseignant = enseignantRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Teacher not found with ID: " + id));

            // Convert to DTO
            return entityMapper.toEnseignantDTO(enseignant);
        } catch (CustomException e) {
            throw e; // Rethrow custom exceptions as-is
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve teacher with ID: " + id, e);
        }
    }

    @Override
    public List<EnseignantDTO> findAll() throws CustomException {
        try {
            // Retrieve all teachers
            List<Enseignant> enseignants = enseignantRepository.findAll();

            // Convert to DTOs
            return enseignants.stream()
                    .map(entityMapper::toEnseignantDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve all teachers", e);
        }
    }

    @Override
    public EnseignantDTO create(EnseignantDTO enseignantDTO) throws CustomException {
        try {
            // Validate input
            if (enseignantDTO == null) {
                throw new CustomException("Teacher data cannot be null");
            }

            // Check for duplicate ID if provided
            if (enseignantDTO.getId() != null && enseignantRepository.existsById(enseignantDTO.getId())) {
                throw new CustomException("Teacher with ID " + enseignantDTO.getId() + " already exists");
            }

            // Convert to entity
            Enseignant enseignant = entityMapper.toEnseignant(enseignantDTO);

            // Save entity
            Enseignant savedEnseignant = enseignantRepository.save(enseignant);

            // Convert back to DTO
            return entityMapper.toEnseignantDTO(savedEnseignant);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to create teacher", e);
        }
    }

    @Override
    public EnseignantDTO update(Long id, EnseignantDTO enseignantDTO) throws CustomException {
        try {
            // Validate inputs
            if (id == null) {
                throw new CustomException("Teacher ID cannot be null");
            }

            if (enseignantDTO == null) {
                throw new CustomException("Teacher data cannot be null");
            }

            // Find existing teacher
            Enseignant existingEnseignant = enseignantRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Teacher not found with ID: " + id));

            // Update entity with DTO data
            entityMapper.updateFromDto(enseignantDTO, existingEnseignant);

            // Save updated entity
            Enseignant updatedEnseignant = enseignantRepository.save(existingEnseignant);

            // Convert back to DTO
            return entityMapper.toEnseignantDTO(updatedEnseignant);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to update teacher with ID: " + id, e);
        }
    }

    @Override
    public void delete(Long id) throws CustomException {
        try {
            // Validate input
            if (id == null) {
                throw new CustomException("Teacher ID cannot be null");
            }

            // Check if teacher exists
            if (!enseignantRepository.existsById(id)) {
                throw new CustomException("Teacher not found with ID: " + id);
            }

            // Delete teacher
            enseignantRepository.deleteById(id);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to delete teacher with ID: " + id, e);
        }
    }

    @Override
    public List<SeanceDTO> getSchedule(Long id) throws CustomException {
        try {
            // Validate input
            if (id == null) {
                throw new CustomException("Teacher ID cannot be null");
            }

            // Find teacher
            Enseignant enseignant = enseignantRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Teacher not found with ID: " + id));

            // Get sessions from teacher - using safe access pattern for collections
            if (enseignant.getSeances() == null) {
                return Collections.emptyList();
            }

            // Convert to DTOs
            return enseignant.getSeances().stream()
                    .filter(Objects::nonNull)
                    .map(entityMapper::toSeanceDTO)
                    .collect(Collectors.toList());
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve schedule for teacher with ID: " + id, e);
        }
    }
    /**
     * Calculates total teaching hours for a teacher within a specific date range.
     * Weekly sessions count once per week, bi-weekly sessions count as half per week,
     * and makeup sessions with specific dates count only once if within range.
     *
     * @param teacherid Teacher's unique identifier
     * @param startdate Start date for calculation
     * @param enddate End date for calculation
     * @return Total hours taught between start and end date
     * @throws CustomException if calculation fails or dates are invalid
     */
    @Override
    public int getTotalTeachingHours(Long teacherid, LocalDate startdate, LocalDate enddate) throws CustomException {
        try {
            // Validate inputs
            if (teacherid == null || startdate == null || enddate == null) {
                throw new CustomException("Teacher ID and date range cannot be null");
            }

            if (startdate.isAfter(enddate)) {
                throw new CustomException("Start date must be before or equal to end date");
            }

            // Find teacher
            Enseignant enseignant = enseignantRepository.findById(teacherid)
                    .orElseThrow(() -> new CustomException("Teacher not found with ID: " + teacherid));

            // Get all sessions for the teacher
            List<Seance> sessions = enseignant.getSeances();
            if (sessions == null || sessions.isEmpty()) {
                return 0;
            }

            int totalHours = 0;

            // Process each session
            for (Seance seance : sessions) {
                if (seance == null || seance.getFrequence() == null) {
                    continue;
                }

                // Calculate session duration
                int sessionHours = getSessionHours(seance);
                if (sessionHours <= 0) continue;

                // Add hours based on session type
                switch (seance.getFrequence()) {
                    case WEEKLY:
                        totalHours += getWeeklyHours(seance, sessionHours, startdate, enddate);
                        break;

                    case BIWEEKLY:
                        totalHours += getBiweeklyHours(seance, sessionHours, startdate, enddate);
                        break;

                    case CATCHUP:
                        totalHours += getCatchupHours(seance, sessionHours, startdate, enddate);
                        break;
                }
            }

            return totalHours;

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to calculate teaching hours: " + e.getMessage(), e);
        }
    }

    /**
     * Calculates session duration in hours
     */
    private int getSessionHours(Seance seance) {
        try {
            LocalTime start = seance.getHeureDebut();
            LocalTime end = seance.getHeureFin();
            return (int) Math.ceil(Duration.between(start, end).toMinutes() / 60.0);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Calculates hours for weekly sessions
     */
    private int getWeeklyHours(Seance seance, int hours, LocalDate start, LocalDate end) {
        try {
            if (seance.getJour() == null) return 0;

            // Get total weeks when this session occurs
            DayOfWeek day = seance.getJour();
            long weeks = getWeeksWithDay(day, start, end);

            return (int) (weeks * hours);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Calculates hours for bi-weekly sessions
     */
    private int getBiweeklyHours(Seance seance, int hours, LocalDate start, LocalDate end) {
        int weeklyHours = getWeeklyHours(seance, hours, start, end);
        return (int) Math.ceil(weeklyHours / 2.0);
    }

    /**
     * Calculates hours for makeup sessions
     */
    private int getCatchupHours(Seance seance, int hours, LocalDate start, LocalDate end) {
        try {
            if (seance.getDate() == null ) return 0;

            LocalDate sessionDate = seance.getDate();
            return (!sessionDate.isBefore(start) && !sessionDate.isAfter(end)) ? hours : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Counts how many times a specific day of week occurs between two dates
     */
    private long getWeeksWithDay(DayOfWeek day, LocalDate start, LocalDate end) {
        // Adjust start date to first occurrence of the day
        LocalDate current = start;
        while (current.getDayOfWeek() != day) {
            current = current.plusDays(1);
            if (current.isAfter(end)) return 0;
        }

        // Count occurrences
        long count = 0;
        while (!current.isAfter(end)) {
            count++;
            current = current.plusWeeks(1);
        }

        return count;
    }
    /**
     * Submits makeup session request.
     * @param id Teacher's unique identifier
     * @param proposition Makeup session proposal DTO
     * @return Created makeup session proposal DTO
     * @throws CustomException if submission fails
     */
    @Override
    public PropositionDeRattrapageDTO submitMakeupRequest(Long id, PropositionDeRattrapageDTO proposition) throws CustomException {
        try {
            // Validate inputs
            if (id == null) {
                throw new CustomException("Teacher ID cannot be null");
            }

            if (proposition == null) {
                throw new CustomException("Makeup session proposal cannot be null");
            }

            // Find teacher
            Enseignant enseignant = enseignantRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Teacher not found with ID: " + id));

            // Convert DTO to entity
            PropositionDeRattrapage propositionEntity = entityMapper.toPropositionDeRattrapage(proposition);

            // Set submission date if not provided
            if (propositionEntity.getDate() == null) {
                propositionEntity.setDate(LocalDateTime.now());
            }

            // Set initial status
            propositionEntity.setStatus(Status.PENDING);

            // Associate proposal with teacher
            propositionEntity.setEnseignant(enseignant);

            // Save the proposition entity
            propositionDeRattrapageRepository.save(propositionEntity);

            // Convert back to DTO
            return entityMapper.toPropositionDeRattrapageDTO(propositionEntity);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to submit makeup session request for teacher with ID: " + id, e);
        }
    }

    /**
     * Submits an issue or suggestion from a teacher.
     * @param id Teacher's unique identifier
     * @param signal Signal DTO containing issue or suggestion
     * @return Created signal DTO
     * @throws CustomException if submission fails
     */
    @Override
    public SignalDTO submitSignal(Long id, SignalDTO signal) throws CustomException {
        try {
            // Validate inputs
            if (id == null) {
                throw new CustomException("Teacher ID cannot be null");
            }

            if (signal == null) {
                throw new CustomException("Signal data cannot be null");
            }

            // Find teacher
            Enseignant enseignant = enseignantRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Teacher not found with ID: " + id));
            CustomLogger.logInfo("teacher found");

            // Convert DTO to entity
            Signal signalEntity = entityMapper.toSignal(signal);
            CustomLogger.logInfo(signalEntity.toString());

            // Set submission date if not provided
            if (signalEntity.getTimestamp() == null) {
                signalEntity.setTimestamp(LocalDateTime.now());
            }

            // Associate signal with teacher
            signalEntity.setEnseignant(enseignant);

            // Add signal to teacher's signals collection (if using bidirectional relationship)
            if (enseignant.getSignals() == null) {
                enseignant.setSignals(new ArrayList<>());
            }
            CustomLogger.logInfo(signalEntity.toString());
            // Save teacher to persist the relationship
            signalRepository.save(signalEntity);

            // Convert back to DTO
            SignalDTO returned = entityMapper.toSignalDTO(signalEntity);
            CustomLogger.logInfo(returned.toString());
            return returned;
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
           // throw new CustomException("Failed to submit signal for teacher with ID: " + id +'\n' + e.toString() );
        throw e ;
        }
    }

    @Override
    public List<SignalDTO> getSignals(Long id) throws CustomException {
        try {
            // Validate input
            if (id == null) {
                throw new CustomException("Teacher ID cannot be null");
            }

            // Find teacher
            Enseignant enseignant = enseignantRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Teacher not found with ID: " + id));

            // Get signals from teacher - using safe access pattern for collections
            if (enseignant.getSignals() == null) {
                return Collections.emptyList();
            }

            // Convert to DTOs
            return enseignant.getSignals().stream()
                    .filter(Objects::nonNull)
                    .map(entityMapper::toSignalDTO)
                    .collect(Collectors.toList());
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve signals for teacher with ID: " + id, e);
        }
    }

    // EnseignantServiceImpl.java
    /**
     * Retrieves all subjects taught by a specific teacher.
     * @param id Teacher's unique identifier
     * @return List of subject names taught by the teacher
     * @throws CustomException if retrieval fails or teacher doesn't exist
     */
    @Override
    public List<String> getSubjects(Long id) throws CustomException {
        try {
            // Validate input
            if (id == null) {
                throw new CustomException("Teacher ID cannot be null");
            }

            // Find teacher
            Enseignant enseignant = enseignantRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Teacher not found with ID: " + id));

            // Get unique subjects from teacher's sessions
            if (enseignant.getSeances() == null) {
                return Collections.emptyList();
            }

            return enseignant.getSeances().stream()
                    .filter(Objects::nonNull)
                    .map(Seance::getMatiere)  // Get subject field from each session
                    .filter(Objects::nonNull)
                    .filter(matiere -> !matiere.trim().isEmpty())
                    .distinct()  // Remove duplicates
                    .collect(Collectors.toList());
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve subjects for teacher with ID: " + id, e);
        }
    }

    /**
     * Retrieves all practical groups (TPs) taught by a specific teacher.
     * @param id Teacher's unique identifier
     * @return List of practical groups (TPs) taught by the teacher
     * @throws CustomException if retrieval fails or teacher doesn't exist
     */
    @Override
    public List<TPDTO> getStudentGroups(Long id) throws CustomException {
        try {
            // Validate input
            if (id == null) {
                throw new CustomException("Teacher ID cannot be null");
            }

            // Find teacher
            Enseignant enseignant = enseignantRepository.findById(id)
                    .orElseThrow(() -> new CustomException("Teacher not found with ID: " + id));

            // Get sessions taught by this teacher
            List<Seance> teacherSeances = enseignant.getSeances();
            if (teacherSeances == null || teacherSeances.isEmpty()) {
                return Collections.emptyList();
            }

            // Extract unique TPs from sessions
            Set<TP> uniqueTPs = new HashSet<>();
            for (Seance seance : teacherSeances) {
                if (seance.getTps() != null) {
                    uniqueTPs.addAll(seance.getTps());
                }
            }

            // Convert to DTOs
            return uniqueTPs.stream()
                    .filter(Objects::nonNull)
                    .map(entityMapper::toTPDTO)
                    .collect(Collectors.toList());
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Failed to retrieve student groups for teacher with ID: " + id, e);
        }
    }
}