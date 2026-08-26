# Cas de Test - Tests d'Intégration BDD - Emplois Maroc

## Vue d'ensemble

Ce document contient les cas de test formatés pour les tests d'intégration BDD. Chaque cas de test peut être converti en scénarios Gherkin (`.feature` files) et les données de test peuvent être exportées au format CSV pour les utilitaires de test.

---

## TC-001: Créer une offre d'emploi avec données valides

### Metadata
- **ID**: TC-001
- **Use Case**: UC-006 (Publier une offre d'emploi)
- **Priority**: High
- **Type**: Smoke, Integration
- **Status**: Ready
- **Tags**: @smoke @creation @integration

### Test Scenario
Tester la création réussie d'une offre d'emploi avec tous les champs requis remplis correctement.

### BDD Scenario (Gherkin Format)
```gherkin
Feature: Job Offer Management
  As a recruiter
  I want to create and manage job offers
  So that I can attract qualified candidates

  Background:
    Given the application is running on "http://localhost:8080"
    And I am on the job creation page "/ajouter-offre-emploi"

  @smoke @creation @integration
  Scenario: Create a new job offer with valid data
    When I fill the job creation form with the following data:
      | field           | value                                    |
      | title           | Développeur Java Senior                  |
      | content         | Nous recherchons un développeur Java     |
      | email           | recruiter@company.com                    |
      | tel             | 0612345678                               |
      | type            | CDI                                      |
      | city            | Casablanca                               |
      | domain          | Informatique                             |
      | companyName     | Tech Company                             |
      | companyCode     | 123456789                                |
      | nbrDePostes     | 2                                        |
      | formation       | bac+5                                    |
      | experienceLevel | De 3 à 5 ans                             |
      | facebook        | https://facebook.com/techcompany         |
      | twitter         | https://twitter.com/techcompany          |
      | linkedin        | https://linkedin.com/company/techcompany |
      | confidentiality | public                                   |
    And I submit the form
    Then I should see a success message
    And I should receive a secret code
    And the job offer should be visible on the homepage
```

### Test Data (CSV Format)

#### CSV Structure
| test_case_id | scenario_name | title | content | email | tel | type | city | domain | companyName | companyCode | nbrDePostes | formation | experienceLevel | expected_result | test_type | notes |
|--------------|---------------|-------|---------|-------|-----|------|------|--------|-------------|-------------|-------------|-----------|-----------------|----------------|-----------|-------|
| TC-001 | Create Job Offer | Développeur Java Senior | Nous recherchons un développeur Java | recruiter@company.com | 0612345678 | CDI | Casablanca | Informatique | Tech Company | 123456789 | 2 | bac+5 | De 3 à 5 ans | Success | positive | Valid case with all fields |
| TC-001 | Create Job Offer | Développeur Python | Recherche développeur Python | python@company.com | 0623456789 | CDD | Rabat | Informatique | Python Corp | 987654321 | 1 | bac+3 | De 1 à 3 ans | Success | positive | Valid case CDD |

#### CSV Export Format
```csv
test_case_id,scenario_name,title,content,email,tel,type,city,domain,companyName,companyCode,nbrDePostes,formation,experienceLevel,expected_result,test_type,notes
TC-001,Create Job Offer,Développeur Java Senior,Nous recherchons un développeur Java,recruiter@company.com,0612345678,CDI,Casablanca,Informatique,Tech Company,123456789,2,bac+5,De 3 à 5 ans,Success,positive,Valid case with all fields
TC-001,Create Job Offer,Développeur Python,Recherche développeur Python,python@company.com,0623456789,CDD,Rabat,Informatique,Python Corp,987654321,1,bac+3,De 1 à 3 ans,Success,positive,Valid case CDD
```

### Test Steps
1. Naviguer vers "/ajouter-offre-emploi"
2. Vérifier que le formulaire s'affiche avec la liste des domaines
3. Remplir tous les champs requis avec des données valides
4. Soumettre le formulaire
5. Vérifier l'affichage d'un message de succès
6. Vérifier la réception d'un code secret
7. Vérifier que l'offre apparaît sur la page d'accueil

### Expected Results
- Le formulaire s'affiche correctement
- Tous les champs sont remplis sans erreur
- Un message de succès s'affiche après soumission
- Un code secret unique est généré et affiché
- L'offre est enregistrée en base de données
- L'offre est visible sur le site

### Test Data Variations
- **Valid Data**: Tous les champs requis remplis avec des valeurs valides
- **Invalid Data**: Champs manquants ou invalides (voir TC-002, TC-003)
- **Edge Cases**: Domaines spéciaux, villes avec caractères spéciaux, emails très longs

### Related Test Cases
- Depends on: None
- Related: TC-002 (Validation champs requis), TC-003 (Validation email)

---

## TC-002: Validation des champs requis lors de la création d'offre

### Metadata
- **ID**: TC-002
- **Use Case**: UC-006 (Publier une offre d'emploi)
- **Priority**: High
- **Type**: Validation, Integration
- **Status**: Ready
- **Tags**: @validation @integration

### Test Scenario
Tester que le système affiche des erreurs de validation lorsque des champs requis sont manquants.

### BDD Scenario (Gherkin Format)
```gherkin
Feature: Job Offer Validation
  @validation @integration
  Scenario: Show error when required fields are missing
    Given I am on the job creation page "/ajouter-offre-emploi"
    When I fill only the title with "Test Job"
    And I submit the form
    Then I should see an error message
    And I should see validation errors for missing fields:
      | field   |
      | content |
      | email   |
      | tel     |
      | type    |
      | city    |
      | domain  |
    And I should remain on the job creation page
    And the form data should be preserved
```

### Test Data (CSV Format)

#### CSV Structure
| test_case_id | scenario_name | title | content | email | tel | type | city | domain | expected_result | test_type | missing_fields | notes |
|--------------|---------------|-------|---------|-------|-----|------|------|--------|-----------------|-----------|----------------|-------|
| TC-002 | Validate Required Fields | Test Job | | | | | | | Error | negative | content,email,tel,type,city,domain | Only title filled |
| TC-002 | Validate Required Fields | | Test Content | | | | | | Error | negative | title,email,tel,type,city,domain | Only content filled |
| TC-002 | Validate Required Fields | Test Job | Test Content | | | | | | Error | negative | email,tel,type,city,domain | Title and content only |

#### CSV Export Format
```csv
test_case_id,scenario_name,title,content,email,tel,type,city,domain,expected_result,test_type,missing_fields,notes
TC-002,Validate Required Fields,Test Job,,,,,,Error,negative,"content,email,tel,type,city,domain",Only title filled
TC-002,Validate Required Fields,,Test Content,,,,,Error,negative,"title,email,tel,type,city,domain",Only content filled
```

### Test Steps
1. Naviguer vers "/ajouter-offre-emploi"
2. Remplir uniquement le champ titre
3. Soumettre le formulaire
4. Vérifier l'affichage d'erreurs de validation
5. Vérifier que les erreurs concernent tous les champs requis manquants
6. Vérifier que le formulaire reste sur la page
7. Vérifier que les données saisies sont conservées

### Expected Results
- Des messages d'erreur s'affichent pour chaque champ requis manquant
- Le formulaire reste sur la page de création
- Les données saisies sont préservées dans le formulaire
- Aucune offre n'est créée en base de données

### Test Data Variations
- **Missing Single Field**: Tester chaque champ requis individuellement
- **Missing Multiple Fields**: Tester plusieurs combinaisons de champs manquants
- **All Fields Missing**: Soumettre le formulaire vide

### Related Test Cases
- Depends on: None
- Related: TC-001 (Création réussie), TC-003 (Validation email)

---

## TC-003: Validation du format d'email

### Metadata
- **ID**: TC-003
- **Use Case**: UC-006 (Publier une offre d'emploi)
- **Priority**: Medium
- **Type**: Validation, Integration
- **Status**: Ready
- **Tags**: @validation @email @integration

### Test Scenario
Tester que le système valide le format des adresses email et affiche une erreur pour les formats invalides.

### BDD Scenario (Gherkin Format)
```gherkin
Feature: Job Offer Email Validation
  @validation @email @integration
  Scenario Outline: Validate email format
    Given I am on the job creation page "/ajouter-offre-emploi"
    When I fill the job creation form with the following data:
      | field | value           |
      | title | Test Job        |
      | email | <email_address> |
    And I fill all other required fields
    And I submit the form
    Then I should see an email validation error "<expected_error>"

    Examples:
      | email_address    | expected_error                    |
      | invalid-email    | Format d'email invalide            |
      | test@            | Format d'email invalide            |
      | @domain.com      | Format d'email invalide            |
      | test@domain      | Format d'email invalide            |
      | test@domain.com  | Success                            |
      | user@test.co.ma | Success                            |
```

### Test Data (CSV Format)

#### CSV Structure
| test_case_id | scenario_name | email | expected_result | test_type | notes |
|--------------|---------------|-------|-----------------|-----------|-------|
| TC-003 | Validate Email | invalid-email | Error | negative | Missing @ symbol |
| TC-003 | Validate Email | test@ | Error | negative | Missing domain |
| TC-003 | Validate Email | @domain.com | Error | negative | Missing username |
| TC-003 | Validate Email | test@domain | Error | negative | Missing TLD |
| TC-003 | Validate Email | test@domain.com | Success | positive | Valid email |
| TC-003 | Validate Email | user@test.co.ma | Success | positive | Valid email with subdomain |

#### CSV Export Format
```csv
test_case_id,scenario_name,email,expected_result,test_type,notes
TC-003,Validate Email,invalid-email,Error,negative,Missing @ symbol
TC-003,Validate Email,test@,Error,negative,Missing domain
TC-003,Validate Email,@domain.com,Error,negative,Missing username
TC-003,Validate Email,test@domain,Error,negative,Missing TLD
TC-003,Validate Email,test@domain.com,Success,positive,Valid email
TC-003,Validate Email,user@test.co.ma,Success,positive,Valid email with subdomain
```

### Test Steps
1. Naviguer vers "/ajouter-offre-emploi"
2. Remplir tous les champs requis sauf l'email
3. Entrer un email invalide
4. Soumettre le formulaire
5. Vérifier l'affichage d'une erreur de validation d'email
6. Répéter avec différents formats d'email invalides
7. Tester avec des emails valides

### Expected Results
- Les emails invalides génèrent une erreur de validation
- Le message d'erreur indique clairement le problème
- Les emails valides sont acceptés
- Le formulaire reste sur la page en cas d'erreur

### Test Data Variations
- **Invalid Formats**: Emails sans @, sans domaine, sans TLD, etc.
- **Valid Formats**: Emails standards, avec sous-domaines, avec extensions locales (.co.ma)
- **Edge Cases**: Emails très longs, avec caractères spéciaux

### Related Test Cases
- Depends on: None
- Related: TC-001 (Création réussie), TC-002 (Validation champs requis)

---

## TC-004: Consulter la page d'accueil

### Metadata
- **ID**: TC-004
- **Use Case**: UC-001 (Consulter la page d'accueil)
- **Priority**: High
- **Type**: Smoke, Integration
- **Status**: Ready
- **Tags**: @smoke @view @integration

### Test Scenario
Tester que la page d'accueil s'affiche correctement avec les offres d'emploi et les régions.

### BDD Scenario (Gherkin Format)
```gherkin
Feature: Homepage Display
  @smoke @view @integration
  Scenario: View homepage with job offers
    Given the application is running on "http://localhost:8080"
    And there are job offers in the system
    When I navigate to the homepage "/"
    Then I should see the homepage
    And I should see a list of job offers (maximum 8)
    And I should see the list of regions
    And each job offer should display:
      | field   |
      | title   |
      | company |
      | city    |
      | domain  |
    And I should see a link to view job details
```

### Test Data (CSV Format)

#### CSV Structure
| test_case_id | scenario_name | url | expected_elements | expected_offers_count | expected_result | test_type | notes |
|--------------|---------------|-----|-------------------|----------------------|-----------------|-----------|-------|
| TC-004 | View Homepage | / | job_offers,regions | 8 | Success | positive | Homepage with offers |
| TC-004 | View Homepage | "" | job_offers,regions | 8 | Success | positive | Empty path also works |

#### CSV Export Format
```csv
test_case_id,scenario_name,url,expected_elements,expected_offers_count,expected_result,test_type,notes
TC-004,View Homepage,/,job_offers,regions,8,Success,positive,Homepage with offers
TC-004,View Homepage,"",job_offers,regions,8,Success,positive,Empty path also works
```

### Test Steps
1. Naviguer vers "/" ou ""
2. Vérifier que la page d'accueil se charge
3. Vérifier l'affichage des offres d'emploi (maximum 8)
4. Vérifier l'affichage de la liste des régions
5. Vérifier que chaque offre affiche les informations essentielles
6. Vérifier la présence de liens vers les détails des offres

### Expected Results
- La page d'accueil se charge sans erreur
- Les offres d'emploi sont affichées (maximum 8 par défaut)
- Les régions sont listées
- Chaque offre affiche titre, entreprise, ville, domaine
- Les liens vers les détails fonctionnent

### Test Data Variations
- **With Offers**: Page avec offres disponibles
- **Without Offers**: Page sans offres (message approprié)
- **Many Offers**: Plus de 8 offres (vérifier pagination)

### Related Test Cases
- Depends on: None
- Related: TC-005 (Consulter détail offre), TC-006 (Rechercher par domaine)

---

## TC-005: Consulter le détail d'une offre d'emploi

### Metadata
- **ID**: TC-005
- **Use Case**: UC-004 (Consulter le détail d'une offre d'emploi)
- **Priority**: High
- **Type**: Integration
- **Status**: Ready
- **Tags**: @view @integration

### Test Scenario
Tester l'affichage des détails complets d'une offre d'emploi.

### BDD Scenario (Gherkin Format)
```gherkin
Feature: Job Offer Details
  @view @integration
  Scenario: View job offer details
    Given a job offer exists with title "Développeur Java Senior"
    And the job offer ID is "<job_id>"
    When I navigate to the job detail page "/offre-emploi-maroc/<job_id>"
    Then I should see the job title "Développeur Java Senior"
    And I should see the company information
    And I should see the job description
    And I should see the location (city, region)
    And I should see the contract type
    And I should see the apply button "/postuler-emploi/<job_id>"
    And I should see social media links if available

    Examples:
      | job_id                       |
      | 507f1f77bcf86cd799439011      |
      | 507f191e810c19729de860ea      |
```

### Test Data (CSV Format)

#### CSV Structure
| test_case_id | scenario_name | job_id | expected_elements | expected_result | test_type | notes |
|--------------|---------------|--------|-------------------|-----------------|-----------|-------|
| TC-005 | View Job Details | 507f1f77bcf86cd799439011 | title,company,description,city,type,apply_button | Success | positive | Valid ObjectId |
| TC-005 | View Job Details | invalid-id | error_page | Error | negative | Invalid ID format |
| TC-005 | View Job Details | 000000000000000000000000 | error_page | Error | negative | Non-existent ID |

#### CSV Export Format
```csv
test_case_id,scenario_name,job_id,expected_elements,expected_result,test_type,notes
TC-005,View Job Details,507f1f77bcf86cd799439011,title,company,description,city,type,apply_button,Success,positive,Valid ObjectId
TC-005,View Job Details,invalid-id,error_page,Error,negative,Invalid ID format
TC-005,View Job Details,000000000000000000000000,error_page,Error,negative,Non-existent ID
```

### Test Steps
1. Créer ou identifier une offre d'emploi existante
2. Naviguer vers "/offre-emploi-maroc/{job_id}"
3. Vérifier l'affichage du titre de l'offre
4. Vérifier l'affichage des informations de l'entreprise
5. Vérifier l'affichage de la description complète
6. Vérifier l'affichage de la localisation
7. Vérifier l'affichage du type de contrat
8. Vérifier la présence du bouton "Postuler"
9. Vérifier les liens réseaux sociaux si disponibles

### Expected Results
- La page de détail se charge correctement
- Toutes les informations de l'offre sont affichées
- Le bouton "Postuler" est présent et fonctionnel
- Les liens vers les réseaux sociaux s'affichent si renseignés
- Les IDs invalides génèrent une erreur 404

### Test Data Variations
- **Valid ID**: ObjectId MongoDB valide existant
- **Invalid ID Format**: ID qui n'est pas un ObjectId valide
- **Non-existent ID**: ObjectId valide mais inexistant en base

### Related Test Cases
- Depends on: TC-001 (Créer offre) ou données de test existantes
- Related: TC-006 (Postuler à une offre)

---

## TC-006: Postuler à une offre d'emploi

### Metadata
- **ID**: TC-006
- **Use Case**: UC-005 (Postuler à une offre d'emploi)
- **Priority**: High
- **Type**: Integration
- **Status**: Ready
- **Tags**: @integration @application

### Test Scenario
Tester la soumission réussie d'une candidature avec upload de CV.

### BDD Scenario (Gherkin Format)
```gherkin
Feature: Job Application
  @integration @application
  Scenario: Submit job application with valid data
    Given a job offer exists with ID "<job_id>"
    And I am on the application page "/postuler-emploi/<job_id>"
    When I fill the application form with:
      | field   | value              |
      | name    | John Doe           |
      | email   | john.doe@email.com |
      | tel     | 0612345678         |
      | message | Je suis intéressé  |
    And I upload a valid CV file "cv.pdf"
    And I submit the form
    Then I should see a success message
    And the application should be saved in the database
    And I should receive a confirmation (if email configured)

    Examples:
      | job_id                       |
      | 507f1f77bcf86cd799439011      |
```

### Test Data (CSV Format)

#### CSV Structure
| test_case_id | scenario_name | job_id | name | email | tel | message | cv_file | expected_result | test_type | notes |
|--------------|---------------|--------|------|-------|-----|---------|---------|-----------------|-----------|-------|
| TC-006 | Submit Application | 507f1f77bcf86cd799439011 | John Doe | john.doe@email.com | 0612345678 | Je suis intéressé | cv.pdf | Success | positive | Valid application |
| TC-006 | Submit Application | 507f1f77bcf86cd799439011 | Jane Smith | jane@email.com | 0623456789 | Candidature motivée | resume.docx | Success | positive | Valid with DOCX |

#### CSV Export Format
```csv
test_case_id,scenario_name,job_id,name,email,tel,message,cv_file,expected_result,test_type,notes
TC-006,Submit Application,507f1f77bcf86cd799439011,John Doe,john.doe@email.com,0612345678,Je suis intéressé,cv.pdf,Success,positive,Valid application
TC-006,Submit Application,507f1f77bcf86cd799439011,Jane Smith,jane@email.com,0623456789,Candidature motivée,resume.docx,Success,positive,Valid with DOCX
```

### Test Steps
1. Naviguer vers "/postuler-emploi/{job_id}"
2. Vérifier que le formulaire s'affiche avec les informations de l'offre pré-remplies
3. Remplir les champs nom, email, téléphone, message
4. Uploader un fichier CV valide (PDF, DOC, DOCX)
5. Soumettre le formulaire
6. Vérifier l'affichage d'un message de succès
7. Vérifier l'enregistrement en base de données
8. Vérifier l'envoi d'email de confirmation (si configuré)

### Expected Results
- Le formulaire s'affiche correctement
- Les données sont validées avant soumission
- Le CV est uploadé avec succès
- La candidature est enregistrée en base
- Un message de confirmation s'affiche
- Un email est envoyé (si configuré)

### Test Data Variations
- **Valid Data**: Tous les champs remplis correctement avec CV valide
- **Invalid Email**: Email au format invalide
- **Invalid CV Format**: Fichier non supporté (ex: .txt, .jpg)
- **CV Too Large**: Fichier dépassant 5 Mo
- **Missing Fields**: Champs requis manquants

### Related Test Cases
- Depends on: TC-005 (Consulter détail offre)
- Related: TC-001 (Créer offre)

---

## TC-007: Rechercher des offres par domaine

### Metadata
- **ID**: TC-007
- **Use Case**: UC-002 (Rechercher des offres d'emploi par domaine)
- **Priority**: High
- **Type**: Integration
- **Status**: Ready
- **Tags**: @search @integration

### Test Scenario
Tester la recherche d'offres d'emploi par domaine avec affichage paginé.

### BDD Scenario (Gherkin Format)
```gherkin
Feature: Search Jobs by Domain
  @search @integration
  Scenario: Search for jobs by domain
    Given there are job offers in different domains
    When I navigate to "/categorie"
    Then I should see a list of all available domains
    When I click on domain "<domain_slug>"
    Then I should see job offers for domain "<domain_name>"
    And I should see pagination if there are more than 10 offers
    And each offer should belong to domain "<domain_name>"

    Examples:
      | domain_slug   | domain_name  |
      | informatique  | Informatique |
      | marketing     | Marketing    |
      | finance       | Finance      |
```

### Test Data (CSV Format)

#### CSV Structure
| test_case_id | scenario_name | domain_slug | domain_name | expected_offers | expected_result | test_type | notes |
|--------------|---------------|-------------|-------------|-----------------|-----------------|-----------|-------|
| TC-007 | Search by Domain | informatique | Informatique | >0 | Success | positive | Valid domain |
| TC-007 | Search by Domain | marketing | Marketing | >0 | Success | positive | Valid domain |
| TC-007 | Search by Domain | invalid-domain | | 0 | Error | negative | Invalid domain slug |

#### CSV Export Format
```csv
test_case_id,scenario_name,domain_slug,domain_name,expected_offers,expected_result,test_type,notes
TC-007,Search by Domain,informatique,Informatique,>0,Success,positive,Valid domain
TC-007,Search by Domain,marketing,Marketing,>0,Success,positive,Valid domain
TC-007,Search by Domain,invalid-domain,,0,Error,negative,Invalid domain slug
```

### Test Steps
1. Naviguer vers "/categorie"
2. Vérifier l'affichage de la liste des domaines
3. Cliquer sur un domaine
4. Vérifier l'affichage des offres du domaine sélectionné
5. Vérifier la pagination si plus de 10 offres
6. Vérifier que toutes les offres affichées appartiennent au domaine

### Expected Results
- La liste des domaines s'affiche correctement
- La sélection d'un domaine affiche les offres correspondantes
- La pagination fonctionne correctement
- Toutes les offres affichées appartiennent au domaine sélectionné
- Les domaines invalides génèrent une erreur ou redirection

### Test Data Variations
- **Valid Domain**: Domaine existant avec offres
- **Valid Domain No Offers**: Domaine existant sans offres
- **Invalid Domain**: Slug de domaine inexistant
- **Pagination**: Domaine avec plus de 10 offres

### Related Test Cases
- Depends on: TC-001 (Créer offre) pour avoir des données
- Related: TC-008 (Rechercher par région)

---

## TC-008: Gérer mes annonces (Liste)

### Metadata
- **ID**: TC-008
- **Use Case**: UC-007.1 (Consulter la liste des annonces)
- **Priority**: Medium
- **Type**: Integration
- **Status**: Ready
- **Tags**: @integration @middle-office

### Test Scenario
Tester l'affichage de la liste paginée des annonces d'un recruteur via son code secret.

### BDD Scenario (Gherkin Format)
```gherkin
Feature: Manage Job Offers
  @integration @middle-office
  Scenario: List my job offers with secret code
    Given I have created job offers with secret code "<secret_code>"
    When I navigate to "/m-office/mes-annonces/<secret_code>"
    Then I should see a list of my job offers
    And I should see pagination if there are more than 10 offers
    And each offer should display:
      | field        |
      | title        |
      | creationDate |
      | status       |
      | editLink     |
      | deleteLink   |
    And I should see links to edit and delete each offer

    Examples:
      | secret_code |
      | abc123xyz   |
      | def456uvw   |
```

### Test Data (CSV Format)

#### CSV Structure
| test_case_id | scenario_name | secret_code | expected_offers | expected_result | test_type | notes |
|--------------|---------------|-------------|-----------------|-----------------|-----------|-------|
| TC-008 | List My Offers | abc123xyz | >0 | Success | positive | Valid secret code |
| TC-008 | List My Offers | invalid-code | 0 | Error | negative | Invalid secret code |
| TC-008 | List My Offers | empty | 0 | Error | negative | Empty secret code |

#### CSV Export Format
```csv
test_case_id,scenario_name,secret_code,expected_offers,expected_result,test_type,notes
TC-008,List My Offers,abc123xyz,>0,Success,positive,Valid secret code
TC-008,List My Offers,invalid-code,0,Error,negative,Invalid secret code
TC-008,List My Offers,empty,0,Error,negative,Empty secret code
```

### Test Steps
1. Créer des offres avec un code secret (via TC-001)
2. Naviguer vers "/m-office/mes-annonces/{secret_code}"
3. Vérifier l'affichage de la liste des annonces
4. Vérifier la pagination si nécessaire
5. Vérifier l'affichage des informations essentielles de chaque offre
6. Vérifier la présence des liens de modification et suppression

### Expected Results
- La liste des annonces s'affiche correctement
- Seules les annonces du code secret sont affichées
- La pagination fonctionne si plus de 10 annonces
- Les liens d'édition et suppression sont présents
- Un code secret invalide génère une erreur 403

### Test Data Variations
- **Valid Secret Code**: Code secret avec annonces
- **Valid Secret Code No Offers**: Code secret sans annonces
- **Invalid Secret Code**: Code secret inexistant
- **Empty Secret Code**: Code secret vide ou null

### Related Test Cases
- Depends on: TC-001 (Créer offre)
- Related: TC-009 (Modifier annonce), TC-010 (Supprimer annonce)

---

## TC-009: Modifier une annonce

### Metadata
- **ID**: TC-009
- **Use Case**: UC-007.2 (Modifier une annonce)
- **Priority**: Medium
- **Type**: Integration
- **Status**: Ready
- **Tags**: @integration @middle-office @update

### Test Scenario
Tester la modification d'une annonce existante via le code secret.

### BDD Scenario (Gherkin Format)
```gherkin
Feature: Update Job Offer
  @integration @middle-office @update
  Scenario: Update an existing job offer
    Given I have created a job offer with secret code "<secret_code>" and ID "<job_id>"
    When I navigate to "/m-office/mes-annonces/update/<secret_code>/<job_id>"
    Then I should see the job offer form pre-filled with existing data
    When I update the title to "<new_title>"
    And I update other fields as needed
    And I submit the form
    Then I should see a success message containing "modifiée"
    And the job offer should be updated in the database
    And I should be redirected to my job offers list

    Examples:
      | secret_code | job_id                       | new_title              |
      | abc123xyz   | 507f1f77bcf86cd799439011     | Updated Job Title       |
```

### Test Data (CSV Format)

#### CSV Structure
| test_case_id | scenario_name | secret_code | job_id | new_title | expected_result | test_type | notes |
|--------------|---------------|-------------|--------|-----------|-----------------|-----------|-------|
| TC-009 | Update Offer | abc123xyz | 507f1f77bcf86cd799439011 | Updated Job Title | Success | positive | Valid update |
| TC-009 | Update Offer | wrong-code | 507f1f77bcf86cd799439011 | Updated Title | Error | negative | Wrong secret code |

#### CSV Export Format
```csv
test_case_id,scenario_name,secret_code,job_id,new_title,expected_result,test_type,notes
TC-009,Update Offer,abc123xyz,507f1f77bcf86cd799439011,Updated Job Title,Success,positive,Valid update
TC-009,Update Offer,wrong-code,507f1f77bcf86cd799439011,Updated Title,Error,negative,Wrong secret code
```

### Test Steps
1. Créer une offre avec un code secret
2. Naviguer vers la page de modification avec code secret et ID
3. Vérifier que le formulaire est pré-rempli avec les données existantes
4. Modifier les champs souhaités
5. Soumettre le formulaire
6. Vérifier l'affichage d'un message de succès
7. Vérifier la mise à jour en base de données
8. Vérifier la redirection vers la liste

### Expected Results
- Le formulaire s'affiche avec les données existantes
- Les modifications sont enregistrées
- Un message de succès s'affiche
- La redirection vers la liste fonctionne
- Un code secret incorrect génère une erreur 403

### Test Data Variations
- **Valid Update**: Modification avec code secret et ID valides
- **Invalid Secret Code**: Tentative avec mauvais code secret
- **Invalid Job ID**: ID d'offre inexistant
- **Validation Errors**: Données invalides lors de la modification

### Related Test Cases
- Depends on: TC-001 (Créer offre), TC-008 (Liste annonces)
- Related: TC-010 (Supprimer annonce)

---

## TC-010: Supprimer une annonce

### Metadata
- **ID**: TC-010
- **Use Case**: UC-007.3 (Supprimer une annonce)
- **Priority**: Medium
- **Type**: Integration
- **Status**: Ready
- **Tags**: @integration @middle-office @delete

### Test Scenario
Tester la suppression d'une annonce existante via le code secret.

### BDD Scenario (Gherkin Format)
```gherkin
Feature: Delete Job Offer
  @integration @middle-office @delete
  Scenario: Delete an existing job offer
    Given I have created a job offer with secret code "<secret_code>" and ID "<job_id>"
    When I navigate to "/m-office/mes-annonces/delete/<secret_code>/<job_id>"
    Then I should see a confirmation message
    When I confirm the deletion
    Then I should see a success message
    And the job offer should be deleted from the database
    And I should be redirected to my job offers list
    And the deleted offer should no longer appear in the list

    Examples:
      | secret_code | job_id                       |
      | abc123xyz   | 507f1f77bcf86cd799439011     |
```

### Test Data (CSV Format)

#### CSV Structure
| test_case_id | scenario_name | secret_code | job_id | expected_result | test_type | notes |
|--------------|---------------|-------------|--------|-----------------|-----------|-------|
| TC-010 | Delete Offer | abc123xyz | 507f1f77bcf86cd799439011 | Success | positive | Valid deletion |
| TC-010 | Delete Offer | wrong-code | 507f1f77bcf86cd799439011 | Error | negative | Wrong secret code |

#### CSV Export Format
```csv
test_case_id,scenario_name,secret_code,job_id,expected_result,test_type,notes
TC-010,Delete Offer,abc123xyz,507f1f77bcf86cd799439011,Success,positive,Valid deletion
TC-010,Delete Offer,wrong-code,507f1f77bcf86cd799439011,Error,negative,Wrong secret code
```

### Test Steps
1. Créer une offre avec un code secret
2. Naviguer vers la page de suppression avec code secret et ID
3. Vérifier l'affichage d'une demande de confirmation
4. Confirmer la suppression
5. Vérifier l'affichage d'un message de succès
6. Vérifier la suppression en base de données
7. Vérifier que l'offre n'apparaît plus dans la liste

### Expected Results
- Une confirmation est demandée avant suppression
- La suppression est effectuée après confirmation
- Un message de succès s'affiche
- L'offre est supprimée de la base de données
- L'offre n'apparaît plus dans la liste
- Un code secret incorrect génère une erreur 403

### Test Data Variations
- **Valid Deletion**: Suppression avec code secret et ID valides
- **Invalid Secret Code**: Tentative avec mauvais code secret
- **Invalid Job ID**: ID d'offre inexistant
- **Cancel Deletion**: Annulation de la suppression

### Related Test Cases
- Depends on: TC-001 (Créer offre), TC-008 (Liste annonces)
- Related: TC-009 (Modifier annonce)

---

## Format CSV pour Export Complet

### Structure Générale du CSV

Le CSV complet pour tous les tests peut être généré avec les colonnes suivantes:

```csv
test_case_id,use_case_id,scenario_name,priority,test_type,status,tags,url,field1,field2,field3,...,expected_result,notes
```

### Exemple de CSV Complet

```csv
test_case_id,use_case_id,scenario_name,priority,test_type,status,tags,url,title,email,domain,city,expected_result,notes
TC-001,UC-006,Create Job Offer,High,Integration,Ready,"@smoke @creation","/ajouter-offre-emploi",Développeur Java,recruiter@company.com,Informatique,Casablanca,Success,Valid case
TC-002,UC-006,Validate Required Fields,High,Validation,Ready,"@validation","/ajouter-offre-emploi",Test Job,,,,Error,Missing fields
TC-003,UC-006,Validate Email,Medium,Validation,Ready,"@validation @email","/ajouter-offre-emploi",Test Job,invalid-email,,,Error,Invalid email
TC-004,UC-001,View Homepage,High,Smoke,Ready,"@smoke @view","/",,,,,Success,Homepage display
TC-005,UC-004,View Job Details,High,Integration,Ready,"@view","/offre-emploi-maroc/{id}",,,,,Success,Job details
TC-006,UC-005,Submit Application,High,Integration,Ready,"@integration","/postuler-emploi/{id}",,,,,Success,Application submission
TC-007,UC-002,Search by Domain,High,Integration,Ready,"@search","/categorie/{domain}",,,,,Success,Domain search
TC-008,UC-007,List My Offers,Medium,Integration,Ready,"@middle-office","/m-office/mes-annonces/{code}",,,,,Success,List offers
TC-009,UC-007,Update Offer,Medium,Integration,Ready,"@update","/m-office/mes-annonces/update/{code}/{id}",,,,,Success,Update offer
TC-010,UC-007,Delete Offer,Medium,Integration,Ready,"@delete","/m-office/mes-annonces/delete/{code}/{id}",,,,,Success,Delete offer
```

---

## Notes

Ce document peut être enrichi via des prompts pour:
- Ajouter de nouveaux cas de test
- Détailer les variations de données de test
- Ajouter des cas de test pour les scénarios alternatifs
- Générer des fichiers CSV complets pour les utilitaires de test
- Convertir les scénarios BDD en fichiers `.feature` Gherkin

### Utilisation

1. **Génération de fichiers `.feature`**: Les scénarios BDD peuvent être copiés directement dans des fichiers `.feature` pour Cucumber
2. **Export CSV**: Les structures CSV peuvent être exportées pour alimenter les utilitaires de test
3. **Génération de prompts**: Chaque cas de test peut servir de base pour générer des prompts de test d'intégration
