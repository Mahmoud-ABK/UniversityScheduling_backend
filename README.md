# University Schedule Management System

## Overview
The University Schedule Management System is a RESTful API designed to streamline academic scheduling, user management, and resource allocation for universities. Built with **Spring Boot**, the system supports administrators, technicians, teachers, and students by providing features such as schedule creation, makeup session management, notifications, and Excel file imports for scheduling data. This project aims to enhance efficiency in managing university timetables and resources.

### Key Features
- **Schedule Management**: Create, update, and delete teaching sessions (lectures, tutorials, practicals).
- **User Management**: Manage accounts for administrators and technicians (students and teachers are publicly accessible in this version).
- **Notifications**: Send and receive notifications for schedule changes or announcements.
- **Makeup Sessions**: Propose, approve, or reject catch-up sessions.
- **Conflict Detection**: Identify and resolve scheduling conflicts.
- **Excel Integration**: Import scheduling data from Excel files.
- **Public Access**: In this initial version, endpoints for students (`/api/students`) and teachers (`/api/teachers`) are publicly accessible without authentication or role-based authorization (`ROLE_STUDENT` and `ROLE_TEACHER` are not used).

## Technologies
- **Backend**: Spring Boot, Spring Security, JPA/Hibernate
- **Database**: Configurable (e.g., MySQL, PostgreSQL)
- **Authentication**: JWT (JSON Web Tokens) for `/api/admin` and `/api/technicians` endpoints
- **Validation**: Jakarta Bean Validation
- **Build Tool**: Maven

## Installation

### Prerequisites
- Java 17 or higher
- Maven 3.6.0 or higher
- A relational database (e.g., MySQL, PostgreSQL)
- Git

### Steps
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/Mahmoud-ABK/UniversityScheduling_backend.git
   cd university-schedule-management
   ```
   or
   ```bash
   git clone git@github.com:Mahmoud-ABK/UniversityScheduling_backend.git
   cd university-schedule-management
   ```

2. **Configure the Database**:
   Update the `application.properties` file in `src/main/resources` with your database settings:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/university_schedule
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   ```

3. **Build the Project**:
   ```bash
   mvn clean install
   ```

4. **Run the Application**:
   ```bash
   mvn spring-boot:run
   ```

5. **Access the API**:
   The API will be available at `http://localhost:8080`. Use a tool like Postman or cURL to interact with the endpoints.

## Swagger UI
To explore and test the API interactively, access the **Swagger UI** at `http://localhost:8080/swagger-ui/index.html` (or the appropriate host/port for your deployment). Swagger UI provides a user-friendly interface to:
- View detailed endpoint documentation, including request/response schemas.
- Test endpoints directly with the "Try it out" feature.
- Authenticate for secured endpoints (`/api/admin`, `/api/technicians`, `/api/rooms`) using JWT by entering `Bearer <token>` in the "Authorize" button.
- Explore public endpoints (`/api/auth`, `/api/teachers`, `/api/students`) without authentication.

