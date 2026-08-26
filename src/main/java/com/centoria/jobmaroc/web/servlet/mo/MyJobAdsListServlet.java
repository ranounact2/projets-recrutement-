package com.centoria.jobmaroc.web.servlet.mo;

import com.centoria.jobmaroc.dto.AdResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet for displaying the paginated list of job offers for a user.
 * URL: /m-office/mes-annonces/{secretCode}
 * Handles GET requests to display a paginated list of job offers
 * associated with a secretCode.
 * This servlet handles ONLY the list functionality.
 * Update and delete are handled by separate servlets.
 */
public class MyJobAdsListServlet extends BaseJobServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        if (checkProcessed(req, resp)) {
            return;
        }

        // Extract secretCode from pathInfo
        // pathInfo = /{secretCode}
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.isEmpty() || pathInfo.equals("/")) {
            showError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    getMessage("error.invalid.secretCode"),
                    req, resp
            );
            return;
        }

        // Remove the first slash and trailing slash if present
        String secretCode = pathInfo.startsWith("/") ? pathInfo.substring(1) : pathInfo;
        if (secretCode.endsWith("/")) {
            secretCode = secretCode.substring(0, secretCode.length() - 1);
        }

        // Parse pagination parameters
        int pageNumber = parseIntParam(req.getParameter("page"), 1);
        int adsPerPage = parseIntParam(req.getParameter("size"), 10);

        // Get ads from business layer
        AdResult adResult = moApp.getAdsBySecretCode(secretCode, pageNumber, adsPerPage);

        // Build model with pagination details
        Map<String, Object> model = extractAdDetails(adResult, pageNumber, adsPerPage);
        
        // Add request parameters to model for template access
        model.put("deleted", req.getParameter("deleted"));
        model.put("updated", req.getParameter("updated"));
        model.put("closed", req.getParameter("closed"));
        model.put("secretCode", secretCode);

        // Render template
        dispatch(req, resp, "m-office/mo-annonce.ftl", model);
    }

    /**
     * Parses an integer parameter with a default value.
     */
    private int parseIntParam(String param, int defaultValue) {
        if (param == null || param.isEmpty()) {
            return defaultValue;
        }
        try {
            int value = Integer.parseInt(param);
            return value < 1 ? defaultValue : value;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Extracts pagination details from AdResult and builds a model map.
     * 
     * @param adResult The result containing ads and total count
     * @param pageNumber Current page number
     * @param adsPerPage Number of ads per page
     * @return Map containing ads, pagination info
     */
    private Map<String, Object> extractAdDetails(AdResult adResult, int pageNumber, int adsPerPage) {
        Map<String, Object> map = new HashMap<>();
        long totalAds = adResult.getTotalAds();
        map.put("ads", adResult.getAds());
        map.put("pageNumber", pageNumber);
        map.put("adsPerPage", adsPerPage);
        int totalPages = (int) Math.ceil((double) totalAds / adsPerPage);
        // Ensure totalPages is at least 1 even if there are no ads
        map.put("totalPages", Math.max(1, totalPages));
        return map;
    }
}
