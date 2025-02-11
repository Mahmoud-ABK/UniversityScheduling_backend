******__### **Documentation Complète des Endpoints**

_Système de Gestion des Emplois du Temps Universitaires_

---

## **1. Authentification**

_Accessible à tous les utilisateurs._

|**Endpoint**|**Méthode**|**Description**|
|---|---|---|
|`/api/auth/connexion`|POST|Authentification (retourne un JWT).|
|`/api/auth/deconnexion`|POST|Déconnexion (invalide le JWT).|
|`/api/auth/rafraichir-token`|POST|Rafraîchit un JWT expiré.|
|`/api/auth/reinitialiser-motdepasse`|POST|Envoie un lien de réinitialisation de mot de passe.|
|`/api/auth/nouveau-motdepasse`|POST|Valide le nouveau mot de passe.|

---

## **2. Administrateur (Admin)**

_Accès exclusif aux administrateurs._

### **Emplois du Temps**

|**Endpoint**|**Méthode**|**Description**|
|---|---|---|
|`/api/admin/emplois-du-temps/import-excel`|POST|Importe des séances depuis un fichier Excel.|
|`/api/admin/emplois-du-temps/generer`|POST|Génère automatiquement un emploi du temps.|

### **Sessions de Rattrapage**

|**Endpoint**|**Méthode**|**Description**|
|---|---|---|
|`/api/admin/sessions-rattrapage`|GET|Liste toutes les demandes de rattrapage.|
|`/api/admin/sessions-rattrapage/{id}/approuver`|PUT|Approuve une demande de rattrapage.|
|`/api/admin/sessions-rattrapage/{id}/rejeter`|PUT|Rejette une demande de rattrapage.|

### **Notifications**

|**Endpoint**|**Méthode**|**Description**|
|---|---|---|
|`/api/admin/notifications/diffuser`|POST|Envoie une notification à tous les utilisateurs.|

---

## **3. Enseignant (Professeur)**

_Accès aux enseignants pour leurs propres données._

|**Endpoint**|**Méthode**|**Description**|
|---|---|---|
|`/api/enseignants/{id}/emploi-du-temps`|GET|Emploi du temps de l'enseignant.|
|`/api/enseignants/{id}/heures-enseignees`|GET|Total des heures enseignées.|
|`/api/enseignants/{id}/sessions-rattrapage`|POST|Soumet une demande de rattrapage.|
|`/api/enseignants/{id}/signalisations`|POST|Soumet une réclamation/suggestion.|
|`/api/enseignants/{id}/signalisations`|GET|Liste les réclamations/suggestions de l'enseignant.|

---

## **4. Étudiant**

_Accès aux étudiants pour leurs propres données._

|**Endpoint**|**Méthode**|**Description**|
|---|---|---|
|`/api/etudiants/{id}/emploi-du-temps`|GET|Emploi du temps personnel.|
|`/api/etudiants/branches/{id}/emploi-du-temps`|GET|Emploi du temps de la branche.|
|`/api/etudiants/{id}/notifications`|GET|Notifications reçues.|

---

## **5. Séances (Seance)**

_Accès aux administrateurs (toutes opérations) et techniciens (modification des salles uniquement)._

### **CRUD Standard**

|**Endpoint**|**Méthode**|**Description**|**Rôles**|
|---|---|---|---|
|`/api/seances`|GET|Liste toutes les séances.|Admin, Technicien|
|`/api/seances/{id}`|GET|Détails d'une séance.|Admin, Technicien|
|`/api/seances`|POST|Crée une nouvelle séance.|Admin|
|`/api/seances/{id}`|PUT|Modifie une séance (salle, horaires, etc.).|Admin (tous), Technicien (salle uniquement)|
|`/api/seances/{id}`|DELETE|Supprime une séance.|Admin|

### **Conflits**

|**Endpoint**|**Méthode**|**Description**|**Rôles**|
|---|---|---|---|
|`/api/seances/conflits`|GET|Liste tous les conflits (salles, enseignants, etc.).|Admin, Technicien|
|`/api/seances/conflits/salles`|GET|Conflits liés aux salles.|Admin, Technicien|
|`/api/seances/conflits/{seanceId}`|GET|Conflits pour une séance spécifique.|Admin, Technicien|

---

## **6. Salles (Salle)**

_Accès aux administrateurs et techniciens._

