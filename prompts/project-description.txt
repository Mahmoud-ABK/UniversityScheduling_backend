this file is in markdown format
# Description projet
### **1. Personne (Person)**
- **Description**: The foundational class representing all individuals within the system—including students, teachers, administrators, and technicians.
- **Attributes**:
    - `CIN`: National identification number.
    - `Nom`: Last name.
    - `Prenom`: First name.    
    - `Email`: Contact email.
    - `Tel`: Telephone number.
    - `Adresse`: Physical address.
### **2. Étudiant (Student)**
- **Description**: Represents a student enrolled in the institution.    
- **Inherits From**: `Personne`.
- **Attributes**:
    - `Matricule`: Student ID.
    - `Branche`: Associated `Branche` (program/specialization).
    - `TP`: List of `TP` (practical sessions) they're enrolled in.
- **Privileges**:
    - **Access**:
        - View their personal schedule, including lectures (`Seance`), tutorials (`TD`), and practicals (`TP`).
        - Receive notifications regarding schedule changes or important announcements.
    - **Limitations**:
        - Cannot modify schedules.
        - Cannot access administrative functions.
### **3. Enseignant (Teacher)**
- **Description**: Represents a teacher or faculty member.
- **Inherits From**: `Personne`.
- **Attributes**:
    - `CodeEnseignant`: Unique teacher code.
    - `Heures`: Total teaching hours.
    - `Seances`: List of `Seance` (sessions) they're teaching.
    - `PropositionDeRattrapage`: List of proposed catch-up sessions.
- **Privileges**:
    - **Access**:
        - View their teaching schedule.
        - Monitor their total teaching hours.
        - Receive notifications about schedule updates or administrative decisions.
    - **Actions**:
        - **Cannot directly modify** their schedule.
        - **Submit "Signal"** (reclamations or suggestions) regarding schedule conflicts or issues.
        - **Propose "PropositionDeRattrapage"** to schedule catch-up sessions.
    - **Limitations**:        
        - Changes to schedules must be approved and made by the administrator.
### **4. Administrateur (Administrator)**
- **Description**: The primary person responsible for managing schedules and overseeing academic operations.
- **Inherits From**: `Personne`.
- **Attributes**:
    - `CodeAdmin`: Unique administrator code.
- **Privileges**:
    - **Full control** over:
        - **Scheduling**: Create, modify, and adjust all schedules (`Seance`, `TD`, `TP`).
        - **Resource Allocation**: Assign teachers and rooms to sessions.
        - **Importing Data**: Import scheduling data from Excel files (`fichier_excel`).
        - **Reclamations**: Review and respond to `Signal` from teachers.
        - **Catch-Up Sessions**: Approve or reject `PropositionDeRattrapage`.
        - **Notifications**: Send out important notifications to students and teachers.
    - **Limitations**:
        - **Cannot manage user accounts** or access privileges—that's under the technician's purview.
### **5. Technicien (Technician and Resource Manager)**
- **Description**: Responsible for technical maintenance, system administration, and resource management.
- **Inherits From**: `Personne`.
- **Attributes**:
    - `CodeTechnicien`: Unique technician code.
- **Privileges**:
    - **Account Management**:
        - Create, modify, and delete user accounts (students, teachers, administrators).
        - Assign roles and set access privileges.
    - **System Maintenance**:
        - Ensure the software and hardware systems are running smoothly.
        - Handle updates, backups, and technical troubleshooting.
    - **Resource Management**:
        - Modify information about rooms (`Salle`), including capacity, type, and availability.
        - Manage databases containing schedules and user information.
    - **Limitations**:
        - Does not engage in schedule creation or modification beyond technical setup.
### **6. Seance (Session)**
- **Description**: Represents a scheduled teaching session (lecture, tutorial, or practical).
- **Attributes**:
    - `ID`: Unique session identifier.
    - `Jour`: Day of the session.
    - `HeureDebut`: Start time.
    - `HeureFin`: End time.
    - `Type`: Type of session (lecture, tutorial, practical).
    - `Matiere`: Subject matter.
    - `Frequence`: How often the session occurs (e.g., weekly).
    - `Salle`: Assigned room (`Salle`).
    - `Enseignant`: Assigned teacher (`Enseignant`).
- **Management**:
    - **Created and modified** by the **Administrateur**.
    - **Viewed** by **Étudiants** and **Enseignants**.
### **7. Salle (Room)**
- **Description**: Represents a physical classroom or lab space.
- **Attributes**:
    - `Identifiant`: Room identifier.
    - `Type`: Room type (lecture hall, lab, etc.).
    - `Capacite`: Capacity of the room.
    - `Disponibilite`: List of available time slots.
- **Privileges**:
    - **Managed by** the **Technicien** for details and availability.
    - **Scheduled by** the **Administrateur** when assigning rooms to sessions.
