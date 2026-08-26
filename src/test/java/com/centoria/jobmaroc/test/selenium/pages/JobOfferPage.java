package com.centoria.jobmaroc.test.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

/**
 * Page Object for the Job Offer creation/update page (/ajouter-offre-emploi).
 * Used for Selenium tests and prepared for BDD transformation.
 * 
 * Field IDs match the actual form in offre.ftl
 */
public class JobOfferPage extends BasePage {
    
    // Form elements - IDs match offre.ftl
    @FindBy(id = "title")
    private WebElement titleInput;
    
    @FindBy(id = "content")
    private WebElement contentTextarea;
    
    @FindBy(id = "email")
    private WebElement emailInput;
    
    @FindBy(id = "tel")  // Note: ID is "tel" not "phone"
    private WebElement phoneInput;
    
    @FindBy(id = "type")  // This is an input, not a select
    private WebElement typeInput;
    
    @FindBy(id = "city")
    private WebElement citySelect;
    
    @FindBy(id = "domain")
    private WebElement domainSelect;
    
    @FindBy(id = "companyName")
    private WebElement companyNameInput;
    
    @FindBy(id = "companyCode")
    private WebElement companyCodeInput;
    
    @FindBy(id = "nbrDePostes")
    private WebElement nbrDePostesInput;
    
    @FindBy(id = "formation")
    private WebElement formationSelect;
    
    @FindBy(id = "experienceLevel")
    private WebElement experienceLevelSelect;
    
    // Social media fields
    @FindBy(id = "facebook")
    private WebElement facebookInput;
    
    @FindBy(id = "twitter")
    private WebElement twitterInput;
    
    @FindBy(id = "linkedin")
    private WebElement linkedinInput;
    
    // Confidentiality radio buttons
    @FindBy(id = "id1")  // Public
    private WebElement confidentialityPublic;
    
    @FindBy(id = "id2")  // Transmitted to candidates
    private WebElement confidentialityTransmitted;
    
    @FindBy(id = "id3")  // Private
    private WebElement confidentialityPrivate;
    
    // Submit button - it's a <button> inside .box-br
    @FindBy(css = "form#offreForm button")
    private WebElement submitButton;
    
    // Success/Error messages (creation: message.ftl .container2 pre; update: mo-annonce.ftl green div)
    @FindBy(css = ".alert-success, .success-message, .message-success, .container2 pre, section.offres div[style*='d4edda']")
    private WebElement successMessage;
    
    // Erreur : .alert-danger, .error-message, ou le bloc d'erreur du formulaire (front/offre.ftl)
    @FindBy(css = ".alert-danger, .error-message, .field-error:not(:empty), form#offreForm div[style*='f8d7da']")
    private WebElement errorMessage;
    
    // Secret code display (on confirmation page)
    @FindBy(css = ".secret-code, #secretCode, .code-secret")
    private WebElement secretCodeDisplay;
    
    public JobOfferPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Navigate to the job offer creation page
     */
    public JobOfferPage navigateToCreatePage(String baseUrl) {
        driver.get(baseUrl + "/ajouter-offre-emploi");
        // Dismiss cookie banner if present
        dismissCookieBannerIfPresent();
        return this;
    }
    
    /**
     * Fill in the job title
     */
    public JobOfferPage enterTitle(String title) {
        clearAndType(titleInput, title);
        return this;
    }
    
    /**
     * Fill in the job content/description
     */
    public JobOfferPage enterContent(String content) {
        clearAndType(contentTextarea, content);
        return this;
    }
    
    /**
     * Fill in the email
     */
    public JobOfferPage enterEmail(String email) {
        clearAndType(emailInput, email);
        return this;
    }
    
    /**
     * Fill in the phone number (field ID is "tel")
     */
    public JobOfferPage enterPhone(String phone) {
        clearAndType(phoneInput, phone);
        return this;
    }
    
    /**
     * Enter the job type (CDI, CDD, etc.) - this is a text input
     */
    public JobOfferPage enterType(String type) {
        clearAndType(typeInput, type);
        return this;
    }
    
    /**
     * Select the city from dropdown
     */
    public JobOfferPage selectCity(String city) {
        new Select(waitForVisibility(citySelect)).selectByVisibleText(city);
        return this;
    }
    
