package com.centoria.jobmaroc.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AboutServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {
        // Protection contre les boucles de traitement
        if (req.getAttribute("processed") != null) {
            showError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Processing loop detected", req, resp);
            return;
        }
        req.setAttribute("processed", true);

        // Pas de modèle spécifique, on passe une map vide
        Map<String, Object> map = new HashMap<>();

        // Affiche le template Freemarker
        dispatch(req, resp, "front/about.ftl", map);
    }
}