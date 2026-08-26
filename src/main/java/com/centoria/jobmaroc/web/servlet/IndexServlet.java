package com.centoria.jobmaroc.web.servlet;

import com.centoria.jobmaroc.app.IndexApp;
import com.centoria.jobmaroc.app.LinksApp;
import com.centoria.jobmaroc.dto.SearchResultAdDto;
import com.centoria.jobmaroc.model.Region;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet pour la page d'accueil.
 * 
 * Ce servlet est mappé sur "/" (default servlet).
 * IMPORTANT: Le default servlet reçoit TOUTES les requêtes qui ne correspondent
 * à aucun autre mapping. Il doit donc vérifier si la requête est pour la page
 * d'accueil et retourner 404 pour les autres URLs.
 */
@Slf4j
public class IndexServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {
        
        String requestUri = req.getRequestURI();
        String contextPath = req.getContextPath();
        
        // Normaliser l'URI en enlevant le contextPath si présent
        String normalizedUri = requestUri;
        if (contextPath != null && !contextPath.isEmpty() && requestUri.startsWith(contextPath)) {
            normalizedUri = requestUri.substring(contextPath.length());
        }
        
        // Vérifier si c'est la page d'accueil ("/" ou "")
        boolean isHomePage = normalizedUri.equals("/") || normalizedUri.equals("");
        
        if (!isHomePage) {
            // Ce n'est pas la page d'accueil - retourner 404
            // sendError() déclenche le CustomErrorHandler qui affiche error-page.ftl
            log.debug("Page non trouvée: {}", normalizedUri);
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Page introuvable: " + normalizedUri);
            return;
        }
        
        log.debug("IndexServlet handling homepage request");
        
        List<Region> regions = LinksApp.getInstance().getAllRegion();
        SearchResultAdDto result = IndexApp.getInstance().indexPage(1, 8);

        Map<String, Object> model = new HashMap<>();
        model.put("data", result);
        model.put("regions", regions);

        dispatch(req, resp, "front/temp/index.ftl", model);
    }
}