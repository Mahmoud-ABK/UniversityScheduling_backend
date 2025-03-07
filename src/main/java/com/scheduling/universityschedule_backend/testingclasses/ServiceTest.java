package com.scheduling.universityschedule_backend.testingclasses;

import com.scheduling.universityschedule_backend.dto.*;
import com.scheduling.universityschedule_backend.exception.CustomException;
import com.scheduling.universityschedule_backend.model.FrequenceType;
import com.scheduling.universityschedule_backend.model.Status;
import com.scheduling.universityschedule_backend.model.SeanceType;
import com.scheduling.universityschedule_backend.service.*;
import com.scheduling.universityschedule_backend.util.CustomLogger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional
public class ServiceTest {

    private final AdministrateurService administrateurService;
    private final BrancheService brancheService;
    private final EtudiantService etudiantService;
    private final ExcelFileService excelFileService;
    private final SalleService salleService;
    private final SeanceService seanceService;
    private final EnseignantService enseignantService;
    private final TDService tdService;
    private final TPService tpService;

    public ServiceTest(AdministrateurService administrateurService, BrancheService brancheService, EtudiantService etudiantService, ExcelFileService excelFileService, SalleService salleService, SeanceService seanceService, EnseignantService enseignantService, TDService tdService, TPService tpService) {
        this.administrateurService = administrateurService;
        this.brancheService = brancheService;
        this.etudiantService = etudiantService;
        this.excelFileService = excelFileService;
        this.salleService = salleService;
        this.seanceService = seanceService;
        this.enseignantService = enseignantService;
        this.tdService = tdService;
        this.tpService = tpService;
    }

    public void testAllServices() throws CustomException {
        //testAdministrateurService();
        testBrancheService();
        testEtudiantService();
        testExcelFileService();
        testSalleService();
        testSeanceService();
        testEnseignantService();
        testTDService();
        testTPService();
    }

    public void testAdministrateurService() throws CustomException {
        CustomLogger.logInfo("=================== Testing AdministrateurService ===================");

        try {
            // Test approve makeup session
            Long testPropositionId = 1L; // Assuming this exists
            Long testSalleId = 1L; // Assuming this exists
            Long testrejectid=15L;
            Long testrejectid2=16L;

            PropositionDeRattrapageDTO approvedProposition =
                    administrateurService.approveMakeupSession(testPropositionId, testSalleId);
            CustomLogger.logInfo("Approved Proposition: " + approvedProposition);

            // Test reject makeup session
            PropositionDeRattrapageDTO rejectedProposition =
                    administrateurService.rejectMakeupSession(testrejectid);
            CustomLogger.logInfo("Rejected Proposition: " + rejectedProposition);

            // Test approve scheduled
            PropositionDeRattrapageDTO approvedScheduled =
                    administrateurService.approveScheduled(18L, testSalleId);
            CustomLogger.logInfo("Approved Scheduled: " + approvedScheduled);

            // Test reject scheduled
            PropositionDeRattrapageDTO rejectedScheduled =
                    administrateurService.rejectScheduled(testrejectid2, "Room not available");
            CustomLogger.logInfo("Rejected Scheduled: " + rejectedScheduled);

        } catch (CustomException e) {
            CustomLogger.logError("AdministrateurService test failed: " + e.getMessage());
            throw e;
        }
    }

