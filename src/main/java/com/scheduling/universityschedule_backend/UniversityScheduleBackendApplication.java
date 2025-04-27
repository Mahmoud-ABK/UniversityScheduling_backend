package com.scheduling.universityschedule_backend;

import com.scheduling.universityschedule_backend.dto.SeanceDTO;
import com.scheduling.universityschedule_backend.dto.SingleSeanceConflictDTO;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.mapper.EntityMapper;
import com.scheduling.universityschedule_backend.model.FrequenceType;
import com.scheduling.universityschedule_backend.model.Seance;
import com.scheduling.universityschedule_backend.repository.SeanceRepository;
import com.scheduling.universityschedule_backend.service.SeanceService;
import com.scheduling.universityschedule_backend.util.CustomLogger;
import com.scheduling.universityschedule_backend.testingclasses.* ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@SpringBootApplication
public class UniversityScheduleBackendApplication implements CommandLineRunner {

	@Autowired
	private JPAtest jpatester;

	@Autowired
	private EntityMapperTester entityMapperTester;
	@Autowired
	ServiceTest serviceTest;
	@Autowired
	SeanceRepository seanceRepository;
	@Autowired
	EntityMapper entityMapper;
    @Autowired
    private SeanceService seanceService;

	public static void main(String[] args) {
		SpringApplication.run(UniversityScheduleBackendApplication.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) {
		try {
			CustomLogger.logInfo("==================== service test======================");
			Seance seance = seanceRepository.findById(1L).orElseThrow(() -> new CustomException("Session not found with "));
			SeanceDTO seanceDTO = entityMapper.toSeanceDTO(seance);
			CustomLogger.logInfo("===================== seance DTO=====================");
			CustomLogger.logInfo(seanceDTO.toString());
			CustomLogger.logInfo("===================== using id=====================");
			List<SingleSeanceConflictDTO> conflicts = seanceService.getConflictsForSession(seanceDTO.getId());
			for (SingleSeanceConflictDTO conflict : conflicts) {
				SeanceDTO s = seanceService.findById(conflict.getSeanceId());
				CustomLogger.logInfo(s.toString());
				CustomLogger.logInfo("info" + conflict.getConflictTypes());
			}

			CustomLogger.logInfo("==================== using dto=======================");
			List<Object[]> conflicts1 = seanceRepository.findConflictsForSeance(
					seanceDTO.getSalleId(),
					seanceDTO.getEnseignantId(),
					seanceDTO.getTpIds(),
					seanceDTO.getTdIds(),
					seanceDTO.getBrancheIds(),
					DayOfWeek.valueOf(seanceDTO.getJour()),
					LocalTime.parse(seanceDTO.getHeureDebut()),
					LocalTime.parse(seanceDTO.getHeureFin()),
					FrequenceType.fromString(seanceDTO.getType()),
					null,
					FrequenceType.BIWEEKLY,
					FrequenceType.CATCHUP
			);
			for (Object[] conflict1 : conflicts1) {
				CustomLogger.logInfo(entityMapper.toSingleSeanceConflictDTO(conflict1).toString());
			}
			List<SingleSeanceConflictDTO> aftermapping = entityMapper.toSingleSeanceConflictDTOList(conflicts1);
			for (SingleSeanceConflictDTO aftermapping1 : aftermapping) {
				CustomLogger.logInfo(aftermapping1.toString());
			}






			//serviceTest.testSeanceService();
			CustomLogger.logInfo("==================== service test end ======================");
		} catch (Exception e) {
			CustomLogger.logError("Error during testing: " + e.getMessage());
			e.printStackTrace();
			CustomLogger.logError("==================== FINISHED WITH ERROR ======================");
		}
	}
}