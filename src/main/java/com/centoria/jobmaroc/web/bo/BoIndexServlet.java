package com.centoria.jobmaroc.web.bo;

import com.centoria.jobmaroc.app.BoApp;
import com.centoria.jobmaroc.model.exception.BusinessException;
import com.centoria.jobmaroc.model.exception.TechnicalException;
import com.centoria.jobmaroc.dto.AdResult;
import com.centoria.jobmaroc.web.servlet.BaseServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Back Office servlet for managing job advertisements.
 * Handles both GET (display admin page) and POST (actions: validate, delete, disable) requests.
 * 
 * Route: /rtsystems_admin_8965_hXtwWUCzukqwPyEuWxcE
 */
@Slf4j
public class BoIndexServlet extends BaseServlet {
    
    private final BoApp boApp = BoApp.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Protection against processing loops
        if (req.getAttribute("processed") != null) {
            showError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Processing loop detected", req, resp);
            return;
        }
        req.setAttribute("processed", true);

        // Display the admin index page
        showIndexPage(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Protection against processing loops
        if (req.getAttribute("processed") != null) {
            showError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Processing loop detected", req, resp);
            return;
        }
        req.setAttribute("processed", true);

        // Handle action parameter
        String action = req.getParameter("action");
        if (action != null) {
            try {
                switch (action) {
                    case "validate":
                        handleValidate(req, resp);
                        break;
                    case "delete":
                        handleDelete(req, resp);
                        break;
                    case "disable":
                        handleDisable(req, resp);
                        break;
                    default:
                        // Unknown action, just show index
                        break;
                }
            } catch (BusinessException | TechnicalException e) {
                // Log error and continue to show index page
                log.error("Error processing action '{}': {}", action, e.getMessage(), e);
            }
        }

        // POST-Redirect-GET : évite l'avertissement « confirmer le renvoi du formulaire » au rafraîchissement
        redirectToIndexGet(req, resp);
    }

    /**
     * Redirige en GET vers la même page (paramètres page/size optionnels) après traitement du POST.
     */
    private void redirectToIndexGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuilder url = new StringBuilder(req.getRequestURI());
        String page = req.getParameter("page");
        String size = req.getParameter("size");
        String filter = req.getParameter("filter");
        boolean hasQuery = false;
        if (page != null && !page.isEmpty()) {
            url.append(hasQuery ? '&' : '?').append("page=").append(URLEncoder.encode(page, StandardCharsets.UTF_8));
            hasQuery = true;
        }
        if (size != null && !size.isEmpty()) {
            url.append(hasQuery ? '&' : '?').append("size=").append(URLEncoder.encode(size, StandardCharsets.UTF_8));
            hasQuery = true;
        }
        if (filter != null && !filter.isEmpty()) {
            url.append(hasQuery ? '&' : '?').append("filter=").append(URLEncoder.encode(filter, StandardCharsets.UTF_8));
        }
        resp.sendRedirect(resp.encodeRedirectURL(url.toString()));
    }

    /**
     * Displays the admin index page with paginated job advertisements.
     */
    private void showIndexPage(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Parse pagination parameters
        String pageParam = req.getParameter("page");
        String sizeParam = req.getParameter("size");
        String statusFilter = BoApp.normalizeBoStatusFilter(req.getParameter("filter"));
        
        int pageNumber = 1;
        int adsPerPage = 10;
        
        try {
            if (pageParam != null && !pageParam.isEmpty()) {
                pageNumber = Integer.parseInt(pageParam);
            }
        } catch (NumberFormatException e) {
            log.error("Invalid page parameter '{}', using default page 1", pageParam, e);
        }
        
        try {
            if (sizeParam != null && !sizeParam.isEmpty()) {
                adsPerPage = Integer.parseInt(sizeParam);
            }
        } catch (NumberFormatException e) {
            log.error("Invalid size parameter '{}', using default size 10", sizeParam, e);
        }

        // Get ads from business layer
        AdResult adResult = boApp.getAds(pageNumber, adsPerPage, statusFilter);

        // Build model with pagination details
        Map<String, Object> model = extractAdDetails(adResult, pageNumber, adsPerPage, statusFilter);

        // Render template
        dispatch(req, resp, "back-office/bo-index.ftl", model);
    }

    /**
     * Handles the validate action.
     */
    private void handleValidate(HttpServletRequest req, HttpServletResponse resp)
            {
        String _id = req.getParameter("key");
        if (_id == null || _id.isEmpty()) {
            throw new BusinessException("400", "Missing job ID");
        }

        // Build site URL
        String scheme = req.getScheme();
        String serverName = req.getServerName();
        int serverPort = req.getServerPort();
        String host = scheme + "://" + serverName
                + ((scheme.equals("http") && serverPort != 80)
                || (scheme.equals("https") && serverPort != 443)
                ? ":" + serverPort : "");

        boApp.validate(_id, host);
    }

    /**
     * Handles the delete action.
     */
    private void handleDelete(HttpServletRequest req, HttpServletResponse resp)
            {
        String _id = req.getParameter("key");
        if (_id == null || _id.isEmpty()) {
            throw new BusinessException("400", "Missing job ID");
        }

        boApp.delete(_id);
    }

    /**
     * Handles the disable action.
     */
    private void handleDisable(HttpServletRequest req, HttpServletResponse resp)
            {
        String _id = req.getParameter("key");
        if (_id == null || _id.isEmpty()) {
            throw new BusinessException("400", "Missing job ID");
        }

        boApp.disable(_id);
    }

    /**
     * Extracts pagination details from AdResult and builds a model map.
     * 
     * @param adResult The result containing ads and total count
     * @param pageNumber Current page number
     * @param adsPerPage Number of ads per page
     * @return Map containing ads, pagination info
     */
    private Map<String, Object> extractAdDetails(AdResult adResult, int pageNumber, int adsPerPage,
                                                 String statusFilter) {
        Map<String, Object> map = new HashMap<>();
        long totalAds = adResult.getTotalAds();
        map.put("ads", adResult.getAds());
        map.put("pageNumber", pageNumber);
        map.put("adsPerPage", adsPerPage);
        map.put("totalPages", Math.max(1, (int) Math.ceil((double) totalAds / adsPerPage)));
        map.put("statusFilter", statusFilter);
        return map;
    }
}
