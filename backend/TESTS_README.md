# Tests - Guide Rapide

## Exécuter les tests

```bash
# Tous les tests
mvn test

# Tests unitaires (service)
mvn test -Dtest=*ServiceTest

# Tests d'intégration
mvn test -Dtest=*IntegrationTest
```

## Structure des Tests

- `service/`: Tests unitaires des services
  - AuthServiceTest
  - UserServiceTest
  - ItemServiceTest
  - MessageServiceTest
  - ExchangeRequestServiceTest
  - ReviewServiceTest
  - ItemListingServiceTest

- `integration/`: Tests d'intégration avec la base de données
  - UserIntegrationTest
  - ItemIntegrationTest
  - ExchangeIntegrationTest