### **8. Notification**
- **Description**: Messages sent to users to inform them of schedule changes, announcements, or alerts.
- **Attributes**:
    - `Message`: Content of the notification.
    - `Date`: Date and time sent.
    - `Recepteur`: Recipient (`Personne`).
    - `Expediteur`: Sender (`Personne` or system).
    - `Type`: Type of notification (update, alert, reminder).
- **Functionality**:
    - **Automatically sent** when schedules are updated.
    - **Sent by** the **Administrateur** for important announcements.
    - **Received by** **Étudiants** and **Enseignants**.
### **9. Signal (Reclamation/Suggestion)**
- **Description**: A message sent by a teacher to report a problem or suggest a change regarding their schedule.
- **Attributes**:
    - `Message`: Details of the issue or suggestion.
    - `Severity`: Importance level.
    - `Timestamp`: Date and time submitted.
- **Privileges**:
    - **Created by** **Enseignants**.
    - **Reviewed and addressed by** the **Administrateur**.
- **Purpose**:
    - Facilitate communication about scheduling conflicts or concerns.
    - Allow teachers to report issues without directly altering the schedule.
### **10. PropositionDeRattrapage (Catch-Up Session Proposal)**
- **Description**: A proposal submitted by a teacher to schedule a make-up session for a missed or extra class.
- **Attributes**:
    - `Date`: Proposed date for the catch-up session.
    - `Reason`: Explanation for the catch-up (e.g., previous cancellation).
    - `Status`: Current status (pending, approved, rejected).
- **Process**:
    - **Submitted by** **Enseignants**.
    - **Reviewed by** the **Administrateur**.
        - **Approved**: The session is added to the schedule, and notifications are sent out.
        - **Rejected**: The teacher is notified with the reason.
- **Privileges**:
    - Teachers can propose but not schedule.
    - Administrators have final approval.
### **11. fichier_excel (Excel File)**
- **Description**: Represents scheduling data imported into the system.
- **Attributes**:
    - `FileName`: Name of the Excel file.
    - `Status`: Import status (successful, failed).
    - `Errors`: List of any errors encountered during import.
    - `ImportDate`: Date and time of import.
- **Privileges**:
    - **Imported by** the **Administrateur**.
    - **Processed to** generate or update schedules.
- **Purpose**:
    - Streamline the initial data entry process.
    - Facilitate bulk updates to scheduling information.
### **12. Branche (Branch/Program)**
- **Description**: Represents an academic program or specialization.
- **Attributes**:
    - `Niveau`: Level of study (e.g., undergraduate, graduate).
    - `Specialite`: Specialization or major.
    - `NbTD`: Number of tutorial sessions.
    - `Departement`: Associated department.
- **Privileges**:
    - **Managed by** the **Administrateur**.
    - **Assigned to** **Étudiants**.
- **Purpose**:
    - Organize students and sessions by academic program.
    - Assist in scheduling appropriate courses and sessions.
### **13. TD (Tutorial Session)**
- **Description**: Represents group tutorial sessions associated with a `Branche`.
- **Attributes**:
    - `NbTP`: Number of practical sessions associated.
- **Privileges**:
    - **Scheduled by** the **Administrateur**.
    - **Participated in** by **Étudiants**.
- **Purpose**:
    - Provide supplemental instruction in smaller group settings.
    - Link to practical sessions (`TP`).
### **14. TP (Practical Session)**
- **Description**: Represents hands-on practical or lab sessions.
- **Privileges**:
    - **Scheduled by** the **Administrateur**.
    - **Attended by** **Étudiants**.
- **Purpose**:
    - Offer practical experience complementing theoretical lessons.
    - Allow for application of concepts in a controlled environment.
### **Summary of Access and Privileges**
- **Étudiants (Students)**:
    - **Can**:
        - View their own schedules and session details.
        - Receive notifications of changes or announcements.
    - **Cannot**:
        - Modify any schedules.
        - Access administrative or technical functions.
- **Enseignants (Teachers)**:
    - **Can**:
        - View their teaching schedules and assigned sessions.
        - Monitor their total teaching hours.
        - Send `Signal` for reclamations or suggestions.
        - Propose `PropositionDeRattrapage` for catch-up sessions.
        - Receive notifications concerning their schedules.
    - **Cannot**:
        - Directly modify any schedules.
        - Manage user accounts or privileges.
- **Administrateurs (Administrators)**:
    - **Can**:
        - Edit and manage all scheduling aspects.
        - Import and process scheduling data.
        - Adjust sessions and resolve conflicts.
        - Accept or reject proposals for catch-up sessions.
        - Send notifications to relevant users.
    - **Cannot**:
        - Manage user accounts or access levels.
- **Techniciens (Technicians)**:
    - **Can**:
        - Manage and configure user accounts.
        - Assign roles and set access privileges.
        - Update room information and availability.
        - Ensure system functionality and security.
    - **Cannot**:
        - Alter schedules or academic content.
