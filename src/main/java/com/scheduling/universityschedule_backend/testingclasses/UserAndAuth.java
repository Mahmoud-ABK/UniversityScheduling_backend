package com.scheduling.universityschedule_backend.testingclasses;

import com.scheduling.universityschedule_backend.model.*;
import com.scheduling.universityschedule_backend.model.enums.AuditAction;
import com.scheduling.universityschedule_backend.model.enums.UserRole;
import com.scheduling.universityschedule_backend.model.enums.UserStatus;
import com.scheduling.universityschedule_backend.repository.AdministrateurRepository;
import com.scheduling.universityschedule_backend.repository.TechnicienRepository;
import com.scheduling.universityschedule_backend.repository.UserCredentialsRepository;
import com.scheduling.universityschedule_backend.repository.AuditLogRepository;
import com.scheduling.universityschedule_backend.util.CustomLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserAndAuth implements CommandLineRunner {

    private final AdministrateurRepository administrateurRepository;
    private final TechnicienRepository technicienRepository;
    private final UserCredentialsRepository userCredentialsRepository;
    private final AuditLogRepository auditLogRepository;
    private final PasswordEncoder passwordEncoder;
    private static final LocalDateTime CURRENT_TIME = LocalDateTime.of(2025, 5, 5, 15, 37, 21);
    private static final String CURRENT_USER = "Mahmoud-ABK";

    @Autowired
    public UserAndAuth(
            AdministrateurRepository administrateurRepository,
            TechnicienRepository technicienRepository,
            UserCredentialsRepository userCredentialsRepository,
            AuditLogRepository auditLogRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.administrateurRepository = administrateurRepository;
        this.technicienRepository = technicienRepository;
        this.userCredentialsRepository = userCredentialsRepository;
        this.auditLogRepository = auditLogRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    @Transactional
    public void run(String... args) {
//        CustomLogger.logInfo("Starting to create test users...");
//        createAdministrateurs();
//        createTechniciens();
//        testRepositories();
//        CustomLogger.logInfo("Finished creating and testing users.");
        CustomLogger.logInfo("tested");
    }

    private void createAdministrateurs() {
        for (int i = 1; i <= 3; i++) {
            // Create Admin
            Administrateur admin = new Administrateur();
            admin.setCodeAdmin(String.format("ADM%03d", i));
            admin.setCin(String.format("AB%06d", i));
            admin.setNom("Admin" + i);
            admin.setPrenom("Super" + i);
            admin.setEmail("admin" + i + "@university.com");
            admin.setTel(String.format("+21260000%04d", i));
            admin.setAdresse("Admin Address " + i);
            admin.setReceivednotifications(new ArrayList<>());
            admin.setSentnotifications(new ArrayList<>());

            admin = administrateurRepository.save(admin);
            CustomLogger.logInfo("Created administrator " + i + " with ID: " + admin.getId());

            // Create Admin Credentials
            UserCredentials adminCred = UserCredentials.builder()
                    .personne(admin)
                    .username("admin" + i)
                    .password(passwordEncoder.encode("admin" + i + "123"))
                    .status(UserStatus.ACTIVE)
                    .role(UserRole.ROLE_ADMIN)
                    .lastPasswordChange(CURRENT_TIME)
                    .failedAttempts(0)
                    .build();

            adminCred = userCredentialsRepository.save(adminCred);
            CustomLogger.logInfo("Created admin credentials for username: " + adminCred.getUsername());

            // Create Audit Log
            AuditLog adminAudit = AuditLog.builder()
                    .userLogin(CURRENT_USER)
                    .action(String.valueOf(AuditAction.USER_CREATED))
                    .entityType("Administrateur")
                    .entityId(admin.getId())
                    .details("Created administrator account " + i + " with username: " + adminCred.getUsername())
                    .build();

            auditLogRepository.save(adminAudit);
        }
    }

    private void createTechniciens() {
        for (int i = 1; i <= 3; i++) {
            // Create Technician
            Technicien tech = new Technicien();
            tech.setCodeTechnicien(String.format("TECH%03d", i));
            tech.setCin(String.format("CD%06d", i));
            tech.setNom("Technicien" + i);
            tech.setPrenom("Principal" + i);
            tech.setEmail("tech" + i + "@university.com");
            tech.setTel(String.format("+21260001%04d", i));
            tech.setAdresse("Tech Address " + i);
            tech.setReceivednotifications(new ArrayList<>());
            tech.setSentnotifications(new ArrayList<>());

            tech = technicienRepository.save(tech);
            CustomLogger.logInfo("Created technician " + i + " with ID: " + tech.getId());

            // Create Technician Credentials
            UserCredentials techCred = UserCredentials.builder()
                    .personne(tech)
                    .username("tech" + i)
                    .password(passwordEncoder.encode("tech" + i + "123"))
                    .status(UserStatus.ACTIVE)
                    .role(UserRole.ROLE_TECHNICIAN)
                    .lastPasswordChange(CURRENT_TIME)
                    .failedAttempts(0)
                    .build();

            techCred = userCredentialsRepository.save(techCred);
            CustomLogger.logInfo("Created technician credentials for username: " + techCred.getUsername());

            // Create Audit Log
            AuditLog techAudit = AuditLog.builder()
                    .userLogin(CURRENT_USER)
                    .action(String.valueOf(AuditAction.USER_CREATED))
                    .entityType("Technicien")
                    .entityId(tech.getId())
                    .details("Created technician account " + i + " with username: " + techCred.getUsername())
                    .build();

            auditLogRepository.save(techAudit);
        }
    }

    @Transactional
    public void testRepositories() {
        CustomLogger.logInfo("Starting repository tests...");

        // Test UserCredentials Repository
        testUserCredentialsRepository();

        // Test AuditLog Repository
        testAuditLogRepository();

        CustomLogger.logInfo("Finished repository tests.");
    }

    private void testUserCredentialsRepository() {
        CustomLogger.logInfo("Testing UserCredentials Repository...");

        // Test findByUsername
        UserCredentials admin1 = userCredentialsRepository.findByUsername("admin1")
                .orElseThrow(() -> new RuntimeException("Admin1 not found"));
        CustomLogger.logInfo("Found admin1: " + admin1.getUsername());

        // Test existsByUsername
        boolean exists = userCredentialsRepository.existsByUsername("tech1");
        CustomLogger.logInfo("tech1 exists: " + exists);

        // Test findByPersonne
        Technicien tech = technicienRepository.findAll().getFirst();
        if (tech != null) {
            UserCredentials techCred = userCredentialsRepository.findByPersonne(tech)
                    .orElseThrow(() -> new RuntimeException("Tech credentials not found"));
            CustomLogger.logInfo("Found credentials for TECH001: " + techCred.getUsername());
        }

        // Test findByPersonneId
        UserCredentials admin2 = userCredentialsRepository.findByPersonneId(2L)
                .orElseThrow(() -> new RuntimeException("Admin2 not found"));
        CustomLogger.logInfo("Found credentials for personne ID 2: " + admin2.getUsername());
    }

    private void testAuditLogRepository() {
        CustomLogger.logInfo("Testing AuditLog Repository...");

        // Test findByUserLogin
        List<AuditLog> userLogs = auditLogRepository.findByUserLogin(CURRENT_USER);
        CustomLogger.logInfo("Found " + userLogs.size() + " logs for user: " + CURRENT_USER);

        // Test findByTimestampBetween
        LocalDateTime startTime = CURRENT_TIME.minusDays(1);
        LocalDateTime endTime = CURRENT_TIME.plusDays(1);
        List<AuditLog> timeLogs = auditLogRepository.findByTimestampBetween(startTime, endTime);
        CustomLogger.logInfo("Found " + timeLogs.size() + " logs between " + startTime + " and " + endTime);

        // Test findByUserLoginAndTimeRange
        List<AuditLog> userTimeLogs = auditLogRepository.findByUserLoginAndTimeRange(
                CURRENT_USER, startTime, endTime);
        CustomLogger.logInfo("Found " + userTimeLogs.size() +
                " logs for user " + CURRENT_USER +
                " in time range");

        // Test findTop10ByUserLoginOrderByTimestampDesc
        List<AuditLog> recentLogs = auditLogRepository.findTop10ByUserLoginOrderByTimestampDesc(CURRENT_USER);
        CustomLogger.logInfo("Found " + recentLogs.size() + " recent logs for user: " + CURRENT_USER);

        // Test findByEntityTypeAndEntityId
        List<AuditLog> entityLogs = auditLogRepository.findByEntityTypeAndEntityId("Administrateur", 1L);
        CustomLogger.logInfo("Found " + entityLogs.size() + " logs for Administrateur with ID 1");
    }
}