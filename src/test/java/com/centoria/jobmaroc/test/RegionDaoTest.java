package com.centoria.jobmaroc.test;

import com.centoria.jobmaroc.dao.ISimpleGenericDao;
import com.centoria.jobmaroc.dao.impl.RegionDao;
import com.centoria.jobmaroc.model.Region;
import com.centoria.jobmaroc.test.integration.MongoDbIntegrationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RegionDaoTest extends MongoDbIntegrationTestBase {

    private ISimpleGenericDao<Region> regionDao;

    @BeforeEach
    public void setUp() {
        regionDao = RegionDao.getInstance();
    }

    @Test
    public void testCreateReadUpdateDelete() {
        // 1. Create
        Region newRegion = new Region();
        newRegion.setName("Test Region");
        newRegion.setSlug("test-region");


        String id = regionDao.addOrUpdate(newRegion);
        assertNotNull(id, "ID should not be null after creation");
        newRegion.setKey(id);
        // 2. Read
        Region retrievedRegion = regionDao.find(id);
        assertNotNull(retrievedRegion, "Region should be found");
        assertEquals("Test Region", retrievedRegion.getName(), "Name should match");
        assertEquals("test-region", retrievedRegion.getSlug(), "Slug should match");

        // 3. Update
        retrievedRegion.setName("Updated Test Region");
        regionDao.addOrUpdate(retrievedRegion);

        Region updatedRegion = regionDao.find(id);
        assertEquals("Updated Test Region", updatedRegion.getName(), "Name should be updated");

        // 4. Delete
        regionDao.delete(updatedRegion);
        Region deletedRegion = regionDao.find(id);
        assertNull(deletedRegion, "Region should be deleted");
    }
}
