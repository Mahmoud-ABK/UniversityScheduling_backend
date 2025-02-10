### **Documentation Complète des Endpoints**

_Système de Gestion des Emplois du Temps Universitaires_

---

## **Rôles et Privilèges**

|**Rôle**|**Accès**|**Limitations**|
|---|---|---|
|**Administrateur**|- Gestion complète des emplois du temps, séances, salles, branches, notifications.|- Ne peut pas gérer les comptes utilisateurs.|
||- Importation de fichiers Excel.||
||- Résolution des conflits.||
|**Technicien**|- Gestion des utilisateurs (création, modification, suppression).|- Ne peut pas créer/supprimer des séances.|
||- Modification des salles et des salles associées aux séances.|- Ne peut pas modifier les horaires, enseignants, ou branches des séances.|
||- Lecture des conflits et des données (salles, branches, TD/TP).||
|**Enseignant**|- Consultation de son emploi du temps et de ses heures enseignées.|- Ne peut modifier aucune donnée académique.|
||- Soumission de demandes de rattrapage et de signalisations.|- Accès restreint à ses propres données.|
|**Étudiant**|- Consultation de son emploi du temps et des notifications.|- Aucun accès aux fonctions administratives ou techniques.|

---

## **Endpoints par Contrôleur**

---

### **1. Authentification**

_Accessible à tous les utilisateurs._

|**Endpoint**|**Méthode**|**Description**|
|---|---|---|
|`/api/auth/connexion`|POST|Authentification et obtention d'un JWT.|
|`/api/auth/deconnexion`|POST|Invalidation du JWT.|
|`/api/auth/rafraichir-token`|POST|Rafraîchissement du JWT expiré.|
|`/api/auth/reinitialiser-motdepasse`|POST|Demande de réinitialisation de mot de passe.|
|`/api/auth/nouveau-motdepasse`|POST|Validation du nouveau mot de passe.|

---

### **2. Administrateur**

#### **Gestion des Emplois du Temps**

|**Endpoint**|**Méthode**|**Description**|
|---|---|---|
|`/api/admin/emplois-du-temps/import-excel`|POST|Importe un fichier Excel pour générer des séances.|
|`/api/admin/emplois-du-temps/generer`|POST|Génère automatiquement un emploi du temps.|
|`/api/admin/emplois-du-temps/conflits`|GET|Liste tous les conflits (salles, enseignants, branches).|

#### **Sessions de Rattrapage**

|**Endpoint**|**Méthode**|**Description**|
|---|---|---|
|`/api/admin/sessions-rattrapage`|GET|Liste toutes les demandes de rattrapage.|
|`/api/admin/sessions-rattrapage/{id}/approuver`|PUT|Approuve une demande de rattrapage.|
|`/api/admin/sessions-rattrapage/{id}/rejeter`|PUT|Rejette une demande de rattrapage.|

#### **Notifications**

|**Endpoint**|**Méthode**|**Description**|
|---|---|---|
|`/api/admin/notifications/diffuser`|POST|Envoie une notification globale à tous les utilisateurs.|

---

### **3. Enseignant**

|**Endpoint**|**Méthode**|**Description**|
|---|---|---|
|`/api/enseignants/{id}/emploi-du-temps`|GET|Retourne l'emploi du temps de l'enseignant.|
|`/api/enseignants/{id}/heures-enseignees`|GET|Retourne le total des heures enseignées.|
|`/api/enseignants/{id}/sessions-rattrapage`|POST|Soumet une demande de session de rattrapage.|
|`/api/enseignants/{id}/sessions-rattrapage`|GET|Liste les demandes de rattrapage de l'enseignant.|
|`/api/enseignants/{id}/signalisations`|POST|Soumet une signalisation (réclamation/suggestion).|
|`/api/enseignants/{id}/signalisations`|GET|Liste les signalisations de l'enseignant.|

---

### **4. Étudiant**

|**Endpoint**|**Méthode**|**Description**|
|---|---|---|
|`/api/etudiants/{id}/emploi-du-temps`|GET|Retourne l'emploi du temps personnel.|
|`/api/etudiants/branches/{id}/emploi-du-temps`|GET|Retourne l'emploi du temps de la branche.|
|`/api/etudiants/{id}/notifications`|GET|Liste les notifications reçues.|

---

### **5. Séances (Seance)**

#### **CRUD Standard**

