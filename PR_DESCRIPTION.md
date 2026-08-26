## Summary

### CI/CD
- Restauration de la config CI (`skipTests` + config deploy)
- Réduction des logs HttpClient

### Tests (JUnit / Cucumber / Selenium)
- **Données de référence** : `MongoDbIntegrationTestBase.ensureReferenceData()` crée désormais les villes Casablanca, Rabat, Marrakech et Tanger (plus le domaine Informatique) pour les tests d’intégration et les scénarios Cucumber.
- **Démarrage en env test** : quand `EM_ENV=test`, l’application seed les mêmes villes/domaines au démarrage (`Main.ensureTestReferenceData()`) pour que les tests Cucumber/Selenium sur `http://localhost:8081` aient le dropdown des villes rempli.
- **Message de succès** : le Page Object `JobOfferPage` reconnaît le message de succès après création (`.container2 pre`) et après mise à jour (`section.offres div` vert).
- **Scénario Update** : implémentation du step « I click on edit for the first job » (clic sur le lien Modifier + attente du formulaire d’édition). Steps « I fill the job creation form with type » et « I fill all other required fields » complétés (ville/domaine) pour que les outlines passent.
- **Prérequis** : lancer l’app sur `http://localhost:8081` (config test) avant d’exécuter les scénarios Cucumber.

### Nouveau thème
- Intégration des changements de `feat/new_theme`
- Mise à jour des Apps (`ApplicantApp`, `BaseApp`, `BoApp`, `MoApp`)
- Nouveau doc de conception (`.ai/docs/CONCEPTION.md`)

### MailerSend
- Ajout du service `MailerSendService` avec `MailServiceFactory`
- Ajout de `TestMailServlet` pour les tests d'envoi de mails
- Configuration ajoutée dans `config.prod`, `config.test` et `config.sample`

**Configuration** (dans `config.test.properties` / `config.prod.properties` / `config.sample.properties`) :

| Propriété | Description |
|-----------|-------------|
| `mail.provider` | `mailersend` pour utiliser MailerSend (ou `smtp` / `mailjet`) |
| `mailersend.token` | Token API MailerSend (dashboard MailerSend) |
| `mail.from` | Email expéditeur (doit être un domaine vérifié MailerSend) |
| `mail.from.name` | Nom affiché pour l'expéditeur |
| `mail.test.to` | Email destinataire pour le test via `/test-mail` |

**Test** : lancer l'app avec `EM_ENV=test`, puis ouvrir `http://localhost:8080/test-mail`.
