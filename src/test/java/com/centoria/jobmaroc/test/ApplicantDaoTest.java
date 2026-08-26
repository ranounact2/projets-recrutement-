package com.centoria.jobmaroc.test;

import com.centoria.jobmaroc.dao.IApplicantDao;
import com.centoria.jobmaroc.dao.impl.ApplicantDao;
import com.centoria.jobmaroc.dto.ApplicantDto;
import com.centoria.jobmaroc.model.Applicant;
import com.centoria.jobmaroc.test.integration.MongoDbIntegrationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ApplicantDaoTest extends MongoDbIntegrationTestBase {

    private IApplicantDao applicantDao;

    @BeforeEach
    public void setUp() {
        applicantDao = ApplicantDao.getInstance();
        assertNotNull(applicantDao);
    }

    @Test
    public void testCreateReadUpdateDelete() {
        // 1. Create
        ApplicantDto newApplicant = new ApplicantDto();
        newApplicant.setNom("Test Nom");
        newApplicant.setPrenom("Test Prenom");
        newApplicant.setEmail("test@example.com");
        newApplicant.setPhone("1234567890");
        newApplicant.setMotivation("Test Motivation");
        newApplicant.setExperienceLevel("Junior");
        newApplicant.setFormation("Master");

        String id = applicantDao.addOrUpdate(newApplicant);
        assertNotNull(id, "ID should not be null after creation");
        newApplicant.setKey(id);

        // 2. Read
        Applicant retrievedApplicant = applicantDao.find(id);

        assertNotNull(retrievedApplicant, "Applicant should be found");
        assertEquals("Test Nom", retrievedApplicant.getNom(), "Nom should match");
        assertEquals("Test Prenom", retrievedApplicant.getPrenom(), "Prenom should match");
        assertEquals("test@example.com", retrievedApplicant.getEmail(), "Email should match");
        assertEquals("1234567890", retrievedApplicant.getPhone(), "Phone should match");
        assertEquals("Test Motivation", retrievedApplicant.getMotivation(), "Motivation should match");
        assertEquals("Junior", retrievedApplicant.getExperienceLevel(), "ExperienceLevel should match");
        assertEquals("Master", retrievedApplicant.getFormation(), "Formation should match");

        // 3. Update
        retrievedApplicant.setMotivation("Updated Motivation");
        applicantDao.addOrUpdate(retrievedApplicant);

        Applicant updatedApplicant = applicantDao.find(id);
        assertEquals("Updated Motivation", updatedApplicant.getMotivation(), "Motivation should be updated");

        // 4. Delete
        applicantDao.delete(updatedApplicant);
        Applicant deletedApplicant = applicantDao.find(id);
        assertNull(deletedApplicant, "Applicant should be deleted");
    }

    @Test
    public void testFindWithQueryProjectionSortPagination() {
        // Create some applicants for testing
        ApplicantDto applicant1 = new ApplicantDto();
        applicant1.setNom("Nom A");
        applicant1.setEmail("a@example.com");
        applicantDao.addOrUpdate(applicant1);

        ApplicantDto applicant2 = new ApplicantDto();
        applicant2.setNom("Nom B");
        applicant2.setEmail("b@example.com");
        applicantDao.addOrUpdate(applicant2);

        // Test find with query, projection, sort, and pagination
        String query = "{ 'email': 'a@example.com' }";
        String projection = "{ 'nom': 1 }";
        String sort = "{ 'nom': 1 }";
        int pageNumber = 1;
        int pageSize = 10;

        List<Applicant> results = applicantDao.find(query, projection, sort, pageNumber, pageSize);

        assertNotNull(results, "Results should not be null");
        assertTrue(results.size() <= pageSize, "Result size should be less than or equal to pageSize");
        assertEquals("Nom A", results.get(0).getNom(), "Nom should match");
        assertNull(results.get(0).getEmail(), "Email should be null due to projection");

        // Clean up
        applicantDao.delete(applicant1);
        applicantDao.delete(applicant2);
    }

    @Test
    public void testSearch() {
        // Create an applicant for testing
        ApplicantDto applicant1 = new ApplicantDto();
        applicant1.setNom("Nom Search");
        applicant1.setMotivation("This is a search test");
        applicantDao.addOrUpdate(applicant1);

        // Test search
        String searchQuery = "{ $match: { 'nom': 'Nom Search' } }";
        List<Applicant> searchResults = applicantDao.search(searchQuery);

        assertNotNull(searchResults, "Search results should not be null");
        assertEquals(1, searchResults.size(), "Should find one applicant");
        assertEquals("Nom Search", searchResults.get(0).getNom(), "Nom should match");
        assertEquals("This is a search test", searchResults.get(0).getMotivation(), "Motivation should match");

        // Clean up
        applicantDao.delete(applicant1);
    }

    @Test
    public void testAddOrUpdateMultiple() {
        // Create some applicants for testing
        ApplicantDto applicant1 = new ApplicantDto();
        applicant1.setNom("Nom D");
        applicant1.setEmail("d@example.com");

        ApplicantDto applicant2 = new ApplicantDto();
        applicant2.setNom("Nom E");
        applicant2.setEmail("e@example.com");

        List<Applicant> applicantsToAdd = new ArrayList<>();
        applicantsToAdd.add(applicant1);
        applicantsToAdd.add(applicant2);

        List<String> ids = applicantDao.addOrUpdate(applicantsToAdd);
        assertNotNull(ids, "IDs should not be null");
        assertEquals(2, ids.size(), "Should have two IDs");

        // Verify that the applicants were added
        Applicant retrievedApplicant1 = applicantDao.find(ids.get(0));
        assertNotNull(retrievedApplicant1, "Applicant 1 should be found");

        Applicant retrievedApplicant2 = applicantDao.find(ids.get(1));
        assertNotNull(retrievedApplicant2, "Applicant 2 should be found");

        // Clean up
        applicantDao.delete(applicant1);
        applicantDao.delete(applicant2);
    }

    @Test
    public void testDeleteById() {
        // Create an applicant for testing
        ApplicantDto applicantToDelete = new ApplicantDto();
        applicantToDelete.setNom("Nom G");
        applicantToDelete.setEmail("g@example.com");

        String id = applicantDao.addOrUpdate(applicantToDelete);
        applicantToDelete.setKey(id);

        // Delete the applicant using the ID
        applicantDao.delete(id);

        // Verify that the applicant was deleted
        Applicant deletedApplicant = applicantDao.find(id);
        assertNull(deletedApplicant, "Applicant should be deleted");
    }

    @Test
    public void testDeleteMultiple() {
        // Create some applicants for testing
        ApplicantDto applicant1 = new ApplicantDto();
        applicant1.setNom("Nom H");
        applicant1.setEmail("h@example.com");

        ApplicantDto applicant2 = new ApplicantDto();
        applicant2.setNom("Nom I");
        applicant2.setEmail("i@example.com");

        String id1 = applicantDao.addOrUpdate(applicant1);
        applicant1.setKey(id1);
        String id2 = applicantDao.addOrUpdate(applicant2);
        applicant2.setKey(id2);

        List<Applicant> applicantsToDelete = new ArrayList<>();
        applicantsToDelete.add(applicant1);
        applicantsToDelete.add(applicant2);

        // Delete the cities
        applicantDao.delete(applicantsToDelete);

        // Verify that the cities were deleted
        Applicant deletedApplicant1 = applicantDao.find(id1);
        assertNull(deletedApplicant1, "Applicant 1 should be deleted");

        Applicant deletedApplicant2 = applicantDao.find(id2);
        assertNull(deletedApplicant2, "Applicant 2 should be deleted");
    }

    @Test
    public void testCount() {
        // Create some Applicants for testing
        ApplicantDto applicant1 = new ApplicantDto();
        applicant1.setNom("Nom J");
        applicant1.setEmail("j@example.com");
        String id = applicantDao.addOrUpdate(applicant1);

        // Test count
        long count = applicantDao.count("{ 'email': 'j@example.com' }");
        assertEquals(1, count, "Count should be 1");

        // Clean up
        applicantDao.delete(applicant1);
    }

    @Test
    public void existsByJobIdAndEmailIgnoreCase_detectsDuplicatePerOffer() {
        String jobA = "offer-id-a";
        String jobB = "offer-id-b";
        ApplicantDto first = new ApplicantDto();
        first.setNom("Nom");
        first.setPrenom("Prenom");
        first.setEmail("unique.dup@example.com");
        first.setJobId(jobA);
        String id = applicantDao.addOrUpdate(first);
        first.setKey(id);

        assertTrue(applicantDao.existsByJobIdAndEmailIgnoreCase(jobA, "unique.dup@example.com"));
        assertTrue(applicantDao.existsByJobIdAndEmailIgnoreCase(jobA, "Unique.DUP@example.com"));
        assertFalse(applicantDao.existsByJobIdAndEmailIgnoreCase(jobB, "unique.dup@example.com"));

        applicantDao.delete(first);
    }
}
