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

public class CategoryServlet extends BaseServlet {
    private final LinksApp linksApp = LinksApp.getInstance();
    private final BaseApp baseApp = BaseApp.getInstance();

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            // 1) Construire le fil d’Ariane
            List<LinkDTO> breadCrumb = baseApp.buildBreadCrumb(
                    null, null, null, "/" + UrlConst.CATEGORY
            );

            // 2) Récupérer tous les domaines
            SearchResultAdDto result = linksApp.allDomainsPage();


            // 3) Préparer le modèle
            Map<String, Object> model = new HashMap<>();

            model.put("linkDto", breadCrumb);
            model.put("data", result);

            // 4) Rendre le template FreeMarker
            dispatch(req, resp, "front/result.ftl", model);

        } catch (BusinessException e) {
            showError(500, getMessage("error.business") + " " + e.getMessage(), req, resp);
        } catch (TechnicalException e) {
            showError(500, getMessage("error.technical") + " " + e.getMessage(), req, resp);
        }
    }
}