# Tests Unitaires et d'Intégration - Schéma d'Exécution

## 📊 Structure Complète

```
backend/src/test/java/com/localexchange/
├── unit/
│   ├── AuthenticationServiceTest.java          (5 tests)
│   ├── ExchangeValidationTest.java             (5 tests)
│   └── ExchangeServiceTest.java                (5 tests)
│
└── integration/
    ├── MessagingIntegrationTest.java           (3 tests)
    └── AuthControllerIntegrationTest.java      (5 tests)
```

## 📈 Résumé Total

| Catégorie | Nombre | Total |
|-----------|--------|-------|
| **Tests Unitaires** | 15 | |
| **Tests Intégration** | 8 | |
| **TOTAL** | | **23 tests** |

---

## 🧪 Tests Unitaires (15 tests)

### 1. AuthenticationServiceTest (5 tests)
- ✅ `testRegisterNewUser` - Enregistrer nouvel utilisateur
- ✅ `testRegisterDuplicateEmail` - Rejet email en double
- ✅ `testLoginSuccess` - Connexion réussie
- ✅ `testLoginInvalidEmail` - Email invalide
- Méthodes testées: `register()`, `login()`

### 2. ExchangeValidationTest (5 tests)
- ✅ `testValidExchangeRequest` - Demande valide
- ✅ `testExchangeWithPastDate` - Date passée
- ✅ `testExchangeSameDonatorBeneficiary` - Même personne
- ✅ `testExchangeOfferNotBlank` - Offre non vide
- Validation: Dates, utilisateurs, offres

### 3. ExchangeServiceTest (5 tests)
- ✅ `testCreateExchangeRequest` - Créer demande
- ✅ `testGetExchangeRequest` - Récupérer demande
- ✅ `testAcceptExchangeRequest` - Accepter demande
- ✅ `testRejectExchangeRequest` - Refuser demande
- Méthodes testées: `createExchangeRequest()`, `getExchangeRequest()`, `acceptExchange()`, `rejectExchange()`

---

## 🔗 Tests d'Intégration (8 tests)

### 1. MessagingIntegrationTest (3 tests)
- ✅ `testSendMessage` - Envoyer message chat
- ✅ `testMarkMessageAsRead` - Marquer comme lu
- ✅ `testConversationFlow` - Flux conversation complet
- **Fonctionnalité**: Messagerie chat fonctionne correctement

### 2. AuthControllerIntegrationTest (5 tests)
- ✅ `testRegisterEndpoint` - POST /api/auth/register
- ✅ `testLoginEndpoint` - POST /api/auth/login
- ✅ `testLoginWithInvalidCredentials` - Login invalide (401)
- ✅ `testRegisterDuplicateEmail` - Email en double (409)
- ✅ `testAuthEndpointRequiresValidInput` - Validation input (400)
- **Endpoints testés**: Register, Login avec validations

---

## 🚀 Commandes d'Exécution

### ▶️ Exécuter TOUS les tests
```bash
cd backend
mvn test
```

### ▶️ Exécuter UNIQUEMENT les tests unitaires
```bash
mvn test -Dtest=*ServiceTest
```

### ▶️ Exécuter UNIQUEMENT les tests d'intégration
```bash
mvn test -Dtest=*IntegrationTest
```

### ▶️ Exécuter un test spécifique
```bash
# Tests Unitaires
mvn test -Dtest=AuthenticationServiceTest
mvn test -Dtest=ExchangeValidationTest
mvn test -Dtest=ExchangeServiceTest

# Tests Intégration
mvn test -Dtest=MessagingIntegrationTest
mvn test -Dtest=AuthControllerIntegrationTest
```

### ▶️ Exécuter une méthode précise
```bash
mvn test -Dtest=AuthenticationServiceTest#testRegisterNewUser
mvn test -Dtest=MessagingIntegrationTest#testConversationFlow
mvn test -Dtest=AuthControllerIntegrationTest#testLoginEndpoint
```

### ▶️ Voir les rapports détaillés
```bash
mvn test -e
mvn test -X
```

---

## ✅ Résultats Attendus

Après exécution réussie:
```
[INFO] Tests run: 23, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

---

## 📚 Détails par Type

### Authentification
- Registration avec validation email unique
- Login avec génération de token
- Gestion des erreurs (400, 401, 409)

### Validation Demandes d'Échange
- Création de demandes valides
- Validation des dates
- Validation des utilisateurs
- Validation des offres

### Service Échange
- CRUD demandes d'échange
- Transitions de statut (PENDING → ACCEPTED → REJECTED)
- Mock repository

### Messagerie
- Envoi de messages
- Marquage comme lu
- Conversation bidirectionnelle

### Auth Controller
- Endpoints REST authentification
- Validation des inputs
- Gestion des codes HTTP
- Génération tokens JWT

---

## 🔍 Couverture

- **Services**: AuthService, ExchangeRequestService
- **Controllers**: AuthController
- **Models**: User, ExchangeRequest, Message
- **Repositories**: UserRepository, ExchangeRequestRepository, MessageRepository
- **Validations**: Email, Dates, Statuts

---

## 📝 Notes

✓ Tous les tests sont indépendants et peuvent s'exécuter en parallèle
✓ Tests unitaires utilisent Mockito pour les dépendances
✓ Tests d'intégration utilisent @SpringBootTest et base de données en mémoire
✓ Transactions rollback automatiquement après chaque test
✓ Aucune dépendance externe requise (JWT, SMTP, etc.)
