package com.centoria.jobmaroc.service.impl;

import com.centoria.jobmaroc.dao.IAdDao;
import com.centoria.jobmaroc.dao.impl.AdDao;
import com.centoria.jobmaroc.dto.AdDto;
import com.centoria.jobmaroc.dto.MailDto;
import com.centoria.jobmaroc.dto.mapper.MapperAdDto;
import com.centoria.jobmaroc.model.Ad;
import com.centoria.jobmaroc.model.City;
import com.centoria.jobmaroc.service.IAdService;
import com.centoria.jobmaroc.service.ICityService;
import com.centoria.jobmaroc.web.base.FreeMarkerEngine;
import org.bson.Document;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AdService extends BaseService<Ad, IAdDao> implements IAdService {

    /** Tri BO « Toutes » : dernière activité ; secours si {@code lastActivityAt} absent. */
    private static final String BO_LISTING_SORT_ALL = "{lastActivityAt: -1, creationDate: -1, updateDate: -1}";
    /** Tri BO « Nouvelles ». */
    private static final String BO_LISTING_SORT_NEW = "{creationDate: -1}";
    /** Tri BO « Modifiées » ; {@code creationDate} en secours si {@code updateDate} nul. */
    private static final String BO_LISTING_SORT_MODIFIED = "{updateDate: -1, creationDate: -1}";

    private static IAdService instance = null;
    FreeMarkerEngine template = new FreeMarkerEngine();
    private MapperAdDto mapperAdDto = Mappers.getMapper(MapperAdDto.class);
    private final ICityService cityService = CityService.getInstance();

    private AdService() {
        this(AdDao.getInstance());
    }

    /**
     * Pour les tests : injecte un DAO (ex. mock) sans toucher au singleton production.
     */
    public AdService(IAdDao adDao) {
        this.dao = adDao;
    }

    public static IAdService getInstance() {
        if (instance == null) {
            instance = new AdService();
        }
        return instance;
    }

    @Override
    public void addOrUpdate(Ad obj) {
        Instant now = Instant.now();
        if (obj.getKey() != null && !obj.getKey().isEmpty()) {
            obj.setUpdateDate(now);
        } else {
            obj.setCreationDate(now);
        }
        obj.setLastActivityAt(now);

        // Generate vector embedding for semantic search
        try {
            String contentToEmbed = String.format("%s %s %s %s", 
                obj.getTitle() != null ? obj.getTitle() : "",
                obj.getContent() != null ? obj.getContent().replaceAll("<[^>]*>", "") : "", // strip HTML
                obj.getDomain() != null ? obj.getDomain() : "",
                obj.getCity() != null ? obj.getCity() : ""
            ).trim();
            
            if (!contentToEmbed.isEmpty()) {
                com.centoria.jobmaroc.service.IAiService aiService = com.centoria.jobmaroc.service.impl.AiService.getInstance();
                obj.setEmbedding(aiService.generateEmbedding(contentToEmbed));
            }
        } catch (Exception e) {
            log.error("Failed to generate embedding for Ad", e);
        }

        super.addOrUpdate(obj);
    }

    @Override
    public void renderTemplate(MailDto<AdDto> mdto, String emailTemplate) {

    }

    @Override
    public void initStateAndType(Ad ad, String state, int stateRank) {

        if (ad.getSecretCode().equals("7064a65f235f76d24e7b3bfc4d0f6b3b")) {
            ad.setAnnouncetype(Ad.ANNOUNCE_TYPE_STAR);
        } else {
            ad.setAnnouncetype(Ad.ANNOUNCE_TYPE_NORMAL);
        }

        ad.setEnabled(true);
        ad.setState(state);
        ad.setStateRank(stateRank);
        Instant now = Instant.now();
        ad.setUpdateDate(now);
        ad.setLastActivityAt(now);
    }


    @Override
    public void updateState(Ad ad) {
        ad.setLastActivityAt(Instant.now());
        dao.addOrUpdate(ad);
    }

//    @Override
//    public void renderTemplate(MailDto<AdDto> mdto, String emailTemplate) {
//        Map<String, Object> map = new HashMap<>();
//        String content = null;
//        String subject = null;
//        AdDto jobdto = (AdDto) mdto.getObject();
//        map.put("job", jobdto);
//        map.put("host", mdto.getHost());
//        if (emailTemplate.equals("creation")) {
//            subject = "Votre annonce " + jobdto.getTitle() + " est en cours de validation";
//            ModelAndView mv = new ModelAndView(map, "emails/createjob");
//            content = template.render(mv);
//        } else if (emailTemplate.equals("modification")) {
//            subject = "Validation de votre modification";
//            ModelAndView mv = new ModelAndView(map, "emails/updatejob");
//            content = template.render(mv);
//        } else if (emailTemplate.equals("validate")) {
//            subject = "Validation de votre annonce";
//            ModelAndView mv = new ModelAndView(map, "emails/validateOffer");
//            content = template.render(mv);
//        } else if (emailTemplate.equals("jobslisk")) {
//            subject = "Votre lien vers vos annonces sur Emplois-maroc";
//            ModelAndView mv = new ModelAndView(map, "emails/jobslink.ftl");
//            content = template.render(mv);
//        } else if (emailTemplate.equals("contact")) {
//            subject = "Votre lien vers vos annonces sur Emplois-maroc";
//            ModelAndView mv = new ModelAndView(map, "emails/jobslink.ftl");
//        }
//
//        mdto.setMailContent(content);
//        mdto.setSubject(subject);
//    }

    @Override
    public List<AdDto> getAddsByDomain(String domain, int pageNumber, int numOfJobs, String keyword) {
        String query = buildQuery(null, domain, keyword);
        List<Ad> ads = get(query, null, "{creationDate: -1}", pageNumber, numOfJobs);
        return mapperAdDto.asDtos(ads);
    }

    @Override
    public List<AdDto> getAdsByDomainSlug(String domainSlug, int pageNumber, int numOfJobs) {
        // Use slug directly - no need to fetch from database
        String query = "{" + "domain:'" + domainSlug + "'," + "enabled: true, state: 'valid'}";
        List<Ad> ads = get(query, null, "{creationDate: -1}", pageNumber, numOfJobs);
        return mapperAdDto.asDtos(ads);
    }

    @Override
    public List<Ad> getAdsByCitySlug(String citySlug,
                                     int numberOfPage,
                                     int numOfAds,
                                     String keyword) {
        // Use slug directly - no need to fetch from database
        String query = buildQuery(citySlug, null, keyword);
        return get(query, null, "{creationDate: -1}", numberOfPage, numOfAds);
    }

    @Override
    public List<AdDto> getAllAds(int pageNumber, int numberOfAds) {
        return getAllAds(pageNumber, numberOfAds, null);
    }

    @Override
    public List<AdDto> getAllAds(int pageNumber, int numberOfAds, String boStatusFilter) {
        String query = buildBoListingQuery(boStatusFilter);
        String sort = buildBoListingSort(boStatusFilter);
        List<Ad> ads = get(query, null, sort, pageNumber, numberOfAds);
        return mapperAdDto.asDtos(ads);
    }

    @Override
    public long countForBoListing(String boStatusFilter) {
        String query = buildBoListingQuery(boStatusFilter);
        if (query == null) {
            return count("{}");
        }
        return count(query);
    }

    /**
     * Filtre BO : {@code null} / all = pas de critère ; new / modified = {@code $in} sur {@code state}.
     */
    private static String buildBoListingQuery(String boStatusFilter) {
        if (boStatusFilter == null || boStatusFilter.isBlank()) {
            return null;
        }
        String f = boStatusFilter.trim().toLowerCase();
        if ("all".equals(f)) {
            return null;
        }
        if ("new".equals(f)) {
            return "{ \"state\": { \"$in\": [\"new\", \"new_verified\"] } }";
        }
        if ("modified".equals(f)) {
            return "{ \"state\": { \"$in\": [\"updated\", \"updated_verified\"] } }";
        }
        return null;
    }

    /**
     * Tri BO selon le filtre : « Toutes » → {@code lastActivityAt} ; « Nouvelles » → {@code creationDate} ;
     * « Modifiées » → {@code updateDate} (puis {@code creationDate}).
     */
    private static String buildBoListingSort(String boStatusFilter) {
        if (boStatusFilter == null || boStatusFilter.isBlank()) {
            return BO_LISTING_SORT_ALL;
        }
        String f = boStatusFilter.trim().toLowerCase();
        if ("all".equals(f)) {
            return BO_LISTING_SORT_ALL;
        }
        if ("new".equals(f)) {
            return BO_LISTING_SORT_NEW;
        }
        if ("modified".equals(f)) {
            return BO_LISTING_SORT_MODIFIED;
        }
        return BO_LISTING_SORT_ALL;
    }

    @Override
    public List<AdDto> getAdsWithDomainAndCity(String citySlug,
                                               String domainSlug,
                                               int pageNumber,
                                               int numberOfAds,
                                               String keyword) {
        // Use slugs directly - no need to fetch from database
        String query = buildQuery(citySlug, domainSlug, keyword);
        List<Ad> ads = get(query, null, "{creationDate: -1}", pageNumber, numberOfAds);
        return mapperAdDto.asDtos(ads);
    }

    @Override
    public long countAdsWithDomainAndCity(String citySlug,
                                          String domainSlug,
                                          String keyword) {
        // Use slugs directly - no need to fetch from database
        String query = buildQuery(citySlug, domainSlug, keyword);
        return count(query);
    }

    @Override
    public List<AdDto> getAdsWithDomainSlugAndCitySlug(String citySlug, String domainSlug, int pageNumber, int numberOfAds) {
        // Use slugs directly - no need to fetch from database
        String query = buildQuery(citySlug, domainSlug, null);
        List<Ad> ads = get(query, null, "{creationDate: -1}", pageNumber, numberOfAds);
        return mapperAdDto.asDtos(ads);
    }

    @Override
    public List<AdDto> getAdsBySecretCode(String secretCode, int pageNumber, int numberOfAds) {
        String query = "{ secretCode: \"" + secretCode + "\", enabled: true, state: { $nin: [\"" + Ad.DELETED + "\", \"" + Ad.DISABLED + "\"] } }";
        List<Ad> ads = get(query, null, "{creationDate: -1}", pageNumber, numberOfAds);
        List<AdDto> dtos = mapperAdDto.asDtos(ads);
        return dtos;
    }

    @Override
    public List<AdDto> getAdsBySecretCodeGroupedByEmail(String secretCode, int pageNumber, int numberOfAds) {
        // Utilisation directe de find() de ISimpleGenericDao
        // Ajout du filtre enabled: true pour ne pas afficher les annonces désactivées
        String simpleQuery = String.format("{\"secretCode\": \"%s\", \"enabled\": true}", secretCode);
        String emailSort = "{\"updateDate\": -1, \"creationDate\": -1}";
        List<Ad> allAdsForSecretCode = dao.find(simpleQuery, null, emailSort, 1, 100);
        
        if (allAdsForSecretCode.isEmpty()) {
            return Collections.emptyList();
        }
        
        // Récupérer l'email de la première annonce
        String targetEmail = allAdsForSecretCode.get(0).getEmail();
        if (targetEmail == null || targetEmail.trim().isEmpty()) {
            return Collections.emptyList();
        }
        
        // Utilisation directe de find() de ISimpleGenericDao avec pagination
        String escapedEmail = escapeRegexForJson(targetEmail.trim());
        // Ajout du filtre enabled: true
        String adsQuery = String.format(
            "{\"secretCode\": \"%s\", \"email\": {\"$regex\": \"^%s$\", \"$options\": \"i\"}, \"enabled\": true}",
            secretCode, escapedEmail
        );
        
        List<Ad> ads = dao.find(adsQuery, null, emailSort, pageNumber, numberOfAds);
        return mapperAdDto.asDtos(ads);
    }

    @Override
    public long countAdsBySecretCodeGroupedByEmail(String secretCode) {
        // Utilisation directe de find() de ISimpleGenericDao pour récupérer l'email
        // Ajout du filtre enabled: true pour ne pas compter les annonces désactivées
        String simpleQuery = String.format("{\"secretCode\": \"%s\", \"enabled\": true}", secretCode);
        String emailSort = "{\"updateDate\": -1, \"creationDate\": -1}";
        List<Ad> emailResult = dao.find(simpleQuery, null, emailSort, 1, 1);
        
        if (emailResult.isEmpty()) {
            return 0;
        }
        
        String targetEmail = emailResult.get(0).getEmail();
        if (targetEmail == null || targetEmail.trim().isEmpty()) {
            return 0;
        }
        
        // Utilisation directe de count() de ISimpleGenericDao
        String escapedEmail = escapeRegexForJson(targetEmail.trim());
        // Ajout du filtre enabled: true
        String countQuery = String.format(
            "{\"secretCode\": \"%s\", \"email\": {\"$regex\": \"^%s$\", \"$options\": \"i\"}, \"enabled\": true}",
            secretCode, escapedEmail
        );
        
        return dao.count(countQuery);
    }
    
    /**
     * Échappe les caractères spéciaux pour une utilisation dans une regex MongoDB
     * Note: Les backslashes doivent être doublés pour être valides dans une chaîne JSON
     */
    private String escapeRegexForJson(String input) {
        // D'abord échapper pour regex, puis doubler les backslashes pour JSON
        String escaped = input.replaceAll("([\\\\^$.|?*+()\\[\\]{}])", "\\\\$1");
        // Doubler les backslashes pour JSON
        return escaped.replace("\\", "\\\\");
    }

    public List<AdDto> getAdsByRegion(String domainSlug, String region, int pageNumber, int numberOfAds) {
        // Use slug directly - no need to fetch domain from database
        List<City> cities = cityService.getCitiesByRegion(region);

        List<AdDto> allAds = new ArrayList<>();

        for (City city : cities) {
            List<AdDto> adsByCity = getAdsWithDomainAndCity(city.getSlug(), domainSlug, 1, Integer.MAX_VALUE, null);
            allAds.addAll(adsByCity);
        }

        int startIndex = (pageNumber - 1) * numberOfAds;
        if (startIndex >= allAds.size()) {
            return new ArrayList<>();

        }
        int endIndex = Math.min(startIndex + numberOfAds, allAds.size());

        return allAds.subList(startIndex, endIndex);
    }

    @Override
    public List<Ad> getAdsByRegions(String region, int pageNumber, int numberOfAds) {
        return getAdsByRegionsAndDomain(region, pageNumber, numberOfAds, null);
    }

    @Override
    public List<Ad> getAdsByRegionsAndDomain(String region, int pageNumber, int numberOfAds, String domainSlug) {
        // 1) Récupération des villes de la région (necessary to get all city slugs)
        List<City> cities = cityService.getCitiesByRegion(region);

        // 2) Agrégation de toutes les annonces valides pour ces villes
        List<Ad> allAds = new ArrayList<>();
        for (City city : cities) {
            StringBuilder queryBuilder = new StringBuilder("{");
            // Use slug directly - no need to fetch domain from database
            queryBuilder.append("city:'").append(city.getSlug()).append("',");
            if (domainSlug != null && !domainSlug.isEmpty()) {
                queryBuilder.append("domain:'").append(domainSlug).append("',");
            }
            queryBuilder.append("enabled:true,");
            queryBuilder.append("state:'").append(Ad.VALID).append("'");
            queryBuilder.append("}");

            List<Ad> adsByCity = get(queryBuilder.toString(), null, "{creationDate:-1}", 1, Integer.MAX_VALUE);
            allAds.addAll(adsByCity);
        }

        // 3) Tri global par date de création (du plus récent au plus ancien)
        allAds.sort((a1, a2) -> a2.getCreationDate().compareTo(a1.getCreationDate()));

        // 4) Application de la pagination
        int startIndex = (pageNumber - 1) * numberOfAds;
        if (startIndex >= allAds.size()) {
            // page hors bornes → renvoyer une liste vide
            return Collections.emptyList();
        }
        int endIndex = Math.min(startIndex + numberOfAds, allAds.size());
        return allAds.subList(startIndex, endIndex);
    }

    @Override
    public List<Ad> getAllAds() {
        List<Ad> ads = get("{}", null, "{}", 0, 12);
        return ads != null ? ads : null;
    }

    @Override
    public List<AdDto> getAdsByDefaults(int pageNumber, int numberOfAds) {
        List<Ad> ads = get("{ state: \"" + Ad.VALID + "\",enabled: true}", null,
                "{announcetype: -1,stateRank: -1, creationDate: -1}", pageNumber, numberOfAds);
        return mapperAdDto.asDtos(ads);
    }

    @Override
    public List<Ad> getAdsByCity(String city, int pageNumber, int numberOfAds, String keyword) {
        String query = buildQuery(city, null, keyword);
        return get(query, null, "{creationDate: -1}", pageNumber, numberOfAds);
    }

    /**
     * Récupère les annonces par nom de ville et domaine (sans utiliser de slug)
     * @param cityName Le nom de la ville (ex: "Casablanca")
     * @param domainName Le nom du domaine (ex: "Informatique")
     * @param pageNumber Numéro de page
     * @param numberOfAds Nombre d'annonces par page
     * @param keyword Mot-clé optionnel pour la recherche
     * @return Liste des annonces DTO
     */
    public List<AdDto> getAdsWithDomainAndCityByName(String cityName,
                                                       String domainName,
                                                       int pageNumber,
                                                       int numberOfAds,
                                                       String keyword) {
        if (cityName == null || cityName.isEmpty()) {
            return Collections.emptyList();
        }

        if (keyword != null && !keyword.trim().isEmpty()) {
            try {
                // 1. Generate embedding for keyword
                com.centoria.jobmaroc.service.IAiService aiService = com.centoria.jobmaroc.service.impl.AiService.getInstance();
                List<Double> queryEmbedding = aiService.generateEmbedding(keyword);
                
                StringBuilder arrayStr = new StringBuilder("[");
                for (int i = 0; i < queryEmbedding.size(); i++) {
                    arrayStr.append(queryEmbedding.get(i));
                    if (i < queryEmbedding.size() - 1) arrayStr.append(",");
                }
                arrayStr.append("]");

                List<String> pipeline = new ArrayList<>();
                pipeline.add("{\"$vectorSearch\": {\"index\": \"vector_index\", \"path\": \"embedding\", \"queryVector\": " + arrayStr.toString() + ", \"numCandidates\": 100, \"limit\": " + (numberOfAds * 3) + "}}");
                pipeline.add("{\"$set\": {\"matchScore\": { \"$meta\": \"vectorSearchScore\" }}}");

                // Match conditions
                Document matchDoc = new Document();
                matchDoc.append("state", Ad.VALID);
                matchDoc.append("enabled", true);
                if (domainName != null && !domainName.isEmpty()) matchDoc.append("domain", domainName);
                if (cityName != null && !cityName.isEmpty()) matchDoc.append("city", cityName);
                
                pipeline.add("{\"$match\": " + matchDoc.toJson() + "}");
                
                int skip = (pageNumber - 1) * numberOfAds;
                pipeline.add("{\"$skip\": " + skip + "}");
                pipeline.add("{\"$limit\": " + numberOfAds + "}");

                List<Ad> ads = dao.aggregate(pipeline);
                return mapperAdDto.asDtos(ads);
            } catch (Exception e) {
                log.error("Vector search failed, falling back to standard search", e);
            }
        }

        String query = buildQuery(cityName, domainName, keyword);
        String sort = BO_LISTING_SORT_ALL;

        List<Ad> ads = get(query, null, sort, pageNumber, numberOfAds);
        return mapperAdDto.asDtos(ads);
    }

    /**
     * Compte les annonces par nom de ville et domaine (sans utiliser de slug)
     * @param cityName Le nom de la ville (ex: "Casablanca")
     * @param domainName Le nom du domaine (ex: "Informatique")
     * @param keyword Mot-clé optionnel pour la recherche
     * @return Nombre total d'annonces
     */
    public long countAdsWithDomainAndCityByName(String cityName,
                                                 String domainName,
                                                 String keyword) {
        if (cityName == null || cityName.isEmpty()) {
            return 0;
        }
        String query = buildQuery(cityName, domainName, keyword);
        return count(query);
    }

    @Override
    public List<AdDto> getSimilarAds(String id, String domain) {
        String query = "{$and:[ " +
                "{ _id: { $ne: { $oid: '" + id + "' } } }, " +
                "{domain:{ $eq: '" + domain + "'}}," +
                "{enabled:true,state:'" + Ad.VALID + "'}" +
                "]}";
        List<Ad> ads = get(query, null, "{announcetype: -1,stateRank: -1, creationDate: -1}", 1, 3);
        return mapperAdDto.asDtos(ads);
    }

    /* this function should be fixed */
    /* $text applied only on indexed field if there is no index in database it won't work */
    @Override
    public List<AdDto> getAdsByKeyword(String keyword, int pageNumber, int numberOfAds) {
        String query = "{$text:{$search:\"" + keyword + "\"}}";
        List<Ad> ads = get(query, null, null, pageNumber, numberOfAds);
        return mapperAdDto.asDtos(ads);
    }

    @Override
    public List<AdDto> getSemanticAdsByQuery(String query, int pageNumber, int numberOfAds) {
        try {
            // 1. Generate embedding for query
            com.centoria.jobmaroc.service.IAiService aiService = com.centoria.jobmaroc.service.impl.AiService.getInstance();
            List<Double> queryEmbedding = aiService.generateEmbedding(query);
            
            // Format array to JSON string for mongo query
            StringBuilder arrayStr = new StringBuilder("[");
            for (int i = 0; i < queryEmbedding.size(); i++) {
                arrayStr.append(queryEmbedding.get(i));
                if (i < queryEmbedding.size() - 1) {
                    arrayStr.append(",");
                }
            }
            arrayStr.append("]");

            // 2. Build aggregation pipeline
            List<String> pipeline = new ArrayList<>();
            
            // Stage 1: vector search
            String vectorSearch = "{" +
                "\"$vectorSearch\": {" +
                    "\"index\": \"vector_index\"," +
                    "\"path\": \"embedding\"," +
                    "\"queryVector\": " + arrayStr.toString() + "," +
                    "\"numCandidates\": 100," +
                    "\"limit\": " + (numberOfAds * 2) + // over-fetch slightly for filtering
                "}" +
            "}";
            pipeline.add(vectorSearch);

            // Stage 2: project score
            String setScore = "{" +
                "\"$set\": {" +
                    "\"matchScore\": { \"$meta\": \"vectorSearchScore\" }" +
                "}" +
            "}";
            pipeline.add(setScore);

            // Stage 3: match only valid/enabled ads
            String match = "{" +
                "\"$match\": {" +
                    "\"state\": \"" + Ad.VALID + "\"," +
                    "\"enabled\": true" +
                "}" +
            "}";
            pipeline.add(match);

            // Stage 4: skip & limit for pagination
            int skip = (pageNumber - 1) * numberOfAds;
            pipeline.add("{\"$skip\": " + skip + "}");
            pipeline.add("{\"$limit\": " + numberOfAds + "}");

            // 3. Execute query
            List<Ad> ads = dao.aggregate(pipeline);
            
            // Map and return
            return mapperAdDto.asDtos(ads);

        } catch (Exception e) {
            log.error("Semantic search failed, falling back to keyword search", e);
            return getAdsByKeyword(query, pageNumber, numberOfAds);
        }
    }

    private String buildQuery(String city, String domain, String keyword) {
        Document query = new Document();
        if (domain != null && !domain.isEmpty()) {
            query.append("domain", domain);
        }
        if (city != null && !city.isEmpty()) {
            query.append("city", city);
        }
        query.append("enabled", true);
        query.append("state", Ad.VALID);

        if (keyword != null) {
            String normalizedKeyword = keyword.trim().toLowerCase();
            if (!normalizedKeyword.isEmpty()) {
                String regexPattern = ".*" + Pattern.quote(normalizedKeyword) + ".*";
                Document regexDoc = new Document("$regex", regexPattern)
                        .append("$options", "i");

                List<Document> keywordConditions = new ArrayList<>();
                keywordConditions.add(new Document("title", regexDoc));
                keywordConditions.add(new Document("content", regexDoc));
                keywordConditions.add(new Document("tags", regexDoc));

                query.append("$or", keywordConditions);
            }
        }

        return query.toJson();
    }
}
