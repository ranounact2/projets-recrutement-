package com.centoria.jobmaroc.test;

import com.centoria.jobmaroc.dao.IAdDao;
import com.centoria.jobmaroc.dao.impl.AdDao;
import com.centoria.jobmaroc.model.Ad;
import com.centoria.jobmaroc.test.integration.MongoDbIntegrationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AdDaoTest extends MongoDbIntegrationTestBase {

    private IAdDao adDao;

    @BeforeEach
    public void setUp() {
        adDao = AdDao.getInstance();
        assertNotNull(adDao);
    }

    @Test
    public void testCreateReadUpdateDelete() {
        // 1. Create
        Ad newAd = new Ad();
        newAd.setTitle("Test Ad Title");
        newAd.setContent("Test Ad Content");
        newAd.setType("Test Type");

        String id = adDao.addOrUpdate(newAd);
        assertNotNull(id, "ID should not be null after creation");
        newAd.setKey(id);

        // 2. Read
        Ad retrievedAd = adDao.find(id);

        assertNotNull(retrievedAd, "Ad should be found");
        assertEquals("Test Ad Title", retrievedAd.getTitle(), "Title should match");
        assertEquals("Test Ad Content", retrievedAd.getContent(), "Content should match");
        assertEquals("Test Type", retrievedAd.getType(), "Type should match");

        // 3. Update
        retrievedAd.setTitle("Updated Ad Title");
        retrievedAd.setContent("Updated Ad Content");
        adDao.addOrUpdate(retrievedAd);

        Ad updatedAd = adDao.find(id);
        assertEquals("Updated Ad Title", updatedAd.getTitle(), "Title should be updated");
        assertEquals("Updated Ad Content", updatedAd.getContent(), "Content should be updated");

        // 4. Delete
        adDao.delete(updatedAd);
        Ad deletedAd = adDao.find(id);
        assertNull(deletedAd, "Ad should be deleted");
    }

    @Test
    public void testFindWithQueryProjectionSortPagination() {
        // Create some cities for testing
        Ad ad1 = new Ad();
        ad1.setTitle("Title A");
        ad1.setCompanyName("Company X");
        adDao.addOrUpdate(ad1);

        Ad ad2 = new Ad();
        ad2.setTitle("Title B");
        ad2.setCompanyName("Company Y");
        adDao.addOrUpdate(ad2);

        // Test find with query, projection, sort, and pagination
        String query = "{ 'companyName': 'Company X' }";
        String projection = "{ 'title': 1 }";
        String sort = "{ 'title': 1 }";
        int pageNumber = 1;
        int pageSize = 10;

        List<Ad> results = adDao.find(query, projection, sort, pageNumber, pageSize);

        assertNotNull(results, "Results should not be null");
        assertTrue(results.size() <= pageSize, "Result size should be less than or equal to pageSize");
        assertEquals("Title A", results.get(0).getTitle(), "Title should match");
        assertNull(results.get(0).getCompanyName(), "CompanyName should be null due to projection");

        // Clean up
        adDao.delete(ad1);
        adDao.delete(ad2);
    }

    @Test
    public void testSearch() {
        // 1. Create a city
        Ad ad1 = new Ad();
        ad1.setTitle("Title Search");
        ad1.setContent("This is content for searching");
        adDao.addOrUpdate(ad1);

        // 2. Search
        String searchQuery = "{ $match: { 'title': 'Title Search' } }";
        List<Ad> searchResults = adDao.search(searchQuery);

        // 3. Assert
        assertNotNull(searchResults, "Search results should not be null");
        assertEquals(1, searchResults.size(), "Should find one city");
        assertEquals("Title Search", searchResults.get(0).getTitle(), "Title should match");
        assertEquals("This is content for searching", searchResults.get(0).getContent(), "Content should match");

        // Clean up
        adDao.delete(ad1);
    }

    @Test
    public void testAddOrUpdateMultiple() {
        // Create some Ads for testing
        Ad ad1 = new Ad();
        ad1.setTitle("Title D");
        ad1.setCompanyName("Company W");

        Ad ad2 = new Ad();
        ad2.setTitle("Title E");
        ad2.setCompanyName("Company V");

        List<Ad> adsToAdd = new ArrayList<>();
        adsToAdd.add(ad1);
        adsToAdd.add(ad2);

        List<String> ids = adDao.addOrUpdate(adsToAdd);
        assertNotNull(ids, "IDs should not be null");
        assertEquals(2, ids.size(), "Should have two IDs");

        // Verify that the ads were added
        Ad retrievedAd1 = adDao.find(ids.get(0));
        assertNotNull(retrievedAd1, "Ad 1 should be found");

        Ad retrievedAd2 = adDao.find(ids.get(1));
        assertNotNull(retrievedAd2, "Ad 2 should be found");

        // Clean up
        adDao.delete(ad1);
        adDao.delete(ad2);
    }

    @Test
    public void testDeleteById() {
        // Create an Ad for testing
        Ad adToDelete = new Ad();
        adToDelete.setTitle("Title G");
        adToDelete.setCompanyName("Company T");

        String id = adDao.addOrUpdate(adToDelete);
        adToDelete.setKey(id);

        // Delete the city using the ID
        adDao.delete(id);

        // Verify that the city was deleted
        Ad deletedAd = adDao.find(id);
        assertNull(deletedAd, "City should be deleted");
    }

    @Test
    public void testDeleteMultiple() {
        // Create some cities for testing
        Ad ad1 = new Ad();
        ad1.setTitle("Title H");
        ad1.setCompanyName("Company S");

        Ad ad2 = new Ad();
        ad2.setTitle("Title I");
        ad2.setCompanyName("Company R");

        String id1 = adDao.addOrUpdate(ad1);
        ad1.setKey(id1);
        String id2 = adDao.addOrUpdate(ad2);
        ad2.setKey(id2);

        List<Ad> adsToDelete = new ArrayList<>();
        adsToDelete.add(ad1);
        adsToDelete.add(ad2);

        // Delete the cities
        adDao.delete(adsToDelete);

        // Verify that the cities were deleted
        Ad deletedAd1 = adDao.find(id1);
        assertNull(deletedAd1, "City 1 should be deleted");

        Ad deletedAd2 = adDao.find(id2);
        assertNull(deletedAd2, "City 2 should be deleted");
    }

    @Test
    public void testCount() {
        // Create some Ads for testing
        Ad ad1 = new Ad();
        ad1.setTitle("Title J");
        ad1.setCompanyName("Company Q");
        String id = adDao.addOrUpdate(ad1);

        // Test count
        long count = adDao.count("{ 'companyName': 'Company Q' }");
        assertEquals(1, count, "Count should be 1");

        // Clean up
        adDao.delete(ad1);
    }
}