## Usage
- **Authentication**: Use `/api/auth/login` to obtain a JWT for accessing `/api/admin` and `/api/technicians` endpoints. No authentication is required for `/api/students` and `/api/teachers`.
- **Administrators**: Manage schedules, approve makeup sessions, and send notifications via `/api/admin` (requires `ROLE_ADMIN`).
- **Technicians**: Manage rooms and user accounts via `/api/technicians` (requires `ROLE_TECHNICIAN`).
- **Teachers**: Access schedules, submit makeup requests, and send signals via `/api/teachers` (publicly accessible).
- **Students**: View personal and branch schedules via `/api/students` (publicly accessible).
- **API Documentation**:
  - English: Refer to [API Documentation](./documentation/API%20Documentation.md) for detailed endpoint descriptions, request/response formats, and examples.
  - French: Refer to [Documentation de l'API](./documentation/API%20Documentation%20[FR]%20.md) for the French version.

### Example Request
**Get a Student's Schedule** (No authentication required):
```bash
curl http://localhost:8080/api/students/3/schedule/personal
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

## API Documentation
For a comprehensive guide to all endpoints, request/response schemas, and error handling:
- English: [API Documentation](./documentation/API%20Documentation.md)
- French: [Documentation de l'API](./documentation/API%20Documentation%20[FR]%20.md)

## Contributing
Contributions are welcome! To contribute:
1. Fork the repository.
2. Create a new branch (`git checkout -b feature/your-feature`).
3. Make your changes and commit (`git commit -m "Add your feature"`).
4. Push to the branch (`git push origin feature/your-feature`).
5. Open a Pull Request.

Please ensure your code follows the project's coding standards and includes tests where applicable.

---

# Système de Gestion des Emplois du Temps Universitaires

## Aperçu
Le Système de Gestion des Emplois du Temps Universitaires est une API REST conçue pour rationaliser la planification académique, la gestion des utilisateurs et l'allocation des ressources dans les universités. Construit avec **Spring Boot**, le système prend en charge les administrateurs, techniciens, enseignants et étudiants en offrant des fonctionnalités telles que la création d'emplois du temps, la gestion des sessions de rattrapage, les notifications et l'importation de données de planification depuis des fichiers Excel. Ce projet vise à améliorer l'efficacité dans la gestion des emplois du temps et des ressources universitaires.

### Fonctionnalités principales
- **Gestion des emplois du temps** : Créer, mettre à jour et supprimer des sessions d'enseignement (cours magistraux, travaux dirigés, travaux pratiques).
- **Gestion des utilisateurs** : Gérer les comptes des administrateurs et techniciens (les étudiants et enseignants sont accessibles publiquement dans cette version).
- **Notifications** : Envoyer et recevoir des notifications pour les changements d'emploi du temps ou les annonces.
- **Sessions de rattrapage** : Proposer, approuver ou rejeter des sessions de rattrapage.
- **Détection des conflits** : Identifier et résoudre les conflits de planification.
- **Intégration Excel** : Importer des données de planification depuis des fichiers Excel.
- **Accès public** : Dans cette version initiale, les points de terminaison pour les étudiants (`/api/students`) et les enseignants (`/api/teachers`) sont accessibles publiquement sans authentification ni autorisation basée sur les rôles (`ROLE_STUDENT` et `ROLE_TEACHER` ne sont pas utilisés).

## Technologies
- **Backend** : Spring Boot, Spring Security, JPA/Hibernate
- **Base de données** : Configurable (par exemple, MySQL, PostgreSQL)
- **Authentification** : JWT (JSON Web Tokens) pour les points de terminaison `/api/admin` et `/api/technicians`
- **Validation** : Jakarta Bean Validation
- **Outil de construction** : Maven

## Installation

### Prérequis
- Java 17 ou supérieur
- Maven 3.6.0 ou supérieur
- Une base de données relationnelle (par exemple, MySQL, PostgreSQL)
- Git

### Étapes
1. **Cloner le dépôt** :
   ```bash
   git clone https://github.com/Mahmoud-ABK/UniversityScheduling_backend.git
   cd university-schedule-management
   ```
   ou
   ```bash
   git clone git@github.com:Mahmoud-ABK/UniversityScheduling_backend.git
   cd university-schedule-management
   ```

2. **Configurer la base de données** :
   Mettez à jour le fichier `application.properties` dans `src/main/resources` avec les paramètres de votre base de données :
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/university_schedule
   spring.datasource.username=votre_nom_utilisateur
   spring.datasource.password=votre_mot_de_passe
   spring.jpa.hibernate.ddl-auto=update
   ```

3. **Construire le projet** :
   ```bash
   mvn clean install
   ```

4. **Lancer l'application** :
   ```bash
   mvn spring-boot:run
   ```

5. **Accéder à l'API** :
   L'API sera disponible à `http://localhost:8080`. Utilisez un outil comme Postman ou cURL pour interagir avec les points de terminaison.

## Swagger UI
Pour explorer et tester l'API de manière interactive, accédez à l'**interface Swagger UI** à `http://localhost:8080/swagger-ui/index.html` (ou l'hôte/port approprié pour votre déploiement). L'interface Swagger UI offre une interface conviviale pour :
- Consulter la documentation détaillée des points de terminaison, y compris les schémas de requête/réponse.
- Tester les points de terminaison directement avec la fonctionnalité "Try it out".
- S'authentifier pour les points de terminaison sécurisés (`/api/admin`, `/api/technicians`, `/api/rooms`) en utilisant JWT en entrant `Bearer <token>` dans le bouton "Authorize".
- Explorer les points de terminaison publics (`/api/auth`, `/api/teachers`, `/api/students`) sans authentification.

## Utilisation
- **Authentification** : Utilisez `/api/auth/login` pour obtenir un JWT pour accéder aux points de terminaison `/api/admin` et `/api/technicians`. Aucune authentification n'est requise pour `/api/students` et `/api/teachers`.
- **Administrateurs** : Gérez les emplois du temps, approuvez les sessions de rattrapage et envoyez des notifications via `/api/admin` (requiert `ROLE_ADMIN`).
- **Techniciens** : Gérez les salles et les comptes utilisateurs via `/api/technicians` (requiert `ROLE_TECHNICIAN`).
- **Enseignants** : Accédez aux emplois du temps, soumettez des demandes de rattrapage et envoyez des signaux via `/api/teachers` (accessible publiquement).
- **Étudiants** : Consultez les emplois du temps personnels et de branche via `/api/students` (accessible publiquement).
- **Documentation de l'API** :
  - Français : Consultez [Documentation de l'API](./documentation/API%20Documentation%20[FR]%20.md) pour des descriptions détaillées des points de terminaison, des formats de requête/réponse et des exemples.
  - Anglais : Consultez [API Documentation](./documentation/API%20Documentation.md) pour la version anglaise.

### Exemple de requête
**Récupérer l'emploi du temps d'un étudiant** (Aucune authentification requise) :
```bash
curl http://localhost:8080/api/students/3/schedule/personal
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

## Documentation de l'API
Pour un guide complet de tous les points de terminaison, schémas de requête/réponse et gestion des erreurs :
- Français : [Documentation de l'API](./documentation/API%20Documentation%20[FR]%20.md)
- Anglais : [API Documentation](./documentation/API%20Documentation.md)

## Contribution
Les contributions sont les bienvenues ! Pour contribuer :
1. Forkez le dépôt.
2. Créez une nouvelle branche (`git checkout -b fonctionnalite/votre-fonctionnalite`).
3. Effectuez vos modifications et validez-les (`git commit -m "Ajouter votre fonctionnalité"`).
4. Poussez vers la branche (`git push origin fonctionnalite/votre-fonctionnalite`).
5. Ouvrez une Pull Request.

Veuillez vous assurer que votre code respecte les normes de codage du projet et inclut des tests lorsque cela est applicable.