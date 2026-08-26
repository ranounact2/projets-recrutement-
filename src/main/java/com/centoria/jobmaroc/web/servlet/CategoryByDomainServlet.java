package com.centoria.jobmaroc.web.servlet;

import com.centoria.jobmaroc.app.BaseApp;
import com.centoria.jobmaroc.app.LinksApp;
import com.centoria.jobmaroc.model.exception.BusinessException;
import com.centoria.jobmaroc.model.exception.TechnicalException;
import com.centoria.jobmaroc.common.globalData.UrlConst;
import com.centoria.jobmaroc.dto.LinkDTO;
import com.centoria.jobmaroc.dto.SearchResultAdDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryByDomainServlet extends BaseServlet {
    private final LinksApp linksApp = LinksApp.getInstance();
    private final BaseApp baseApp = BaseApp.getInstance();

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        // 1) Anti‐boucle
        if (req.getAttribute("processed") != null) {
            showError(500, "Processing loop detected", req, resp);
            return;
        }
        req.setAttribute("processed", true);

        // 2) Découpe du pathInfo
        String pathInfo = req.getPathInfo();                 // ex. "/informatique/ile-de-france" ou "/informatique/ile-de-france/paris"
        String clean = (pathInfo == null ? "" : pathInfo.replaceAll("^/|/$", ""));
        String[] parts = clean.isEmpty() ? new String[0] : clean.split("/");

        try {
            if (parts.length == 1) {
                // /categorie/{domain}
                showResultByCategory(req, resp, parts[0]);

            } else if (parts.length == 2) {
                // /categorie/{domain}/{region}
                showTopCitiesByDomain(req, resp, parts[0], parts[1]);

            } else if (parts.length == 3) {
                // /categorie/{domain}/{region}/{city}
                new CatgoryByDomainByCityServlet()
                        .showResultByCityCategory(req, resp,
                                parts[0], parts[1], parts[2]);

            } else {
                showError(404, getMessage("error.page.notFound"), req, resp);
            }

        } catch (BusinessException e) {
            showError(500, getMessage("error.business") + " " + e.getMessage(), req, resp);
        } catch (TechnicalException e) {
            showError(500, getMessage("error.technical") + " " + e.getMessage(), req, resp);
        }
    }


    /**
     * /categorie/{domain}
     */
    private void showResultByCategory(HttpServletRequest req,
                                      HttpServletResponse resp,
                                      String domainSlug)
            throws ServletException, IOException {

        // Extraire les paramètres de pagination et keyword
        int page = req.getParameter("page") != null
                ? Integer.parseInt(req.getParameter("page"))
                : 1;
        int itemPage = req.getParameter("itemPage") != null
                ? Integer.parseInt(req.getParameter("itemPage"))
                : 5;
        
        String keyword = req.getParameter("keyword");
        if (keyword != null) {
            keyword = keyword.trim();
            if (keyword.isEmpty()) {
                keyword = null;
            }
        }

        // 1) Fil d'Ariane
        List<LinkDTO> breadCrumb = baseApp.buildBreadCrumb(
                domainSlug, null, null, "/" + UrlConst.CATEGORY
        );

        // 2) Données du domaine avec keyword
        SearchResultAdDto result = linksApp.singleDomainPage(domainSlug, page, itemPage, keyword);

        // 3) Modèle FreeMarker
        Map<String, Object> model = new HashMap<>();
        model.put("domainSlug", domainSlug);
        model.put("linkDto", breadCrumb);
        model.put("data", result);  // Pour l'affichage des domaines
        model.put("resultat", result);  // Pour l'affichage des annonces (jobs)

        // 4) Rendu
        dispatch(req, resp, "front/result.ftl", model);
    }

    /**
     * /categorie/{domain}/{region}
     */
    private void showTopCitiesByDomain(HttpServletRequest req,
                                       HttpServletResponse resp,
                                       String domainSlug,
                                       String regionSlug)
            throws ServletException, IOException {

        int page = req.getParameter("page") != null
                ? Integer.parseInt(req.getParameter("page"))
                : 1;
        int itemPage = req.getParameter("itemPage") != null
                ? Integer.parseInt(req.getParameter("itemPage"))
                : 5;
        // 1) Fil d’Ariane
        List<LinkDTO> breadCrumb = baseApp.buildBreadCrumb(
                domainSlug, null, regionSlug, "/" + UrlConst.CATEGORY
        );

        // 2) Chargement des villes
        SearchResultAdDto result = linksApp.getCitiesAndAdsByRegion(regionSlug, page, itemPage, domainSlug);
        // 3) Modèle FreeMarker
        Map<String, Object> model = new HashMap<>();
        model.put("dataAdsAndRegion", result);
        model.put("domainSlug", domainSlug);
        model.put("regionSlug", regionSlug);
        model.put("linkDto", breadCrumb);
        // 4) Rendu
        dispatch(req, resp, "front/result.ftl", model);
    }
}
