# University Schedule System Service Layer Summary

## AdministrateurService
### CRUD Operations
```java
AdministrateurDTO findById(Long id)
List<AdministrateurDTO> findAll()
AdministrateurDTO create(AdministrateurDTO administrateur)
AdministrateurDTO update(Long id, AdministrateurDTO administrateur)
void delete(Long id)
```
### Functionalities
```java
void importExcelSchedule(FichierExcelDTO fichier)
void generateSchedule()
List<PropositionDeRattrapageDTO> getAllMakeupSessions()
void approveMakeupSession(Long id)
void rejectMakeupSession(Long id)
void broadcastNotification(NotificationDTO notification)
List<SeanceConflictDTO> getAllConflicts()
List<SeanceConflictDTO> getRoomConflicts()
```

## BrancheService
### CRUD Operations
```java
BrancheDTO findById(Long id)
List<BrancheDTO> findAll()
BrancheDTO create(BrancheDTO branche)
BrancheDTO update(Long id, BrancheDTO branche)
void delete(Long id)
```
### Functionalities
```java
void assignTeacherToBranch(Long branchId, EnseignantDTO teacherDTO)
void removeTeacherFromBranch(Long branchId, Long teacherId)
void assignStudentToBranch(Long branchId, EtudiantDTO studentDTO)
void removeStudentFromBranch(Long branchId, Long studentId)
List<SeanceDTO> getCoursesByBranch(Long branchId)
```

## EnseignantService
### CRUD Operations
```java
EnseignantDTO findById(Long id)
List<EnseignantDTO> findAll()
EnseignantDTO create(EnseignantDTO enseignant)
EnseignantDTO update(Long id, EnseignantDTO enseignant)
void delete(Long id)
```
### Functionalities
```java
List<SeanceDTO> getSchedule(Long id)
int getTotalTeachingHours(Long id)
PropositionDeRattrapageDTO submitMakeupRequest(Long id, PropositionDeRattrapageDTO proposition)
SignalDTO submitSignal(Long id, SignalDTO signal)
List<SignalDTO> getSignals(Long id)
```

## EtudiantService
### CRUD Operations
```java
EtudiantDTO findById(Long id)
List<EtudiantDTO> findAll()
EtudiantDTO create(EtudiantDTO etudiant)
EtudiantDTO update(Long id, EtudiantDTO etudiant)
void delete(Long id)
```
### Functionalities
```java
List<SeanceDTO> getPersonalSchedule(Long id)
List<SeanceDTO> getBranchSchedule(Long brancheId)
List<NotificationDTO> getNotifications(Long id)
```

## ExcelFileService
### CRUD Operations
```java
FichierExcelDTO findById(Long id)
List<FichierExcelDTO> findAll()
FichierExcelDTO create(FichierExcelDTO fichierExcel)
FichierExcelDTO update(Long id, FichierExcelDTO fichierExcel)
void delete(Long id)
```
### Functionalities
```java
void upload(FichierExcelDTO file)
List<FichierExcelDTO> getImportHistory()
```

## NotificationService
### CRUD Operations
```java
NotificationDTO findById(Long id)
List<NotificationDTO> findAll()
NotificationDTO create(NotificationDTO notification)
NotificationDTO update(Long id, NotificationDTO notification)
void delete(Long id)
```
### Functionalities
```java
void markAsRead(Long id)
List<NotificationDTO> getUnreadNotifications()
void broadcastNotification(NotificationDTO notificationDTO)
```

## SalleService
### CRUD Operations
```java
SalleDTO findById(Long id)
List<SalleDTO> findAll()
SalleDTO create(SalleDTO salle)
SalleDTO update(Long id, SalleDTO salle)
void delete(Long id)
```
### Functionalities
```java
List<SalleDTO> getAvailableRooms(String date, String startTime, String endTime)
```

## SeanceService
### CRUD Operations
```java
List<SeanceDTO> findAll()
SeanceDTO findById(Long id)
SeanceDTO create(SeanceDTO seance)
SeanceDTO update(Long id, SeanceDTO seance)
void delete(Long id)
```
### Functionalities
```java
List<SeanceConflictDTO> getAllConflicts()
List<SeanceConflictDTO> getRoomConflicts()
List<SeanceConflictDTO> getConflictsForSession(Long seanceId)
```

## TDService (Tutorial Groups)
### CRUD Operations
```java
List<TDDTO> findAll()
TDDTO findById(Long id)
TDDTO create(TDDTO td)
TDDTO update(Long id, TDDTO td)
void delete(Long id)
```
### Functionalities
```java
List<TPDTO> getTPs(Long tdId)
```

## TPService (Practical Sessions)
### CRUD Operations
```java
List<TPDTO> findAll()
TPDTO findById(Long id)
TPDTO create(TPDTO tp)
TPDTO update(Long id, TPDTO tp)
void delete(Long id)
```
### Functionalities
```java
List<EtudiantDTO> getStudents(Long tpId)
```
