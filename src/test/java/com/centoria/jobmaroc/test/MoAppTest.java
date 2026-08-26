package com.centoria.jobmaroc.test;

import com.centoria.jobmaroc.app.MoApp;
import com.centoria.jobmaroc.model.exception.BusinessException;
import com.centoria.jobmaroc.model.exception.TechnicalException;
import com.centoria.jobmaroc.dto.AdDto;
import com.centoria.jobmaroc.dto.AdWithReferenceDataDto;
import com.centoria.jobmaroc.dto.InputSearchAdDTO;
import com.centoria.jobmaroc.model.Ad;
import com.centoria.jobmaroc.service.IAdService;
import com.centoria.jobmaroc.service.impl.AdService;
import com.centoria.jobmaroc.test.integration.MongoDbIntegrationTestBase;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MoApp focusing on job creation and update functionality.
 */
public class MoAppTest extends MongoDbIntegrationTestBase {

    private MoApp moApp;
    private IAdService adService;

    @BeforeEach
    public void setUp() {
        moApp = MoApp.getInstance();
        adService = AdService.getInstance();
        assertNotNull(moApp, "MoApp should not be null");
        assertNotNull(adService, "AdService should not be null");
    }

    /**
     * Test job creation - when key is null, it should create a new job.
     */
    @Test
    public void testCreateJob() throws BusinessException, TechnicalException {
        // Arrange: Create a new job DTO without key (creation scenario)
        AdWithReferenceDataDto jobDto = AdWithReferenceDataDto.builder()
                .siteUrl("http://localhost:8081")
                .build();

        AdDto job = new AdDto();
        job.setTitle("Test Job Creation");
        job.setContent("Test content for job creation");
        String email = "test@example.com";
        job.setEmail(email);
        // Generate secretCode from email like fillFromParams does
        job.setSecretCode(DigestUtils.md5Hex(email.toLowerCase().replaceAll(" ", "")));
        job.setPhone("0612345678");
        job.setType("CDI");
        job.setCity("casablanca"); // slug
        job.setDomain("informatique"); // slug
        job.setCompanyName("Test Company");
        job.setCompanyCode("123456");
        job.setNbrDePostes(1);
        job.setFormation("bac+3");
        job.setExperienceLevel("De 1 à 3 ans");
        job.setConfidentiality("Mes coordonnées sont publiques");
        job.setFacebook("https://facebook.com/test");
        job.setTwitter("https://twitter.com/test");
        job.setLinkedin("https://linkedin.com/test");
        // key is null - this is a creation
        job.setKey(null);

        jobDto.setJob(job);

        // Act: Create the job
        AdWithReferenceDataDto result = moApp.addOrUpdate(jobDto);

        // Assert: Job should be created successfully
        assertNotNull(result, "Result should not be null");
        assertTrue(result.getErrors().isEmpty(), "No errors should be present");
        assertNotNull(result.getJob().getKey(), "Job should have a key after creation");
        assertNotNull(result.getJob().getSecretCode(), "Job should have a secretCode after creation");
        assertEquals("Test Job Creation", result.getJob().getTitle(), "Title should match");
        assertEquals(Ad.NEW, result.getJob().getState(), "State should be NEW for creation");
        assertEquals(Ad.NEW_RANK, result.getJob().getStateRank(), "State rank should be NEW_RANK");

        // Clean up: Delete the created job
        if (result.getJob().getKey() != null) {
            adService.delete(result.getJob().getKey());
        }
    }