    /**
     * Select the domain/category from dropdown
     */
    public JobOfferPage selectDomain(String domain) {
        new Select(waitForVisibility(domainSelect)).selectByVisibleText(domain);
        return this;
    }
    
    /**
     * Fill in the company name
     */
    public JobOfferPage enterCompanyName(String companyName) {
        clearAndType(companyNameInput, companyName);
        return this;
    }
    
    /**
     * Fill in the company code (Patente/ICE)
     */
    public JobOfferPage enterCompanyCode(String companyCode) {
        clearAndType(companyCodeInput, companyCode);
        return this;
    }
    
    /**
     * Fill in the number of positions
     */
    public JobOfferPage enterNbrDePostes(String nbrDePostes) {
        clearAndType(nbrDePostesInput, nbrDePostes);
        return this;
    }
    
    /**
     * Select the formation level from dropdown
     */
    public JobOfferPage selectFormation(String formation) {
        new Select(waitForVisibility(formationSelect)).selectByVisibleText(formation);
        return this;
    }
    
    /**
     * Select the experience level from dropdown
     */
    public JobOfferPage selectExperienceLevel(String experienceLevel) {
        new Select(waitForVisibility(experienceLevelSelect)).selectByVisibleText(experienceLevel);
        return this;
    }
    
    /**
     * Fill in Facebook URL
     */
    public JobOfferPage enterFacebook(String facebook) {
        clearAndType(facebookInput, facebook);
        return this;
    }
    
    /**
     * Fill in Twitter URL
     */
    public JobOfferPage enterTwitter(String twitter) {
        clearAndType(twitterInput, twitter);
        return this;
    }
    
    /**
     * Fill in LinkedIn URL
     */
    public JobOfferPage enterLinkedin(String linkedin) {
        clearAndType(linkedinInput, linkedin);
        return this;
    }
    
    /**
     * Select confidentiality option by type
     * @param confidentiality "public", "transmitted", or "private"
     */
    public JobOfferPage selectConfidentiality(String confidentiality) {
        switch (confidentiality.toLowerCase()) {
            case "public":
            case "mes coordonnées sont publiques":
                click(confidentialityPublic);
                break;
            case "transmitted":
            case "mes coordonnées sont transmises aux candidats":
                click(confidentialityTransmitted);
                break;
            case "private":
            case "mes coordonnées sont privées":
                click(confidentialityPrivate);
                break;
            default:
                click(confidentialityPublic); // Default to public
        }
        return this;
    }
    
    /**
     * Submit the form
     */
    public JobOfferPage submitForm() {
        click(submitButton);
        return this;
    }
    
    /**
     * Fill all required fields for job creation
     */
    public JobOfferPage fillJobCreationForm(
            String title,
            String content,
            String email,
            String phone,
            String type,
            String city,
            String domain,
            String companyName,
            String companyCode,
            String nbrDePostes,
            String formation,
            String experienceLevel,
            String facebook,
            String twitter,
            String linkedin,
            String confidentiality) {
        
        enterTitle(title);
        selectDomain(domain);
        enterType(type);
        selectCity(city);
        enterNbrDePostes(nbrDePostes);
        selectFormation(formation);
        selectExperienceLevel(experienceLevel);
        enterContent(content);
        enterFacebook(facebook);
        enterTwitter(twitter);
        enterLinkedin(linkedin);
        enterCompanyCode(companyCode);
        enterCompanyName(companyName);
        enterEmail(email);
        enterPhone(phone);
        selectConfidentiality(confidentiality);
        
        return this;
    }
    
    /**
     * Check if success message is displayed
     */
    public boolean isSuccessMessageDisplayed() {
        try {
            return waitForVisibility(successMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        try {
            return waitForVisibility(errorMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get the success message text
     */
    public String getSuccessMessage() {
        return waitForVisibility(successMessage).getText();
    }
    
    /**
     * Get the error message text
     */
    public String getErrorMessage() {
        return waitForVisibility(errorMessage).getText();
    }
    
    /**
     * Get the secret code after job creation
     */
    public String getSecretCode() {
        return waitForVisibility(secretCodeDisplay).getText();
    }
    
    /**
     * Check if we are on the job creation page
     */
    public boolean isOnJobCreationPage() {
        return driver.getCurrentUrl().contains("/ajouter-offre-emploi");
    }
}
