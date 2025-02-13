package com.scheduling.universityschedule_backend;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.service.AdministrateurService;
import com.scheduling.universityschedule_backend.service.BrancheService;
import com.scheduling.universityschedule_backend.service.EnseignantService;
import com.scheduling.universityschedule_backend.service.EtudiantService;
import com.scheduling.universityschedule_backend.service.ExcelFileService;
import com.scheduling.universityschedule_backend.service.NotificationService;
import com.scheduling.universityschedule_backend.service.SalleService;
import com.scheduling.universityschedule_backend.service.SeanceService;
import com.scheduling.universityschedule_backend.service.TDService;
import com.scheduling.universityschedule_backend.service.TPService;
import com.scheduling.universityschedule_backend.util.CustomLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class UniversityScheduleBackendApplication implements CommandLineRunner {

	// Autowire the service layer beans
	@Autowired
	private AdministrateurService administrateurService;

	@Autowired
	private BrancheService brancheService;

	@Autowired
	private EnseignantService enseignantService;

	@Autowired
	private EtudiantService etudiantService;

	@Autowired
	private ExcelFileService fichierExcelService;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private SalleService salleService;

	@Autowired
	private SeanceService seanceService;

	@Autowired
	private TDService tdService;

	@Autowired
	private TPService tpService;

	public static void main(String[] args) {
		SpringApplication.run(UniversityScheduleBackendApplication.class, args);
	}

	@Override
	public void run(String... args) {

			}
}