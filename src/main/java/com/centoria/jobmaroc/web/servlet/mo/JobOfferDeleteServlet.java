package com.centoria.jobmaroc.web.servlet.mo;

import com.centoria.jobmaroc.model.exception.BusinessException;
import com.centoria.jobmaroc.model.exception.TechnicalException;
import com.centoria.jobmaroc.model.Ad;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Servlet for deleting a job offer.
 * URL: /m-office/mes-annonces/delete/{secretCode}/{id}
 * Handles GET requests to delete a job offer and redirect to the list.
 * This servlet handles ONLY the delete functionality.
 * List and update are handled by separate servlets.
 */
public class JobOfferDeleteServlet extends BaseJobServlet {

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

        // Retrieve and validate the offer
        Ad ad = getAndValidateAd(adId, req, resp);
        if (ad == null) {
            return; // Error already sent
        }

        // Check authorization
        if (checkAuthorization(ad, secretCode, req, resp)) {
            return; // Error already sent
        }

        try {
            // Soft delete: disable the offer instead of deleting it (enabled = false)
            moApp.closeAd(adId, ad.getSecretCode());

            // Redirect to the list of offers with success message
            resp.sendRedirect("/m-office/mes-annonces/" + secretCode + "?deleted=true");

        } catch (BusinessException | TechnicalException e) {
            showError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    req, resp
            );
        }
    }
}
