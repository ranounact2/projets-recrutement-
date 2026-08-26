package com.centoria.jobmaroc.web.servlet.mo;

import com.centoria.jobmaroc.model.exception.BusinessException;
import com.centoria.jobmaroc.model.exception.TechnicalException;
import com.centoria.jobmaroc.dto.AdWithReferenceDataDto;
import com.centoria.jobmaroc.dto.InputSearchAdDTO;
import com.centoria.jobmaroc.model.Ad;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet for displaying the update form for a job offer.
 * URL: /m-office/mes-annonces/update/{secretCode}/{id}
 * Handles GET requests to display the edit form for an existing job offer.
 * This servlet handles ONLY the update functionality.
 * List and delete are handled by separate servlets.
 */
public class JobOfferUpdateServlet extends BaseJobServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        if (checkProcessed(req, resp)) {
            return;
        }

        // Extract secretCode and adId from pathInfo
        // pathInfo = /{secretCode}/{id}
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.isEmpty()) {
            showError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Format d'URL invalide",
                    req, resp
            );
            return;
        }

        String[] pathParts = pathInfo.split("/");
        // pathParts[0] is empty (before first /), pathParts[1] = secretCode, pathParts[2] = id
        if (pathParts.length < 3) {
            showError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Format d'URL invalide - secretCode et id requis",
                    req, resp
            );
            return;
        }

        String secretCode = pathParts[1];
        String adId = pathParts[2];

        try {
            // Retrieve and validate the offer
            Ad ad = getAndValidateAd(adId, req, resp);
            if (ad == null) {
                return; // Error already sent
            }

            // Check authorization
            if (checkAuthorization(ad, secretCode, req, resp)) {
                return; // Error already sent
            }

            // Retrieve the offer data with references (cities, domains)
            // Method will fetch Ad by ID and convert to DTO using mapper
            InputSearchAdDTO inputDto = InputSearchAdDTO.builder()
                    .id(adId)
                    .withCities(true)
                    .withCategories(true)
                    .build();

            AdWithReferenceDataDto result = moApp.getWithReferenceData(inputDto);
            
            if (result == null) {
                showError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "Offre non trouvée",
                        req, resp
                );
                return;
            }
            
            // Build the model
            Map<String, Object> model = new HashMap<>();
            model.put("data", result);
            model.put("secretCode", secretCode);

            // Display the edit form
            dispatch(req, resp, "front/offre.ftl", model);

        } catch (BusinessException | TechnicalException e) {
            showError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    req, resp
            );
        }
    }
}
