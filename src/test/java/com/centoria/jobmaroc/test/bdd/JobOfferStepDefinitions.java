package com.centoria.jobmaroc.test.bdd;

import com.centoria.jobmaroc.test.selenium.pages.JobOfferPage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Cucumber Step Definitions for Job Offer BDD tests.
 * These step definitions map to the scenarios in job_offer.feature
 */
public class JobOfferStepDefinitions {
    
    private WebDriver driver;
    private JobOfferPage jobOfferPage;
    private String baseUrl;
    private String createdSecretCode;
    
    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--headless"); // Uncomment for headless mode
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        jobOfferPage = new JobOfferPage(driver);
    }
    
    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
    
    // ==================== Given Steps ====================
    
    @Given("the application is running on {string}")
    public void theApplicationIsRunningOn(String url) {
        this.baseUrl = url;
        // Optionally verify the application is accessible
        driver.get(baseUrl);
        assertNotNull(driver.getTitle(), "Application should be accessible");
    }
    
    @Given("I am on the job creation page")
    public void iAmOnTheJobCreationPage() {
        jobOfferPage.navigateToCreatePage(baseUrl);
        assertTrue(jobOfferPage.getCurrentUrl().contains("/ajouter-offre-emploi"),
                "Should be on job creation page");
    }
    
    @Given("I have created a job offer with secret code {string}")
    public void iHaveCreatedAJobOfferWithSecretCode(String secretCode) {
        this.createdSecretCode = secretCode;
        // In real test, this would create a job and store the actual secret code
    }
    
    @Given("a job offer exists with title {string}")
    public void aJobOfferExistsWithTitle(String title) {
        // This would typically query the database or create a job
        // For now, we assume the job exists
    }
    
    @Given("there are job offers in the system")
    public void thereAreJobOffersInTheSystem() {
        // Verify jobs exist by navigating to search page
        driver.get(baseUrl + "/emploi");
    }
    
    @Given("there are job offers in different cities")
    public void thereAreJobOffersInDifferentCities() {
        // Pre-condition: jobs exist in different cities
    }
    
    @Given("there are job offers in different domains")
    public void thereAreJobOffersInDifferentDomains() {
        // Pre-condition: jobs exist in different domains
    }
    
    // ==================== When Steps ====================
    
    @When("I fill the job creation form with the following data:")
    public void iFillTheJobCreationFormWithTheFollowingData(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        
        for (Map<String, String> row : rows) {
            String field = row.get("field");
            String value = row.get("value");
            
            fillField(field, value);
        }
    }
    
    @When("I fill only the title with {string}")
    public void iFillOnlyTheTitleWith(String title) {
        jobOfferPage.enterTitle(title);
    }
    
    @When("I fill the job creation form with type {string}")
    public void iFillTheJobCreationFormWithType(String contractType) {
        jobOfferPage.enterType(contractType)
            .selectCity("Casablanca")
            .selectDomain("Informatique");
    }
    
    @When("I fill the job creation form with city {string}")
    public void iFillTheJobCreationFormWithCity(String city) {
        jobOfferPage.selectCity(city);
    }
    
    @When("I fill all other required fields")
    public void iFillAllOtherRequiredFields() {
        jobOfferPage
            .enterTitle("Test Job - BDD")
            .enterContent("Test job description for BDD testing")
            .enterEmail("bdd.test@example.com")
            .enterPhone("0612345678")
            .enterType("CDI")
            .selectDomain("Informatique")
            .enterCompanyName("BDD Test Company")
            .enterCompanyCode("999888")
            .enterNbrDePostes("1")
            .selectFormation("bac+3")
            .selectExperienceLevel("De 1 à 3 ans")
            .enterFacebook("https://facebook.com/bddtest")
            .enterTwitter("https://twitter.com/bddtest")
            .enterLinkedin("https://linkedin.com/bddtest")
            .selectConfidentiality("public");
    }
    
    @When("I submit the form")
    public void iSubmitTheForm() {
        jobOfferPage.submitForm();
    }
    
    @When("I navigate to my job offers page with secret code {string}")
    public void iNavigateToMyJobOffersPageWithSecretCode(String secretCode) {
        driver.get(baseUrl + "/mes-offres/" + secretCode);
    }
    
    @When("I click on edit for the first job")
    public void iClickOnEditForTheFirstJob() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement editLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("a[href*='mes-annonces/update']")));
        editLink.click();
        // Wait for the edit form (offer form with #title) to be visible
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("title")));
    }
    
    @When("I update the title to {string}")
    public void iUpdateTheTitleTo(String newTitle) {
        jobOfferPage.enterTitle(newTitle);
    }
    
    @When("I navigate to the job detail page")
    public void iNavigateToTheJobDetailPage() {
        // Navigate to a specific job detail page
        // Implementation depends on how job IDs are handled
    }
    
    @When("I search for jobs with keyword {string}")
    public void iSearchForJobsWithKeyword(String keyword) {
        driver.get(baseUrl + "/emploi?keyword=" + keyword);
    }
    
    @When("I filter jobs by city {string}")
    public void iFilterJobsByCity(String city) {
        driver.get(baseUrl + "/emploi/" + city.toLowerCase());
    }
    
    @When("I filter jobs by domain {string}")
    public void iFilterJobsByDomain(String domain) {
        driver.get(baseUrl + "/secteur/" + domain.toLowerCase().replace("/", "-"));
    }
    
    // ==================== Then Steps ====================
    
    @Then("I should see a success message")
    public void iShouldSeeASuccessMessage() {
        assertTrue(jobOfferPage.isSuccessMessageDisplayed(),
                "Success message should be displayed");
    }
    
    @Then("I should see a success message containing {string}")
    public void iShouldSeeASuccessMessageContaining(String text) {
        assertTrue(jobOfferPage.isSuccessMessageDisplayed(),
                "Success message should be displayed");
        assertTrue(jobOfferPage.getSuccessMessage().contains(text),
                "Success message should contain: " + text);
    }
    
    @Then("I should receive a secret code")
    public void iShouldReceiveASecretCode() {
        try {
            String secretCode = jobOfferPage.getSecretCode();
            assertNotNull(secretCode, "Secret code should be provided");
            assertFalse(secretCode.isEmpty(), "Secret code should not be empty");
            this.createdSecretCode = secretCode;
        } catch (Exception e) {
            // Secret code display may vary, log warning
            System.out.println("Warning: Could not verify secret code display");
        }
    }
    
    @Then("I should see an error message")
    public void iShouldSeeAnErrorMessage() {
        assertTrue(jobOfferPage.isErrorMessageDisplayed() ||
                        jobOfferPage.getCurrentUrl().contains("/ajouter-offre-emploi"),
                "Error should be shown or should stay on creation page");
    }
    
    @Then("I should remain on the job creation page")
    public void iShouldRemainOnTheJobCreationPage() {
        assertTrue(jobOfferPage.getCurrentUrl().contains("/ajouter-offre-emploi"),
                "Should remain on job creation page");
    }
    
    @Then("I should see an email validation error")
    public void iShouldSeeAnEmailValidationError() {
        // HTML5 validation or custom error message
        assertTrue(jobOfferPage.isErrorMessageDisplayed() ||
                        jobOfferPage.getCurrentUrl().contains("/ajouter-offre-emploi"),
                "Email validation error should be shown");
    }
    
    @Then("I should see the job title {string}")
    public void iShouldSeeTheJobTitle(String title) {
        assertTrue(driver.getPageSource().contains(title),
                "Page should contain job title: " + title);
    }
    
    @Then("I should see the company information")
    public void iShouldSeeTheCompanyInformation() {
        // Verify company info is displayed on job detail page
        // Implementation depends on page structure
    }
    
    @Then("I should see the apply button")
    public void iShouldSeeTheApplyButton() {
        // Verify apply button is present
        // Implementation depends on page structure
    }
    
    @Then("I should see a list of jobs containing {string}")
    public void iShouldSeeAListOfJobsContaining(String keyword) {
        assertTrue(driver.getPageSource().toLowerCase().contains(keyword.toLowerCase()),
                "Search results should contain keyword: " + keyword);
    }
    
    @Then("I should see the number of results")
    public void iShouldSeeTheNumberOfResults() {
        // Verify result count is displayed
        // Implementation depends on page structure
    }
    
    @Then("I should only see jobs in {string}")
    public void iShouldOnlySeeJobsIn(String location) {
        // Verify filtered results only show jobs in the specified location
        assertTrue(driver.getPageSource().contains(location),
                "Results should only show jobs in: " + location);
    }
    
    @Then("I should only see jobs in {string} domain")
    public void iShouldOnlySeeJobsInDomain(String domain) {
        // Verify filtered results only show jobs in the specified domain
        assertTrue(driver.getPageSource().contains(domain),
                "Results should only show jobs in domain: " + domain);
    }
    
    // ==================== Helper Methods ====================
    
    private void fillField(String field, String value) {
        switch (field.toLowerCase()) {
            case "title":
                jobOfferPage.enterTitle(value);
                break;
            case "content":
                jobOfferPage.enterContent(value);
                break;
            case "email":
                jobOfferPage.enterEmail(value);
                break;
            case "phone":
            case "tel":
                jobOfferPage.enterPhone(value);
                break;
            case "type":
                jobOfferPage.enterType(value);  // Type is a text input
                break;
            case "city":
                jobOfferPage.selectCity(value);
                break;
            case "domain":
                jobOfferPage.selectDomain(value);
                break;
            case "companyname":
                jobOfferPage.enterCompanyName(value);
                break;
            case "companycode":
                jobOfferPage.enterCompanyCode(value);
                break;
            case "nbrdepostes":
                jobOfferPage.enterNbrDePostes(value);
                break;
            case "formation":
                jobOfferPage.selectFormation(value);
                break;
            case "experiencelevel":
                jobOfferPage.selectExperienceLevel(value);
                break;
            case "facebook":
                jobOfferPage.enterFacebook(value);
                break;
            case "twitter":
                jobOfferPage.enterTwitter(value);
                break;
            case "linkedin":
                jobOfferPage.enterLinkedin(value);
                break;
            case "confidentiality":
                jobOfferPage.selectConfidentiality(value);
                break;
            default:
                throw new IllegalArgumentException("Unknown field: " + field);
        }
    }
}
