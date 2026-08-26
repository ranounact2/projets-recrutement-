package com.centoria.jobmaroc.web.servlet;

import com.centoria.jobmaroc.app.ApplicantApp;
import com.centoria.jobmaroc.app.MoApp;
import com.centoria.jobmaroc.model.exception.BusinessException;
import com.centoria.jobmaroc.model.exception.TechnicalException;
import com.centoria.jobmaroc.dto.AdWithReferenceDataDto;
import com.centoria.jobmaroc.dto.SearchResultAdDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AddJobOfferServlet extends BaseServlet {
    private final ApplicantApp applicantApp = ApplicantApp.getInstance();
    private final MoApp moApp = MoApp.getInstance();

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            // Création directe du modèle FreeMarker
            Map<String, Object> model = new HashMap<>();

            // Récupération des données métier
            SearchResultAdDto result = applicantApp.getReferenceData();
            model.put("data", result);

            // Dispatch vers le template FreeMarker
            dispatch(req, resp, "front/offre.ftl", model);

        } catch (BusinessException | TechnicalException e) {
            showError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    e.getMessage(), req, resp);
        }
    }

    private boolean verifyKaptcha(HttpServletRequest req) {
        String userInput = req.getParameter("kaptchaResponse");
        if (userInput == null || userInput.trim().isEmpty()) {
            return false;
        }
        Object sessionValue = req.getSession().getAttribute(KaptchaServlet.SESSION_KEY);
        if (sessionValue == null) {
            return false;
        }
        req.getSession().removeAttribute(KaptchaServlet.SESSION_KEY);
        return userInput.trim().equalsIgnoreCase(sessionValue.toString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log.info("Processing job offer submission");
        Map<String, Object> model = new HashMap<>();
        AdWithReferenceDataDto jobDto = new AdWithReferenceDataDto();

        String template = "front/message.ftl";
        Part file;
        try {
            file = req.getPart("logoImage");
            jobDto.fillFromParams(file, requestPartToMap(req,
                    "key", "domain", "city", "title", "type", "content", "email", "tel",
                    "confidentialite", "companyName", "companyCode", "nbrDePostes",
                    "formation", "experienceLevel", "adtype", "facebook", "twitter", "linkedin"));

            if (!verifyKaptcha(req)) {
                SearchResultAdDto refData = applicantApp.getReferenceData();
                jobDto.setCities(refData.getCities());
                jobDto.setDomains(refData.getDomains());
                model.put("data", jobDto);
                model.put("captchaError", "Code de vérification incorrect. Veuillez réessayer.");
                dispatch(req, resp, "front/offre.ftl", model);
                return;
            }

            // IMPORTANT: Déterminer si c'est une mise à jour AVANT d'appeler addOrUpdate
            // car addOrUpdate modifie le key du DTO après la sauvegarde
            boolean isUpdate = jobDto.getJob().getKey() != null && !jobDto.getJob().getKey().isEmpty();

            // Construire l'URL du site
            String scheme = req.getScheme();
            String serverName = req.getServerName();
            int serverPort = req.getServerPort();
            String siteUrl = scheme + "://" + serverName;
            if ((scheme.equals("http") && serverPort != 80) || (scheme.equals("https") && serverPort != 443)) {
                siteUrl += ":" + serverPort;
            }
            jobDto.setSiteUrl(siteUrl);

            /*
             * le dto en Sortie
             */
            AdWithReferenceDataDto result = moApp.addOrUpdate(jobDto);

            /*
             * Vérification de la taille du fichier
             */
            if (file != null && file.getSize() / (1024 * 1024) > 4) {
                result.getErrors().put("logoImage", "taille maximale du logo 3.5 Mo");
            }
            log.info("Job offer submission errors: {}", result.getErrors());

            if (result.getErrors().isEmpty()) {
                if (isUpdate) {
                    // Mise à jour réussie → rediriger vers la page de liste avec message de succès
                    String returnSecretCode = req.getParameter("returnSecretCode");
                    String secretCode = (returnSecretCode != null && !returnSecretCode.isEmpty())
                            ? returnSecretCode
                            : result.getJob().getSecretCode();
                    resp.sendRedirect("/m-office/mes-annonces/" + secretCode + "?updated=true");
                    return;
                }

                // Création réussie → afficher le message de confirmation
                model.put("data", result);
                dispatch(req, resp, template, model);
            } else {
                // Erreurs de validation → réafficher le formulaire avec les erreurs
                // result contient déjà cities, domains, job (avec les valeurs saisies) et errors
                model.put("data", result);
                if (isUpdate) {
                    String returnSecretCode = req.getParameter("returnSecretCode");
                    if (returnSecretCode != null && !returnSecretCode.isEmpty()) {
                        model.put("secretCode", returnSecretCode);
                    }
                }
                dispatch(req, resp, "front/offre.ftl", model);
            }

        } catch (IOException e) {
            log.error("Error processing file upload", e);

            // Récupération des données de référence pour réafficher le formulaire
            try {
                SearchResultAdDto data = applicantApp.getReferenceData();
                model.put("data", data);
                model.put("error", getMessage("error.file.processing"));
                dispatch(req, resp, "front/offre.ftl", model);
            } catch (BusinessException | TechnicalException ex) {
                showError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        getMessage("error.data.retrieval"), req, resp);
            }

        } catch (ServletException e) {
            log.error("Servlet error processing job offer", e);
            showError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    getMessage("error.request.processing"), req, resp);
        } catch (BusinessException | TechnicalException e) {
            log.error("Error processing job offer", e);

            // En cas d'erreur métier, on affiche à nouveau le formulaire avec les messages d'erreur
            try {
                SearchResultAdDto data = applicantApp.getReferenceData();
                model.put("data", data);
                model.put("error", e.getMessage());
                dispatch(req, resp, "front/offre.ftl", model);
            } catch (BusinessException | TechnicalException ex) {
                showError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        getMessage("error.data.retrieval"), req, resp);
            }
        } catch (RuntimeException e) {
            log.error("Unexpected error processing job offer", e);

            // En cas d'erreur inattendue, réafficher le formulaire
            try {
                SearchResultAdDto data = applicantApp.getReferenceData();
                model.put("data", data);
                model.put("error", "Une erreur inattendue est survenue. Veuillez réessayer.");
                dispatch(req, resp, "front/offre.ftl", model);
            } catch (BusinessException | TechnicalException ex) {
                showError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        getMessage("error.data.retrieval"), req, resp);
            }
        }
    }

    // Méthode pour convertir les parties de la requête en map
    private Map<String, String> requestPartToMap(HttpServletRequest request, String... paramNames) {
        Map<String, String> result = new HashMap<>();
        for (String paramName : paramNames) {
            String paramValue = request.getParameter(paramName);
            if (paramValue != null) {
                result.put(paramName, paramValue);
            }
        }
        return result;
    }
}