    public void testBrancheService() throws CustomException {
        CustomLogger.logInfo("=================== Testing BrancheService ===================");

        try {
            // Test findAll
            List<BrancheDTO> allBranches = brancheService.findAll();
            CustomLogger.logInfo("All Branches: " + allBranches.size());

            if (!allBranches.isEmpty()) {
                Long testBranchId = allBranches.getFirst().getId();

                // Test findById
                BrancheDTO branch = brancheService.findById(testBranchId);
                CustomLogger.logInfo("Found Branch: " + branch);

                // Test create
                BrancheDTO newBranch = new BrancheDTO();
                newBranch.setNiveau("L3");
                newBranch.setSpecialite("INFO");
                newBranch.setDepartement("Informatique");
                newBranch.setNbTD(3);
                BrancheDTO createdBranch = brancheService.create(newBranch);
                CustomLogger.logInfo("Created Branch: " + createdBranch);

                // Test update
                createdBranch.setNbTD(4);
                BrancheDTO updatedBranch = brancheService.update(createdBranch.getId(), createdBranch);
                CustomLogger.logInfo("Updated Branch: " + updatedBranch);

                // Test getSchedule
                List<SeanceDTO> schedule = brancheService.getSchedule(testBranchId);
                CustomLogger.logInfo("Branch Schedule: " + schedule.size() + " sessions");

                // Test getEtudiants
                List<EtudiantDTO> students = brancheService.getEtudiants(testBranchId);
                CustomLogger.logInfo("Branch Students: " + students.size() + " students");

                // Test delete
                brancheService.delete(createdBranch.getId());
                CustomLogger.logInfo("Deleted Branch with ID: " + createdBranch.getId());
            }
        } catch (CustomException e) {
            CustomLogger.logError("BrancheService test failed: " + e.getMessage());
            throw e;
        }
    }

    public void testEtudiantService() throws CustomException {
        CustomLogger.logInfo("=================== Testing EtudiantService ===================");

        try {
            // Test findAll
            List<EtudiantDTO> allStudents = etudiantService.findAll();
            CustomLogger.logInfo("All Students: " + allStudents.size());

            if (!allStudents.isEmpty()) {
                Long testStudentId = allStudents.getFirst().getId();

                // Test findById
                EtudiantDTO student = etudiantService.findById(testStudentId);
                CustomLogger.logInfo("Found Student: " + student);

                // Test create
                EtudiantDTO newStudent = new EtudiantDTO();
                newStudent.setNom("Test");
                newStudent.setPrenom("Student");
                newStudent.setEmail("test.student@example.com");
                newStudent.setMatricule("TEST123");
                EtudiantDTO createdStudent = etudiantService.create(newStudent);
                CustomLogger.logInfo("Created Student: " + createdStudent);

                // Test update
                createdStudent.setEmail("updated.test@example.com");
                EtudiantDTO updatedStudent = etudiantService.update(createdStudent.getId(), createdStudent);
                CustomLogger.logInfo("Updated Student: " + updatedStudent);

                // Test getPersonalSchedule
                List<SeanceDTO> personalSchedule = etudiantService.getPersonalSchedule(testStudentId);
                CustomLogger.logInfo("Personal Schedule: " + personalSchedule.size() + " sessions");

                // Test getBranchSchedule
                List<SeanceDTO> branchSchedule = etudiantService.getBranchSchedule(testStudentId);
                CustomLogger.logInfo("Branch Schedule: " + branchSchedule.size() + " sessions");

                // Test getTDSchedule
                List<SeanceDTO> tdSchedule = etudiantService.getTDSchedule(testStudentId);
                CustomLogger.logInfo("TD Schedule: " + tdSchedule.size() + " sessions");

                // Test getNotifications
                List<NotificationDTO> notifications = etudiantService.getNotifications(testStudentId);
                CustomLogger.logInfo("Notifications: " + notifications.size() + " notifications");

                // Test delete
                etudiantService.delete(createdStudent.getId());
                CustomLogger.logInfo("Deleted Student with ID: " + createdStudent.getId());
            }
        } catch (CustomException e) {
            CustomLogger.logError("EtudiantService test failed: " + e.getMessage());
            throw e;
        }
    }

