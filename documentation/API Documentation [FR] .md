# Documentation de l'API de Gestion des Emplois du Temps Universitaires

Cette documentation fournit un guide complet des points de terminaison de l'API REST pour le système de gestion des emplois du temps universitaires. Le système est conçu pour gérer les emplois du temps, les comptes utilisateurs, les notifications et les ressources pour les étudiants, enseignants, administrateurs et techniciens. L'API est construite avec Spring Boot et suit les principes REST.

---

## Table des matières
1. [Aperçu](#aperçu)
2. [Authentification](#authentification)
3. [Points de terminaison](#points-de-terminaison)
   - [Points de terminaison d'authentification](#points-de-terminaison-dauthentification)
   - [Points de terminaison des administrateurs](#points-de-terminaison-des-administrateurs)
   - [Points de terminaison des enseignants](#points-de-terminaison-des-enseignants)
   - [Points de terminaison des étudiants](#points-de-terminaison-des-étudiants)
   - [Points de terminaison de gestion des salles](#points-de-terminaison-de-gestion-des-salles)
   - [Points de terminaison des techniciens](#points-de-terminaison-des-techniciens)
4. [Modèles de données](#modèles-de-données)
5. [Gestion des erreurs](#gestion-des-erreurs)
6. [Sécurité](#sécurité)
7. [Notes d'utilisation](#notes-dutilisation)

---

## Aperçu

L'API de gestion des emplois du temps universitaires permet d'interagir avec un système qui gère la planification académique, la gestion des utilisateurs et l'allocation des ressources. Les principales fonctionnalités incluent :
- **Gestion des emplois du temps** : Créer, mettre à jour et supprimer des sessions d'enseignement (cours magistraux, travaux dirigés, travaux pratiques).
- **Gestion des utilisateurs** : Gérer les comptes des administrateurs et techniciens.
- **Notifications** : Envoyer et recevoir des notifications pour les changements d'emploi du temps ou les annonces.
- **Allocation des ressources** : Gérer les salles et leur disponibilité.
- **Sessions de rattrapage** : Proposer, approuver ou rejeter des sessions de rattrapage.
- **Intégration Excel** : Importer des données de planification depuis des fichiers Excel.

L'API est organisée en contrôleurs basés sur les rôles et responsabilités des utilisateurs. Dans cette version initiale, les points de terminaison **Étudiants** et **Enseignants** sont accessibles publiquement et ne nécessitent ni authentification ni autorisation basée sur les rôles.

---

## Authentification

L'API utilise des **JWT (JSON Web Tokens)** pour l'authentification et l'autorisation des points de terminaison **Administrateurs** et **Techniciens**. Les utilisateurs ayant ces rôles doivent s'authentifier via les points de terminaison `/api/auth` pour obtenir un jeton d'accès, qui doit être inclus dans l'en-tête `Authorization` pour les points de terminaison protégés.

**Format de l'en-tête** (pour les points de terminaison protégés) :
```
Authorization: Bearer <jeton_d_accès>
```

### Rôles et permissions
- **ROLE_ADMIN** : Accès complet à la gestion des emplois du temps, aux approbations des sessions de rattrapage et aux notifications.
- **ROLE_TECHNICIAN** : Gère les comptes utilisateurs et les ressources des salles.
- **ROLE_TEACHER** : Non utilisé dans cette version ; les points de terminaison des enseignants sont accessibles publiquement.
- **ROLE_STUDENT** : Non utilisé dans cette version ; les points de terminaison des étudiants sont accessibles publiquement.

**Note** : Les points de terminaison sous `/api/students` et `/api/teachers` ne nécessitent pas d'authentification ni de rôles spécifiques dans cette version initiale.

---

## Points de terminaison

### Points de terminaison d'authentification

**Chemin de base** : `/api/auth`

| Méthode | Point de terminaison     | Description                              | Corps de la requête                | Corps de la réponse               | Codes de statut |
|---------|--------------------------|------------------------------------------|------------------------------------|-----------------------------------|----------------|
| POST    | `/login`                 | Authentifie un utilisateur et retourne des jetons. | `LoginRequestDTO`         | `LoginResponseDTO`               | 200, 401       |
| POST    | `/refresh`               | Rafraîchit un jeton d'accès.            | `RefreshTokenRequestDTO`          | `LoginResponseDTO`               | 200, 401       |
| POST    | `/logout`                | Déconnecte un utilisateur et invalide le jeton. | Aucun                     | Aucun                            | 200            |
| POST    | `/password/reset`        | Initie un processus de réinitialisation de mot de passe. | `PasswordResetRequestDTO` | Aucun                            | 200, 400       |
| POST    | `/password/change`       | Modifie le mot de passe de l'utilisateur. | `PasswordChangeRequestDTO`       | Aucun                            | 200, 400, 401  |

#### Exemple : Connexion
**Requête** :
```json
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "motdepasse123"
}
```

**Réponse** :
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

---

### Points de terminaison des administrateurs

**Chemin de base** : `/api/admin`

**Rôle requis** : `ROLE_ADMIN`

**Authentification** : Requise (Jeton JWT Bearer)

#### Gestion des emplois du temps
| Méthode | Point de terminaison      | Description                              | Corps de la requête   | Corps de la réponse             | Codes de statut |
|---------|---------------------------|------------------------------------------|-----------------------|---------------------------------|----------------|
| GET     | `/seances`                | Récupère toutes les sessions.           | Aucun                 | `List<SeanceDTO>`              | 200            |
| GET     | `/seances/{id}`           | Récupère une session par ID.            | Aucun                 | `SeanceDTO`                    | 200, 404       |
| POST    | `/seances`                | Crée une nouvelle session.              | `SeanceDTO`           | `SeanceDTO`                    | 200, 400       |
| PUT     | `/seances/{id}`           | Met à jour une session.                 | `SeanceDTO`           | `SeanceDTO`                    | 200, 400, 404  |
| DELETE  | `/seances/{id}`           | Supprime une session.                   | Aucun                 | Aucun                          | 200, 404       |

#### Gestion des sessions de rattrapage
| Méthode | Point de terminaison                   | Description                              | Corps de la requête   | Corps de la réponse                 | Codes de statut |
|---------|----------------------------------------|------------------------------------------|-----------------------|-------------------------------------|----------------|
| GET     | `/makeup-sessions`                     | Récupère toutes les propositions de sessions de rattrapage. | Aucun      | `List<PropositionDeRattrapageDTO>` | 200            |
| PUT     | `/makeup-sessions/{id}/approve`        | Approuve une session de rattrapage.     | Aucun                 | `PropositionDeRattrapageDTO`       | 200, 400, 404  |
| PUT     | `/makeup-sessions/{id}/reject`         | Rejette une session de rattrapage.      | Aucun                 | `PropositionDeRattrapageDTO`       | 200, 400, 404  |
| PUT     | `/makeup-sessions/{id}/approve-scheduled` | Approuve une session de rattrapage planifiée. | Aucun         | `PropositionDeRattrapageDTO`       | 200, 400, 404  |
| PUT     | `/makeup-sessions/{id}/reject-scheduled` | Rejette une session de rattrapage planifiée. | Aucun         | `PropositionDeRattrapageDTO`       | 200, 400, 404  |

#### Gestion des conflits
| Méthode | Point de terminaison            | Description                              | Corps de la requête | Corps de la réponse                 | Codes de statut |
|---------|---------------------------------|------------------------------------------|--------------------|-------------------------------------|----------------|
| GET     | `/conflicts`                    | Récupère tous les conflits de sessions. | Aucun              | `List<SeanceConflictDTO>`          | 200            |
| GET     | `/conflicts/rooms`              | Récupère les conflits spécifiques aux salles. | Aucun        | `List<SeanceRoomConflictDTO>`      | 200            |
| GET     | `/conflicts/seances/{seanceId}` | Récupère les conflits pour une session spécifique. | Aucun    | `List<SingleSeanceConflictDTO>`    | 200, 404       |

#### Gestion des notifications
| Méthode | Point de terminaison            | Description                              | Corps de la requête | Corps de la réponse | Codes de statut |
|---------|---------------------------------|------------------------------------------|--------------------|--------------------|----------------|
| POST    | `/notifications/broadcast`      | Diffuse une notification à tous les utilisateurs. | `NotificationDTO` | Aucun              | 200, 400       |
| POST    | `/notifications/teachers`       | Envoie une notification à tous les enseignants. | `NotificationDTO` | Aucun              | 200, 400       |
| POST    | `/notifications/students`       | Envoie une notification à tous les étudiants. | `NotificationDTO` | Aucun              | 200, 400       |
| POST    | `/notifications/branches`       | Envoie une notification à des branches spécifiques. | `NotificationDTO`, `branchIds` | Aucun | 200, 400       |

#### Gestion des fichiers Excel
| Méthode | Point de terminaison      | Description                              | Corps de la requête             | Corps de la réponse             | Codes de statut |
|---------|---------------------------|------------------------------------------|--------------------------------|---------------------------------|----------------|
| POST    | `/excel/upload`           | Télécharge un fichier Excel avec des emplois du temps. | `FichierExcelDTO`, `List<SeanceDTO>` | Aucun           | 200, 400       |
| GET     | `/excel/history`          | Récupère l'historique des importations de fichiers Excel. | Aucun                 | `List<FichierExcelDTO>`        | 200            |

#### Exemple : Créer une session
**Requête** :
```json
POST /api/admin/seances
Content-Type: application/json
Authorization: Bearer <jeton_d_accès>

{
  "name": "Cours de Mathématiques",
  "jour": "Lundi",
  "heureDebut": "09:00",
  "heureFin": "11:00",
  "type": "CR",
  "matiere": "Mathématiques",
  "frequence": "hebdomadaire",
  "salleId": 1,
  "enseignantId": 2,
  "brancheIds": [1],
  "tdIds": [1],
  "tpIds": [1]
}
```

**Réponse** :
```json
{
  "id": 1,
  "name": "Cours de Mathématiques",
  "jour": "Lundi",
  "heureDebut": "09:00",
  "heureFin": "11:00",
  "type": "CR",
  "matiere": "Mathématiques",
  "frequence": "hebdomadaire",
  "salleId": 1,
  "enseignantId": 2,
  "brancheIds": [1],
  "tdIds": [1],
  "tpIds": [1]
}
```

---

### Points de terminaison des enseignants

**Chemin de base** : `/api/teachers`

**Authentification** : Non requise dans cette version

**Rôle requis** : Aucun (accessible publiquement)

#### Gestion du profil
| Méthode | Point de terminaison | Description                              | Corps de la requête | Corps de la réponse | Codes de statut |
|---------|----------------------|------------------------------------------|--------------------|---------------------|----------------|
| GET     | `/{id}`              | Récupère le profil de l'enseignant.      | Aucun              | `EnseignantDTO`    | 200, 404       |
| PUT     | `/{id}`              | Met à jour le profil de l'enseignant.    | `EnseignantDTO`    | `EnseignantDTO`    | 200, 400, 404  |

#### Gestion des emplois du temps
| Méthode | Point de terminaison         | Description                              | Corps de la requête | Corps de la réponse     | Codes de statut |
|---------|------------------------------|------------------------------------------|--------------------|-------------------------|----------------|
| GET     | `/{id}/schedule`             | Récupère l'emploi du temps de l'enseignant. | Aucun          | `List<SeanceDTO>`      | 200, 404       |
| GET     | `/{id}/teaching-hours`       | Récupère les heures d'enseignement pour une période. | `startDate`, `endDate` | `Integer` | 200, 404       |
| GET     | `/{id}/subjects`             | Récupère les matières enseignées par l'enseignant. | Aucun      | `List<String>`         | 200, 404       |
| GET     | `/{id}/student-groups`       | Récupère les groupes d'étudiants enseignés. | Aucun        | `List<TPDTO>`          | 200, 404       |

#### Sessions de rattrapage
| Méthode | Point de terminaison         | Description                              | Corps de la requête             | Corps de la réponse                 | Codes de statut |
|---------|------------------------------|------------------------------------------|--------------------------------|-------------------------------------|----------------|
| POST    | `/{id}/makeup-requests`      | Soumet une demande de session de rattrapage. | `PropositionDeRattrapageDTO` | `PropositionDeRattrapageDTO`       | 200, 400, 404  |

#### Communication
| Méthode | Point de terminaison    | Description                              | Corps de la requête | Corps de la réponse     | Codes de statut |
|---------|-------------------------|------------------------------------------|--------------------|-------------------------|----------------|
| POST    | `/{id}/signals`         | Soumet un signal (réclamation/suggestion). | `SignalDTO`       | `SignalDTO`            | 200, 400, 404  |
| GET     | `/{id}/signals`         | Récupère les signaux de l'enseignant.    | Aucun              | `List<SignalDTO>`      | 200, 404       |

#### Notifications
| Méthode | Point de terminaison              | Description                              | Corps de la requête | Corps de la réponse     | Codes de statut |
|---------|-----------------------------------|------------------------------------------|--------------------|-------------------------|----------------|
| GET     | `/{id}/notifications/unread`      | Récupère les notifications non lues.     | Aucun              | `List<NotificationDTO>` | 200            |
| PUT     | `/notifications/{notificationId}/read` | Marque une notification comme lue.  | Aucun              | Aucun                   | 200, 404       |

#### Exemple : Soumettre une demande de rattrapage
**Requête** :
```json
POST /api/teachers/2/makeup-requests
Content-Type: application/json

{
  "name": "Rattrapage Mathématiques",
  "matiere": "Mathématiques",
  "type": "CR",
  "heureDebut": "14:00",
  "heureFin": "16:00",
  "date": "2025-06-01T00:00:00",
  "reason": "Session manquée en raison d'un jour férié",
  "status": "EN_ATTENTE",
  "enseignantId": 2,
  "brancheIds": [1],
  "tdIds": [1],
  "tpIds": [1]
}
```

**Réponse** :
```json
{
  "id": 1,
  "name": "Rattrapage Mathématiques",
  "matiere": "Mathématiques",
  "type": "CR",
  "heureDebut": "14:00",
  "heureFin": "16:00",
  "date": "2025-06-01T00:00:00",
  "reason": "Session manquée en raison d'un jour férié",
  "status": "EN_ATTENTE",
  "enseignantId": 2,
  "brancheIds": [1],
  "tdIds": [1],
  "tpIds": [1]
}
```

---

### Points de terminaison des étudiants

**Chemin de base** : `/api/students`

**Authentification** : Non requise dans cette version

**Rôle requis** : Aucun (accessible publiquement)

#### Gestion du profil
| Méthode | Point de terminaison | Description                              | Corps de la requête | Corps de la réponse | Codes de statut |
|---------|----------------------|------------------------------------------|--------------------|---------------------|----------------|
| GET     | `/{id}`              | Récupère le profil de l'étudiant.        | Aucun              | `EtudiantDTO`      | 200, 404       |
| PUT     | `/{id}`              | Met à jour le profil de l'étudiant.      | `EtudiantDTO`      | `EtudiantDTO`      | 200, 400, 404  |

#### Accès aux emplois du temps
| Méthode | Point de terminaison         | Description                              | Corps de la requête | Corps de la réponse     | Codes de statut |
|---------|------------------------------|------------------------------------------|--------------------|-------------------------|----------------|
| GET     | `/{id}/schedule/personal`    | Récupère l'emploi du temps personnel de l'étudiant. | Aucun    | `List<SeanceDTO>`      | 200, 404       |
| GET     | `/{id}/schedule/branch`      | Récupère l'emploi du temps de la branche. | Aucun         | `List<SeanceDTO>`      | 200, 404       |
| GET     | `/{id}/schedule/td`          | Récupère l'emploi du temps des TD.       | Aucun              | `List<SeanceDTO>`      | 200, 404       |

#### Notifications
| Méthode | Point de terminaison              | Description                              | Corps de la requête | Corps de la réponse     | Codes de statut |
|---------|-----------------------------------|------------------------------------------|--------------------|-------------------------|----------------|
| GET     | `/{id}/notifications`             | Récupère les notifications de l'étudiant. | Aucun             | `List<NotificationDTO>` | 200, 404       |
| GET     | `/{id}/notifications/unread`      | Récupère les notifications non lues.     | Aucun              | `List<NotificationDTO>` | 200            |
| PUT     | `/notifications/{notificationId}/read` | Marque une notification comme lue.  | Aucun              | Aucun                   | 200, 404       |

#### Exemple : Récupérer l'emploi du temps personnel
**Requête** :
```json
GET /api/students/3/schedule/personal
```

**Réponse** :
```json
[
  {
    "id": 1,
    "name": "Cours de Mathématiques",
    "jour": "Lundi",
    "heureDebut": "09:00",
    "heureFin": "11:00",
    "type": "CR",
    "matiere": "Mathématiques",
    "frequence": "hebdomadaire",
    "salleId": 1,
    "enseignantId": 2,
    "brancheIds": [1],
    "tdIds": [1],
    "tpIds": [1]
  }
]
```

---

### Points de terminaison de gestion des salles

**Chemin de base** : `/api/rooms`

**Rôle requis** : `ROLE_TECHNICIAN`

**Authentification** : Requise (Jeton JWT Bearer)

| Méthode | Point de terminaison | Description                              | Corps de la requête | Corps de la réponse     | Codes de statut |
|---------|----------------------|------------------------------------------|--------------------|-------------------------|----------------|
| GET     | `/`                  | Récupère toutes les salles.              | Aucun              | `List<SalleDTO>`       | 200            |
| GET     | `/{id}`              | Récupère une salle par ID.               | Aucun              | `SalleDTO`             | 200, 404       |
| POST    | `/`                  | Crée une nouvelle salle.                 | `SalleDTO`         | `SalleDTO`             | 201, 400       |
| PUT     | `/{id}`              | Met à jour une salle.                    | `SalleDTO`         | `SalleDTO`             | 200, 400, 404  |
| DELETE  | `/{id}`              | Supprime une salle.                      | Aucun              | Aucun                  | 200, 404       |

#### Exemple : Créer une salle
**Requête** :
```json
POST /api/rooms
Content-Type: application/json
Authorization: Bearer <jeton_d_accès>

{
  "identifiant": "A101",
  "type": "Amphithéâtre",
  "capacite": 50,
  "seanceIds": []
}
```

**Réponse** :
```json
{
  "id": 1,
  "identifiant": "A101",
  "type": "Amphithéâtre",
  "capacite": 50,
  "seanceIds": []
}
```

---

### Points de terminaison des techniciens

**Chemin de base** : `/api/technicians`

**Rôle requis** : `ROLE_TECHNICIAN`

**Authentification** : Requise (Jeton JWT Bearer)

#### Gestion des techniciens
| Méthode | Point de terminaison | Description                              | Corps de la requête | Corps de la réponse     | Codes de statut |
|---------|----------------------|------------------------------------------|--------------------|-------------------------|----------------|
| GET     | `/{id}`              | Récupère le profil du technicien.        | Aucun              | `TechnicienDTO`        | 200, 404       |
| GET     | `/`                  | Récupère tous les techniciens.           | Aucun              | `List<TechnicienDTO>`  | 200            |
| POST    | `/`                  | Crée un nouveau technicien.              | `TechnicienDTO`    | `TechnicienDTO`        | 201, 400       |
| PUT     | `/{id}`              | Met à jour un technicien.                | `TechnicienDTO`    | `TechnicienDTO`        | 200, 400, 404  |
| DELETE  | `/{id}`              | Supprime un technicien.                  | Aucun              | Aucun                  | 204, 404       |

#### Gestion des utilisateurs
| Méthode | Point de terminaison         | Description                              | Corps de la requête | Corps de la réponse     | Codes de statut |
|---------|------------------------------|------------------------------------------|--------------------|-------------------------|----------------|
| GET     | `/users/{id}`                | Récupère un utilisateur par ID.          | Aucun              | `UserDTO`              | 200, 404       |
| GET     | `/users`                     | Récupère tous les utilisateurs.          | Aucun              | `List<UserDTO>`        | 200            |
| POST    | `/users`                     | Crée un nouvel utilisateur.              | `UserDTO`          | `UserDTO`              | 201, 400       |
| PUT     | `/users/{id}`                | Met à jour un utilisateur.               | `UserDTO`          | `UserDTO`              | 200, 400, 404  |
| DELETE  | `/users/{id}`                | Supprime un utilisateur.                 | Aucun              | Aucun                  | 204, 404       |
| GET     | `/users/personne/{personneId}` | Récupère un utilisateur par ID de personne. | Aucun         | `UserDTO`              | 200, 404       |

#### Exemple : Créer un utilisateur
**Requête** :
```json
POST /api/technicians/users
Content-Type: application/json
Authorization: Bearer <jeton_d_accès>

{
  "personneId": 1,
  "username": "admin1",
  "password": "motdepasse123",
  "role": "ROLE_ADMIN",
  "status": "ACTIF",
  "personneData": {
    "cin": "12345678",
    "nom": "Doe",
    "prenom": "Jane",
    "email": "jane.doe@example.com",
    "tel": "1234567890",
    "adresse": "123 Rue Principale",
    "codeAdmin": "ADM001"
  }
}
```

**Réponse** :
```json
{
  "id": 1,
  "personneId": 1,
  "username": "admin1",
  "password": "[mot_de_passe_haché]",
  "role": "ROLE_ADMIN",
  "status": "ACTIF",
  "personneData": {
    "cin": "12345678",
    "nom": "Doe",
    "prenom": "Jane",
    "email": "jane.doe@example.com",
    "tel": "1234567890",
    "adresse": "123 Rue Principale",
    "codeAdmin": "ADM001"
  }
}
```

---

## Modèles de données

Ci-dessous sont listés les principaux objets de transfert de données (DTO) utilisés dans l'API.

### `PersonneDTO`
DTO de base pour toutes les personnes.
```json
{
  "id": Long,
  "cin": String,
  "nom": String,
  "prenom": String,
  "email": String,
  "tel": String,
  "adresse": String
}
```

### `AdministrateurDTO`
Étend `PersonneDTO`.
```json
{
  ...PersonneDTO,
  "codeAdmin": String
}
```

### `EnseignantDTO`
Étend `PersonneDTO`.
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
Étend `PersonneDTO`.
```json
{
  ...PersonneDTO,
  "matricule": String,
  "brancheId": Long,
  "tpId": Long
}
```

### `TechnicienDTO`
Étend `PersonneDTO`.
```json
{
  ...PersonneDTO,
  "codeTechnicien": String
}
```

### `SeanceDTO`
Représente une session d'enseignement.
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
Représente une salle.
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
Représente un programme académique.
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
Représente un groupe de travaux dirigés.
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
Représente un groupe de travaux pratiques.
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
Représente une proposition de session de rattrapage.
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
Représente une notification.
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
Représente une réclamation ou suggestion d'un enseignant.
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
Représente un fichier Excel importé.
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
Représente un compte utilisateur.
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
Représente une réponse d'opération par lot.
```json
{
  "ids": List<Long>,
  "message": String,
  "success": Boolean,
  "entityType": String
}
```

### DTO d'authentification
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

## Gestion des erreurs

L'API utilise une exception personnalisée (`CustomException`) pour gérer les erreurs. Les réponses suivent ce format :

```json
{
  "error": String,
  "message": String,
  "status": Integer,
  "timestamp": String
}
```

**Codes de statut courants** :
- **200 OK** : Requête réussie.
- **201 Créé** : Ressource créée.
- **204 Pas de contenu** : Requête réussie, aucun contenu retourné.
- **400 Mauvaise requête** : Données de requête invalides.
- **401 Non autorisé** : Échec de l'authentification (applicable à `/api/admin` et `/api/technicians`).
- **403 Interdit** : Permissions insuffisantes (applicable à `/api/admin` et `/api/technicians`).
- **404 Non trouvé** : Ressource non trouvée.
- **500 Erreur interne du serveur** : Erreur du serveur.

---

## Sécurité

- **Authentification JWT** : Requise pour les points de terminaison `/api/admin` et `/api/technicians`. Non requise pour `/api/students` et `/api/teachers` dans cette version.
- **Contrôle d'accès basé sur les rôles** : Appliqué à `/api/admin` (requiert `ROLE_ADMIN`) et `/api/technicians` (requiert `ROLE_TECHNICIAN`). Aucun contrôle de rôle pour `/api/students` et `/api/teachers`.
- **Validation des entrées** : Les corps des requêtes sont validés à l'aide des annotations de validation Jakarta Bean (par exemple, `@NotBlank`, `@NotNull`).
- **HTTPS** : Toutes les requêtes API doivent être effectuées via HTTPS pour garantir la sécurité des données.

---

## Notes d'utilisation

- **Horodatages** : Utilisez le format ISO 8601 (par exemple, `2025-06-01T14:00:00`) pour les champs de date et heure.
- **Pagination** : Pour les points de terminaison renvoyant des listes, envisagez d'implémenter la pagination dans les futures versions.
- **Téléchargements Excel** : Assurez-vous que les fichiers Excel respectent le schéma attendu pour éviter les erreurs d'importation.
- **Détection des conflits** : Utilisez les points de terminaison de gestion des conflits pour identifier et résoudre les conflits de planification avant de finaliser les emplois du temps.
- **Notifications** : Les notifications sont déclenchées automatiquement pour les changements d'emploi du temps, mais peuvent également être envoyées manuellement par les administrateurs.
- **Accès public** : Les points de terminaison `/api/students` et `/api/teachers` sont accessibles publiquement dans cette version, ce qui peut poser des risques de sécurité. Envisagez d'ajouter une authentification dans les futures itérations.

Pour plus de détails ou d'assistance, contactez l'administrateur du système ou consultez le dépôt du projet.