    /**
     * Test job update - when key exists, it should update an existing job.
     */
    @Test
    public void testUpdateJob() throws BusinessException, TechnicalException {
        // Arrange: First create a job
        AdWithReferenceDataDto createDto = AdWithReferenceDataDto.builder()
                .siteUrl("http://localhost:8081")
                .build();

        AdDto createJob = new AdDto();
        createJob.setTitle("Original Title");
        createJob.setContent("Original content");
        createJob.setEmail("update@example.com");
        createJob.setPhone("0612345678");
        createJob.setType("CDI");
        createJob.setCity("casablanca");
        createJob.setDomain("informatique");
        createJob.setCompanyName("Original Company");
        createJob.setCompanyCode("123456");
        createJob.setNbrDePostes(1);
        createJob.setFormation("bac+3");
        createJob.setExperienceLevel("De 1 à 3 ans");
        createJob.setConfidentiality("Mes coordonnées sont publiques");
        createJob.setFacebook("https://facebook.com/original");
        createJob.setTwitter("https://twitter.com/original");
        createJob.setLinkedin("https://linkedin.com/original");
        createJob.setKey(null); // Creation

        createDto.setJob(createJob);

        // Create the job
        AdWithReferenceDataDto createdResult = moApp.addOrUpdate(createDto);
        assertNotNull(createdResult.getJob().getKey(), "Job should be created");
        String jobKey = createdResult.getJob().getKey();
        String originalEmail = createdResult.getJob().getEmail();
        int originalAnnounceType = createdResult.getJob().getAnnouncetype();

        // Now update the job
        AdWithReferenceDataDto updateDto = AdWithReferenceDataDto.builder()
                .siteUrl("http://localhost:8081")
                .build();

        AdDto updateJob = new AdDto();
        updateJob.setKey(jobKey); // Set key for update
        updateJob.setTitle("Updated Title");
        updateJob.setContent("Updated content");
        updateJob.setEmail(originalEmail); // Must match original email
        updateJob.setPhone("0698765432");
        updateJob.setType("CDD");
        updateJob.setCity("rabat");
        updateJob.setDomain("marketing");
        updateJob.setCompanyName("Updated Company");
        updateJob.setCompanyCode("654321");
        updateJob.setNbrDePostes(2);
        updateJob.setFormation("bac+5");
        updateJob.setExperienceLevel("De 3 à 5 ans");
        updateJob.setConfidentiality("Mes coordonnées sont transmises aux candidats");
        updateJob.setFacebook("https://facebook.com/updated");
        updateJob.setTwitter("https://twitter.com/updated");
        updateJob.setLinkedin("https://linkedin.com/updated");
        updateJob.setAnnouncetype(originalAnnounceType); // Must match original

        updateDto.setJob(updateJob);

        // Act: Update the job
        AdWithReferenceDataDto updateResult = moApp.addOrUpdate(updateDto);

        // Assert: Job should be updated successfully
        assertNotNull(updateResult, "Update result should not be null");
        assertTrue(updateResult.getErrors().isEmpty(), "No errors should be present");
        assertEquals(jobKey, updateResult.getJob().getKey(), "Key should remain the same");
        assertEquals("Updated Title", updateResult.getJob().getTitle(), "Title should be updated");
        assertEquals("Updated content", updateResult.getJob().getContent(), "Content should be updated");
        assertEquals(Ad.UPDATED_VERIFIED, updateResult.getJob().getState(), "State should be UPDATED_VERIFIED");
        assertEquals(Ad.UPDATED_VERIFIED_RANK, updateResult.getJob().getStateRank(), "State rank should be UPDATED_VERIFIED_RANK");

        // Clean up: Delete the job
        adService.delete(jobKey);
    }

    /**
     * Test getWithReferenceData - Scenario 1: Creation (no ID provided).
     */
    @Test
    public void testGetWithReferenceData_CreationScenario() throws BusinessException, TechnicalException {
        // Arrange: No ID - creation scenario (new offer form)
        InputSearchAdDTO inputDto = InputSearchAdDTO.builder()
                .id(null) // No ID - creation scenario
                .withCities(true)
                .withCategories(true)
                .build();

        // Act
        AdWithReferenceDataDto result = moApp.getWithReferenceData(inputDto);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertNotNull(result.getJob(), "Job DTO should not be null (@Builder.Default creates empty AdDto)");
        assertNull(result.getJob().getKey(), "Job key should be null for creation scenario");
        assertNotNull(result.getCities(), "Cities should be loaded");
        assertNotNull(result.getDomains(), "Domains should be loaded");
        assertFalse(result.getCities().isEmpty(), "Cities list should not be empty");
        assertFalse(result.getDomains().isEmpty(), "Domains list should not be empty");
    }

    /**
     * Test getWithReferenceData - Scenario 2: Update (ID provided).
     */
    @Test
    public void testGetWithReferenceData_UpdateScenario() throws BusinessException, TechnicalException {
        // Arrange: First create a job
        AdWithReferenceDataDto createDto = AdWithReferenceDataDto.builder()
                .siteUrl("http://localhost:8081")
                .build();

        AdDto createJob = new AdDto();
        createJob.setTitle("Test Job for Update Scenario");
        createJob.setContent("Test content");
        createJob.setEmail("updatescenario@example.com");
        createJob.setPhone("0612345678");
        createJob.setType("CDI");
        createJob.setCity("casablanca");
        createJob.setDomain("informatique");
        createJob.setCompanyName("Test Company");
        createJob.setCompanyCode("123456");
        createJob.setNbrDePostes(1);
        createJob.setFormation("bac+3");
        createJob.setExperienceLevel("De 1 à 3 ans");
        createJob.setConfidentiality("Mes coordonnées sont publiques");
        createJob.setFacebook("https://facebook.com/test");
        createJob.setTwitter("https://twitter.com/test");
        createJob.setLinkedin("https://linkedin.com/test");
        createJob.setKey(null);

        createDto.setJob(createJob);
        AdWithReferenceDataDto createdResult = moApp.addOrUpdate(createDto);
        String jobKey = createdResult.getJob().getKey();
        assertNotNull(jobKey, "Job should be created");

        // Build inputDto with ID (method fetches Ad by ID using service)
        InputSearchAdDTO inputDto = InputSearchAdDTO.builder()
                .id(jobKey)
                .withCities(true)
                .withCategories(true)
                .build();

        // Act: Get with reference data (ID provided - update scenario)
        AdWithReferenceDataDto result = moApp.getWithReferenceData(inputDto);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertNotNull(result.getJob(), "Job DTO should not be null");
        assertEquals(jobKey, result.getJob().getKey(), "Job key should match");
        assertEquals("Test Job for Update Scenario", result.getJob().getTitle(), "Title should match");
        assertNotNull(result.getCities(), "Cities should be loaded");
        assertNotNull(result.getDomains(), "Domains should be loaded");
        // City should be converted from slug to name for form display
        assertNotNull(result.getJob().getCity(), "City should be set");

        // Clean up
        adService.delete(jobKey);
    }

