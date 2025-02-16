package com.scheduling.universityschedule_backend;

import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.util.CustomLogger;
import com.scheduling.universityschedule_backend.testingclasses.* ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UniversityScheduleBackendApplication implements CommandLineRunner {

	@Autowired
	private DatabasePopulator databasePopulator;

	@Autowired
	private EntityMapperTester entityMapperTester;

	public static void main(String[] args) {
		SpringApplication.run(UniversityScheduleBackendApplication.class, args);
	}

	@Override
	public void run(String... args) {
		try {
			// Populate the database with sample data
			databasePopulator.populateDatabase();

			// Test the EntityMapper methods
			entityMapperTester.testEntityMapper();

		} catch (CustomException e) {
			CustomLogger.logError("Error during testing: " + e.getMessage());
		}
	}
}