package com.centoria.jobmaroc.test;

import com.centoria.jobmaroc.dao.ICityDao;
import com.centoria.jobmaroc.dao.impl.CityDao;
import com.centoria.jobmaroc.model.City;
import com.centoria.jobmaroc.test.integration.MongoDbIntegrationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CityDaoTest extends MongoDbIntegrationTestBase {

    private ICityDao cityDao;

    @BeforeEach
    public void setUp() {
        cityDao = CityDao.getInstance();
        assertNotNull(cityDao);
    }

    @Test
    public void testCreateReadUpdateDelete() {
        // 1. Create
        City newCity = new City();
        newCity.setName("Test City");
        newCity.setPays("Test Country");
        newCity.setCodePays("TC");
        newCity.setSlug("test-city");
        newCity.setPopulation(1000000);

        String id = cityDao.addOrUpdate(newCity);
        assertNotNull(id, "ID should not be null after creation");
        newCity.setKey(id);

        // 2. Read
        City retrievedCity = cityDao.find(id);

        assertNotNull(retrievedCity, "City should be found");
        assertEquals("Test City", retrievedCity.getName(), "Name should match");
        assertEquals("Test Country", retrievedCity.getPays(), "Pays should match");
        assertEquals("TC", retrievedCity.getCodePays(), "CodePays should match");
        assertEquals("test-city", retrievedCity.getSlug(), "Slug should match");
        assertEquals(1000000, retrievedCity.getPopulation(), "Population should match");

        // 3. Update
        retrievedCity.setName("Updated Test City");
        retrievedCity.setPopulation(1500000);
        cityDao.addOrUpdate(retrievedCity);

        City updatedCity = cityDao.find(id);
        assertEquals("Updated Test City", updatedCity.getName(), "Name should be updated");
        assertEquals(1500000, updatedCity.getPopulation(), "Population should be updated");

        // 4. Delete
        cityDao.delete(updatedCity);
        City deletedCity = cityDao.find(id);
        assertNull(deletedCity, "City should be deleted");
    }

    @Test
    public void testFindWithQueryProjectionSortPagination() {
        // Create some cities for testing
        City city1 = new City();
        city1.setName("City A");
        city1.setPays("Country X");
        cityDao.addOrUpdate(city1);

        City city2 = new City();
        city2.setName("City B");
        city2.setPays("Country Y");
        cityDao.addOrUpdate(city2);

        // Test find with query, projection, sort, and pagination
        String query = "{ 'pays': 'Country X' }";
        String projection = "{ 'name': 1 }";
        String sort = "{ 'name': 1 }";
        int pageNumber = 1;
        int pageSize = 10;

        List<City> results = cityDao.find(query, projection, sort, pageNumber, pageSize);

        assertNotNull(results, "Results should not be null");
        assertTrue(results.size() <= pageSize, "Result size should be less than or equal to pageSize");
        assertEquals("City A", results.get(0).getName(), "Name should match");
        assertNull(results.get(0).getPays(), "Pays should be null due to projection");

        // Clean up
        cityDao.delete(city1);
        cityDao.delete(city2);
    }

    @Test
    public void testSearch() {
        // 1. Create a city
        City city1 = new City();
        city1.setName("City X");
        city1.setPays("Country Y");
        city1.setCodePays("CY");
        city1.setSlug("city-x");


        String id = cityDao.addOrUpdate(city1);
        assertNotNull(id, "ID should not be null after creation");
        city1.setKey(id);

        // 2. Search
        String searchQuery = "{ $match: { 'name': 'City X' } }";
        List<City> searchResults = cityDao.search(searchQuery);

        // 3. Assert
        assertNotNull(searchResults, "Search results should not be null");
        assertEquals(1, searchResults.size(), "Should find one city");
        assertEquals("City X", searchResults.get(0).getName(), "Name should match");
        assertEquals("Country Y", searchResults.get(0).getPays(), "Pays should match");
        assertEquals("CY", searchResults.get(0).getCodePays(), "CodePays should match");
        assertEquals("city-x", searchResults.get(0).getSlug(), "Slug should match");

        // Clean up
        cityDao.delete(city1);
    }

    @Test
    public void testAddOrUpdateMultiple() {
        // Create some cities for testing
        City city1 = new City();
        city1.setName("City D");
        city1.setPays("Country W");

        City city2 = new City();
        city2.setName("City E");
        city2.setPays("Country V");

        List<City> citiesToAdd = new ArrayList<>();
        citiesToAdd.add(city1);
        citiesToAdd.add(city2);

        List<String> ids = cityDao.addOrUpdate(citiesToAdd);
        assertNotNull(ids, "IDs should not be null");
        assertEquals(2, ids.size(), "Should have two IDs");

        // Verify that the cities were added
        City retrievedCity1 = cityDao.find(ids.get(0));
        assertNotNull(retrievedCity1, "City 1 should be found");

        City retrievedCity2 = cityDao.find(ids.get(1));
        assertNotNull(retrievedCity2, "City 2 should be found");

        // Clean up
        cityDao.delete(city1);
        cityDao.delete(city2);
    }

    @Test
    public void testDeleteById() {
        // Create a city for testing
        City cityToDelete = new City();
        cityToDelete.setName("City G");
        cityToDelete.setPays("Country T");

        String id = cityDao.addOrUpdate(cityToDelete);
        cityToDelete.setKey(id);

        // Delete the city using the ID
        cityDao.delete(id);

        // Verify that the city was deleted
        City deletedCity = cityDao.find(id);
        assertNull(deletedCity, "City should be deleted");
    }

    @Test
    public void testDeleteMultiple() {
        // Create some cities for testing
        City city1 = new City();
        city1.setName("City H");
        city1.setPays("Country S");

        City city2 = new City();
        city2.setName("City I");
        city2.setPays("Country R");

        String id1 = cityDao.addOrUpdate(city1);
        city1.setKey(id1);
        String id2 = cityDao.addOrUpdate(city2);
        city2.setKey(id2);

        List<City> citiesToDelete = new ArrayList<>();
        citiesToDelete.add(city1);
        citiesToDelete.add(city2);

        // Delete the cities
        cityDao.delete(citiesToDelete);

        // Verify that the cities were deleted
        City deletedCity1 = cityDao.find(id1);
        assertNull(deletedCity1, "City 1 should be deleted");

        City deletedCity2 = cityDao.find(id2);
        assertNull(deletedCity2, "City 2 should be deleted");
    }

    @Test
    public void testCount() {
        // Create some cities for testing
        City city1 = new City();
        city1.setName("City J");
        city1.setPays("Country Q");
        String id = cityDao.addOrUpdate(city1);

        // Test count
        long count = cityDao.count("{ 'pays': 'Country Q' }");
        assertEquals(1, count, "Count should be 1");

        // Clean up
        cityDao.delete(city1);
    }
}
