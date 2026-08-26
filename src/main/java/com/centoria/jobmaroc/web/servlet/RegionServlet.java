package com.centoria.jobmaroc.web.servlet;

import com.centoria.jobmaroc.app.BaseApp;
import com.centoria.jobmaroc.app.LinksApp;
import com.centoria.jobmaroc.model.exception.BusinessException;
import com.centoria.jobmaroc.model.exception.TechnicalException;
import com.centoria.jobmaroc.common.globalData.UrlConst;
import com.centoria.jobmaroc.dto.LinkDTO;
import com.centoria.jobmaroc.model.Ad;
import com.centoria.jobmaroc.model.Region;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegionServlet extends BaseServlet {
    private final LinksApp linksApp = LinksApp.getInstance();
    private final BaseApp baseApp = BaseApp.getInstance();

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        // 1) Anti-boucle
        if (req.getAttribute("processed") != null) {
            showError(500, "Processing loop detected", req, resp);
            return;
        }
        req.setAttribute("processed", true);

        // 2) Découpe du pathInfo
        String pathInfo = req.getPathInfo();
        String clean = (pathInfo == null ? "" : pathInfo.replaceAll("^/|/$", ""));
        String[] parts = clean.isEmpty() ? new String[0] : clean.split("/");

        try {
            if (parts.length == 0) {
                // /region  → liste des régions
                List<Region> allRegions = linksApp.getAllRegion();
                List<Ad> allAds = linksApp.getAllAds();
                List<LinkDTO> bc = baseApp.buildBreadCrumb(
                        null, null, null, "/" + UrlConst.REGION
                );
                Map<String, Object> model = new HashMap<>();
                model.put("allRegion", allRegions);
                model.put("allAds", allAds);
                model.put("linkDto", bc);
                dispatch(req, resp, "front/result.ftl", model);

            } else if (parts.length == 1) {
                new RegionByCityServlet()
                        .showTopCitiesByRegion(req, resp, parts[0]);

            } else if (parts.length == 2) {
                // /region/{region}/{city} → délègue à RegionByCityByDomainServlet
                new RegionByCityByDomainServlet()
                        .showAllDomainsByRegionCity(req, resp, parts[0], parts[1]);

            } else {
                // trop de segments → 404
                showError(404, getMessage("error.page.notFound"), req, resp);
            }

        } catch (BusinessException e) {
            showError(500, getMessage("error.business") + " " + e.getMessage(), req, resp);
        } catch (TechnicalException e) {
            showError(500, getMessage("error.technical") + " " + e.getMessage(), req, resp);
        }
    }
}