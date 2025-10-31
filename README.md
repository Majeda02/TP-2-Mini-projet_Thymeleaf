"# TP-2-Mini-projet_Thymeleaf" 


# 1. Description du projet
## Contexte fonctionnel du domaine choisi (hôtel): 
Cette application est un système de gestion hôtelière complet permettant de gérer les chambres, les clients et les réservations d'un hôtel. Elle offre une interface moderne et intuitive pour les gestionnaires d'hôtel.
## Objectif de l'application:
Digitaliser et automatiser la gestion quotidienne (réservations, suivi clients, disponibilité des chambres, statistiques).
## Public cible / cas d'usage:
### Administration d’hôtel : 
   -Gestion des chambres, clients, réservations
   -Analyse des statistiques (occupation, répartition, volumes)
## Ce que l'application permet concrètement :
Gérer le cycle de vie des réservations de bout en bout (création/édition/suppression), synchroniser la disponibilité des chambres et visualiser des indicateurs sur un tableau de bord.

# 2. Architecture technique
## 2.1 Stack technologique
### - Backend :
       * Spring Boot 3.5.6
       * Spring Data JPA (Hibernate)
       * Validation (spring-boot-starter-validation)
### - Frontend :
       * Thymeleaf
       * HTML/CSS/Bootstrap 5
### - Base de données :
      * MySQL 8.0.
### - Build : 
      * Maven
      * Java 21

## 2.2 Structure du code
- `entity/` : classes JPA.
- `repository/` : interfaces d'accès aux données.
- `service/` : logique métier.
- `controller/` : contrôleurs web MVC.
- `templates/` : vues Thymeleaf.
- `static/` : CSS, JS, images.

  <img width="1182" height="909" alt="image" src="https://github.com/user-attachments/assets/c89db3ee-f3f4-40fa-9695-e70a5a7154ac" />





## 2.3 Diagramme d’architecture 
### Flux
Navigateur → Contrôleur Spring MVC → Service → Repository (Spring Data JPA) → MySQL → Retour Vue Thymeleaf
![WhatsApp Image 2025-10-29 à 10 40 57_19a9a1c8](https://github.com/user-attachments/assets/d5272478-7cd4-4351-b7b3-da5b2c6ca3a7)

## 3. Fonctionnalités principales
### CRUD sur les entités principales
- *Chambres* : Création, modification, suppression, consultation
- *Clients* : Gestion des informations clients (nom, passeport)
- *Réservations* : Création, modification, annulation des réservations
### Recherche / Filtrage
- *Chambres* : Filtrage par type (Single, Double, Suite, Deluxe) et disponibilité
- *Réservations* : Filtrage par période (check-in/check-out) et statut
### Tableau de bord / Statistiques
- Taux d’occupation simple (% chambres indisponibles)
- Répartition des chambres par type
- Nombre de réservations par statut
- Totaux: chambres, clients, réservations
 ### Gestion des statuts
- Chambres : Disponible / Indisponible
- Réservations : Confirmée / En attente / Annulée
- Règles :
     -  Réservation confirmée rend la chambre indisponible
     -  Annulation peut libérer la chambre si aucune autre réservation            confirmée
     -  Contrôle de chevauchement des réservations (exclut les annulées)
     -  Impossible de modifier une chambre avec réservations confirmées

## 4. Modèle de données

 ### 4.1 Entités principales
#### Chambre
- id (Long) : Identifiant unique
- numero (String) : Numéro de la chambre
- type (String) : Type de chambre (Single, Double, Suite, Deluxe)
- prixNuit (BigDecimal) : Prix par nuit
- disponible (Boolean) : Statut de disponibilité
#### Client
- id (Long) : Identifiant unique
- nom (String) : Nom du client
- passeport (String) : Numéro de passeport
#### Reservation
- PK (ReservationPK) : Clé composite
- statut (String) : Statut de la réservation
- total (BigDecimal) : Montant total
- Relations vers Chambre et Client
#### ReservationPK (clé composite)
- chambre_id: Long
- client_id: Long
- checkIn: LocalDate
- checkOut: LocalDate

###  4.2 Relations
- *Chambre <--> Reservation* : @OneToMany / @ManyToOne
- *Client <--> Reservation* : @OneToMany / @ManyToOne
- *ReservationPK* : Clé composite (chambre_id, client_id, checkIn, checkOut)
  
<img width="747" height="373" alt="Hotel 1" src="https://github.com/user-attachments/assets/54d204a9-0660-4b6a-a152-d90eacdabead" />
  
 ### 4.3 Configuration base de données
 
<img width="1184" height="461" alt="image" src="https://github.com/user-attachments/assets/381c1bdf-bfb3-4ff7-b08e-b883f846ce3a" />

# 5. Lancer le projet
#### -URL d'accès à l'application (page d'accueil):
http://localhost:8080/
<img width="1919" height="951" alt="image" src="https://github.com/user-attachments/assets/75c2ad29-cef9-42ad-b6cd-9772b058f6fc" />

#### -URL du tableau de bord / statistiques.
http://localhost:8080/stats
<img width="1883" height="870" alt="image" src="https://github.com/user-attachments/assets/22f05729-b30c-4227-847b-f45370c3d165" />
<img width="1878" height="886" alt="image" src="https://github.com/user-attachments/assets/ae40d7b7-065d-4f3b-be90-021c0899c0b5" />


# 6. Démonstration
https://drive.google.com/drive/folders/1NTa9MfB1wZ-Tb8X6R9AcwIjIKydqnJOJ?usp=drive_link


# _____________________________________

###### L’Ecole Normale Supérieure de Marrakech 
###### Module: Programmation avancées et DevOps
###### Encadré par : Mr.LACHGAR Mohamed  
###### Réalisé par : BEN-LAGHFIRI Majeda 

