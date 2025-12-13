# Schéma d'Exécution des Tests

## Structure des Tests

```
tests/
├── unit/
│   └── CalculatorTest.java (4 tests)
│
├── integration/
│   ├── UserRepositoryIntegrationTest.java (3 tests)
│   ├── MessageRepositoryIntegrationTest.java (1 test)
│   └── ItemListingIntegrationTest.java (1 test)
```

## Résumé

- **Tests Unitaires**: 4 (simples validations)
- **Tests d'Intégration**: 5 (avec base de données)
- **Total**: 9 tests

## Commandes d'Exécution

### 1. Exécuter TOUS les tests

```bash
cd backend
mvn test
```

### 2. Exécuter UNIQUEMENT les tests unitaires

```bash
mvn test -Dtest=CalculatorTest
```

### 3. Exécuter UNIQUEMENT les tests d'intégration

```bash
mvn test -Dtest=*IntegrationTest
```

### 4. Exécuter un test spécifique

```bash
mvn test -Dtest=UserRepositoryIntegrationTest
mvn test -Dtest=MessageRepositoryIntegrationTest
mvn test -Dtest=ItemListingIntegrationTest
mvn test -Dtest=CalculatorTest
```

### 5. Exécuter une méthode de test spécifique

```bash
mvn test -Dtest=CalculatorTest#testAddition
mvn test -Dtest=UserRepositoryIntegrationTest#testSaveAndFindUser
```

## Détails des Tests

### Tests Unitaires (CalculatorTest)

1. **testAddition** - Validation d'une addition (5 + 3 = 8)
2. **testSubtraction** - Validation d'une soustraction (10 - 4 = 6)
3. **testStringValidation** - Validation du format email
4. **testPasswordValidation** - Validation des critères de mot de passe

### Tests d'Intégration

#### UserRepositoryIntegrationTest (3 tests)

1. **testSaveAndFindUser** - Créer et récupérer un utilisateur
2. **testUpdateUser** - Modifier un utilisateur existant
3. **testDeleteUser** - Supprimer un utilisateur

#### MessageRepositoryIntegrationTest (1 test)

1. **testSaveMessage** - Créer et récupérer un message

#### ItemListingIntegrationTest (1 test)

1. **testCreateItemListing** - Créer et récupérer un article

## Voir les Résultats

Après exécution, les résultats s'affichent dans le terminal:

```
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

## Logs Détaillés

Pour voir tous les détails:

```bash
mvn test -e
```

Pour voir les logs avec plus d'infos:

```bash
mvn test -X
```
