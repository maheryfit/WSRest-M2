# WSRest-M2 — Food Delivery API

Une API REST robuste pour une plateforme de livraison de nourriture, développée avec **Spring Boot 3+** (Java 17). Cette application gère l'ensemble du cycle de vie des commandes, de l'authentification des utilisateurs à la gestion des statistiques de vente.

## 🚀 Technologies utilisées

*   **Backend** : Spring Boot, Spring Security (JWT), Spring Data JPA
*   **Base de données** : H2 (en mémoire par défaut)
*   **Sécurité** : Authentification stateless via JSON Web Tokens (JJWT)
*   **Documentation** : OpenAPI / Swagger UI
*   **Tests/Seeding** : Postman (Collection Master incluse)

## 🛠️ Installation et Démarrage

### 1. Prérequis
*   **JDK 17** ou supérieur
*   **Maven 3.6+**

### 2. Cloner et Lancer
```bash
# Lancer l'application
./mvnw spring-boot:run
```
L'API sera disponible sur : `http://localhost:8080`

### 3. Accès Base de données (H2 Console)
L'interface H2 est accessible pour inspecter les données en temps réel :
*   **URL** : `http://localhost:8080/h2-console`
*   **JDBC URL** : `jdbc:h2:mem:testdb` (ou selon `application.properties`)
*   **User** : `sa` / **Password** : (vide)

## 🧪 Initialisation des données (Bootstrap)

Pour faciliter le test, une collection Postman **`WSRest_M2_MASTER.postman_collection.json`** est fournie à la racine du projet.

### Procédure de peuplement automatique :
1.  Ouvrez Postman et importez la collection.
2.  Exécutez le dossier **`0 ─ BOOTSTRAP`**. Cela effectuera dans l'ordre :
    *   La création du compte Administrateur initial (`admin@gmail.com`).
    *   Le login pour capturer le token admin.
    *   L'insertion de **10 clients** types.
    *   L'insertion de **5 restaurants** à Antananarivo.
    *   L'insertion de **20 plats** variés (4 par restaurant).

> [!IMPORTANT]
> Le mot de passe par défaut pour TOUS les utilisateurs créés via le script est : **`password`**.

## 📍 Endpoints Principaux

### Authentification
*   `POST /api/utilisateurs/register-admin` : Création d'un premier admin (endpoint public de bootstrap).
*   `POST /api/utilisateurs/login` : Authentification et récupération du JWT.

### Gestion
*   `/api/clients` : Profils clients, adresses de livraison.
*   `/api/restaurants` : Menu, horaires, avis.
*   `/api/commandes` : Passage de commande, suivi d'état (Panier -> Prête -> Payée).
*   `/api/stats` : Rapports sur les meilleurs restaurants, clients et volume par jour.

## 📊 Statistiques (Mode Admin)
L'application propose des endpoints d'analyse performants :
*   **Top Restaurants** : Basé sur le chiffre d'affaires et les notes.
*   **Meilleurs Clients** : Basé sur les dépenses totales.
*   **Volume Journalier** : Agrégation des commandes par date.

## 📄 Documentation API
Une fois l'application lancée, consultez la documentation interactive :
*   **Swagger UI** : `http://localhost:8080/swagger-ui/index.html`

---
*Projet réalisé dans le cadre du module WSRest - Master 2.*
