# University Schedule Management API Documentation

This API documentation provides a comprehensive guide to the RESTful API endpoints for the University Schedule Management System. The system is designed to manage schedules, user accounts, notifications, and resources for students, teachers, administrators, and technicians. The API is built using Spring Boot and follows REST principles.

---

## Table of Contents
1. [Overview](#overview)
2. [Authentication](#authentication)
3. [Endpoints](#endpoints)
   - [Authentication Endpoints](#authentication-endpoints)
   - [Administrator Endpoints](#administrator-endpoints)
   - [Teacher Endpoints](#teacher-endpoints)
   - [Student Endpoints](#student-endpoints)
   - [Room Management Endpoints](#room-management-endpoints)
   - [Technician Endpoints](#technician-endpoints)
4. [Data Models](#data-models)
5. [Error Handling](#error-handling)
6. [Security](#security)
7. [Usage Notes](#usage-notes)

---

## Overview

The University Schedule Management API enables interaction with a system that handles academic scheduling, user management, and resource allocation. Key features include:
- **Schedule Management**: Create, update, and delete teaching sessions (lectures, tutorials, practicals).
- **User Management**: Manage accounts for administrators and technicians.
- **Notifications**: Send and receive notifications for schedule changes or announcements.
- **Resource Allocation**: Manage rooms and their availability.
- **Makeup Sessions**: Propose, approve, or reject catch-up sessions.
- **Excel Integration**: Import scheduling data from Excel files.

The API is organized into controllers based on user roles and responsibilities. Note that in this initial version, **Student** and **Teacher** endpoints are publicly accessible and do not require authentication or role-based authorization.

---

## Authentication

The API uses **JWT (JSON Web Tokens)** for authentication and authorization for **Administrator** and **Technician** endpoints. Users with these roles must authenticate via the `/api/auth` endpoints to obtain an access token, which must be included in the `Authorization` header for protected endpoints.

**Header Format** (for protected endpoints):
```
Authorization: Bearer <access_token>
```

### Roles and Permissions
- **ROLE_ADMIN**: Full access to schedule management, makeup session approvals, and notifications.
- **ROLE_TECHNICIAN**: Manages user accounts and room resources.
- **ROLE_TEACHER**: Not used in this version; teacher endpoints are publicly accessible.
- **ROLE_STUDENT**: Not used in this version; student endpoints are publicly accessible.

**Note**: Endpoints under `/api/students` and `/api/teachers` do not require authentication or specific roles in this initial version.

---

## Endpoints

### Authentication Endpoints

**Base Path**: `/api/auth`

| Method | Endpoint                | Description                              | Request Body                       | Response Body                     | Status Codes |
|--------|-------------------------|------------------------------------------|------------------------------------|-----------------------------------|--------------|
| POST   | `/login`                | Authenticates a user and returns tokens. | `LoginRequestDTO`                 | `LoginResponseDTO`               | 200, 401     |
| POST   | `/refresh`              | Refreshes an access token.              | `RefreshTokenRequestDTO`          | `LoginResponseDTO`               | 200, 401     |
| POST   | `/logout`               | Logs out a user and invalidates token.  | None                              | None                             | 200          |
| POST   | `/password/reset`       | Initiates a password reset process.     | `PasswordResetRequestDTO`         | None                             | 200, 400     |
| POST   | `/password/change`      | Changes the user's password.            | `PasswordChangeRequestDTO`        | None                             | 200, 400, 401|

#### Example: Login
**Request**:
```json
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "password123"
}
```

**Response**:
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

---

### Administrator Endpoints

**Base Path**: `/api/admin`

**Required Role**: `ROLE_ADMIN`

**Authentication**: Required (JWT Bearer Token)

#### Schedule Management
| Method | Endpoint                  | Description                              | Request Body          | Response Body                   | Status Codes |
|--------|---------------------------|------------------------------------------|-----------------------|---------------------------------|--------------|
| GET    | `/seances`                | Retrieves all sessions.                 | None                  | `List<SeanceDTO>`              | 200          |
| GET    | `/seances/{id}`           | Retrieves a session by ID.              | None                  | `SeanceDTO`                    | 200, 404     |
| POST   | `/seances`                | Creates a new session.                  | `SeanceDTO`           | `SeanceDTO`                    | 200, 400     |
| PUT    | `/seances/{id}`           | Updates a session.                      | `SeanceDTO`           | `SeanceDTO`                    | 200, 400, 404|
| DELETE | `/seances/{id}`           | Deletes a session.                      | None                  | None                           | 200, 404     |

#### Makeup Session Management
| Method | Endpoint                               | Description                              | Request Body          | Response Body                       | Status Codes |
|--------|----------------------------------------|------------------------------------------|-----------------------|-------------------------------------|--------------|
| GET    | `/makeup-sessions`                     | Retrieves all makeup session proposals.  | None                  | `List<PropositionDeRattrapageDTO>` | 200          |
| PUT    | `/makeup-sessions/{id}/approve`        | Approves a makeup session.              | None                  | `PropositionDeRattrapageDTO`       | 200, 400, 404|
| PUT    | `/makeup-sessions/{id}/reject`         | Rejects a makeup session.               | None                  | `PropositionDeRattrapageDTO`       | 200, 400, 404|
| PUT    | `/makeup-sessions/{id}/approve-scheduled` | Approves a scheduled makeup session. | None                  | `PropositionDeRattrapageDTO`       | 200, 400, 404|
| PUT    | `/makeup-sessions/{id}/reject-scheduled` | Rejects a scheduled makeup session.  | None                  | `PropositionDeRattrapageDTO`       | 200, 400, 404|

#### Conflict Management
| Method | Endpoint                        | Description                              | Request Body | Response Body                       | Status Codes |
|--------|---------------------------------|------------------------------------------|--------------|-------------------------------------|--------------|
| GET    | `/conflicts`                    | Retrieves all session conflicts.         | None         | `List<SeanceConflictDTO>`          | 200          |
| GET    | `/conflicts/rooms`              | Retrieves room-specific conflicts.       | None         | `List<SeanceRoomConflictDTO>`      | 200          |
| GET    | `/conflicts/seances/{seanceId}` | Retrieves conflicts for a specific session. | None       | `List<SingleSeanceConflictDTO>`    | 200, 404     |

#### Notification Management
| Method | Endpoint                        | Description                              | Request Body       | Response Body | Status Codes |
|--------|---------------------------------|------------------------------------------|--------------------|---------------|--------------|
| POST   | `/notifications/broadcast`      | Broadcasts a notification to all users.  | `NotificationDTO`  | None          | 200, 400     |
| POST   | `/notifications/teachers`       | Sends a notification to all teachers.    | `NotificationDTO`  | None          | 200, 400     |
| POST   | `/notifications/students`       | Sends a notification to all students.    | `NotificationDTO`  | None          | 200, 400     |
| POST   | `/notifications/branches`       | Sends a notification to specific branches. | `NotificationDTO`, `branchIds` | None | 200, 400     |

#### Excel File Management
| Method | Endpoint                  | Description                              | Request Body                   | Response Body                   | Status Codes |
|--------|---------------------------|------------------------------------------|--------------------------------|---------------------------------|--------------|
| POST   | `/excel/upload`           | Uploads an Excel file with schedules.    | `FichierExcelDTO`, `List<SeanceDTO>` | None                   | 200, 400     |
| GET    | `/excel/history`          | Retrieves import history of Excel files. | None                           | `List<FichierExcelDTO>`        | 200          |

#### Example: Create a Session
**Request**:
```json
POST /api/admin/seances
Content-Type: application/json
Authorization: Bearer <access_token>

{
  "name": "Math Lecture",
  "jour": "Monday",
  "heureDebut": "09:00",
  "heureFin": "11:00",
  "type": "CR",
  "matiere": "Mathematics",
  "frequence": "weekly",
  "salleId": 1,
  "enseignantId": 2,
  "brancheIds": [1],
  "tdIds": [1],
  "tpIds": [1]
}
```

**Response**:
```json
{
  "id": 1,
  "name": "Math Lecture",
  "jour": "Monday",
  "heureDebut": "09:00",
  "heureFin": "11:00",
  "type": "CR",
  "matiere": "Mathematics",
  "frequence": "weekly",
  "salleId": 1,
  "enseignantId": 2,
  "brancheIds": [1],
  "tdIds": [1],
  "tpIds": [1]
}
```

---

### Teacher Endpoints

**Base Path**: `/api/teachers`

**Authentication**: Not required in this version

**Required Role**: None (publicly accessible)

#### Profile Management
| Method | Endpoint         | Description                              | Request Body       | Response Body       | Status Codes |
|--------|------------------|------------------------------------------|--------------------|---------------------|--------------|
| GET    | `/{id}`          | Retrieves teacher profile.               | None               | `EnseignantDTO`    | 200, 404     |
| PUT    | `/{id}`          | Updates teacher profile.                 | `EnseignantDTO`    | `EnseignantDTO`    | 200, 400, 404|

#### Schedule Management
| Method | Endpoint                     | Description                              | Request Body | Response Body            | Status Codes |
|--------|------------------------------|------------------------------------------|--------------|--------------------------|--------------|
| GET    | `/{id}/schedule`             | Retrieves teacher's schedule.            | None         | `List<SeanceDTO>`       | 200, 404     |
| GET    | `/{id}/teaching-hours`       | Retrieves teaching hours for a period.   | `startDate`, `endDate` | `Integer`      | 200, 404     |
| GET    | `/{id}/subjects`             | Retrieves subjects taught by teacher.    | None         | `List<String>`          | 200, 404     |
| GET    | `/{id}/student-groups`       | Retrieves student groups taught.         | None         | `List<TPDTO>`           | 200, 404     |

#### Makeup Sessions
| Method | Endpoint                     | Description                              | Request Body                   | Response Body                       | Status Codes |
|--------|------------------------------|------------------------------------------|--------------------------------|-------------------------------------|--------------|
| POST   | `/{id}/makeup-requests`      | Submits a makeup session request.        | `PropositionDeRattrapageDTO`   | `PropositionDeRattrapageDTO`       | 200, 400, 404|

#### Communication
| Method | Endpoint                | Description                              | Request Body   | Response Body            | Status Codes |
|--------|-------------------------|------------------------------------------|----------------|--------------------------|--------------|
| POST   | `/{id}/signals`         | Submits a signal (complaint/suggestion). | `SignalDTO`    | `SignalDTO`             | 200, 400, 404|
| GET    | `/{id}/signals`         | Retrieves teacher's signals.             | None           | `List<SignalDTO>`       | 200, 404     |

#### Notifications
| Method | Endpoint                          | Description                              | Request Body | Response Body            | Status Codes |
|--------|-----------------------------------|------------------------------------------|--------------|--------------------------|--------------|
| GET    | `/{id}/notifications/unread`      | Retrieves unread notifications.          | None         | `List<NotificationDTO>` | 200          |
| PUT    | `/notifications/{notificationId}/read` | Marks a notification as read.        | None         | None                    | 200, 404     |

#### Example: Submit a Makeup Request
**Request**:
```json
POST /api/teachers/2/makeup-requests
Content-Type: application/json

{
  "name": "Math Makeup",
  "matiere": "Mathematics",
  "type": "CR",
  "heureDebut": "14:00",
  "heureFin": "16:00",
  "date": "2025-06-01T00:00:00",
  "reason": "Missed session due to holiday",
  "status": "PENDING",
  "enseignantId": 2,
  "brancheIds": [1],
  "tdIds": [1],
  "tpIds": [1]
}
```

**Response**:
```json
{
  "id": 1,
  "name": "Math Makeup",
  "matiere": "Mathematics",
  "type": "CR",
  "heureDebut": "14:00",
  "heureFin": "16:00",
  "date": "2025-06-01T00:00:00",
  "reason": "Missed session due to holiday",
  "status": "PENDING",
  "enseignantId": 2,
  "brancheIds": [1],
  "tdIds": [1],
  "tpIds": [1]
}
```

---

### Student Endpoints

**Base Path**: `/api/students`

**Authentication**: Not required in this version

**Required Role**: None (publicly accessible)

#### Profile Management
| Method | Endpoint         | Description                              | Request Body       | Response Body       | Status Codes |
|--------|------------------|------------------------------------------|--------------------|---------------------|--------------|
| GET    | `/{id}`          | Retrieves student profile.               | None               | `EtudiantDTO`      | 200, 404     |
| PUT    | `/{id}`          | Updates student profile.                 | `EtudiantDTO`      | `EtudiantDTO`      | 200, 400, 404|

#### Schedule Access
| Method | Endpoint                     | Description                              | Request Body | Response Body            | Status Codes |
|--------|------------------------------|------------------------------------------|--------------|--------------------------|--------------|
| GET    | `/{id}/schedule/personal`    | Retrieves student's personal schedule.   | None         | `List<SeanceDTO>`       | 200, 404     |
| GET    | `/{id}/schedule/branch`      | Retrieves branch schedule.               | None         | `List<SeanceDTO>`       | 200, 404     |
| GET    | `/{id}/schedule/td`          | Retrieves TD schedule.                   | None         | `List<SeanceDTO>`       | 200, 404     |

#### Notifications
| Method | Endpoint                          | Description                              | Request Body | Response Body            | Status Codes |
|--------|-----------------------------------|------------------------------------------|--------------|--------------------------|--------------|
| GET    | `/{id}/notifications`             | Retrieves student's notifications.       | None         | `List<NotificationDTO>` | 200, 404     |
| GET    | `/{id}/notifications/unread`      | Retrieves unread notifications.          | None         | `List<NotificationDTO>` | 200          |
| PUT    | `/notifications/{notificationId}/read` | Marks a notification as read.        | None         | None                    | 200, 404     |

#### Example: Get Personal Schedule
**Request**:
```json
GET /api/students/3/schedule/personal
```

**Response**:
```json
[
  {
    "id": 1,
    "name": "Math Lecture",
    "jour": "Monday",
    "heureDebut": "09:00",
    "heureFin": "11:00",
    "type": "CR",
    "matiere": "Mathematics",
    "frequence": "weekly",
    "salleId": 1,
    "enseignantId": 2,
    "brancheIds": [1],
    "tdIds": [1],
    "tpIds": [1]
  }
]
```

---

### Room Management Endpoints

**Base Path**: `/api/rooms`

**Required Role**: `ROLE_TECHNICIAN`

**Authentication**: Required (JWT Bearer Token)

| Method | Endpoint         | Description                              | Request Body   | Response Body            | Status Codes |
|--------|------------------|------------------------------------------|----------------|--------------------------|--------------|
| GET    | `/`              | Retrieves all rooms.                     | None           | `List<SalleDTO>`        | 200          |
| GET    | `/{id}`          | Retrieves a room by ID.                  | None           | `SalleDTO`              | 200, 404     |
| POST   | `/`              | Creates a new room.                      | `SalleDTO`     | `SalleDTO`              | 201, 400     |
| PUT    | `/{id}`          | Updates a room.                          | `SalleDTO`     | `SalleDTO`              | 200, 400, 404|
| DELETE | `/{id}`          | Deletes a room.                          | None           | None                    | 200, 404     |

#### Example: Create a Room
**Request**:
```json
POST /api/rooms
Content-Type: application/json
Authorization: Bearer <access_token>

{
  "identifiant": "A101",
  "type": "Lecture Hall",
  "capacite": 50,
  "seanceIds": []
}
```

**Response**:
```json
{
  "id": 1,
  "identifiant": "A101",
  "type": "Lecture Hall",
  "capacite": 50,
  "seanceIds": []
}
```

---

### Technician Endpoints

**Base Path**: `/api/technicians`

**Required Role**: `ROLE_TECHNICIAN`

**Authentication**: Required (JWT Bearer Token)

#### Technician Management
| Method | Endpoint         | Description                              | Request Body        | Response Body            | Status Codes |
|--------|------------------|------------------------------------------|---------------------|--------------------------|--------------|
| GET    | `/{id}`          | Retrieves technician profile.            | None                | `TechnicienDTO`         | 200, 404     |
| GET    | `/`              | Retrieves all technicians.               | None                | `List<TechnicienDTO>`   | 200          |
| POST   | `/`              | Creates a new technician.                | `TechnicienDTO`     | `TechnicienDTO`         | 201, 400     |
| PUT    | `/{id}`          | Updates a technician.                    | `TechnicienDTO`     | `TechnicienDTO`         | 200, 400, 404|
| DELETE | `/{id}`          | Deletes a technician.                    | None                | None                    | 204, 404     |

#### User Management
| Method | Endpoint                     | Description                              | Request Body   | Response Body            | Status Codes |
|--------|------------------------------|------------------------------------------|----------------|--------------------------|--------------|
| GET    | `/users/{id}`                | Retrieves a user by ID.                  | None           | `UserDTO`               | 200, 404     |
| GET    | `/users`                     | Retrieves all users.                     | None           | `List<UserDTO>`         | 200          |
| POST   | `/users`                     | Creates a new user.                      | `UserDTO`      | `UserDTO`               | 201, 400     |
| PUT    | `/users/{id}`                | Updates a user.                          | `UserDTO`      | `UserDTO`               | 200, 400, 404|
| DELETE | `/users/{id}`                | Deletes a user.                          | None           | None                    | 204, 404     |
| GET    | `/users/personne/{personneId}` | Retrieves a user by Personne ID.       | None           | `UserDTO`               | 200, 404     |

#### Example: Create a User
**Request**:
```json
POST /api/technicians/users
Content-Type: application/json
Authorization: Bearer <access_token>

{
  "personneId": 1,
  "username": "admin1",
  "password": "password123",
  "role": "ROLE_ADMIN",
  "status": "ACTIVE",
  "personneData": {
    "cin": "12345678",
    "nom": "Doe",
    "prenom": "Jane",
    "email": "jane.doe@example.com",
    "tel": "1234567890",
    "adresse": "123 Main St",
    "codeAdmin": "ADM001"
  }
}
```

**Response**:
```json
{
  "id": 1,
  "personneId": 1,
  "username": "admin1",
  "password": "[hashed_password]",
  "role": "ROLE_ADMIN",
  "status": "ACTIVE",
  "personneData": {
    "cin": "12345678",
    "nom": "Doe",
    "prenom": "Jane",
    "email": "jane.doe@example.com",
    "tel": "1234567890",
    "adresse": "123 Main St",
    "codeAdmin": "ADM001"
  }
}
```

---

## Data Models

Below are the key Data Transfer Objects (DTOs) used in the API.

### `PersonneDTO`
Base DTO for all individuals.
```json
{
  "id": Long,
  "cin": String,
  "nom": String,
  "prenom": String,
  "email": String,
  "tel": String,
  "adresse": багато
}
```

### `AdministrateurDTO`
Extends `PersonneDTO`.
```json
{
  ...PersonneDTO,
  "codeAdmin": String
}
```

### `EnseignantDTO`
Extends `PersonneDTO`.
```json
{
  ...PersonneDTO,
  "codeEnseignant": String,
  "heures": Integer,
  "seanceIds": List<Long>,
  "propositionIds": List<Long>,
  "signalIds": List<Long>
}
```

### `EtudiantDTO`
Extends `PersonneDTO`.
```json
{
  ...PersonneDTO,
  "matricule": String,
  "brancheId": Long,
  "tpId": Long
}
```

### `TechnicienDTO`
Extends `PersonneDTO`.
```json
{
  ...PersonneDTO,
  "codeTechnicien": String
}
```

### `SeanceDTO`
Represents a teaching session.
```json
{
  "id": Long,
  "name": String,
  "jour": String,
  "heureDebut": String,
  "heureFin": String,
  "type": String,
  "matiere": String,
  "frequence": String,
  "date": String,
  "salleId": Long,
  "enseignantId": Long,
  "brancheIds": List<Long>,
  "tdIds": List<Long>,
  "tpIds": List<Long>
}
```

### `SalleDTO`
Represents a room.
```json
{
  "id": Long,
  "identifiant": String,
  "type": String,
  "capacite": Integer,
  "seanceIds": List<Long>
}
```

### `BrancheDTO`
Represents an academic program.
```json
{
  "id": Long,
  "niveau": String,
  "specialite": String,
  "nbTD": Integer,
  "departement": String,
  "seanceIds": List<Long>,
  "tdIds": List<Long>
}
```

### `TDDTO`
Represents a tutorial group.
```json
{
  "id": Long,
  "nb": Integer,
  "nbTP": Integer,
  "brancheId": Long,
  "tpIds": List<Long>,
  "seanceIds": List<Long>
}
```

### `TPDTO`
Represents a practical session group.
```json
{
  "id": Long,
  "nb": Integer,
  "tdId": Long,
  "etudiantIds": List<Long>,
  "seanceIds": List<Long>
}
```

### `PropositionDeRattrapageDTO`
Represents a makeup session proposal.
```json
{
  "id": Long,
  "name": String,
  "matiere": String,
  "type": String,
  "heureDebut": String,
  "heureFin": String,
  "date": String,
  "reason": String,
  "status": String,
  "enseignantId": Long,
  "brancheIds": List<Long>,
  "tdIds": List<Long>,
  "tpIds": List<Long>
}
```

### `NotificationDTO`
Represents a notification.
```json
{
  "id": Long,
  "message": String,
  "date": String,
  "type": String,
  "isread": Boolean,
  "recepteurId": Long,
  "expediteurId": Long
}
```

### `SignalDTO`
Represents a teacher’s complaint or suggestion.
```json
{
  "id": Long,
  "message": String,
  "severity": String,
  "timestamp": String,
  "enseignantId": String
}
```

### `FichierExcelDTO`
Represents an imported Excel file.
```json
{
  "id": Long,
  "fileName": String,
  "status": String,
  "errors": List<String>,
  "importDate": String
}
```

### `UserDTO`
Represents a user account.
```json
{
  "id": Long,
  "personneId": Long,
  "username": String,
  "password": String,
  "role": String,
  "status": String,
  "personneData": Object
}
```

### `BatchDTO`
Represents a batch operation response.
```json
{
  "ids": List<Long>,
  "message": String,
  "success": Boolean,
  "entityType": String
}
```

### Authentication DTOs
#### `LoginRequestDTO`
```json
{
  "username": String,
  "password": String
}
```

#### `LoginResponseDTO`
```json
{
  "accessToken": String,
  "refreshToken": String
}
```

#### `RefreshTokenRequestDTO`
```json
{
  "refreshToken": String
}
```

#### `PasswordResetRequestDTO`
```json
{
  "email": String
}
```

#### `PasswordChangeRequestDTO`
```json
{
  "oldPassword": String,
  "newPassword": String
}
```

---

## Error Handling

The API uses a custom exception (`CustomException`) to handle errors. Responses follow this format:

```json
{
  "error": String,
  "message": String,
  "status": Integer,
  "timestamp": String
}
```

**Common Status Codes**:
- **200 OK**: Request successful.
- **201 Created**: Resource created.
- **204 No Content**: Request successful, no content returned.
- **400 Bad Request**: Invalid request data.
- **401 Unauthorized**: Authentication failed (applicable to `/api/admin` and `/api/technicians`).
- **403 Forbidden**: Insufficient permissions (applicable to `/api/admin` and `/api/technicians`).
- **404 Not Found**: Resource not found.
- **500 Internal Server Error**: Server error.

---

## Security

- **JWT Authentication**: Required for `/api/admin` and `/api/technicians` endpoints. Not required for `/api/students` and `/api/teachers` in this version.
- **Role-Based Access Control**: Applied to `/api/admin` (requires `ROLE_ADMIN`) and `/api/technicians` (requires `ROLE_TECHNICIAN`). No role checks for `/api/students` and `/api/teachers`.
- **Input Validation**: Request bodies are validated using Jakarta Bean Validation annotations (e.g., `@NotBlank`, `@NotNull`).
- **HTTPS**: All API requests should be made over HTTPS to ensure data security.

---

## Usage Notes

- **Timestamps**: Use ISO 8601 format (e.g., `2025-06-01T14:00:00`) for date-time fields.
- **Excel Uploads**: Ensure Excel files follow the expected schema to avoid import errors.
- **Conflict Detection**: Use conflict management endpoints to identify and resolve scheduling conflicts before finalizing schedules.
- **Notifications**: Notifications are automatically triggered for schedule changes but can also be sent manually by administrators.
- **Public Access**: The `/api/students` and `/api/teachers` endpoints are publicly accessible in this version, which may pose security risks. Consider adding authentication in future iterations.

For further details or support, contact the system administrator or refer to the project repository.