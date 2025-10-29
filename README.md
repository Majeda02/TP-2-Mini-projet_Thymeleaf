"# TP-2-Mini-projet_Thymeleaf" 


# 1. Description du projet
## Contexte fonctionnel du domaine choisi (hôtel): 
Cette application est un système de gestion hôtelière complet permettant de gérer les chambres, les clients et les réservations d'un hôtel. Elle offre une interface moderne et intuitive pour les gestionnaires d'hôtel.
## Objectif de l'application:
L'objectif est de digitaliser et automatiser la gestion quotidienne d'un hôtel en fournissant des outils de gestion des réservations, de suivi des clients et d'analyse des performances.
## Public cible / cas d'usage:
L'administration d'hôtel --> Pour gérer les réservations , les chambres et les clients ainsi qu'analyser les statistiques et performances
## Ce que l'application permet concrètement :
Elle  permet de gérer complètement le cycle de vie des réservations hôtelières, de la création des chambres à l'analyse des performances via un tableau de bord statistique.

# 2. Architecture technique
## 2.1 Stack technologique
- Backend : Spring Boot 3.5.6, Spring Data JPA/Hibernate
- Frontend : Thymeleaf, HTML/CSS/Bootstrap et React.js, Chart.js (Pour les graphiques)
- Base de données : MySQL 8.0.
- Build : Maven.

## 2.2 Structure du code
- `entity/` : classes JPA.
- `repository/` : interfaces d'accès aux données.
- `service/` : logique métier.
- `controller/` : contrôleurs web MVC.
- `templates/` : vues Thymeleaf.
- `static/` : CSS, JS, images.

<img width="521" height="736" alt="image" src="https://github.com/user-attachments/assets/b1e1b915-9443-4d0d-ac81-b585d4257b08" />


## 2.3 Diagramme d’architecture 
- Interface Thymeleaf :
1. Templates HTML avec Bootstrap
2. Contrôleurs MVC retournent des vues Thymeleaf
3. CSS/JS statiques servis depuis /static/
- Interface React :
1. Composants React envoient requêtes HTTP vers API REST
2. Contrôleurs REST retournent JSON
3. Affichage des graphiques avec Chart.js

## 3. Fonctionnalités principales
### CRUD sur les entités principales
- *Chambres* : Création, modification, suppression, consultation
- *Clients* : Gestion des informations clients (nom, passeport)
- *Réservations* : Création, modification, annulation des réservations
### Recherche / Filtrage
- *Chambres* : Filtrage par type (Single, Double, Suite, Deluxe) et disponibilité
- *Réservations* : Filtrage par période (check-in/check-out) et statut
- *Clients* : Recherche par nom et passeport
### Tableau de bord / Statistiques
- *Taux d'occupation* : Pourcentage de chambres occupées
- *RevPAR* : Revenue Per Available Room (Revenu par chambre disponible)
- *Répartition des chambres* par type
- *Statistiques des réservations* par statut
 ### Gestion des statuts
- *Chambres* : Disponible / Indisponible
- *Réservations* : Confirmée / En attente / Annulée
- *Protection des données* : Impossible de modifier une chambre avec réservations confirmées

## 4. Modèle de données

 ### 4.1 Entités principales
Chambre
- id (Long) : Identifiant unique
- numero (String) : Numéro de la chambre
- type (String) : Type de chambre (Single, Double, Suite, Deluxe)
- prixNuit (BigDecimal) : Prix par nuit
- disponible (Boolean) : Statut de disponibilité
Client
- id (Long) : Identifiant unique
- nom (String) : Nom du client
- passeport (String) : Numéro de passeport
Reservation
- PK (ReservationPK) : Clé composite
- statut (String) : Statut de la réservation
- total (BigDecimal) : Montant total
- Relations vers Chambre et Client

###  4.2 Relations
- *Chambre <--> Reservation* : @OneToMany / @ManyToOne
- *Client <--> Reservation* : @OneToMany / @ManyToOne
- *ReservationPK* : Clé composite (chambre_id, client_id, checkIn, checkOut)
  
<img width="747" height="373" alt="Hotel 1" src="https://github.com/user-attachments/assets/54d204a9-0660-4b6a-a152-d90eacdabead" />
  
 ### 4.3 Configuration base de données
 
<img width="1184" height="461" alt="image" src="https://github.com/user-attachments/assets/381c1bdf-bfb3-4ff7-b08e-b883f846ce3a" />

# 5. Lancer le projet
-URL d'accès à l'application (page d'accueil):
http://localhost:8080/
<img width="1919" height="951" alt="image" src="https://github.com/user-attachments/assets/75c2ad29-cef9-42ad-b6cd-9772b058f6fc" />

-URL du tableau de bord / statistiques.
http://localhost:8080/stats
<img width="1910" height="940" alt="image" src="https://github.com/user-attachments/assets/2e9abc35-620f-488b-9bb8-de4124a781b4" />
<img width="1898" height="945" alt="image" src="https://github.com/user-attachments/assets/6ef1dc60-05ae-4117-8337-c369780a7c2b" />

# 6. Démonstration
https://drive.google.com/drive/folders/1NTa9MfB1wZ-Tb8X6R9AcwIjIKydqnJOJ?usp=drive_link

# _____________________________________

###### L’Ecole Normale Supérieure de Marrakech 
###### Module: Programmation avancées et DevOps
###### Encadré par : Mr.LACHGAR Mohamed  
###### Réalisé par : BEN-LAGHFIRI Majeda 

