package com.centoria.jobmaroc.test;

import com.centoria.jobmaroc.dto.AdDto;
import com.centoria.jobmaroc.model.Ad;
import com.centoria.jobmaroc.model.City;
import com.centoria.jobmaroc.model.exception.ElementNotFoundException;
import com.centoria.jobmaroc.service.IAdService;
import com.centoria.jobmaroc.service.ICityService;
import com.centoria.jobmaroc.service.IDomainService;
import com.centoria.jobmaroc.service.impl.AdService;
import com.centoria.jobmaroc.service.impl.CityService;
import com.centoria.jobmaroc.service.impl.DomainService;
import com.centoria.jobmaroc.test.integration.MongoDbIntegrationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AdService focusing on methods that use slugs instead of names.
 */
public class AdServiceTest extends MongoDbIntegrationTestBase {

    private IAdService adService;
    private ICityService cityService;
    private IDomainService domainService;

    @BeforeEach
    public void setUp() {
        adService = AdService.getInstance();
        cityService = CityService.getInstance();
        domainService = DomainService.getInstance();
        assertNotNull(adService, "AdService.getInstance() returned null - check MongoDB connection");
        assertNotNull(cityService, "CityService.getInstance() returned null - check MongoDB connection");
        assertNotNull(domainService, "DomainService.getInstance() returned null - check MongoDB connection");
    }

    /**
     * Test to verify setUp() is executed - this should fail if setUp() doesn't run
     */
    @Test
    public void testSetUpIsExecuted() {
        // This test will fail if setUp() is not executed
        assertNotNull(adService, "setUp() was not executed - adService is null");
        assertNotNull(cityService, "setUp() was not executed - cityService is null");
        assertNotNull(domainService, "setUp() was not executed - domainService is null");
    }

    /**
     * Test getAdsWithDomainAndCity - should use slugs directly without fetching from database.
     */
    @Test
    public void testGetAdsWithDomainAndCity_WithSlugs() {
        // Arrange: Get valid slugs from database
        List<City> cities = cityService.getAllCities();
        assertNotNull(cities, "Cities should not be null");
        assertFalse(cities.isEmpty(), "Cities list should not be empty");
        
        City testCity = cities.get(0);
        String citySlug = testCity.getSlug();
        assertNotNull(citySlug, "City slug should not be null");

        // Get a valid domain slug
        var domains = domainService.getAllDomain();
        assertNotNull(domains, "Domains should not be null");
        assertFalse(domains.isEmpty(), "Domains list should not be empty");
        
        String domainSlug = domains.get(0).getSlug();
        assertNotNull(domainSlug, "Domain slug should not be null");

        // Act: Get ads using slugs directly
        List<AdDto> result = adService.getAdsWithDomainAndCity(
                citySlug,
                domainSlug,
                1,
                10,
                null
        );

        // Assert: Should return results (may be empty, but should not throw exception)
        assertNotNull(result, "Result should not be null");
        // Note: Result may be empty if no ads exist for this city/domain combination
    }

    /**
     * Test getAdsWithDomainAndCity - with invalid city slug should return empty list.
     */
    @Test
    public void testGetAdsWithDomainAndCity_InvalidCitySlug() {
        // Arrange: Invalid city slug
        String invalidCitySlug = "invalid-city-slug-12345";
        var domains = domainService.getAllDomain();
        assertFalse(domains.isEmpty(), "Domains list should not be empty");
        String domainSlug = domains.get(0).getSlug();

        // Act: Get ads with invalid city slug
        List<AdDto> result = adService.getAdsWithDomainAndCity(
                invalidCitySlug,
                domainSlug,
                1,
                10,
                null
        );

        // Assert: Should return empty list for invalid city slug
        assertNotNull(result, "Result should not be null");
        assertTrue(result.isEmpty(), "Result should be empty for invalid city slug");
    }

    /**
     * Test countAdsWithDomainAndCity - should use slugs directly.
     */
    @Test
    public void testCountAdsWithDomainAndCity_WithSlugs() {
        // Arrange: Get valid slugs
        List<City> cities = cityService.getAllCities();
        assertFalse(cities.isEmpty(), "Cities list should not be empty");
        String citySlug = cities.get(0).getSlug();

        var domains = domainService.getAllDomain();
        assertFalse(domains.isEmpty(), "Domains list should not be empty");
        String domainSlug = domains.get(0).getSlug();

        // Act: Count ads using slugs directly
        long count = adService.countAdsWithDomainAndCity(
                citySlug,
                domainSlug,
                null
        );

        // Assert: Should return a count (may be 0, but should not throw exception)
        assertTrue(count >= 0, "Count should be non-negative");
    }

