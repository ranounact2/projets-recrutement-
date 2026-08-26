package com.centoria.jobmaroc.web;

import com.centoria.jobmaroc.app.*;
import com.centoria.jobmaroc.common.ihm.IWebExecutor;
import com.centoria.jobmaroc.web.base.BaseController;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FrontController extends BaseController implements IWebExecutor {

    private final IndexApp indexApp = IndexApp.getInstance();

    private final ApplicantApp applicantApp = ApplicantApp.getInstance();
    private final BaseApp baseApp = BaseApp.getInstance();
    private final MoApp moApp = MoApp.getInstance();
    private final LinksApp linksApp = LinksApp.getInstance();

    @Override
    public void defineRoutes() {

//        Spark.get("/mentions-legales", (request, response) -> getBasePage("front/mention_legale.ftl", null, request), TEMPLATEENGINE);
//        Spark.get("/a-propos-emplois-maroc", (request, response) -> getBasePage("front/about.ftl", null, request), TEMPLATEENGINE);
//        Spark.get("/", this::index, TEMPLATEENGINE);
//        Spark.get("/keyword", this::showResultByKeyword, TEMPLATEENGINE);
//        /* get offer by domain pagination query params. */
//        Spark.path("/" + UrlConst.CATEGORY, () -> {
//            /* Affiche tous les jobs */
//            Spark.get("", this::showAllDomains, TEMPLATEENGINE);
//            Spark.get("/", this::showAllDomains, TEMPLATEENGINE);
//            Spark.get("/:domain", this::showResultByCategory, TEMPLATEENGINE);
//            Spark.get("/:domain/", this::showResultByCategory, TEMPLATEENGINE);
//            Spark.get("/:domain/:region", this::showTopCitiesByRegionAndAds, TEMPLATEENGINE);
//            Spark.get("/:domain/:region/", this::showTopCitiesByRegionAndAds, TEMPLATEENGINE);
//            Spark.get("/:domain/:region/:city", this::showResultByCityCategory, TEMPLATEENGINE);
//            Spark.get("/:domain/:region/:city/", this::showResultByCityCategory, TEMPLATEENGINE);
//            Spark.get("/:domain/:city", this::showResultByCityCategory, TEMPLATEENGINE);
//            Spark.get("/:domain/:city/", this::showResultByCityCategory, TEMPLATEENGINE);
//        });
//        /*  get offer by city pagination query params */
//        Spark.get("/" + UrlConst.VILLE, this::showAllCities, TEMPLATEENGINE);
//        Spark.get("/" + UrlConst.VILLE + "/", this::showAllCities, TEMPLATEENGINE);
//        /* get offer by city pagination query params */
//        Spark.get("/" + UrlConst.VILLE + "/:city", this::showResultByCity, TEMPLATEENGINE);
//        Spark.get("/" + UrlConst.VILLE + "/:city" + "/", this::showResultByCity, TEMPLATEENGINE);
//        Spark.get("/" + UrlConst.CONTACT, this::showContact, TEMPLATEENGINE);
//        Spark.post("/contact", this::sendContact, TEMPLATEENGINE);
//        /* detail job with similar Jobs */
//        Spark.get("/offre-emploi-maroc/:id", this::jobDetail, TEMPLATEENGINE);
//        Spark.get("/postuler-emploi/:id", (request, response) -> showApply(request, response, null), TEMPLATEENGINE);
//        Spark.post("/postuler-emploi/:id", this::apply, TEMPLATEENGINE);
//        /* this route used also for update job */
//        Spark.post("/ajouter-offre-emploi", this::addJob, TEMPLATEENGINE);
//        /*  add jobs page */
//        Spark.get("/ajouter-offre-emploi", this::showAdJob, TEMPLATEENGINE);
//        Spark.path("/" + UrlConst.REGION, () -> {
//            Spark.get("", this::showAllRegion, TEMPLATEENGINE);
//            Spark.get("/", this::showAllRegion, TEMPLATEENGINE);
//            Spark.get("/:region", this::showTopCitiesByRegion, TEMPLATEENGINE);
//            Spark.get("/:region/", this::showTopCitiesByRegion, TEMPLATEENGINE);
//            Spark.get("/:region/:city", this::showAllDomainsByRegionCity, TEMPLATEENGINE);
//            Spark.get("/:region/:city/", this::showAllDomainsByRegionCity, TEMPLATEENGINE);
//        });
//
//        Spark.get("/err/", (request, response) -> {
//            throw new IOException();
//        }, TEMPLATEENGINE);
//        Spark.exception(Exception.class, (e, request, response) -> {
//            log.error("Exception occurred", e);
//            log.error(e.getMessage());
//        });
//
//        Spark.exception(BusinessException.class, (e, request, response) -> {
//            log.error(e.getMessage());
//            response.status(501);
//            response.body("something incorrect happen");
//        });
//
//        Spark.exception(ElementNotFoundException.class, (e, request, responce) -> {
//            log.error(e.getMessage());
//        });
//        Spark.exception(TechnicalException.class, (e, request, response) -> {
//            log.error(e.getMessage());
//            response.status(500);
//            response.body("technical error");
//        });
//        Spark.get("/500", this::show500, TEMPLATEENGINE);
//        Spark.notFound(this::show404);
    }


//    private ModelAndView showResultByKeyword(Request request, Response response) throws BusinessException, TechnicalException {
//        Map<String, Object> map = getMap(request);
//        request.attribute("route", "/keyword");
//        String keyword = request.queryParams("k");
//        SearchResultAdDto result = null;
//        result = indexApp.searchAdsByKeyword(keyword, 1, 8);
//        String[] params = {"keyword"};
//        /* this should be fixed later*/
//        addPagination(new InputSearchAdDTO(), result, "/keyword", params, request);
//        map.put("data", result);
//        return getBasePage("front/searchBykeywordResult.ftl", map, request);
//        /* Put the route in request for zonepages */
//    }
//
//
//    private ModelAndView showContact(Request request, Response response) {
//        Map<String, Object> map = getMap(request);
//        ContactDto contactDto = new ContactDto();
//        map.put("captcha", contactDto.getCaptcha());
//        return getBasePage("front/contact.ftl", map, request);
//
//    }
//
//
//    private ModelAndView showAllCities(Request request, Response response) throws BusinessException, TechnicalException {
//        Map<String, Object> map = getMap(request);
//        request.attribute("route", "/" + UrlConst.VILLE + "/");
//        SearchResultAdDto result = null;
//        result = linksApp.allCitiesPage();
//        map.put("data", result);
//        return getBasePage("front/result.ftl", map, request);
//        /* Put the route in request for zonepages */
//    }
//
//
//    private ModelAndView apply(Request request, Response response) throws BusinessException, TechnicalException, ServletException, IOException {
//        String template = "front/message.ftl";
//        Map<String, Object> map = getMap(request);
//        String host = request.scheme() + "://" + request.host();
//        String jobId = request.params("id");
//        MultipartConfigElement multipartConfigElement = new MultipartConfigElement(
//                System.getProperty("java.io.tmpdir")); // allow upload spark
//        request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
//        Part file = null;
//        ApplicantDto inputDto = null;
//        ApplicantDto outputDto = null;
//        file = request.raw().getPart("uploaded_file");
//        inputDto = new ApplicantDto(file, requestPartToMap(request, "nom", "prenom", "email",
//                "phone", "motivation", "experienceLevel", "g-recaptcha-response", "formation"));
//        inputDto.setSiteUrl(host);
//        inputDto.setJobId(jobId);
//        outputDto = applicantApp.add(inputDto);
//        map.put("data", outputDto);
//        if (outputDto == null || outputDto.getErrors() != null && !outputDto.getErrors().isEmpty()) {
//            Map<String, String> errors = null;
//            if (outputDto.getErrors() != null) {
//                errors = outputDto.getErrors();
//
//            } else {
//                errors = new HashMap<>();
//                errors.put("message", "Quelque chose c'est mal passé, essayez plus tard ou contactez l'admin du site");
//
//            }
//            return showApply(request, response, errors);
//        }
//        return getBasePage(template, map, request);
//    }
//
//    private ModelAndView showApply(Request request, Response response, Map<String, String> errors) throws BusinessException, TechnicalException {
//        Map<String, Object> map = getMap(request);
//        ApplicantDto applicantDto = new ApplicantDto();
//        applicantDto.setWithOffers(false);
//        applicantDto.setInputKey(request.params("id"));
//        ApplicantDto result = null;
//        result = applicantApp.getByJobId(applicantDto);
//        if (result != null && errors != null && !errors.isEmpty()) {
//            result.setErrors(errors);
//        }
//        map.put("data", result);
//        return getBasePage("front/add-applicant.ftl", map, request);
//    }
//
//    private ModelAndView showResultByCityCategory(Request request, Response response)
//            throws TechnicalException, BusinessException {
//
//        String domainSlug = request.params("domain");
//        String citySlug = request.params("city");
//        String regionSlug = request.params("region");
//
//        Integer page = request.queryParams("page") != null ? Integer.valueOf(request.queryParams("page")) : 1;
//        Integer itemPage = request.queryParams("itemPage") != null ? Integer.valueOf(request.queryParams("itemPage")) : 5;
//
//        SearchResultAdDto result = ResultApp.getInstance().getResultByCityCategory(domainSlug, citySlug, regionSlug, page, itemPage);
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("data", result);
//        map.put("domainSlug", domainSlug);
//        map.put("breadCrumb", result.getBreadCrumb());
//
//        return getBasePage("front/result.ftl", map, request);
//    }
//
//
//    private ModelAndView showResultByCity(Request request, Response response) throws BusinessException, TechnicalException {
//        Map<String, Object> map = getMap(request);
//        /* Put the route in request for zonepages */
//        request.attribute("route", "/" + UrlConst.VILLE + "/:city");
//        InputSearchAdDTO inputSearchAdDTO = InputSearchAdDTO.builder().citySlug(request.params("city")).build();
//        SearchResultAdDto result = indexApp.cityAdPage(request.params("city"), 1, 8);
//        map.put("data", result);
//        return getBasePage("front/result.ftl", map, request);
//    }
//
//    private ModelAndView showResultByCategory(Request request, Response response) throws TechnicalException, BusinessException {
//        Map<String, Object> map = getMap(request);
//        String domainSlug = request.params("domain");
//        List<LinkDTO> breadCrumbDto = baseApp.buildBreadCrumb(domainSlug, null, null, "/" + UrlConst.CATEGORY);
//
//        request.attribute("route", "/" + UrlConst.CATEGORY + "/:domain");
//        SearchResultAdDto result = null;
//        result = linksApp.singleDomainPage(domainSlug);
//        map.put("domainSlug", domainSlug);
//        map.put("data", result);
//        map.put("linkDto", breadCrumbDto);
//        return getBasePage("front/result.ftl", map, request);
//    }
//
//    private ModelAndView showAllDomains(Request request, Response response) throws TechnicalException, BusinessException {
//        Map<String, Object> map = getMap(request);
//        List<LinkDTO> breadCrumb = baseApp.buildBreadCrumb(null, null, null, "/" + UrlConst.CATEGORY);
//
//        request.attribute("route", "/" + UrlConst.CATEGORY);
//        SearchResultAdDto result = null;
//        result = linksApp.allDomainsPage();
//
//        map.put("linkDto", breadCrumb);
//        map.put("data", result);
//        return getBasePage("front/result.ftl", map, request);
//    }


//    private ModelAndView index(Request request, Response response) throws BusinessException, TechnicalException {
//        request.attribute("route", "/");
//        Map<String, Object> map = request.attribute("data") != null ? request.attribute("data") : new HashMap<>();
//        List<Region> regions = linksApp.getAllRegion();
//
//        SearchResultAdDto result = indexApp.indexPage(1, 8);
//        map.put("data", result);
//        map.put("regions", regions);
//        return getBasePage("front/index.ftl", map, request);
//    }

//    private ModelAndView addJob(Request request, Response response) {
//        System.out.println("show job ");
//        Map<String, Object> map = getMap(request);
//        AdWithReferenceDataDto jobDto = new AdWithReferenceDataDto();
//        // By adding a few lines of code to add the multipart config, you can handle
//        // multipart / form-data without an external library
//        MultipartConfigElement multipartConfigElement = new MultipartConfigElement(
//                System.getProperty("java.io.tmpdir"));
//        request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
//        String template = "front/message.ftl";
//        Part file;
//        try {
//            file = request.raw().getPart("logoImage");
//            jobDto.fillFromParams(file,
//                    requestPartToMap(request, "key", "domain", "city", "title", "type", "content", "email", "tel",
//                            "confidentialite", "g-recaptcha-response", "companyName", "companyCode", "nbrDePostes",
//                            "formation", "experienceLevel", "adtype", "facebook", "twitter", "linkedin"));
//            jobDto.setSiteUrl(request.scheme() + "://" + request.host());
//            /*
//             * le dto en Sortie
//             */
//            AdWithReferenceDataDto result = moApp.addOrUpdate(jobDto);
//            /*
//             * TODO : Temporary fix
//             * Partie à refaire complètement dans tout le code
//             * Gestion de l'alimentation des forms, des erreurs et de la validation à supprimer et refaire
//             */
//            if (file.getSize() / (1024 * 1024) > 4) {
//                result.getErrors().put("logoImage", "taille maximale du logo 3.5 Mo");
//            }
//            System.out.println(result.getErrors());
//            if (!result.getErrors().isEmpty()) {
//                template = "front/offre.ftl";
//            }
//            SearchResultAdDto oldResult = null;
//            oldResult = applicantApp.getReferenceData(null);
//            oldResult.setJobs(List.of(new AdDto(), new AdDto(), new AdDto()));
//            map.put("data", result);
//        } catch (IOException e) {
//            log.error("Error processing file upload", e);
//            /*
//             * Ajout d'un message spécifique
//             */
//            template = "front/offre.ftl";
//        } catch (ServletException | BusinessException | TechnicalException e) {
//            log.error("Error processing job offer", e);
//            template = "front/offre.ftl";
//        }
//        return getBasePage(template, map, request);
//    }
//
//    private ModelAndView sendContact(Request request, Response response) throws BusinessException, TechnicalException {
//        Map<String, Object> map = getMap(request);
//        String host = request.scheme() + "://" + request.host();
//        ContactDto contactDto = new ContactDto(
//                requestToMap(request, "name", "email", "phone", "objet", "message", "g-recaptcha-response"));
//        contactDto.setSiteUrl(host);
//        ContactDto conDto;
//        conDto = moApp.sendContactMail(contactDto);
//        System.out.println(request.params("g-recaptcha-response"));
//        map.put("captcha", contactDto.getCaptcha());
//        map.put("data", conDto);
//        return getBasePage("front/contact.ftl", map, request);
//    }
//
//
//    private ModelAndView jobDetail(Request request, Response response) throws BusinessException, TechnicalException {
//        Map<String, Object> map = getMap(request);
//        AdDisplayDto jobDto = new AdDisplayDto();
//        jobDto.setInputKey(request.params("id"));
//        AdDisplayDto result = moApp.getById(jobDto);
//        map.put("data", result);
//        return getBasePage("front/job-detail.ftl", map, request);
//    }
//
//    private ModelAndView showAdJob(Request request, Response response) throws BusinessException, TechnicalException {
//        Map<String, Object> map = getMap(request);
//        SearchResultAdDto result = null;
//        InputSearchAdDTO inputSearchAdDTO = InputSearchAdDTO.builder()
//                .id(request.params("id"))
//                .build();
//        result = applicantApp.getReferenceData(inputSearchAdDTO);
//        map.put("data", result);
//        return getBasePage("front/offre.ftl", map, request);
//    }
//
//    private ModelAndView showAllRegion(Request request, Response response) throws BusinessException, TechnicalException {
//        Map<String, Object> map = getMap(request);
//        List<LinkDTO> breadCrumbDto = baseApp.buildBreadCrumb(null, null, null, "/" + UrlConst.REGION);
//
//        request.attribute("route", "/" + UrlConst.REGION);
//        List<Region> regions = linksApp.getAllRegion();
//
//        map.put("allRegion", regions);
//        map.put("linkDto", breadCrumbDto);
//        return getBasePage("front/result.ftl", map, request);
//    }
//
//
//    private ModelAndView showTopCitiesByRegion(Request request, Response response) throws BusinessException, TechnicalException {
//        String regionSlug = request.params("region");
//        String domainSlug = request.params("domain");
//        List<LinkDTO> breadCrumbDto = baseApp.buildBreadCrumb(domainSlug, null, regionSlug, "/" + UrlConst.REGION);
//        Map<String, Object> map = new HashMap<>();
//        List<City> cities = linksApp.getCitiesByRegion(regionSlug);
//
//        map.put("allCities", cities);
//        map.put("linkDto", breadCrumbDto);
//        map.put("regionSlug", regionSlug);
//        map.put("domainSlug", domainSlug);
//        return getBasePage("front/result.ftl", map, request);
//    }
//
//    private ModelAndView showTopCitiesByRegionAndAds(Request request, Response response) throws BusinessException, TechnicalException {
//        String regionSlug = request.params("region");
//        String domainSlug = request.params("domain");
//        List<LinkDTO> breadCrumbDto = baseApp.buildBreadCrumb(domainSlug, null, regionSlug, "/" + UrlConst.REGION);
//        Map<String, Object> map = new HashMap<>();
//        Integer page = request.queryParams("page") != null ? Integer.valueOf(request.queryParams("page")) : 1;
//        Integer itemPage = request.queryParams("itemPage") != null ? Integer.valueOf(request.queryParams("itemPage")) : 5;
//        SearchResultAdDto result = null;
//        result = ResultApp.getInstance().getAdsAndCitiesByRegion(domainSlug, regionSlug, page, itemPage);
//        map.put("linkDto", breadCrumbDto);
//        map.put("domainSlug", domainSlug);
//        map.put("regionSlug", regionSlug);
//        map.put("jobs", result);
//        return getBasePage("front/result.ftl", map, request);
//    }
//
//    private ModelAndView showAllDomainsByRegionCity(Request request, Response response) {
//        String regionSlug = request.params("region");
//        String citySlug = request.params("city");
//
//        List<LinkDTO> breadCrumbDto = baseApp.buildBreadCrumb(null, citySlug, regionSlug, "/" + UrlConst.REGION);
//        SearchResultAdDto result = linksApp.allDomainsPage();
//        Map<String, Object> map = getMap(request);
//        map.put("data", result);
//        map.put("linkDto", breadCrumbDto);
//        map.put("regionSlug", regionSlug);
//        map.put("citySlug", citySlug);
//
//        return getBasePage("front/result.ftl", map, request);
//    }


}