    /**
     * Test getWithReferenceData - Fetch Ad by ID (duplicate of UpdateScenario, kept for clarity).
     */
    @Test
    public void testGetWithReferenceData_FetchById() throws BusinessException, TechnicalException {
        // Arrange: First create a job
        AdWithReferenceDataDto createDto = AdWithReferenceDataDto.builder()
                .siteUrl("http://localhost:8081")
                .build();

        AdDto createJob = new AdDto();
        createJob.setTitle("Test Job for Fetch by ID");
        createJob.setContent("Test content");
        createJob.setEmail("fetchbyid@example.com");
        createJob.setPhone("0612345678");
        createJob.setType("CDI");
        createJob.setCity("casablanca");
        createJob.setDomain("informatique");
        createJob.setCompanyName("Test Company");
        createJob.setCompanyCode("123456");
        createJob.setNbrDePostes(1);
        createJob.setFormation("bac+3");
        createJob.setExperienceLevel("De 1 à 3 ans");
        createJob.setConfidentiality("Mes coordonnées sont publiques");
        createJob.setFacebook("https://facebook.com/test");
        createJob.setTwitter("https://twitter.com/test");
        createJob.setLinkedin("https://linkedin.com/test");
        createJob.setKey(null);

        createDto.setJob(createJob);
        AdWithReferenceDataDto createdResult = moApp.addOrUpdate(createDto);
        String jobKey = createdResult.getJob().getKey();
        assertNotNull(jobKey, "Job should be created");

        InputSearchAdDTO inputDto = InputSearchAdDTO.builder()
                .id(jobKey)
                .withCities(true)
                .withCategories(true)
                .build();

        // Act: Get with reference data (fetch Ad by ID)
        AdWithReferenceDataDto result = moApp.getWithReferenceData(inputDto);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertNotNull(result.getJob(), "Job DTO should not be null");
        assertEquals(jobKey, result.getJob().getKey(), "Job key should match");
        assertEquals("Test Job for Fetch by ID", result.getJob().getTitle(), "Title should match");
        assertNotNull(result.getCities(), "Cities should be loaded");
        assertNotNull(result.getDomains(), "Domains should be loaded");

        // Clean up
        adService.delete(jobKey);
    }

    /**
     * Test getWithReferenceData - Only reference data (no ID provided).
     */
    @Test
    public void testGetWithReferenceData_ReferenceDataOnly() throws BusinessException, TechnicalException {
        // Arrange: No ID - only reference data
        InputSearchAdDTO inputDto = InputSearchAdDTO.builder()
                .id(null)
                .withCities(true)
                .withCategories(true)
                .build();

        // Act: Get reference data only
        AdWithReferenceDataDto result = moApp.getWithReferenceData(inputDto);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertNotNull(result.getJob(), "Job should not be null (@Builder.Default creates empty AdDto)");
        assertNull(result.getJob().getKey(), "Job key should be null for reference data only scenario");
        assertNotNull(result.getCities(), "Cities should be loaded");
        assertNotNull(result.getDomains(), "Domains should be loaded");
        assertFalse(result.getCities().isEmpty(), "Cities list should not be empty");
        assertFalse(result.getDomains().isEmpty(), "Domains list should not be empty");
    }

