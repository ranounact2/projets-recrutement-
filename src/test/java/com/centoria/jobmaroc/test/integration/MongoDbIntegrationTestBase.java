package com.centoria.jobmaroc.test.integration;

import com.centoria.jobmaroc.common.context.ApplicationContext;
import com.centoria.jobmaroc.model.City;
import com.centoria.jobmaroc.model.Domain;
import com.centoria.jobmaroc.service.ICityService;
import com.centoria.jobmaroc.service.IDomainService;
import com.centoria.jobmaroc.service.impl.CityService;
import com.centoria.jobmaroc.service.impl.DomainService;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Base pour les tests d'intégration avec une base MongoDB.
 * Utilise EM_MONGO_URI si définie (ex. GitLab CI avec service mongo), sinon MongoDB local 127.0.0.1:27017.
 * La base "emplois-maroc" est vidée au début de chaque classe de tests pour repartir d'un état propre.
 */
public abstract class MongoDbIntegrationTestBase {

    private static final String ENV_MONGO_URI = "EM_MONGO_URI";
    private static final String CONFIG_PATH = "config/config.integration.properties";
    private static final String URI_PLACEHOLDER = "PLACEHOLDER_MONGODB_URI";
    private static final String LOCAL_MONGO_URI = "mongodb://127.0.0.1:27017";
    private static final String DB_NAME = "emplois-maroc";

    private static String resolvedMongoUri;

    static {
        String envUri = System.getenv(ENV_MONGO_URI);
        resolvedMongoUri = (envUri != null && !envUri.isBlank()) ? envUri.trim() : LOCAL_MONGO_URI;
    }

    @BeforeAll
    static void dropDatabaseForFreshRun() {
        try (MongoClient client = MongoClients.create(resolvedMongoUri)) {
            client.getDatabase(DB_NAME).drop();
        }
    }

    @BeforeEach
    void loadConfigWithMongoUri() throws IOException {
        String configContent = loadResourceAndReplaceUri(CONFIG_PATH, resolvedMongoUri);
        ApplicationContext.getInstance().getProps(new ByteArrayInputStream(configContent.getBytes(StandardCharsets.UTF_8)));
        ensureReferenceData();
    }

    /**
     * Ensures at least one city and one domain exist so tests that depend on reference data (e.g. AdServiceTest, MoAppTest) pass.
     * Also adds cities required by Cucumber scenarios (Rabat, Marrakech, Tanger).
     * No-op if collections already have data.
     */
    protected void ensureReferenceData() {
        ICityService cityService = CityService.getInstance();
        if (cityService == null) {
            return;
        }
        if (cityService.getAllCities().isEmpty()) {
            addCity(cityService, "Casablanca", "casablanca", 3_500_000);
            addCity(cityService, "Rabat", "rabat", 580_000);
            addCity(cityService, "Marrakech", "marrakech", 930_000);
            addCity(cityService, "Tanger", "tanger", 1_000_000);
        }
        IDomainService domainService = DomainService.getInstance();
        if (domainService != null && domainService.getAllDomain().isEmpty()) {
            Domain domain = new Domain();
            domain.setName("Informatique");
            domain.setSlug("informatique");
            domain.setPopulation(0);
            domainService.addOrUpdate(domain);
        }
    }

    private static void addCity(ICityService cityService, String name, String slug, int population) {
        City city = new City();
        city.setName(name);
        city.setPays("Maroc");
        city.setCodePays("MA");
        city.setSlug(slug);
        city.setPopulation(population);
        cityService.addOrUpdate(city);
    }

    private static String loadResourceAndReplaceUri(String resourcePath, String mongoUri) throws IOException {
        try (InputStream in = MongoDbIntegrationTestBase.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            try (Scanner sc = new Scanner(in, StandardCharsets.UTF_8)) {
                String content = sc.useDelimiter("\\A").next();
                return content.replace(URI_PLACEHOLDER, mongoUri);
            }
        }
    }
}
