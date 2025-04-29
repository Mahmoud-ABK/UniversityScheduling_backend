package com.scheduling.universityschedule_backend;

import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.testingclasses.ServiceTest;
import com.scheduling.universityschedule_backend.util.CustomLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class UniversityScheduleBackendApplication implements CommandLineRunner {

	private final ServiceTest serviceTest;

	public UniversityScheduleBackendApplication(ServiceTest serviceTest) {
		this.serviceTest = serviceTest;
	}

	public static void main(String[] args) {
		SpringApplication.run(UniversityScheduleBackendApplication.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) {
		try {
			CustomLogger.logInfo("==================== Starting Operation  ======================");

			// Call the populateDatabase method with a sample size of 5
			// You can adjust this number based on how much test data you want
			//serviceTest.populateDatabase(5);

			serviceTest.rudTest();
			CustomLogger.logInfo("==================== Finished operation successfully ======================");
		} catch (CustomException e) {
			CustomLogger.logError("Error during database population: " + e.getMessage());
			e.printStackTrace();
			CustomLogger.logError("==================== FINISHED WITH ERROR ======================");
		} catch (Exception e) {
			CustomLogger.logError("Unexpected error: " + e.getMessage());
			e.printStackTrace();
			CustomLogger.logError("==================== FINISHED WITH UNEXPECTED ERROR ======================");
		}
	}
}