|**Endpoint**|**Méthode**|**Description**|**Rôles**|
|---|---|---|---|
|`/api/seances`|GET|Liste toutes les séances.|Admin, Technicien|
|`/api/seances/{id}`|GET|Détails d'une séance.|Admin, Technicien|
|`/api/seances`|POST|Crée une nouvelle séance.|Admin|
|`/api/seances/{id}`|PUT|Modifie une séance (technicien : uniquement la salle).|Admin, Technicien|
|`/api/seances/{id}`|DELETE|Supprime une séance.|Admin|

#### **Filtres & Conflits**

|**Endpoint**|**Méthode**|**Description**|**Rôles**|
|---|---|---|---|
|`/api/seances/quotidiens`|GET|Séances pour un jour spécifique (`jour=2023-10-05`).|Admin, Technicien|
|`/api/seances/conflits/salles`|GET|Conflits liés aux salles (`salleId=5` pour filtrer).|Admin, Technicien|
|`/api/seances/conflits/{seanceId}`|GET|Conflits pour une séance spécifique.|Admin, Technicien|

---

### **6. Salles (Salle)**

|**Endpoint**|**Méthode**|**Description**|**Rôles**|
|---|---|---|---|
|`/api/salles`|GET|Liste toutes les salles.|Admin, Technicien|
|`/api/salles/{id}`|GET|Détails d'une salle.|Admin, Technicien|
|`/api/salles`|POST|Crée une nouvelle salle.|Admin, Technicien|
|`/api/salles/{id}`|PUT|Modifie une salle (capacité, disponibilité, type).|Admin, Technicien|
|`/api/salles/{id}`|DELETE|Supprime une salle.|Admin, Technicien|
|`/api/salles/disponibles`|GET|Salles disponibles pour un jour donné (`jour=2023-10-05`).|Admin, Technicien|

---

### **7. Utilisateurs (Technicien)**

|**Endpoint**|**Méthode**|**Description**|
|---|---|---|
|`/api/utilisateurs`|GET|Liste tous les utilisateurs.|
|`/api/utilisateurs`|POST|Crée un nouvel utilisateur.|
|`/api/utilisateurs/{id}/mot-de-passe`|PUT|Réinitialise le mot de passe d'un utilisateur.|
|`/api/utilisateurs/roles/{role}`|GET|Filtre les utilisateurs par rôle (`étudiant`, `enseignant`, etc.).|

---

### **8. Fichiers Excel (Admin)**

|**Endpoint**|**Méthode**|**Description**|
|---|---|---|
|`/api/fichiers/excel/televerser`|POST|Téléverse un fichier Excel pour importer des données.|
|`/api/fichiers/excel/{id}/statut`|GET|Retourne le statut d'importation d'un fichier.|
|`/api/fichiers/excel/historique`|GET|Retourne l'historique des imports.|

---

## **Paramètres Communs**

- **Pagination** : `page`, `taille`, `tri` (ex: `tri=jour,asc`).

- **Filtres Temporels** : `dateDebut`, `dateFin`.

- **Recherche** : `recherche` (filtre par nom, matière, etc.).


---

## **Réponses Standardisées**

#### **Succès**

- `200 OK` : Requête traitée avec succès.

- `201 Created` : Ressource créée (ex: nouvelle séance).

- `204 No Content` : Suppression réussie.


#### **Erreurs**

- `400 Bad Request` : Paramètres invalides.

- `401 Unauthorized` : Non authentifié.

- `403 Forbidden` : Accès refusé (rôle non autorisé).

- `404 Not Found` : Ressource introuvable.

- `409 Conflict` : Conflit détecté (ex: salle déjà réservée).


---

## **Exemple de Requête et Réponse**

#### **Modification d'une Salle par un Technicien**

http

Copy

PUT /api/salles/3 HTTP/1.1
Content-Type: application/json
Authorization: Bearer <JWT>
{
"capacite": 50,
"type": "Laboratoire"
}

#### **Réponse Succès (200 OK)**

json

Copy

{
"id": 3,
"identifiant": "LAB-101",
"type": "Laboratoire",
"capacite": 50,
"disponibilite": ["08:00-10:00", "14:00-16:00"]
}

#### **Réponse Conflit (409 Conflict)**

json

Copy

{
"erreur": "CONFLIT_SALLE",
"message": "La salle LAB-101 est déjà réservée pour la plage horaire 14:00-16:00."
}

---

## **Notes Finales**

1. **Sécurité** :

    - Tous les endpoints (sauf `/auth/*`) nécessitent un **JWT valide**.

    - Les privilèges sont validés via annotations `@PreAuthorize` dans le backend.

2. **Conflits** :

    - Les conflits sont détectés mais ne bloquent pas les opérations (sauf configuration explicite).

3. **Documentation Interactive** :

    - Implémentez Swagger/OpenAPI pour une exploration simplifiée des endpoints.


---