    public void testExcelFileService() throws CustomException {
        CustomLogger.logInfo("=================== Testing ExcelFileService ===================");

        try {
            // Test findAll
            List<FichierExcelDTO> allFiles = excelFileService.findAll();
            CustomLogger.logInfo("All Files: " + allFiles.size());

            // Test create
            FichierExcelDTO newFile = new FichierExcelDTO();
            newFile.setFileName("test_file.xlsx");
            newFile.setStatus("Processing");
            newFile.setImportDate(LocalDateTime.now());
            newFile.setErrors(List.of("No errors"));
            FichierExcelDTO createdFile = excelFileService.create(newFile);
            CustomLogger.logInfo("Created File: " + createdFile);

            // Test findById
            FichierExcelDTO foundFile = excelFileService.findById(createdFile.getId());
            CustomLogger.logInfo("Found File: " + foundFile);

            // Test update
            createdFile.setStatus("Completed");
            FichierExcelDTO updatedFile = excelFileService.update(createdFile.getId(), createdFile);
            CustomLogger.logInfo("Updated File: " + updatedFile);

            // Test upload
            List<SeanceDTO> seances = new ArrayList<>(); // Add some test seances
            excelFileService.upload(createdFile, seances);
            CustomLogger.logInfo("Uploaded File with sessions");

            // Test getImportHistory
            List<FichierExcelDTO> history = excelFileService.getImportHistory();
            CustomLogger.logInfo("Import History: " + history.size() + " files");

            // Test delete
            excelFileService.delete(createdFile.getId());
            CustomLogger.logInfo("Deleted File with ID: " + createdFile.getId());

        } catch (CustomException e) {
            CustomLogger.logError("ExcelFileService test failed: " + e.getMessage());
            throw e;
        }
    }

    public void testSalleService() throws CustomException {
        CustomLogger.logInfo("=================== Testing SalleService ===================");

        try {
            // Test findAll
            List<SalleDTO> allRooms = salleService.findAll();
            CustomLogger.logInfo("All Rooms: " + allRooms.size());

            // Test create
            SalleDTO newRoom = new SalleDTO();
            newRoom.setIdentifiant("TEST-ROOM");
            newRoom.setType("Classroom");
            newRoom.setCapacite(30);
            SalleDTO createdRoom = salleService.create(newRoom);
            CustomLogger.logInfo("Created Room: " + createdRoom);

            // Test findById
            SalleDTO foundRoom = salleService.findById(createdRoom.getId());
            CustomLogger.logInfo("Found Room: " + foundRoom);

            // Test update
            createdRoom.setCapacite(40);
            SalleDTO updatedRoom = salleService.update(createdRoom.getId(), createdRoom);
            CustomLogger.logInfo("Updated Room: " + updatedRoom);

            // Test getAvailableRooms
            List<SalleDTO> availableRooms = salleService.getAvailableRooms(
                    LocalDate.now(),
                    DayOfWeek.MONDAY,
                    LocalTime.of(9, 0),
                    LocalTime.of(10, 30)
            );
            CustomLogger.logInfo("Available Rooms: " + availableRooms.size());

            // Test delete
            salleService.delete(createdRoom.getId());
            CustomLogger.logInfo("Deleted Room with ID: " + createdRoom.getId());

        } catch (CustomException e) {
            CustomLogger.logError("SalleService test failed: " + e.getMessage());
            throw e;
        }
    }

    public void testSeanceService() throws CustomException {
        CustomLogger.logInfo("=================== Testing SeanceService ===================");

        try {
            // Test findAll
            List<SeanceDTO> allSessions = seanceService.findAll();
            CustomLogger.logInfo("All Sessions: " + allSessions.size());

            // Test create
            SeanceDTO newSession = new SeanceDTO();
            newSession.setMatiere("Test Subject");
            newSession.setType(String.valueOf(SeanceType.CR));
            newSession.setJour(String.valueOf(DayOfWeek.MONDAY));
            newSession.setHeureDebut(String.valueOf(LocalTime.of(9, 0)));
            newSession.setHeureFin(String.valueOf(LocalTime.of(10, 30)));
            newSession.setEnseignantId(11L);
            newSession.setFrequence(String.valueOf(FrequenceType.WEEKLY));
            SeanceDTO createdSession = seanceService.create(newSession);
            SeanceDTO createdSession2 = seanceService.create(newSession);
            CustomLogger.logInfo("Created Session: " + createdSession);

            // Test findById
            SeanceDTO foundSession = seanceService.findById(createdSession.getId());
            CustomLogger.logInfo("Found Session: " + foundSession);

            // Test update
            SeanceDTO updatedSession = seanceService.update(createdSession.getId(), createdSession);
            CustomLogger.logInfo("Updated Session: " + updatedSession);

            // Test getAllConflicts
            List<SeanceConflictDTO> conflicts = seanceService.getAllConflicts();
            CustomLogger.logInfo("All Conflicts: " + conflicts.size());
            List<SeanceConflictDTO> setofConflicts = conflicts.stream().limit(3).toList();

            for (SeanceConflictDTO seanceConflict : setofConflicts) {
                CustomLogger.logInfo("Found Conflict: " + seanceConflict);
            }
            Long conflictId = setofConflicts.getFirst().getSeance1Id();

            // Test getRoomConflicts
            List<SeanceRoomConflictDTO> roomConflicts = seanceService.getRoomConflicts();
            CustomLogger.logInfo("Room Conflicts: " + roomConflicts.size());

            // Test getConflictsForSession (by ID)
            List<SingleSeanceConflictDTO> sessionConflicts =
                    seanceService.getConflictsForSession(conflictId);
            CustomLogger.logInfo("Session Conflicts: " + sessionConflicts.size());
            CustomLogger.logInfo("to search "+seanceService.findById(conflictId));
            // Test getConflictsForSession (by DTO)
            List<SingleSeanceConflictDTO> dtoConflicts =
                    seanceService.getConflictsForSession(seanceService.findById(conflictId));
            CustomLogger.logInfo("DTO Conflicts: " + dtoConflicts.size());

            // Test delete
            seanceService.delete(createdSession.getId());
            CustomLogger.logInfo("Deleted Session with ID: " + createdSession.getId());

        } catch (CustomException e) {
            CustomLogger.logError("SeanceService test failed: " + e.getMessage());
            throw e;
        }
    }
    // Add these test methods to your ServiceTest class

