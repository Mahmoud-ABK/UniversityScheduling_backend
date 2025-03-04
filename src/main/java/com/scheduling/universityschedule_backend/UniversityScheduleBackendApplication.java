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
	private JPAtest jpatester;

	@Autowired
	private EntityMapperTester entityMapperTester;

	public static void main(String[] args) {
		SpringApplication.run(UniversityScheduleBackendApplication.class, args);
	}

	@Override
	public void run(String... args) {
		try {
			CustomLogger.logInfo("==================== STARTING FULL JPA TEST SUITE ======================");

			// Populate database with test data
//			CustomLogger.logInfo("----- Database Population Test -----");
//			jpatester.populateDatabase(20);

			// Basic CRUD tests
//			CustomLogger.logInfo("----- Basic CRUD Tests -----");
//			jpatester.testRetrieveEntities();
//			jpatester.testRetrieveAndModify();
//			jpatester.testRetrieveAndDelete();

			// Entity relationship tests
//			CustomLogger.logInfo("----- Entity Relationship Tests -----");
//			jpatester.testListFetch();

			// Date/time functionality tests
			CustomLogger.logInfo("----- Date/Time Type Tests -----");
			jpatester.testDateTimeOperations();

			// Room availability and conflict tests
//			CustomLogger.logInfo("----- Scheduling Tests -----");
//			jpatester.testRoomAvailability();
//			jpatester.testConflictDetection();

//			// Comprehensive scheduling test
//			CustomLogger.logInfo("----- Comprehensive Scheduling Test -----");
//			jpatester.testComprehensiveScheduling();
//
//			// Data migration test
//			CustomLogger.logInfo("----- Data Migration Test -----");
//			jpatester.testDataMigration();

			CustomLogger.logInfo("==================== FULL JPA TEST SUITE COMPLETED ======================");
		} catch (Exception e) {
			CustomLogger.logError("Error during testing: " + e.getMessage());
			e.printStackTrace();
			CustomLogger.logError("==================== FINISHED WITH ERROR ======================");
		}
	}
}