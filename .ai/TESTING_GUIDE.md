# Guide de Tests - Emplois Maroc

## 📋 Vue d'ensemble

Ce projet contient trois types de tests :
1. **Tests Unitaires** (JUnit 5) - Tests des services et applications
2. **Tests d'Intégration Selenium** - Tests UI automatisés
3. **Tests BDD** (Cucumber) - Tests basés sur le comportement

## 🧪 Tests Unitaires

### Exécution
```bash
# Tous les tests unitaires
mvn test

# Tests spécifiques
mvn test -Dtest=MoAppTest
mvn test -Dtest=AdServiceTest
mvn test -Dtest=MoAppTest,AdServiceTest
```

### Tests disponibles
- **MoAppTest** (8 tests) : Création, mise à jour, validation des offres d'emploi
- **AdServiceTest** (11 tests) : Services de récupération d'annonces avec slugs
- **AdDaoTest** : Tests du DAO
- **CityDaoTest** : Tests du DAO des villes
- **RegionDaoTest** : Tests du DAO des régions
- **ApplicantDaoTest** : Tests du DAO des candidats

## 🌐 Tests Selenium (UI)

### Prérequis
1. **L'application doit être démarrée** sur `http://localhost:8080`
2. **MongoDB doit être accessible**
3. **Chrome doit être installé** (WebDriverManager gère automatiquement le driver)

### Exécution
```bash
# Démarrer l'application (dans un terminal)
# L'application est probablement déjà en cours d'exécution depuis IntelliJ

# Lancer les tests Selenium (dans un autre terminal)
mvn test -Dtest=JobOfferSeleniumTest
```

### Tests Selenium disponibles
1. ✅ Navigation vers la page de création
2. ✅ Création d'une offre avec données valides
3. ✅ Validation des champs requis manquants
4. ✅ Validation du format email
5. ✅ Création avec type de contrat CDD

### Structure des Page Objects
```
src/test/java/com/centoria/jobmaroc/test/selenium/
├── pages/
│   ├── BasePage.java          # Classe de base avec méthodes communes
│   └── JobOfferPage.java      # Page Object pour /ajouter-offre-emploi
└── JobOfferSeleniumTest.java  # Tests JUnit 5
```

## 🥒 Tests BDD (Cucumber)

### Exécution
```bash
# Tous les scénarios BDD
mvn test -Dtest=CucumberTestRunner

# Scénarios avec tags spécifiques
mvn test -Dtest=CucumberTestRunner -Dcucumber.filter.tags="@smoke"
mvn test -Dtest=CucumberTestRunner -Dcucumber.filter.tags="@creation"
mvn test -Dtest=CucumberTestRunner -Dcucumber.filter.tags="@validation"
```

### Tags disponibles
- `@smoke` : Tests de base (sanity checks)
- `@creation` : Tests de création d'offres
- `@update` : Tests de mise à jour
- `@validation` : Tests de validation
- `@search` : Tests de recherche
- `@filter` : Tests de filtrage
- `@view` : Tests de visualisation

### Fichiers BDD
```
src/test/resources/features/
└── job_offer.feature          # 10 scénarios Gherkin

src/test/java/com/centoria/jobmaroc/test/bdd/
├── JobOfferStepDefinitions.java  # Implémentation des steps
└── CucumberTestRunner.java       # Runner JUnit Platform
```

## 📊 Rapports de Tests

### Rapports Surefire (JUnit)
```bash
# Après exécution, voir :
target/surefire-reports/
```

### Rapports Cucumber
```bash
# Après exécution des tests BDD, voir :
target/cucumber-reports/
├── cucumber.html    # Rapport HTML
└── cucumber.json    # Rapport JSON
```

## 🔧 Configuration

### Variables d'environnement
Les tests utilisent la configuration par défaut (`config.local.properties`).

### Chrome Headless (optionnel)
Pour exécuter Chrome en mode headless (sans interface graphique), décommenter dans :
- `JobOfferSeleniumTest.java` ligne ~45
- `JobOfferStepDefinitions.java` ligne ~25

```java
options.addArguments("--headless");
```

## 🚀 Intégration avec Katalon

Les tests Selenium peuvent être importés dans Katalon Studio :

1. **Import du projet Java** :
   - Katalon Studio → File → Import → Existing Java Project
   - Sélectionner `/home/centoria/git/emplois-maroc`

2. **Conversion en Test Cases Katalon** :
   - Les Page Objects peuvent être convertis en Keyword-driven tests
   - Les scénarios BDD peuvent être utilisés comme base pour les Test Cases

3. **Exécution depuis Katalon** :
   - Les tests peuvent être exécutés via l'interface Katalon
   - Les rapports seront générés dans Katalon

## 📝 Notes importantes

### Tests Selenium
- ⚠️ **L'application doit être démarrée** avant d'exécuter les tests Selenium
- Les tests gèrent automatiquement les bannières de cookies
- Les tests utilisent des données de test qui peuvent créer des enregistrements en base

### Tests BDD
- Les scénarios BDD sont prêts pour une transformation complète en workflow Katalon
- Les step definitions peuvent être étendues pour couvrir plus de fonctionnalités

### Maintenance
- Mettre à jour les sélecteurs dans `JobOfferPage.java` si le formulaire change
- Ajouter de nouveaux scénarios dans `job_offer.feature` pour de nouvelles fonctionnalités
- Les tests unitaires doivent être mis à jour si la logique métier change

## 🐛 Dépannage

### Port 8080 déjà utilisé
```bash
# Vérifier quel processus utilise le port
lsof -i :8080

# Tuer le processus si nécessaire
kill -9 <PID>
```

### ChromeDriver non trouvé
WebDriverManager télécharge automatiquement le driver. Si problème :
```bash
# Nettoyer le cache
rm -rf ~/.cache/selenium/
```

### Tests Selenium échouent
1. Vérifier que l'application est accessible sur `http://localhost:8080`
2. Vérifier que MongoDB est démarré
3. Vérifier les logs de l'application pour les erreurs

## 📚 Ressources

- [Selenium Documentation](https://www.selenium.dev/documentation/)
- [Cucumber Documentation](https://cucumber.io/docs/cucumber/)
- [JUnit 5 Documentation](https://junit.org/junit5/docs/current/user-guide/)
- [Katalon Studio Documentation](https://docs.katalon.com/)