    /**
     * Test update job with invalid email (should fail validation).
     */
    @Test
    public void testUpdateJob_InvalidEmail() throws BusinessException, TechnicalException {
        // Arrange: First create a job
        AdWithReferenceDataDto createDto = AdWithReferenceDataDto.builder()
                .siteUrl("http://localhost:8081")
                .build();

        AdDto createJob = new AdDto();
        createJob.setTitle("Test Job");
        createJob.setContent("Test content");
        createJob.setEmail("valid@example.com");
        createJob.setPhone("0612345678");
        createJob.setType("CDI");
        createJob.setCity("casablanca");
        createJob.setDomain("informatique");
        createJob.setCompanyName("Test Company");
        createJob.setCompanyCode("123456");
        createJob.setNbrDePostes(1);
        createJob.setFormation("bac+3");
        createJob.setExperienceLevel("De 1 à 3 ans");
        createJob.setConfidentiality("Mes coordonnées sont publiques");
        createJob.setKey(null);

        createDto.setJob(createJob);
        AdWithReferenceDataDto createdResult = moApp.addOrUpdate(createDto);
        String jobKey = createdResult.getJob().getKey();
        String originalEmail = createdResult.getJob().getEmail();
        int originalAnnounceType = createdResult.getJob().getAnnouncetype();

        // Try to update with invalid email
        AdWithReferenceDataDto updateDto = AdWithReferenceDataDto.builder()
                .siteUrl("http://localhost:8081")
                .build();

        AdDto updateJob = new AdDto();
        updateJob.setKey(jobKey);
        updateJob.setTitle("Updated Title");
        updateJob.setContent("Updated content");
        updateJob.setEmail("invalid-email"); // Invalid email
        updateJob.setPhone("0612345678");
        updateJob.setType("CDI");
        updateJob.setCity("casablanca");
        updateJob.setDomain("informatique");
        updateJob.setCompanyName("Test Company");
        updateJob.setCompanyCode("123456");
        updateJob.setNbrDePostes(1);
        updateJob.setFormation("bac+3");
        updateJob.setExperienceLevel("De 1 à 3 ans");
        updateJob.setConfidentiality("Mes coordonnées sont publiques");
        updateJob.setAnnouncetype(originalAnnounceType);

        updateDto.setJob(updateJob);

        // Act: Try to update with invalid email
        AdWithReferenceDataDto updateResult = moApp.addOrUpdate(updateDto);

        // Assert: Should have validation errors
        assertNotNull(updateResult, "Result should not be null");
        assertFalse(updateResult.getErrors().isEmpty(), "Should have validation errors");
        assertTrue(updateResult.getErrors().containsKey("email"), "Should have email validation error");

        // Clean up
        adService.delete(jobKey);
    }

    /**
     * Test update job with different email (should fail security check).
     */
    @Test
    public void testUpdateJob_DifferentEmail() throws BusinessException, TechnicalException {
        // Arrange: First create a job
        AdWithReferenceDataDto createDto = AdWithReferenceDataDto.builder()
                .siteUrl("http://localhost:8081")
                .build();

        AdDto createJob = new AdDto();
        createJob.setTitle("Test Job");
        createJob.setContent("Test content");
        createJob.setEmail("original@example.com");
        createJob.setPhone("0612345678");
        createJob.setType("CDI");
        createJob.setCity("casablanca");
        createJob.setDomain("informatique");
        createJob.setCompanyName("Test Company");
        createJob.setCompanyCode("123456");
        createJob.setNbrDePostes(1);
        createJob.setFormation("bac+3");
        createJob.setExperienceLevel("De 1 à 3 ans");
        createJob.setConfidentiality("Mes coordonnées sont publiques");
        createJob.setKey(null);

        createDto.setJob(createJob);
        AdWithReferenceDataDto createdResult = moApp.addOrUpdate(createDto);
        String jobKey = createdResult.getJob().getKey();
        int originalAnnounceType = createdResult.getJob().getAnnouncetype();

        // Try to update with different email
        AdWithReferenceDataDto updateDto = AdWithReferenceDataDto.builder()
                .siteUrl("http://localhost:8081")
                .build();

        AdDto updateJob = new AdDto();
        updateJob.setKey(jobKey);
        updateJob.setTitle("Updated Title");
        updateJob.setContent("Updated content");
        updateJob.setEmail("different@example.com"); // Different email
        updateJob.setPhone("0612345678");
        updateJob.setType("CDI");
        updateJob.setCity("casablanca");
        updateJob.setDomain("informatique");
        updateJob.setCompanyName("Test Company");
        updateJob.setCompanyCode("123456");
        updateJob.setNbrDePostes(1);
        updateJob.setFormation("bac+3");
        updateJob.setExperienceLevel("De 1 à 3 ans");
        updateJob.setConfidentiality("Mes coordonnées sont publiques");
        updateJob.setAnnouncetype(originalAnnounceType);

        updateDto.setJob(updateJob);

        // Act: Try to update with different email
        AdWithReferenceDataDto updateResult = moApp.addOrUpdate(updateDto);

        // Assert: Should have error (security check failed)
        assertNotNull(updateResult, "Result should not be null");
        assertFalse(updateResult.getErrors().isEmpty(), "Should have errors");
        assertTrue(updateResult.getErrors().containsKey("message"), "Should have error message");

        // Clean up
        adService.delete(jobKey);
    }
}
