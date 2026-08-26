package com.centoria.jobmaroc.web;

import com.centoria.jobmaroc.common.context.ApplicationContext;
import com.centoria.jobmaroc.common.globalData.UrlConst;
import com.centoria.jobmaroc.model.City;
import com.centoria.jobmaroc.model.Domain;
import com.centoria.jobmaroc.service.ICityService;
import com.centoria.jobmaroc.service.IDomainService;
import com.centoria.jobmaroc.service.impl.CityService;
import com.centoria.jobmaroc.service.impl.DomainService;
import com.centoria.jobmaroc.web.bo.BoIndexServlet;
import com.centoria.jobmaroc.web.handler.CustomErrorHandler;
import com.centoria.jobmaroc.web.servlet.*;
import freemarker.template.Configuration;
import jakarta.servlet.MultipartConfigElement;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;

/**
 * @author lspm2868
 */
@Slf4j
public class Main {

    /**
     * Initialisation
     */
//    public void init() {
//        try {
//            InputStream inputStream = Main.class.getResourceAsStream("/config/config." + System.getenv("EM_ENV") + ".properties");
//            if (inputStream == null) {
//                throw new RuntimeException("Configuration file not found");
//            }
//            ApplicationContext.getInstance().getProps(inputStream);
//        } catch (IOException e) {
//            log.error("Error loading configuration", e);
//        }
//        IWebExecutor[] executors = {new FrontController(), new BoController(), new MoController()};
//
//        IWebExecutor[] filters = {new FilterController()};
//
//        /*
//         * Start all routes
//         */
//        for (IWebExecutor filter : filters) {
//            filter.defineRoutes();
//        }
//
//        for (IWebExecutor executor : executors) {
//            executor.defineRoutes();
//        }
//    }
//
//    public static void main(String[] args) throws Exception {
//        initConfig();
//
//        Server server = new Server(5024);
//        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
//        context.setContextPath("/");
//
//        // fichiers statiques
//        context.addServlet(DefaultServlet.class, "/static/*")
//                .setInitParameter("resourceBase", "src/main/resources");
//
//        // **Ici** vos controllers, désormais vrais servlets :
//        context.addServlet(FrontController.class, "/");
//
//        server.setHandler(context);
//        server.start();
//        server.join();
//    }
//
//    private static void initConfig() {
//        try {
//            String env = System.getenv("EM_ENV");
//            InputStream is = Main.class.getResourceAsStream("/config/config." + env + ".properties");
//            if (is == null) throw new RuntimeException("Config non trouvée pour EM_ENV=" + env);
//            ApplicationContext.getInstance().getProps(is);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
    /**
     * Verifies that required Jetty EE10 classes are available.
     * Throws RuntimeException if incompatible dependencies are detected.
     */
    private static void verifyJettyCompatibility() {
        try {
            // Check EE10 servlet classes
            Class<?> servletContextHandler = Class.forName("org.eclipse.jetty.ee10.servlet.ServletContextHandler");
            Class.forName("org.eclipse.jetty.ee10.servlet.ServletHolder");
            
            // Check core server classes
            Class.forName("org.eclipse.jetty.server.Server");
            Class.forName("org.eclipse.jetty.server.Handler");
            
            // Check HTTP classes
            Class.forName("org.eclipse.jetty.http.HttpStatus");
            
            // Check util classes
            Class.forName("org.eclipse.jetty.util.Callback");
            
            // Verify Jetty version (check package version)
            Package jettyPackage = servletContextHandler.getPackage();
            String implVersion = jettyPackage != null ? jettyPackage.getImplementationVersion() : "unknown";
            
            log.info("Jetty compatibility check passed:");
            log.info("  - EE10 Servlet classes: OK");
            log.info("  - Server classes: OK");
            log.info("  - HTTP classes: OK");
            log.info("  - Util classes: OK");
            log.info("  - Jetty version: {}", implVersion != null && !implVersion.equals("unknown") ? implVersion : "detected");
            
            // Warn if version doesn't match expected
            if (implVersion != null && !implVersion.equals("unknown") && !implVersion.startsWith("12.1")) {
                log.warn("Jetty version {} detected, expected 12.1.x. This may cause compatibility issues.", implVersion);
            }
            
        } catch (ClassNotFoundException e) {
            String errorMsg = String.format(
                "Jetty EE10 compatibility check failed: Missing required class '%s'. " +
                "Please ensure jetty-ee10-servlet dependency is correctly configured in pom.xml. " +
                "Expected Jetty version: 12.1.5",
                e.getMessage()
            );
            log.error(errorMsg);
            throw new RuntimeException(errorMsg, e);
        } catch (Exception e) {
            log.error("Unexpected error during Jetty compatibility check", e);
            throw new RuntimeException("Jetty compatibility check failed", e);
        }
    }

