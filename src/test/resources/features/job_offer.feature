# language: en
# Job Offer BDD Test Scenarios
# These scenarios correspond to the Selenium tests in JobOfferSeleniumTest.java

Feature: Job Offer Management
  As a recruiter
  I want to create and manage job offers
  So that I can attract qualified candidates

  Background:
    # Port 8081 = config.test (server.port). Si 8080 est occupé, l'app tourne sur 8081.
    # IMPORTANT: Start the application on http://localhost:8081 before running these scenarios (e.g. run the Jetty server); the step only navigates to the URL and does not start the server.
    Given the application is running on "http://localhost:8081"
    And I am on the job creation page

  # Scenario 1: Successful job creation
  @smoke @creation
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

  # Scenario 2: Missing required fields
  @validation
  Scenario: Show error when required fields are missing
    When I fill only the title with "Test Job"
    And I submit the form
    Then I should see an error message
    And I should remain on the job creation page

  # Scenario 3: Invalid email validation
  @validation @email
  Scenario: Validate email format
    When I fill the job creation form with the following data:
      | field | value         |
      | title | Test Job      |
      | email | invalid-email |
    And I submit the form
    Then I should see an email validation error

  # Scenario 4: Update existing job offer
  @update
  Scenario: Update an existing job offer
    Given I have created a job offer with secret code "abc123"
    When I navigate to my job offers page with secret code "abc123"
    And I click on edit for the first job
    And I update the title to "Updated Job Title"
    And I submit the form
    Then I should see a success message containing "modifiée"

  # Scenario 5: Different contract types
  @smoke @creation
  Scenario Outline: Create job offers with different contract types
    When I fill the job creation form with type "<contract_type>"
    And I fill all other required fields
    And I submit the form
    Then I should see a success message

    Examples:
      | contract_type |
      | CDI           |
      | CDD           |
      | Stage         |
      | Freelance     |

  # Scenario 6: Different cities
  @creation
  Scenario Outline: Create job offers for different cities
    When I fill the job creation form with city "<city>"
    And I fill all other required fields
    And I submit the form
    Then I should see a success message

    Examples:
      | city       |
      | Casablanca |
      | Rabat      |
      | Marrakech  |
      | Tanger     |

  # Scenario 7: Job detail view
  @view
  Scenario: View job offer details
    Given a job offer exists with title "Test Job Offer"
    When I navigate to the job detail page
    Then I should see the job title "Test Job Offer"
    And I should see the company information
    And I should see the apply button

  # Scenario 8: Search for jobs
  @search
  Scenario: Search for jobs by keyword
    Given there are job offers in the system
    When I search for jobs with keyword "Java"
    Then I should see a list of jobs containing "Java"
    And I should see the number of results

  # Scenario 9: Filter jobs by city
  @filter
  Scenario: Filter jobs by city
    Given there are job offers in different cities
    When I filter jobs by city "Casablanca"
    Then I should only see jobs in "Casablanca"

  # Scenario 10: Filter jobs by domain
  @filter
  Scenario: Filter jobs by domain
    Given there are job offers in different domains
    When I filter jobs by domain "Informatique"
    Then I should only see jobs in "Informatique" domain
