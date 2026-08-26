package com.centoria.jobmaroc.web.servlet.mo;

import com.centoria.jobmaroc.app.MoApp;
import com.centoria.jobmaroc.model.exception.BusinessException;
import com.centoria.jobmaroc.model.exception.TechnicalException;
import com.centoria.jobmaroc.dto.AdResult;
import com.centoria.jobmaroc.model.Ad;
import com.centoria.jobmaroc.web.servlet.BaseServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Base class for Middle Office job offer servlets.
 * Contains common functionality shared between list, update, and delete operations:
 * - Security checks (secretCode, email validation)
 * - Shared instances (MoApp)
 * - Request processing loop prevention
 * - Ad retrieval and validation
 */
public abstract class BaseJobServlet extends BaseServlet {

    protected final MoApp moApp = MoApp.getInstance();

    /**
     * Checks if the request has already been processed to avoid processing loops.
     * @return true if already processed (error sent), false otherwise
     */
    protected boolean checkProcessed(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (req.getAttribute("processed") != null) {
            showError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    getMessage("error.processingLoop"),
                    req, resp
            );
            return true;
        }
        req.setAttribute("processed", true);
        return false;
    }

    /**
     * Checks authorization and returns an error if not authorized.
     * 
     * @param ad The offer to verify
     * @param secretCode The secretCode provided by the user
     * @param req The HTTP request
     * @param resp The HTTP response
     * @return true if error sent (not authorized or exception), false if authorized
     * @throws ServletException If servlet error
     * @throws IOException If I/O error
     */
    protected boolean checkAuthorization(Ad ad, String secretCode,
                                        HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            // Validation des paramètres d'entrée
            if (ad == null || ad.getSecretCode() == null || secretCode == null || secretCode.trim().isEmpty()) {
                showError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "Accès non autorisé",
                        req, resp
                );
                return true;
            }
            
            // CAS 1: Le secretCode correspond directement → Autoriser immédiatement
            if (ad.getSecretCode().equals(secretCode)) {
                return false; // false = autorisé, pas d'erreur
            }
            
            // CAS 2: Le secretCode ne correspond pas directement
            // Vérifier si le secretCode correspond à un email qui match l'offre
            AdResult testResult = moApp.getAdsBySecretCode(secretCode, 1, 1);
            if (testResult.getAds() == null || testResult.getAds().isEmpty()) {
                // Aucune offre trouvée avec ce secretCode → Non autorisé
                showError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "Accès non autorisé",
                        req, resp
                );
                return true;
            }
            
            // CAS 3: Vérifier que l'email de l'offre correspond à l'email du secretCode
            String emailFromSecretCode = testResult.getAds().get(0).getEmail();
            if (emailFromSecretCode == null || ad.getEmail() == null) {
                // Email manquant → Non autorisé
                showError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "Accès non autorisé",
                        req, resp
                );
                return true;
            }
            
            // Comparaison des emails normalisés
            String normalizedEmail1 = ad.getEmail().trim().toLowerCase();
            String normalizedEmail2 = emailFromSecretCode.trim().toLowerCase();
            boolean authorized = normalizedEmail1.equals(normalizedEmail2);
            
            if (!authorized) {
                showError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "Accès non autorisé",
                        req, resp
                );
                return true;
            }
            
            return false; // Autorisation OK
        } catch (BusinessException | TechnicalException e) {
            showError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    req, resp
            );
            return true;
        }
    }

    /**
     * Retrieves and validates an offer by its ID.
     * 
     * @param adId The offer ID
     * @param req The HTTP request
     * @param resp The HTTP response
     * @return The offer if found and valid, null otherwise (error already sent)
     * @throws ServletException If servlet error
     * @throws IOException If I/O error
     */
    protected Ad getAndValidateAd(String adId, HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            Ad ad = moApp.getAdById(adId);
            if (ad == null) {
                showError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "Offre non trouvée",
                        req, resp
                );
                return null;
            }
            return ad;
        } catch (BusinessException | TechnicalException e) {
            showError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    req, resp
            );
            return null;
        }
    }
}
