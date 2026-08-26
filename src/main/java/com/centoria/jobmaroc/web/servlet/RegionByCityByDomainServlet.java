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

public class RegionByCityByDomainServlet extends BaseServlet {

    private final LinksApp linksApp = LinksApp.getInstance();
    private final BaseApp baseApp = BaseApp.getInstance();

    /**
     * Affiche les domaines d’une ville dans une région
     * (/region/{region}/{city})
     */
    public void showAllDomainsByRegionCity(HttpServletRequest req,
                                           HttpServletResponse resp,
                                           String regionSlug,
                                           String citySlug)
            throws ServletException, IOException {

        int page = 1;
        int size = 5;
        try {
            String pageParam = req.getParameter("page");
            String sizeParam = req.getParameter("size");
            if (pageParam != null) {
                page = Integer.parseInt(pageParam);
            }
            if (sizeParam != null) {
                size = Integer.parseInt(sizeParam);
            }
        } catch (NumberFormatException ignore) {
            page = 1;
            size = 5;
        }

        String keyword = req.getParameter("keyword");
        if (keyword != null) {
            keyword = keyword.trim();
            if (keyword.isEmpty()) {
                keyword = null;
            }
        }

        SearchResultAdDto result = linksApp.getDomainsAndAdsByRegionCity(regionSlug, citySlug, page, size, keyword);
        List<LinkDTO> breadCrumbDto = baseApp.buildBreadCrumb(null, citySlug, regionSlug, "/" + UrlConst.REGION);

        Map<String, Object> map = new HashMap<>();
//        model.put("domains",    domains);
        map.put("data", result);
        map.put("linkDto", breadCrumbDto);
        map.put("regionSlug", regionSlug);
        map.put("citySlug", citySlug);

        dispatch(req, resp, "front/result.ftl", map);
    }
}