    public void testEnseignantService() throws CustomException {
        CustomLogger.logInfo("=================== Testing EnseignantService ===================");

        try {
            // Test findAll
            List<EnseignantDTO> allTeachers = enseignantService.findAll();
            CustomLogger.logInfo("All Teachers: " + allTeachers.size());

            // Test create
            EnseignantDTO newTeacher = new EnseignantDTO();
            newTeacher.setNom("Test");
            newTeacher.setPrenom("Teacher");
            newTeacher.setEmail("test.teacher@example.com");
            newTeacher.setCodeEnseignant("TEACH123");
            newTeacher.setHeures(20);
            EnseignantDTO createdTeacher = enseignantService.create(newTeacher);
            CustomLogger.logInfo("Created Teacher: " + createdTeacher);

            // Test findById
            EnseignantDTO foundTeacher = enseignantService.findById(createdTeacher.getId());
            CustomLogger.logInfo("Found Teacher: " + foundTeacher);

            // Test update
            createdTeacher.setHeures(25);
            EnseignantDTO updatedTeacher = enseignantService.update(createdTeacher.getId(), createdTeacher);
            CustomLogger.logInfo("Updated Teacher: " + updatedTeacher);

            // Test getSchedule
            List<SeanceDTO> schedule = enseignantService.getSchedule(createdTeacher.getId());
            CustomLogger.logInfo("Teacher Schedule: " + schedule.size() + " sessions");

            // Test getTotalTeachingHours
            int hours = enseignantService.getTotalTeachingHours(
                    createdTeacher.getId(),
                    LocalDate.now(),
                    LocalDate.now().plusMonths(1)
            );
            CustomLogger.logInfo("Total Teaching Hours: " + hours);

            // Test submitMakeupRequest
            PropositionDeRattrapageDTO proposition = new PropositionDeRattrapageDTO();
            proposition.setName("Makeup Test");
            proposition.setMatiere("Test Subject");
            proposition.setType(String.valueOf(SeanceType.CI));
            proposition.setDate(String.valueOf(LocalDateTime.now().plusDays(7)));
            PropositionDeRattrapageDTO submittedProposition =
                    enseignantService.submitMakeupRequest(createdTeacher.getId(), proposition);
            CustomLogger.logInfo("Submitted Makeup Request: " + submittedProposition);

            // Test submitSignal
            SignalDTO signal = new SignalDTO();
            signal.setMessage("Test Signal");
            signal.setSeverity("Low");
            SignalDTO submittedSignal = enseignantService.submitSignal(createdTeacher.getId(), signal);
            CustomLogger.logInfo("Submitted Signal: " + submittedSignal);

            // Test getSignals
            List<SignalDTO> signals = enseignantService.getSignals(createdTeacher.getId());
            CustomLogger.logInfo("Teacher Signals: " + signals.size());

            // Test getSubjects
            List<String> subjects = enseignantService.getSubjects(createdTeacher.getId());
            CustomLogger.logInfo("Teacher Subjects: " + subjects.size());

            // Test getStudentGroups
            List<TPDTO> groups = enseignantService.getStudentGroups(createdTeacher.getId());
            CustomLogger.logInfo("Teacher Student Groups: " + groups.size());

            // Test delete
            enseignantService.delete(createdTeacher.getId());
            CustomLogger.logInfo("Deleted Teacher with ID: " + createdTeacher.getId());

        } catch (CustomException e) {
            CustomLogger.logError("EnseignantService test failed: " + e.getMessage());
            throw e;
        }
    }

