package com.centoria.jobmaroc.web.servlet;

import com.centoria.jobmaroc.app.ApplicantApp;
import com.centoria.jobmaroc.model.exception.BusinessException;
import com.centoria.jobmaroc.model.exception.TechnicalException;
import com.centoria.jobmaroc.dto.ApplicantDto;
import com.centoria.jobmaroc.model.Ad;
import com.centoria.jobmaroc.service.IAdService;
import com.centoria.jobmaroc.service.IApplicantService;
import com.centoria.jobmaroc.service.impl.AdService;
import com.centoria.jobmaroc.service.impl.ApplicantService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class PostulerEmploiServlet extends BaseServlet {
    private final IApplicantService applicantService = ApplicantService.getInstance();
    private final IAdService adService = AdService.getInstance();
    private final ApplicantApp applicantApp = ApplicantApp.getInstance();

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {
        if (req.getAttribute("processed") != null) {
            showError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Processing loop detected", req, resp);
            return;
        }
        req.setAttribute("processed", true);

        String pathInfo = req.getPathInfo();
        String jobKey = (pathInfo != null && pathInfo.length() > 1)
                ? pathInfo.substring(1)
                : null;

        try {
            ApplicantDto result = applicantService.prepareApplicationForm(jobKey);
            // S'assurer que errors est initialisé (ne devrait jamais être null, mais par sécurité)
            if (result.getErrors() == null) {
                result.setErrors(new HashMap<>());
            }
            Map<String, Object> model = new HashMap<>();
            model.put("data", result);
            dispatch(req, resp, "front/add-applicant.ftl", model);
        } catch (BusinessException e) {
            log.warn("Offre introuvable pour la candidature {}", jobKey, e);
            showError(HttpServletResponse.SC_NOT_FOUND,
                    "Offre introuvable", req, resp);
        } catch (TechnicalException e) {
            log.error("Erreur lors du chargement du formulaire de candidature pour {}", jobKey, e);
            showError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Impossible d'afficher le formulaire pour le moment", req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        log.info("Processing job application submission");
        Map<String, Object> model = new HashMap<>();

        try {
            // Récupération du jobKey depuis pathInfo
            String pathInfo = req.getPathInfo();
            String jobKey = (pathInfo != null && pathInfo.length() > 1)
                    ? pathInfo.substring(1)
                    : null;

            if (jobKey == null || jobKey.isEmpty()) {
                showError(HttpServletResponse.SC_BAD_REQUEST,
                        "Identifiant d'offre manquant", req, resp);
                return;
            }

            // Vérification que l'offre existe
            Ad ad = adService.get(jobKey);
            if (ad == null) {
                log.warn("Offre introuvable pour la candidature {}", jobKey);
                showError(HttpServletResponse.SC_NOT_FOUND,
                        "Offre introuvable", req, resp);
                return;
            }

            // Récupération des paramètres du formulaire
            Map<String, String> params = new HashMap<>();
            String[] paramNames = {"nom", "prenom", "email", "phone", "motivation", 
                                   "formation", "experienceLevel"};
            for (String paramName : paramNames) {
                String value = req.getParameter(paramName);
                if (value != null) {
                    params.put(paramName, value);
                }
            }

            // Récupération du fichier CV
            Part cvFile = null;
            try {
                cvFile = req.getPart("uploaded_file");
                if (cvFile != null) {
                    log.info("CV récupéré: nom={}, taille={}, type={}", 
                            cvFile.getSubmittedFileName(), 
                            cvFile.getSize(), 
                            cvFile.getContentType());
                } else {
                    log.warn("Aucun fichier CV trouvé dans la requête");
                }
            } catch (Exception e) {
                log.error("Erreur lors de la récupération du fichier CV", e);
            }

            // Création du DTO avec validation
            ApplicantDto applicantDto = new ApplicantDto(cvFile, params);
            
            // Log pour débogage
            if (applicantDto.getCv() != null && applicantDto.getCv().getCvFile() != null) {
                log.info("CV validé et prêt à être enregistré, taille: {} bytes", 
                        applicantDto.getCv().getCvFile().length);
            } else {
                log.warn("CV non présent ou invalide dans le DTO après validation");
            }

            // Construction de l'URL du site
            String scheme = req.getScheme();
            String serverName = req.getServerName();
            int serverPort = req.getServerPort();
            String siteUrl = scheme + "://" + serverName;
            if ((scheme.equals("http") && serverPort != 80) || 
                (scheme.equals("https") && serverPort != 443)) {
                siteUrl += ":" + serverPort;
            }
            applicantDto.setSiteUrl(siteUrl);

            // Définition du jobId (utiliser la clé de l'offre)
            applicantDto.setJobId(ad.getKey());

            // Appel métier pour enregistrer la candidature
            ApplicantDto result = applicantApp.add(applicantDto);

            // Si succès (pas d'erreurs)
            if (result.getErrors() == null || result.getErrors().isEmpty()) {
                // Afficher le message de succès
                model.put("data", result);
                dispatch(req, resp, "front/message.ftl", model);
            } else {
                // Réafficher le formulaire avec les erreurs
                log.warn("Erreurs de validation pour la candidature: {}", result.getErrors());
                // Recharger les données de l'offre pour réafficher le formulaire
                ApplicantDto formData = applicantService.prepareApplicationForm(jobKey);
                // Fusionner les erreurs et les données saisies
                formData.setErrors(result.getErrors());
                // Préserver les valeurs saisies
                if (result.getNom() != null) formData.setNom(result.getNom());
                if (result.getPrenom() != null) formData.setPrenom(result.getPrenom());
                if (result.getEmail() != null) formData.setEmail(result.getEmail());
                if (result.getPhone() != null) formData.setPhone(result.getPhone());
                if (result.getMotivation() != null) formData.setMotivation(result.getMotivation());
                if (result.getFormation() != null) formData.setFormation(result.getFormation());
                if (result.getExperienceLevel() != null) formData.setExperienceLevel(result.getExperienceLevel());
                
                model.put("data", formData);
                dispatch(req, resp, "front/add-applicant.ftl", model);
            }

        } catch (BusinessException e) {
            log.error("Erreur métier lors de la soumission de candidature", e);
            showError(HttpServletResponse.SC_BAD_REQUEST,
                    e.getMessage() != null ? e.getMessage() : getMessage("error.business"), req, resp);
        } catch (TechnicalException e) {
            log.error("Erreur technique lors de la soumission de candidature", e);
            showError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    getMessage("error.internal"), req, resp);
        } catch (Exception e) {
            log.error("Erreur inattendue lors de la soumission de candidature", e);
            showError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    getMessage("error.internal"), req, resp);
        }
    }
}