    /**
     * When EM_ENV=test, ensures cities and domains exist so Cucumber/Selenium tests
     * (e.g. job creation with Rabat, Marrakech, Tanger) can run. No-op if collections already have data.
     */
    private static void ensureTestReferenceData() {
        try {
            ICityService cityService = CityService.getInstance();
            if (cityService != null && cityService.getAllCities().isEmpty()) {
                addCity(cityService, "Casablanca", "casablanca", 3_500_000);
                addCity(cityService, "Rabat", "rabat", 580_000);
                addCity(cityService, "Marrakech", "marrakech", 930_000);
                addCity(cityService, "Tanger", "tanger", 1_000_000);
                log.info("Test reference data: seeded cities (Casablanca, Rabat, Marrakech, Tanger)");
            }
            IDomainService domainService = DomainService.getInstance();
            if (domainService != null && domainService.getAllDomain().isEmpty()) {
                Domain domain = new Domain();
                domain.setName("Informatique");
                domain.setSlug("informatique");
                domain.setPopulation(0);
                domainService.addOrUpdate(domain);
                log.info("Test reference data: seeded domain (Informatique)");
            }
        } catch (Exception e) {
            log.warn("Could not seed test reference data (MongoDB may be unavailable): {}", e.getMessage());
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

    public static void main(String[] args) throws Exception {
        // --- 0) Verify Jetty EE10 compatibility ---
        verifyJettyCompatibility();
        
        // --- 0) Load application configuration ---
        ApplicationContext.getInstance().getProps(); // Initialize configuration

        // --- 0.1) Seed reference data when running in test env (for Cucumber/Selenium UI tests) ---
        if ("test".equals(ApplicationContext.getInstance().getCurrentEnvironment())) {
            ensureTestReferenceData();
        }

        // --- 1) Configuration FreeMarker ---
        Configuration freemarkerConfig =
                new Configuration(Configuration.VERSION_2_3_31);
        freemarkerConfig.setClassLoaderForTemplateLoading(
                Main.class.getClassLoader(), "/"
        );
        freemarkerConfig.setDefaultEncoding("UTF-8");

        // --- 2) Contexte pour servlets dynamiques ---
        ServletContextHandler servletContext =
                new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContext.setContextPath("");
        
        // --- 2.1) Configuration du gestionnaire d'erreurs personnalisé ---
        CustomErrorHandler customErrorHandler = new CustomErrorHandler();
        customErrorHandler.setFreemarkerConfig(freemarkerConfig); // Passer la config FreeMarker
        servletContext.setErrorHandler(customErrorHandler);
        
        // on met la config dans le context pour y accéder plus tard
        servletContext.setAttribute("freemarkerConfig", freemarkerConfig);

        // IMPORTANT: Les patterns spécifiques doivent être enregistrés AVANT le catch-all "/*"
        // L'ordre d'enregistrement est important: les patterns les plus spécifiques en premier,
        // et le catch-all "/*" (IndexServlet) en dernier.
        
        // --- 1) Servlets avec patterns très spécifiques ---
        // Robots.txt servlet - handles dynamic robots.txt based on environment
        servletContext.addServlet(
                new ServletHolder(new RobotsServlet()),
                "/robots.txt"
        );
        
        // Back Office servlet - admin route
        servletContext.addServlet(
                new ServletHolder(new BoIndexServlet()),
                "/rtsystems_admin_8965_hXtwWUCzukqwPyEuWxcE"
        );
        
        // --- 2) Middle Office servlets ---
        // IMPORTANT: Specific patterns must be registered BEFORE generic ones
        // Update job offer: /m-office/mes-annonces/update/{secretCode}/{id}
        servletContext.addServlet(
                new ServletHolder(new com.centoria.jobmaroc.web.servlet.mo.JobOfferUpdateServlet()),
                "/m-office/mes-annonces/update/*"
        );
        // Delete job offer: /m-office/mes-annonces/delete/{secretCode}/{id}
        servletContext.addServlet(
                new ServletHolder(new com.centoria.jobmaroc.web.servlet.mo.JobOfferDeleteServlet()),
                "/m-office/mes-annonces/delete/*"
        );
        // List job offers: /m-office/mes-annonces/{secretCode} (generic pattern last)
        servletContext.addServlet(
                new ServletHolder(new com.centoria.jobmaroc.web.servlet.mo.MyJobAdsListServlet()),
                "/m-office/mes-annonces/*"
        );
        servletContext.addServlet(
                new ServletHolder(new MyJobAdsServlet()),
                "/mes-annonces-emploi"
        );

        // --- 3) Front Office servlets avec patterns spécifiques ---
        // Ne mappez QUE /region/* sur RegionServlet qui fait office de router
        servletContext.addServlet(
                new ServletHolder(new RegionServlet()),
                "/" + UrlConst.REGION + "/*"
        );
        
        // CategoryServlet → /categorie et /categorie/
        servletContext.addServlet(
                new ServletHolder(new CategoryServlet()),
                "/" + UrlConst.CATEGORY
        );
        servletContext.addServlet(
                new ServletHolder(new CategoryServlet()),
                "/" + UrlConst.CATEGORY + "/"
        );

        // CategoryByDomainServlet → tout sous /categorie/*
        servletContext.addServlet(
                new ServletHolder(new CategoryByDomainServlet()),
                "/" + UrlConst.CATEGORY + "/*"
        );

        String tmpDir = System.getProperty("java.io.tmpdir");
        MultipartConfigElement multipartConfig =
                new MultipartConfigElement(
                        tmpDir,
                        5 * 1024 * 1024,      // maxFileSize = 5 Mo
                        10 * 1024 * 1024,     // maxRequestSize = 10 Mo
                        0                     // fileSizeThreshold
                );

        // AddJobOfferServlet avec configuration multipart
        ServletHolder offerHolder = new ServletHolder(new AddJobOfferServlet());
        offerHolder.getRegistration().setMultipartConfig(multipartConfig);
        servletContext.addServlet(offerHolder, "/ajouter-offre-emploi");

        servletContext.addServlet(
                new ServletHolder(new ContactServlet()),
                "/" + UrlConst.CONTACT
        );
        servletContext.addServlet(
                new ServletHolder(new AboutServlet()),
                "/a-propos-emplois-maroc"
        );
        servletContext.addServlet(
                new ServletHolder(new MentionsServlet()),
                "/mentions"
        );
        
        // PostulerEmploiServlet avec configuration multipart (upload de CV)
        ServletHolder applicantHolder = new ServletHolder(new PostulerEmploiServlet());
        applicantHolder.getRegistration().setMultipartConfig(multipartConfig);
        servletContext.addServlet(
                applicantHolder,
                "/postuler-emploi/*"
        );
        
        servletContext.addServlet(
                new ServletHolder(new DetailOfferServlet()),
                "/offre-emploi-maroc/*"
        );
        
        // --- Test servlet for mail sending ---
        servletContext.addServlet(
                new ServletHolder(new TestMailServlet()),
                "/test-mail"
        );

        // --- Kaptcha image generation servlet ---
        servletContext.addServlet(
                new ServletHolder(new KaptchaServlet()),
                "/captcha-image"
        );

        // --- 4) IndexServlet pour la page d'accueil ---
        // Mappé sur "/" (default servlet) - gère les requêtes vers "/" et ""
        // Note: Le default servlet "/" est utilisé quand aucun autre mapping ne correspond
        servletContext.addServlet(
                new ServletHolder(new IndexServlet()),
                "/"
        );
        
        // --- 5) Gestion des 404 ---
        // Les requêtes non trouvées sont gérées par CustomErrorHandler (configuré ligne 98-99)
        // qui rend le template errors/error-page.ftl
        // Note: Ne PAS utiliser "/*" pour un catch-all car il a priorité sur "/"

        // --- 3) Handler pour fichiers statiques ---
        // Create handlers for both /assets and /assets-theme paths
        Handler.Sequence staticHandlers = StaticFileServlet.createStaticHandlers();

        // --- 4) Combinaison et démarrage ---
        // Use Handler.Sequence (Jetty 12 API) instead of HandlerList
        Handler.Sequence handlers = new Handler.Sequence();
        handlers.addHandler(staticHandlers);
        handlers.addHandler(servletContext);

        // Get server port from configuration, default to 8080 if not configured
        int serverPort = 8080; // Default port
        try {
            String portValue = ApplicationContext.getInstance().getProps().getValue("server.port");
            if (portValue != null && !portValue.trim().isEmpty()) {
                serverPort = Integer.parseInt(portValue.trim());
            }
        } catch (NumberFormatException e) {
            log.warn("Invalid server.port value in configuration, using default: {}", serverPort);
        }
        
        Server server = new Server(serverPort);
        
        // --- 4.1) Configuration du gestionnaire d'erreurs au niveau du serveur ---
        // This ensures all unhandled exceptions are caught at the server level
        server.setErrorHandler(customErrorHandler);
        
        server.setHandler(handlers);
        server.start();
        log.info("Server démarré sur http://localhost:{}", serverPort);
        log.info("Error handling configured with CustomErrorHandler");
        server.join();
    }
}

