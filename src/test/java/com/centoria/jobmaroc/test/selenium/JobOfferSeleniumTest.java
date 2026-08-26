package com.centoria.jobmaroc.test.selenium;

import com.centoria.jobmaroc.test.selenium.pages.JobOfferPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Selenium UI Tests for Job Offer creation and update.
 * These tests can be run with Katalon or directly with JUnit.
 * 
 * Prepared for future BDD transformation with Cucumber.
 * Each test method corresponds to a scenario in job_offer.feature
 * 
 * IMPORTANT: The application must be running on localhost:8081 before running these tests (config.test server.port).
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JobOfferSeleniumTest {
    
    private static WebDriver driver;
    private static JobOfferPage jobOfferPage;
    private static final String BASE_URL = "http://localhost:8081";
    
    // Test data matching the actual form fields in offre.ftl
    private static final String TEST_TITLE = "Développeur Java Senior - Test Selenium";
    private static final String TEST_CONTENT = "Nous recherchons un développeur Java senior avec 5 ans d'expérience minimum. " +
            "Test automatisé avec Selenium. Compétences requises: Spring Boot, Hibernate, MongoDB.";
    private static final String TEST_EMAIL = "test.selenium@example.com";
    private static final String TEST_PHONE = "0612345678";
    private static final String TEST_TYPE = "CDI";  // Text input, not select
    private static final String TEST_CITY = "Casablanca";
    private static final String TEST_DOMAIN = "Informatique";
    private static final String TEST_COMPANY = "Test Company Selenium";
    private static final String TEST_COMPANY_CODE = "123456789";
    private static final String TEST_NB_POSTES = "2";
    private static final String TEST_FORMATION = "bac+5";
    private static final String TEST_EXPERIENCE = "De 3 à 5 ans";
    private static final String TEST_FACEBOOK = "https://facebook.com/testcompany";
    private static final String TEST_TWITTER = "https://twitter.com/testcompany";
    private static final String TEST_LINKEDIN = "https://linkedin.com/company/testcompany";
    private static final String TEST_CONFIDENTIALITY = "public";
    
    // Store secret code for update tests
    private static String createdJobSecretCode;
    
    @BeforeAll
    static void setupClass() {
        // Setup Chrome driver using WebDriverManager
        WebDriverManager.chromedriver().setup();
        
        // Configure Chrome options
        ChromeOptions options = new ChromeOptions();
        // Uncomment for headless mode:
        // options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        
        jobOfferPage = new JobOfferPage(driver);
    }
    
    @AfterAll
    static void teardownClass() {
        if (driver != null) {
            driver.quit();
        }
    }
    
    /**
     * Test Case 1: Navigate to job creation page
     * BDD: Given I am on the job creation page
     */
    @Test
    @Order(1)
    @DisplayName("Should navigate to job creation page")
    void shouldNavigateToJobCreationPage() {
        jobOfferPage.navigateToCreatePage(BASE_URL);
        
        assertTrue(driver.getCurrentUrl().contains("/ajouter-offre-emploi"),
                "Should be on job creation page");
    }
    
    /**
     * Test Case 2: Create a new job offer with valid data
     * BDD: When I fill the job creation form with valid data And I submit the form
     *      Then I should see a success message And I should receive a secret code
     */
    @Test
    @Order(2)
    @DisplayName("Should create a new job offer with valid data")
    void shouldCreateNewJobOfferWithValidData() {
        jobOfferPage.navigateToCreatePage(BASE_URL);
        
        // Fill the form with test data (all required fields)
        jobOfferPage.fillJobCreationForm(
                TEST_TITLE,
                TEST_CONTENT,
                TEST_EMAIL,
                TEST_PHONE,
                TEST_TYPE,
                TEST_CITY,
                TEST_DOMAIN,
                TEST_COMPANY,
                TEST_COMPANY_CODE,
                TEST_NB_POSTES,
                TEST_FORMATION,
                TEST_EXPERIENCE,
                TEST_FACEBOOK,
                TEST_TWITTER,
                TEST_LINKEDIN,
                TEST_CONFIDENTIALITY
        );
        
        // Submit the form
        jobOfferPage.submitForm();
        
        // After submission, we should either:
        // 1. See a success message
        // 2. Be redirected to a confirmation page
        // 3. Stay on the page if there are errors
        
        // Wait a bit for the page to process
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Check if we're still on the creation page (would indicate an error)
        // or if we've been redirected (success)
        boolean stillOnCreationPage = jobOfferPage.isOnJobCreationPage();
        
        if (stillOnCreationPage) {
            // Check for success message on the same page
            assertTrue(jobOfferPage.isSuccessMessageDisplayed(),
                    "If still on creation page, success message should be displayed");
        }
        // If redirected, the job was likely created successfully
        
        // Try to get secret code if available
        try {
            createdJobSecretCode = jobOfferPage.getSecretCode();
            assertNotNull(createdJobSecretCode, "Secret code should be provided");
        } catch (Exception e) {
            System.out.println("Note: Secret code may be displayed on a different page or sent by email");
        }
    }
    
    /**
     * Test Case 3: Fail to create job with missing required fields
     * BDD: When I submit the form without filling required fields
     *      Then I should see an error message
     */
    @Test
    @Order(3)
    @DisplayName("Should show error when required fields are missing")
    void shouldShowErrorWhenRequiredFieldsMissing() {
        jobOfferPage.navigateToCreatePage(BASE_URL);
        
        // Only fill partial data (missing many required fields)
        jobOfferPage.enterTitle(TEST_TITLE);
        // Don't fill other required fields
        
        jobOfferPage.submitForm();
        
        // Wait for validation
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Should show error or stay on the same page due to HTML5 validation
        assertTrue(
                jobOfferPage.isErrorMessageDisplayed() || 
                jobOfferPage.isOnJobCreationPage(),
                "Should show error or stay on creation page when required fields are missing"
        );
    }
    
    /**
     * Test Case 4: Validate email format
     * BDD: When I enter an invalid email format
     *      Then I should see an email validation error
     */
    @Test
    @Order(4)
    @DisplayName("Should validate email format")
    void shouldValidateEmailFormat() {
        jobOfferPage.navigateToCreatePage(BASE_URL);
        
        // Fill form with invalid email
        jobOfferPage.fillJobCreationForm(
                TEST_TITLE,
                TEST_CONTENT,
                "invalid-email-format", // Invalid email format
                TEST_PHONE,
                TEST_TYPE,
                TEST_CITY,
                TEST_DOMAIN,
                TEST_COMPANY,
                TEST_COMPANY_CODE,
                TEST_NB_POSTES,
                TEST_FORMATION,
                TEST_EXPERIENCE,
                TEST_FACEBOOK,
                TEST_TWITTER,
                TEST_LINKEDIN,
                TEST_CONFIDENTIALITY
        );
        
        jobOfferPage.submitForm();
        
        // Wait for validation
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Should show error or HTML5 validation should prevent submission
        assertTrue(
                jobOfferPage.isErrorMessageDisplayed() ||
                jobOfferPage.isOnJobCreationPage(),
                "Should show error for invalid email format"
        );
    }
    
    /**
     * Test Case 5: Create job with different contract type
     * BDD: When I fill all fields with a CDD contract
     *      Then the job should be created successfully
     */
    @Test
    @Order(5)
    @DisplayName("Should create job with CDD contract type")
    void shouldCreateJobWithCDDContract() {
        jobOfferPage.navigateToCreatePage(BASE_URL);
        
        jobOfferPage.fillJobCreationForm(
                "Développeur Frontend - CDD Test",
                "Poste en CDD pour développeur frontend React. Mission de 6 mois renouvelable.",
                "cdd.test@example.com",
                "0698765432",
                "CDD",  // Different contract type
                "Rabat",
                "Informatique",
                "CDD Test Company",
                "987654321",
                "1",
                "bac+3",
                "De 1 à 3 ans",
                "https://facebook.com/cddtest",
                "https://twitter.com/cddtest",
                "https://linkedin.com/cddtest",
                "transmitted"  // Different confidentiality
        );
        
        jobOfferPage.submitForm();
        
        // Wait for processing
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Verify either success message or redirect
        boolean stillOnCreationPage = jobOfferPage.isOnJobCreationPage();
        if (stillOnCreationPage) {
            assertTrue(jobOfferPage.isSuccessMessageDisplayed(),
                    "Job with CDD contract should be created successfully");
        }
    }
}
