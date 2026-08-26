package com.centoria.jobmaroc.web.servlet;

import com.centoria.jobmaroc.app.ResultApp;
import com.centoria.jobmaroc.dto.SearchResultAdDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CatgoryByDomainByCityServlet extends BaseServlet {
    private final ResultApp resultApp = ResultApp.getInstance();

    public void showResultByCityCategory(HttpServletRequest req,
                                         HttpServletResponse resp,
                                         String domainSlug,
                                         String regionSlug,
                                         String citySlug)
            throws ServletException, IOException {

        // paramètres de pagination
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

        // appel métier
        SearchResultAdDto result = ResultApp
                .getInstance()
                .getResultByCityCategory(domainSlug,
                        citySlug,
                        regionSlug,
                        page,
                        itemPage,
                        keyword);

        // construction du modèle
        Map<String, Object> model = new HashMap<>();
        model.put("resultat", result);
        model.put("domainSlug", domainSlug);
        model.put("regionSlug", regionSlug);
        model.put("citySlug", citySlug);
        model.put("breadCrumbResultat", result.getBreadCrumb());

        // rendu FreeMarker
        dispatch(req, resp, "front/result.ftl", model);
    }
}