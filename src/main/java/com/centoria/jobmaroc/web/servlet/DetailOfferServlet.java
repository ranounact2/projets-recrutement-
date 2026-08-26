package com.centoria.jobmaroc.web.servlet;

import com.centoria.jobmaroc.app.MoApp;
import com.centoria.jobmaroc.model.exception.BusinessException;
import com.centoria.jobmaroc.model.exception.TechnicalException;
import com.centoria.jobmaroc.dto.AdDisplayDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class DetailOfferServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    // Récupération du service métier (à adapter selon votre implémentation)
    private final MoApp moApp = MoApp.getInstance();

    /**
     * Valide si une chaîne est un ObjectId MongoDB valide (24 caractères hexadécimaux)
     */
    private boolean isValidObjectId(String id) {
        if (id == null || id.isEmpty()) {
            return false;
        }
        // Un ObjectId MongoDB doit avoir exactement 24 caractères hexadécimaux
        if (id.length() != 24) {
            return false;
        }
        try {
            // Vérifie que tous les caractères sont hexadécimaux
            for (char c : id.toCharArray()) {
                if (!((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F'))) {
                    return false;
                }
            }
            // Tente de créer un ObjectId pour valider complètement
            new ObjectId(id);
            return true;
        } catch (IllegalArgumentException e) {
            log.debug("ID non valide pour ObjectId: {}", id);
            return false;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {
        // 1) Empêcher la boucle
        if (req.getAttribute("processed") != null) {
            showError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Processing loop detected", req, resp);
            return;
        }
        req.setAttribute("processed", true);

        // 2) Extraction de l'ID depuis le path info (/offre-emploi-maroc/{id})
        String pathInfo = req.getPathInfo();               // "/66438bf50b4ec34dff021477" ou "/IMG-1578146041091.jpg"
        String id = (pathInfo != null && pathInfo.length() > 1)
                ? pathInfo.substring(1)               // "66438bf50b4ec34dff021477" ou "IMG-1578146041091.jpg"
                : "";

        // 3) Validation de l'ID - doit être un ObjectId MongoDB valide
        // Si ce n'est pas un ObjectId valide (ex: nom de fichier image), retourner 404
        if (id.isEmpty()) {
            log.warn("ID manquant pour la requête: {}", req.getRequestURI());
            showError(HttpServletResponse.SC_NOT_FOUND,
                    "Offre introuvable", req, resp);
            return;
        }

        if (!isValidObjectId(id)) {
            log.warn("ID invalide (pas un ObjectId MongoDB valide) pour la requête: {} (ID: {})", 
                    req.getRequestURI(), id);
            showError(HttpServletResponse.SC_NOT_FOUND,
                    "Offre introuvable", req, resp);
            return;
        }

        // 4) Appel du service métier pour récupérer les données
        AdDisplayDto jobDto = new AdDisplayDto();
        jobDto.setInputKey(id);
        AdDisplayDto result;
        try {
            result = moApp.getById(jobDto);
            if (result == null || result.getJob() == null) {
                log.warn("Offre introuvable pour l'ID: {}", id);
                showError(HttpServletResponse.SC_NOT_FOUND,
                        "Offre introuvable", req, resp);
                return;
            }
        } catch (BusinessException e) {
            log.warn("Erreur métier lors de la récupération de l'offre {}", id, e);
            showError(HttpServletResponse.SC_NOT_FOUND,
                    "Offre introuvable", req, resp);
            return;
        } catch (TechnicalException e) {
            log.error("Erreur technique lors de la récupération de l'offre {}", id, e);
            // Si c'est une erreur d'ObjectId, retourner 404 au lieu de 500
            if (e.getMessage() != null && e.getMessage().contains("hexString has 24 characters")) {
                log.warn("ID invalide détecté dans TechnicalException: {}", id);
                showError(HttpServletResponse.SC_NOT_FOUND,
                        "Offre introuvable", req, resp);
                return;
            }
            showError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    getMessage("error.offer.retrieval"), req, resp);
            return;
        }

        // 5) Préparation du model pour FreeMarker
        Map<String, Object> model = new HashMap<>();
        model.put("data", result);

        // 6) Dispatch vers le template FreeMarker
        dispatch(req, resp, "front/temp/job-detail.ftl", model);
    }
}