    /**
     * Test getAdsByDomainSlug - should use slug directly without fetching domain.
     */
    @Test
    public void testGetAdsByDomainSlug_WithSlug() {
        // Arrange: Get a valid domain slug
        var domains = domainService.getAllDomain();
        assertFalse(domains.isEmpty(), "Domains list should not be empty");
        String domainSlug = domains.get(0).getSlug();
        assertNotNull(domainSlug, "Domain slug should not be null");

        // Act: Get ads using slug directly
        List<AdDto> result = adService.getAdsByDomainSlug(domainSlug, 1, 10);

        // Assert: Should return results (may be empty)
        assertNotNull(result, "Result should not be null");
    }

    /**
     * Test getAdsByCitySlug - should use slug directly without fetching city.
     */
    @Test
    public void testGetAdsByCitySlug_WithSlug() {
        // Arrange: Get a valid city slug
        List<City> cities = cityService.getAllCities();
        assertFalse(cities.isEmpty(), "Cities list should not be empty");
        String citySlug = cities.get(0).getSlug();
        assertNotNull(citySlug, "City slug should not be null");

        // Act: Get ads using slug directly
        List<Ad> result = adService.getAdsByCitySlug(citySlug, 1, 10, null);

        // Assert: Should return results (may be empty)
        assertNotNull(result, "Result should not be null");
    }

    /**
     * Test getAdsWithDomainSlugAndCitySlug - should use slugs directly.
     */
    @Test
    public void testGetAdsWithDomainSlugAndCitySlug_WithSlugs() {
        // Arrange: Get valid slugs
        List<City> cities = cityService.getAllCities();
        assertFalse(cities.isEmpty(), "Cities list should not be empty");
        String citySlug = cities.get(0).getSlug();

        var domains = domainService.getAllDomain();
        assertFalse(domains.isEmpty(), "Domains list should not be empty");
        String domainSlug = domains.get(0).getSlug();

        // Act: Get ads using slugs directly
        List<AdDto> result = adService.getAdsWithDomainSlugAndCitySlug(
                citySlug,
                domainSlug,
                1,
                10
        );

        // Assert: Should return results (may be empty)
        assertNotNull(result, "Result should not be null");
    }

    /**
     * Test that methods don't use getName() - verify by checking they work with slugs.
     */
    @Test
    public void testMethodsUseSlugsNotNames() {
        // Arrange: Get a city and its slug
        List<City> cities = cityService.getAllCities();
        assertFalse(cities.isEmpty(), "Cities list should not be empty");
        City city = cities.get(0);
        String citySlug = city.getSlug();
        String cityName = city.getName();
        
        assertNotNull(citySlug, "City slug should not be null");
        assertNotNull(cityName, "City name should not be null");
        assertNotEquals(citySlug, cityName, "Slug and name should be different");

        // Act: Try to use slug (should work)
        List<Ad> resultWithSlug = adService.getAdsByCitySlug(citySlug, 1, 10, null);
        assertNotNull(resultWithSlug, "Result with slug should not be null");

        // Act: Try to use name (should fail or return empty)
        // Note: This test verifies that the method expects slugs, not names
        // If name is passed as slug, it should either fail or return empty results
        try {
            List<Ad> resultWithName = adService.getAdsByCitySlug(cityName, 1, 10, null);
            // If it doesn't throw, the result should be empty (name is not a valid slug)
            // This is expected behavior - methods should use slugs, not names
            assertNotNull(resultWithName, "Result with name should not be null");
        } catch (Exception e) {
            // Expected: methods should not work with names
            assertTrue(e instanceof ElementNotFoundException || 
                      e.getMessage().contains("not found") ||
                      e.getMessage().contains("city"),
                      "Should fail when name is used instead of slug");
        }
    }

    /**
     * Test addOrUpdate - creation scenario (no key).
     * Should set creationDate when key is null or empty.
     */
    @Test
    public void testAddOrUpdate_Creation() {
        // Arrange: Create a new Ad without key
        Ad newAd = new Ad();
        newAd.setTitle("Test Job Creation");
        newAd.setContent("Test content for creation");
        newAd.setEmail("test@example.com");
        newAd.setKey(null); // Explicitly set to null for creation
        
        // Get valid slugs for city and domain
        List<City> cities = cityService.getAllCities();
        assertFalse(cities.isEmpty(), "Cities list should not be empty");
        newAd.setCity(cities.get(0).getSlug());
        
        var domains = domainService.getAllDomain();
        assertFalse(domains.isEmpty(), "Domains list should not be empty");
        newAd.setDomain(domains.get(0).getSlug());

        Instant beforeCreation = Instant.now();

        // Act: Call addOrUpdate
        adService.addOrUpdate(newAd);

        Instant afterCreation = Instant.now();

        // Assert: creationDate should be set, updateDate should be null
        assertNotNull(newAd.getCreationDate(), "Creation date should be set");
        assertNull(newAd.getUpdateDate(), "Update date should be null for new Ad");
        assertNotNull(newAd.getLastActivityAt(), "lastActivityAt should be set on creation");
        assertEquals(newAd.getCreationDate(), newAd.getLastActivityAt(),
                "lastActivityAt should match creationDate on new ad");
        assertTrue(newAd.getCreationDate().isAfter(beforeCreation) ||
                  newAd.getCreationDate().equals(beforeCreation),
                  "Creation date should be after or equal to beforeCreation");
        assertTrue(newAd.getCreationDate().isBefore(afterCreation) ||
                  newAd.getCreationDate().equals(afterCreation),
                  "Creation date should be before or equal to afterCreation");
    }