# USE CASE Diagramme 
```plantuml
@startuml
actor Administrateur
actor Professeur
actor Étudiant
actor "Technicien et Gestionnaire"

usecase "Importation des fichiers Excel" as UC1
usecase "Génération automatique des emplois du temps" as UC2
usecase "Notifications automatiques aux enseignants et étudiants" as UC3
usecase "Modifier les emplois du temps" as UC4
usecase "Proposition de rattrapage" as UC5
usecase "Consulter nombre des heures d'enseignement" as UC6
usecase "Consulter l'emploi du temps" as UC7
usecase "Recevoir des notifications" as UC8
usecase "Répartition des salles et des ressources" as UC9
usecase "Authentification" as UC10

Administrateur -> UC1
UC1 ..|> UC2 : <<include>>
UC2 ..|> UC3 : <<include>>
Administrateur -> UC4
UC4 ..|> UC3 : <<include>>

Professeur -> UC5
UC5 ..|> UC10 : <<include>>
Professeur -> UC6
UC6 ..|> UC10 : <<include>>

Étudiant -> UC7
UC7 ..|> UC10 : <<include>>
Étudiant -> UC8
UC8 ..|> UC10 : <<include>>

"Technicien et Gestionnaire" -> UC9

@enduml
```
# Class diagramme

```plantuml
@startuml
' Base class for people
class Personne {
  - CIN: string
  - Nom: string
  - Prenom: string
  - Email: string
  - Tel: string
  - Adresse: string
}

' Branch class for programs and specializations
class Branche {
  - Niveau: string
  - Specialite: string
  - NbTD: int
  - Departement: string
}

' TD class for tutorial sessions
class TD {
  - NbTP: int
}

' TP class for practical work sessions
class TP {}

' Etudiant class inheriting from Personne
class Etudiant {
  - Matricule: string
  - Branche: Branche
  - TP: List<TP>  ' Added relation to TP
}

' Enseignant class inheriting from Personne
class Enseignant {
  - CodeEnseignant: string
  - Heures: int
  - Seances: List<Seance>
  - PropositionDeRattrapage: List<PropositionDeRattrapage>  ' Added relation to PropositionDeRattrapage
}

' Seance class representing a session
class Seance {
  - ID: string
  - Jour: string
  - HeureDebut: string
  - HeureFin: string
  - Type: string
  - Matiere: string
  - Frequence: string
  - Salle: Salle
  - Enseignant: Enseignant
}

' Salle class representing rooms
class Salle {
  - Identifiant: string
  - Type: string
  - Capacite: int
  - Disponibilite: List<string>
  + isAvailable(timeSlot: string): boolean
}

' Administrateur class inheriting from Personne
class Administrateur {
  - CodeAdmin: string
  + importFile(file: fichier_excel): boolean
  + acceptRetakeProposition(proposition: PropositionDeRattrapage): boolean
}

' Technicien class inheriting from Personne
class Technicien {
  - CodeTechnicien: string
  + modifySalle(salle: Salle): boolean
}

' Notification class for user notifications
class Notification {
  - Message: string
  - Date: datetime
  - Recepteur: Personne
  - Expediteur: Personne
  - Type: string
  + sendNotification(): boolean
}

' Signal class for system alerts
class Signal {
  - Message: string
  - Severity: string
  - Timestamp: datetime
  + logSignal(): void
}

' fichier_excel class representing the imported file
class fichier_excel {
  - FileName: string
  - Status: string
  - Errors: List<string>
  - ImportDate: datetime
}

' PropositionDeRattrapage class for rescheduling sessions
class PropositionDeRattrapage {
  - Date: datetime
  - Reason: string
  - Status: string
  + sendProposition(): boolean
}

' Relationships between entities

Personne <|-- Etudiant
Personne <|-- Enseignant
Personne <|-- Administrateur
Personne <|-- Technicien

Branche "1" -- "n" TD : "Etudiant et liee n to n avec TD et Branche"
TD "1" -- "n" TP
TP "1" -- "n" Seance : "Seance et liee n to n avec TD et Branche"
Seance "n" -- "1" Salle
Enseignant "1" -- "n" Seance
Etudiant "n" -- "n" TP : "Etudiant inscrit dans plusieurs TP"
Enseignant "1" -- "n" PropositionDeRattrapage : "Enseignant gère les propositions de rattrapage"

Administrateur -- fichier_excel : "Extends"
Administrateur -- PropositionDeRattrapage : "accepter"
Technicien -- Salle : "modifier"
Technicien -- fichier_excel : "importer"
Notification <|-- Signal : "Extends"
Signal -- Notification : "logSignal"
PropositionDeRattrapage -- Seance : "relate to"
Salle -- PropositionDeRattrapage : "send to"

@enduml

```