    public void testTDService() throws CustomException {
        CustomLogger.logInfo("=================== Testing TDService ===================");

        try {
            // Test findAll
            List<TDDTO> allTDs = tdService.findAll();
            CustomLogger.logInfo("All TDs: " + allTDs.size());

            // Test create
            TDDTO newTD = new TDDTO();
            newTD.setNb(1);
            newTD.setNbTP(2);
            TDDTO createdTD = tdService.create(newTD);
            CustomLogger.logInfo("Created TD: " + createdTD);

            // Test findById
            TDDTO foundTD = tdService.findById(createdTD.getId());
            CustomLogger.logInfo("Found TD: " + foundTD);

            // Test update
            createdTD.setNbTP(3);
            TDDTO updatedTD = tdService.update(createdTD.getId(), createdTD);
            CustomLogger.logInfo("Updated TD: " + updatedTD);

            // Test getTPs
            List<TPDTO> tps = tdService.getTPs(createdTD.getId());
            CustomLogger.logInfo("TD TPs: " + tps.size());

            // Test generateSchedule
            List<SeanceDTO> schedule = tdService.generateSchedule(createdTD.getId());
            CustomLogger.logInfo("TD Schedule: " + schedule.size() + " sessions");

            // Test getEtudiants
            List<EtudiantDTO> students = tdService.getEtudiants(createdTD.getId());
            CustomLogger.logInfo("TD Students: " + students.size());

            // Test delete
            tdService.delete(createdTD.getId());
            CustomLogger.logInfo("Deleted TD with ID: " + createdTD.getId());

        } catch (CustomException e) {
            CustomLogger.logError("TDService test failed: " + e.getMessage());
            throw e;
        }
    }

    public void testTPService() throws CustomException {
        CustomLogger.logInfo("=================== Testing TPService ===================");

        try {
            // Test findAll
            List<TPDTO> allTPs = tpService.findAll();
            CustomLogger.logInfo("All TPs: " + allTPs.size());

            // Test create
            TPDTO newTP = new TPDTO();
            newTP.setNb(1);
            TPDTO createdTP = tpService.create(newTP);
            CustomLogger.logInfo("Created TP: " + createdTP);

            // Test findById
            TPDTO foundTP = tpService.findById(createdTP.getId());
            CustomLogger.logInfo("Found TP: " + foundTP);

            // Test update
            createdTP.setNb(2);
            TPDTO updatedTP = tpService.update(createdTP.getId(), createdTP);
            CustomLogger.logInfo("Updated TP: " + updatedTP);

            // Test getStudents
            List<EtudiantDTO> students = tpService.getStudents(createdTP.getId());
            CustomLogger.logInfo("TP Students: " + students.size());

            // Test generateSchedule
            List<SeanceDTO> schedule = tpService.generateSchedule(createdTP.getId());
            CustomLogger.logInfo("TP Schedule: " + schedule.size() + " sessions");

            // Test getEtudiants
            List<EtudiantDTO> allStudents = tpService.getEtudiants(createdTP.getId());
            CustomLogger.logInfo("TP All Students: " + allStudents.size());

            // Test delete
            tpService.delete(createdTP.getId());
            CustomLogger.logInfo("Deleted TP with ID: " + createdTP.getId());

        } catch (CustomException e) {
            CustomLogger.logError("TPService test failed: " + e.getMessage());
            throw e;
        }
    }
}