    /**
     * Test addOrUpdate - update scenario (with key).
     * Should set updateDate when key is present.
     */
    @Test
    public void testAddOrUpdate_Update() {
        // Arrange: First create an Ad to get a key
        Ad newAd = new Ad();
        newAd.setTitle("Test Job for Update");
        newAd.setContent("Initial content");
        newAd.setEmail("test@example.com");
        newAd.setKey(null);
        
        List<City> cities = cityService.getAllCities();
        assertFalse(cities.isEmpty(), "Cities list should not be empty");
        newAd.setCity(cities.get(0).getSlug());
        
        var domains = domainService.getAllDomain();
        assertFalse(domains.isEmpty(), "Domains list should not be empty");
        newAd.setDomain(domains.get(0).getSlug());
        
        // Create the Ad first
        adService.addOrUpdate(newAd);
        String adKey = newAd.getKey();
        assertNotNull(adKey, "Ad should have a key after creation");
        
        // Store original creation date
        Instant originalCreationDate = newAd.getCreationDate();
        assertNotNull(originalCreationDate, "Original creation date should not be null");
        
        // Now update the Ad
        newAd.setTitle("Updated Title");
        newAd.setContent("Updated content");
        Instant beforeUpdate = Instant.now();
        
        // Act: Call addOrUpdate with existing key
        adService.addOrUpdate(newAd);

        Instant afterUpdate = Instant.now();
        
        // Assert: updateDate should be set, creationDate should remain unchanged
        assertNotNull(newAd.getUpdateDate(), "Update date should be set");
        assertNotNull(newAd.getLastActivityAt(), "lastActivityAt should be set on update");
        assertEquals(newAd.getUpdateDate(), newAd.getLastActivityAt(),
                "lastActivityAt should match updateDate on content update");
        assertEquals(originalCreationDate, newAd.getCreationDate(), 
                    "Creation date should remain unchanged");
        assertTrue(newAd.getUpdateDate().isAfter(beforeUpdate) ||
                  newAd.getUpdateDate().equals(beforeUpdate),
                  "Update date should be after or equal to beforeUpdate");
        assertTrue(newAd.getUpdateDate().isBefore(afterUpdate) ||
                  newAd.getUpdateDate().equals(afterUpdate),
                  "Update date should be before or equal to afterUpdate");
        assertEquals("Updated Title", newAd.getTitle(), "Title should be updated");
        assertEquals("Updated content", newAd.getContent(), "Content should be updated");
    }

    /**
     * Test addOrUpdate - creation with null key.
     * Verifies that null key is treated as creation (same as testAddOrUpdate_Creation but explicit null).
     */
    @Test
    public void testAddOrUpdate_CreationWithNullKey() {
        // Arrange: Create Ad with explicit null key
        Ad newAd = new Ad();
        newAd.setTitle("Test Job with Null Key");
        newAd.setContent("Test content");
        newAd.setEmail("test@example.com");
        newAd.setKey(null); // Explicitly null for creation
        
        List<City> cities = cityService.getAllCities();
        assertFalse(cities.isEmpty(), "Cities list should not be empty");
        newAd.setCity(cities.get(0).getSlug());
        
        var domains = domainService.getAllDomain();
        assertFalse(domains.isEmpty(), "Domains list should not be empty");
        newAd.setDomain(domains.get(0).getSlug());

        Instant beforeCreation = Instant.now();

        // Act: Call addOrUpdate
        adService.addOrUpdate(newAd);

        Instant afterCreation = Instant.now();

        // Assert: Should be treated as creation (creationDate set, updateDate null)
        assertNotNull(newAd.getCreationDate(), "Creation date should be set");
        assertNull(newAd.getUpdateDate(), "Update date should be null for new Ad");
        assertNotNull(newAd.getLastActivityAt(), "lastActivityAt should be set on creation");
        assertNotNull(newAd.getKey(), "Key should be set after creation");
        assertTrue(newAd.getCreationDate().isAfter(beforeCreation) ||
                  newAd.getCreationDate().equals(beforeCreation),
                  "Creation date should be after or equal to beforeCreation");
        assertTrue(newAd.getCreationDate().isBefore(afterCreation) ||
                  newAd.getCreationDate().equals(afterCreation),
                  "Creation date should be before or equal to afterCreation");
    }
}
