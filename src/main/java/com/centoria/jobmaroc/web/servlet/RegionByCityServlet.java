package com.centoria.jobmaroc.web.servlet;

import com.centoria.jobmaroc.app.BaseApp;
import com.centoria.jobmaroc.app.LinksApp;
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


public class RegionByCityServlet extends BaseServlet {

    private final LinksApp linksApp = LinksApp.getInstance();
    private final BaseApp baseApp = BaseApp.getInstance();

    /**
     * Affiche les villes d’une région (/region/{region})
     */
    public void showTopCitiesByRegion(HttpServletRequest req,
                                      HttpServletResponse resp,
                                      String regionSlug)
            throws ServletException, IOException {

        String domainSlug = req.getParameter("domain");

        // Récupération des paramètres de pagination (avec valeurs par défaut)
        int page = 1;
        int size = 5;
        try {
            String p = req.getParameter("page");
            String s = req.getParameter("size");
            if (p != null) page = Integer.parseInt(p);
            if (s != null) size = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            // tu peux logger ou laisser les valeurs par défaut
        }

        // Appel de la méthode avec pagination
        SearchResultAdDto result = linksApp.getCitiesAndAdsByRegion(regionSlug, page, size, domainSlug);

        List<LinkDTO> bc = baseApp.buildBreadCrumb(
                domainSlug, null, regionSlug, "/" + UrlConst.REGION
        );

        Map<String, Object> model = new HashMap<>();
        model.put("allCities", result.getCities());
        model.put("domainAds",     result.getDomains());
        model.put("linkDto",    bc);
        model.put("regionSlug", regionSlug);
        model.put("domainSlug", domainSlug);

        dispatch(req, resp, "front/result.ftl", model);
    }
}
