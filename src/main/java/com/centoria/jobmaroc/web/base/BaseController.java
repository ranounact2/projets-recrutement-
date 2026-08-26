package com.centoria.jobmaroc.web.base;

/**
 * Base class for controllers.
 *
 * @author centoria
 */

public abstract class BaseController {

    // @TODO delete after keep now to move method in the new BaseServlet

//    public final static TemplateEngine TEMPLATEENGINE = new FreeMarkerEngine();
//
//    protected Gson gson = new Gson();
//    protected I18n i18n = new I18n();
//    private ZonePageApp zonePageApp = ZonePageApp.getInstance();
//
//    public ModelAndView getBasePage(String path, Map<String, Object> map, Request request) {
//        String hostUrl = request.url();
//        String url = request.attribute("route");
//
//        if (map == null) {
//            map = getMap(request);
//        }
//        map.put("host", hostUrl);
//
//        if (!request.cookies().containsKey("local")) {
//            i18n.setDefaultLocale(new Locale("fr"));
//            map.put("lang", "ar");
//        } else {
//            if ("ar".equals(request.cookies().get("local"))) {
//                i18n.setDefaultLocale(new Locale("ar"));
//                map.put("lang", "fr");
//            } else {
//                i18n.setDefaultLocale(new Locale("fr"));
//                map.put("lang", "ar");
//            }
//        }
//        map.put("utils", new I18n());
//        ModelAndView mv = new ModelAndView(map, path);
//
//        if (url != null && !url.contains("/assets") && !url.contains("/public")) {
//
//            String page = mv.getViewName();
//
//            /*
//             * Build vars
//             */
//            Map<String, String> params = request.params();
//            Map<String, String[]> queryParams = request.queryMap().toMap();
//            /*
//             * Faire un méthode spécifique pour traiter les paramètres et renvoyer le bon zonePage
//             * zp.zones.head.values.meta_description.value
//             */
//            List<PageZones> zones;
//            try {
//                zones = zonePageApp.search(page, url, params, queryParams);
//                map.put("zpJson", gson.toJson(zones));
//            } catch (TechnicalException | BusinessException e) {
//                // TODO Auto-generated catch block
//                log.error("Error processing zone pages", e);
//            }
//        }
//        return mv;
//    }
//
//    protected String show404(Request request, Response response) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("error", "Erreur 404, page introuvable");
//        map.put("code", "404");
//        ModelAndView mv = getBasePage("errors/error-page.ftl", map, request);
//        return TEMPLATEENGINE.render(mv);
//    }
//
//    protected ModelAndView show500(Request request, Response response) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("error", "500 erreur interne du serveur");
//        map.put("code", "500");
//        return getBasePage("errors/error-page.ftl", map, request);
//    }
//
//    protected Map<String, String> requestToMap(Request request, String... fields) {
//        Map<String, String> map = new HashMap<>();
//        for (int i = 0; i < fields.length; i++) {
//            String value = request.queryParams(fields[i]);
//            if (value != null) {
//                map.put(fields[i], value);
//            }
//        }
//        return map;
//    }
//
//    protected Map<String, String> requestPartToMap(HttpServletRequest request, String... fields)
//            throws IOException, ServletException {
//        Map<String, String> map = new HashMap<>();
//
//        for (int i = 0; i < fields.length; i++) {
//            String value = request.getParameter(fields[i]);
//            if (value != null) {
//                map.put(fields[i], value);
//            }
//        }
//
//        return map;
//    }
//
//    protected Map<String, String> requestPartToMap(Request request, String... fields)
//            throws IOException, ServletException {
//        Map<String, String> map = new HashMap<>();
//
//        for (int i = 0; i < fields.length; i++) {
//            Part part = request.raw().getPart(fields[i]);
//            String value = null;
//            if (part != null && part.getSize() > 0) {
//                try {
//                    value = IOUtils.toString(part.getInputStream());
//                } catch (Exception e) {
//                    log.error("Error reading part input stream", e);
//                }
//            }
//            if (value != null) {
//                map.put(fields[i], value);
//            }
//        }
//
//        return map;
//    }
//
//
//    protected Map<String, Object> getMap(Request request) {
//        Map<String, Object> map = request.attribute("map");
//        if (map == null) {
//            map = new HashMap<>();
//            request.attribute("map", map);
//        }
//        return map;
//    }
//
//    /**
//     * Does not work for pagination POST Form result.
//     *
//     * @param inputDto
//     * @param result
//     * @param url
//     * @param paramKeys
//     * @param request
//     */
//    protected void addPagination(InputSearchAdDTO inputDto, SearchResultAdDto result, String url, String[] paramKeys,
//                                 Request request) {
//        int nextPage = inputDto.getPage() + 1;
//        int prevPage = inputDto.getPage() - 1;
//        /*
//         * Put existing params in url to keep form param (get method) or url params this
//         * method does not work for paginating POST Forms
//         */
//        String baseLink = url;
//        String prevLink = baseLink;
//        String nextLink = baseLink;
//
//        if (paramKeys != null && paramKeys.length > 0) {
//            for (int i = 0; i < paramKeys.length; i++) {
//                if (i == 0) {
//                    nextLink += "?";
//                    prevLink += "?";
//                } else {
//                    nextLink += "&";
//                    prevLink += "&";
//                }
//                nextLink += paramKeys[i] + "=" + (request.queryParams(paramKeys[i]) != null ? request.queryParams(paramKeys[i]) : "");
//                prevLink += paramKeys[i] + "=" + (request.queryParams(paramKeys[i]) != null ? request.queryParams(paramKeys[i]) : "");
//            }
//            nextLink += "&";
//            prevLink += "&";
//        } else {
//            prevLink += "?";
//            nextLink += "?";
//        }
//
//        /*
//         * Add next page in link
//         */
//        if (result.getJobs() != null && result.getJobs().size() >= AbstractResultDto.DEFAULT_PAGE_SIZE) {
//            nextLink += "page=" + nextPage;
//            LinkDTO next = LinkDTO.builder().link(nextLink).build();
//            result.setNext(next);
//        }
//
//        /*
//         * Add prevPage
//         */
//        if (inputDto.getPage() > 1) {
//            prevLink += "page=" + prevPage;
//            LinkDTO prev = LinkDTO.builder().link(prevLink).build();
//            result.setPrev(prev);
//        }
//    }
//
//    protected Map<String, Object> extractAdDetails(AdResult adResult, int pageNumber, int adsPerPage){
//        Map<String, Object> map = new HashMap<>();
//        long totalAds = adResult.getTotalAds();
//        List<AdDto> currentAds = adResult.getAds();
//        int totalPages = (int) Math.ceil((double) totalAds/ adsPerPage);
//        map.put("ads", currentAds);
//        map.put("pageNumber", pageNumber);
//        map.put("adsPerPage", adsPerPage);
//        map.put("totalPages", totalPages);
//        return map;
//    }

}