# LocalExchange - Plateforme d'Échange Local

## Description

LocalExchange est une application web collaborative permettant l'échange de biens physiques et de compétences entre membres d'une même communauté locale. L'objectif principal est de favoriser l'économie circulaire, réduire la consommation et renforcer les liens sociaux de proximité.

## Table des Matières

- [Prérequis](#prérequis)
- [Installation](#installation)
- [Configuration](#configuration)
- [Démarrage](#démarrage)
- [Architecture](#architecture)
- [Tests](#tests)
- [Déploiement](#déploiement)
- [API Documentation](#api-documentation)
- [Contribution](#contribution)
- [Licence](#licence)

---

## Prérequis

### Logiciels Requis

- **Java Development Kit (JDK) 17 ou supérieur**
  - Télécharger depuis : https://www.oracle.com/java/technologies/downloads/
  - Vérifier l'installation : `java -version`

- **Node.js 18 ou supérieur**
  - Télécharger depuis : https://nodejs.org/
  - Vérifier l'installation : `node -v` et `npm -v`

- **MySQL 8.0 ou supérieur**
  - Télécharger depuis : https://dev.mysql.com/downloads/
  - Alternative : WampServer (Windows) ou MAMP (macOS)

- **Maven 3.8 ou supérieur**
  - Télécharger depuis : https://maven.apache.org/download.cgi
  - Vérifier l'installation : `mvn -version`

- **Git**
  - Télécharger depuis : https://git-scm.com/downloads
  - Vérifier l'installation : `git --version`

---

## Installation

### 1. Cloner le Projet

```bash
git clone https://github.com/votre-organisation/local-exchange-app.git
cd local-exchange-app
```

### 2. Configuration de la Base de Données

Démarrez votre serveur MySQL et exécutez les commandes suivantes :

```sql
CREATE DATABASE local_exchange_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. Installation du Backend

```bash
cd backend
mvn clean install
```

Cette commande télécharge toutes les dépendances nécessaires et compile le projet.

### 4. Installation du Frontend

```bash
cd frontend
npm install
```

Cette commande installe toutes les dépendances Node.js nécessaires.

---

## Configuration

### Configuration Backend

Le fichier de configuration principal se trouve dans `backend/src/main/resources/application.yml`

Paramètres par défaut :

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/local_exchange_db
    username: root
    password: 
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

server:
  port: 8080

jwt:
  secret: votre_secret_key_ici
  expiration: 86400000
```

**Important** : Modifiez les paramètres de connexion à la base de données selon votre configuration locale.

### Configuration Frontend

Le fichier de configuration se trouve dans `frontend/src/services/api.js`

```javascript
const API_BASE_URL = 'http://localhost:8080/api';
```

---

## Démarrage

### Lancer le Backend

Ouvrez un terminal et exécutez :

```bash
cd backend
mvn spring-boot:run
```

Le serveur backend démarrera sur le port 8080.

### Lancer le Frontend

Ouvrez un nouveau terminal et exécutez :

```bash
cd frontend
npm run dev
```

Le serveur de développement démarrera sur le port 5173.

### Accès à l'Application

- **Frontend** : http://localhost:5173
- **API Backend** : http://localhost:8080

---

## Architecture

### Vue d'Ensemble

L'application suit une architecture trois-tiers :

1. **Couche Présentation** : Interface utilisateur React
2. **Couche Métier** : API REST Spring Boot
3. **Couche Données** : Base de données MySQL

### Stack Technologique

**Backend**
- Spring Boot 3.2.0
- Spring Security avec JWT
- Spring Data JPA
- Hibernate
- MySQL 8.0
- Maven

**Frontend**
- React 18.2
- React Router 6.20
- Axios 1.6
- Tailwind CSS 3.3
- Vite 5.0

### Structure des Dossiers

```
local-exchange-app/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/localexchange/
│   │   │   │   ├── model/          # Entités JPA
│   │   │   │   ├── dto/            # Data Transfer Objects
│   │   │   │   ├── repository/     # Interfaces Repository
│   │   │   │   ├── service/        # Logique métier
│   │   │   │   ├── controller/     # Endpoints REST
│   │   │   │   ├── security/       # Configuration sécurité
│   │   │   │   ├── config/         # Configurations
│   │   │   │   └── exception/      # Gestion des exceptions
│   │   │   └── resources/
│   │   │       └── application.yml
│   │   └── test/                   # Tests unitaires
│   └── pom.xml
│
└── frontend/
    ├── src/
    │   ├── components/
    │   │   ├── common/             # Composants réutilisables
    │   │   └── layout/             # Layout de l'application
    │   ├── pages/                  # Pages principales
    │   ├── services/               # Appels API
    │   ├── context/                # État global
    │   └── utils/                  # Utilitaires
    ├── package.json
    └── vite.config.js
```

---

## Tests

### Tests Backend

**Exécuter tous les tests** :
```bash
cd backend
mvn test
```

**Exécuter les tests avec rapport de couverture** :
```bash
mvn verify jacoco:report
```

Le rapport de couverture sera disponible dans `target/site/jacoco/index.html`

**Tests unitaires** :
- AuthService : 8 tests
- ItemListingService : 12 tests
- ExchangeRequestService : 15 tests
- MessageService : 6 tests
- ReviewService : 7 tests

**Tests d'intégration** :
- Controllers avec MockMvc
- Tests de bout en bout des endpoints REST

### Tests Frontend

**Exécuter les tests** :
```bash
cd frontend
npm test
```

**Exécuter les tests avec couverture** :
```bash
npm run test:coverage
```

---

## API Documentation

### Endpoints Principaux

**Authentification**
- `POST /api/auth/register` - Inscription
- `POST /api/auth/login` - Connexion

**Utilisateurs**
- `GET /api/users/profile` - Profil utilisateur
- `PUT /api/users/profile` - Mise à jour profil

**Annonces**
- `GET /api/items` - Liste des annonces
- `POST /api/items` - Créer une annonce
- `GET /api/items/{id}` - Détails d'une annonce
- `PUT /api/items/{id}` - Modifier une annonce
- `DELETE /api/items/{id}` - Supprimer une annonce

**Échanges**
- `POST /api/exchanges` - Créer une demande
- `PUT /api/exchanges/{id}/accept` - Accepter une demande
- `PUT /api/exchanges/{id}/refuse` - Refuser une demande
- `PUT /api/exchanges/{id}/complete` - Marquer comme terminé

**Messages**
- `GET /api/messages/exchange/{id}` - Messages d'un échange
- `POST /api/messages` - Envoyer un message

**Avis**
- `POST /api/reviews` - Créer un avis
- `GET /api/reviews/user/{id}` - Avis d'un utilisateur

---

## Sécurité

### Bonnes Pratiques Implémentées

- Authentification JWT avec expiration de 24 heures
- Hachage des mots de passe avec BCrypt (coût 10)
- Validation des entrées côté serveur et client
- Protection contre les injections SQL via JPA
- Configuration CORS restrictive
- Protection contre XSS via React
- HTTPS obligatoire en production

### Signalement de Vulnérabilités

Pour signaler une vulnérabilité de sécurité, envoyez un email à : sudozed@gmail.com
---

## Maintenance

### Sauvegarde de la Base de Données

```bash
mysqldump -u root -p local_exchange_db > backup_$(date +%Y%m%d).sql
```

### Mise à Jour de l'Application

```bash
# 1. Sauvegarde
mysqldump -u root -p local_exchange_db > backup.sql

# 2. Pull des changements
git pull origin main

# 3. Backend
cd backend
mvn clean package -DskipTests
sudo systemctl restart localexchange

# 4. Frontend
cd frontend
npm run build
sudo cp -r dist/* /var/www/localexchange/
```
---

## Dépannage

### Le backend ne démarre pas

**Vérifier MySQL** :
```bash
sudo systemctl status mysql
mysql -u root -p -e "SHOW DATABASES;"
```

**Vérifier les logs** :
```bash
cd backend
mvn spring-boot:run
# Observer les messages d'erreur
```

### Erreur de connexion à la base de données

Vérifiez les paramètres dans `application.yml` :
- URL de connexion
- Nom d'utilisateur
- Mot de passe
- Nom de la base de données

### Le frontend ne se connecte pas au backend

Vérifiez :
1. Le backend est démarré sur le port 8080
2. La configuration CORS dans `SecurityConfig.java`
3. L'URL de l'API dans `frontend/src/services/api.js`

### Erreur 401 Unauthorized

- Vérifiez que le token JWT est présent dans les headers
- Vérifiez que le token n'est pas expiré (durée de vie : 24h)
- Reconnectez-vous pour obtenir un nouveau token

---


## Auteurs

BENCHARKI Anas  /  FABAS Lou

---