|**Endpoint**|**Méthode**|**Description**|**Rôles**|
|---|---|---|---|
|`/api/salles`|GET|Liste toutes les salles.|Admin, Technicien|
|`/api/salles/{id}`|GET|Détails d'une salle.|Admin, Technicien|
|`/api/salles`|POST|Crée une nouvelle salle.|Admin, Technicien|
|`/api/salles/{id}`|PUT|Modifie une salle (capacité, type, disponibilité).|Admin, Technicien|
|`/api/salles/{id}`|DELETE|Supprime une salle.|Admin, Technicien|
|`/api/salles/disponibles`|GET|Salles disponibles à une date/heure.|Admin, Technicien|

---

## **7. Branches (Branche)**

_Accès aux administrateurs (écriture) et techniciens (lecture)._

|**Endpoint**|**Méthode**|**Description**|**Rôles**|
|---|---|---|---|
|`/api/branches`|GET|Liste toutes les branches.|Admin, Technicien|
|`/api/branches/{id}`|GET|Détails d'une branche.|Admin, Technicien|
|`/api/branches`|POST|Crée une nouvelle branche.|Admin|
|`/api/branches/{id}`|PUT|Modifie une branche.|Admin|
|`/api/branches/{id}`|DELETE|Supprime une branche.|Admin|

---

## **8. TD/TP**

_Accès aux administrateurs (écriture) et techniciens (lecture)._

|**Endpoint**|**Méthode**|**Description**|**Rôles**|
|---|---|---|---|
|`/api/tds`|GET|Liste tous les TD.|Admin, Technicien|
|`/api/tds/{id}/tps`|GET|Liste les TP associés à un TD.|Admin, Technicien|
|`/api/tps`|GET|Liste tous les TP.|Admin, Technicien|
|`/api/tps/{id}/etudiants`|GET|Étudiants inscrits à un TP.|Admin, Technicien|

---

## **9. Notifications**

_Accessible à tous les utilisateurs (leurs propres notifications)._

|**Endpoint**|**Méthode**|**Description**|**Rôles**|
|---|---|---|---|
|`/api/notifications`|GET|Liste toutes les notifications.|Tous (lecture)|
|`/api/notifications/{id}/marquer-lu`|PUT|Marque une notification comme lue.|Tous|
|`/api/notifications/non-lues`|GET|Liste les notifications non lues.|Tous|

---

## **10. Utilisateurs**

_Accès exclusif aux techniciens._

|**Endpoint**|**Méthode**|**Description**|**Rôles**|
|---|---|---|---|
|`/api/utilisateurs`|GET|Liste tous les utilisateurs.|Technicien|
|`/api/utilisateurs`|POST|Crée un nouvel utilisateur.|Technicien|
|`/api/utilisateurs/{id}`|PUT|Modifie un utilisateur.|Technicien|
|`/api/utilisateurs/{id}/mot-de-passe`|PUT|Réinitialise le mot de passe.|Technicien|

---

## **11. Fichiers Excel**

_Accès exclusif aux administrateurs._

|**Endpoint**|**Méthode**|**Description**|**Rôles**|
|---|---|---|---|
|`/api/fichiers/excel/televerser`|POST|Téléverse un fichier Excel.|Admin|
|`/api/fichiers/excel/historique`|GET|Historique des fichiers importés.|Admin|

---

## **Paramètres Communs**

- **Pagination** : `page`, `taille`, `tri` (ex: `tri=jour,asc`).

- **Filtres** : `dateDebut`, `dateFin`, `recherche`.

- **Format des Dates** : `AAAA-MM-JJ` (ex: `2023-10-05`).


---

## **Réponses Standardisées**

- **Succès** :

    - `200 OK` : Requête traitée avec succès.

    - `201 Created` : Ressource créée (ex: nouvelle salle).

    - `204 No Content` : Suppression réussie.

- **Erreurs** :

    - `400 Bad Request` : Paramètres invalides.

    - `401 Unauthorized` : JWT manquant ou invalide.

    - `403 Forbidden` : Accès refusé (rôle non autorisé).

    - `404 Not Found` : Ressource introuvable.

    - `409 Conflict` : Conflit détecté (ex: salle déjà réservée).


---

## **Résumé des Rôles**

|**Rôle**|**Accès**|
|---|---|
|**Administrateur**|Gestion complète des séances, salles, branches, et fichiers Excel.|
|**Technicien**|Gestion des utilisateurs et des salles. Lecture des conflits et séances.|
|**Enseignant**|Consultation de son emploi du temps et soumission de demandes.|
|**Étudiant**|Consultation de son emploi du temps et notifications.|

---

**Notes** :

- Tous les endpoints (sauf `/auth/*`) nécessitent un **JWT valide**.

- Les conflits sont automatiquement détectés mais ne bloquent pas la sauvegarde.__******