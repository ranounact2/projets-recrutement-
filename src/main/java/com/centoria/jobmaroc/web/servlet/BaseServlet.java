package com.centoria.jobmaroc.web.servlet;

import com.centoria.jobmaroc.app.ZonePageApp;
import com.centoria.jobmaroc.model.exception.BusinessException;
import com.centoria.jobmaroc.model.exception.TechnicalException;
import com.centoria.jobmaroc.common.utils.MessageBundle;
import com.centoria.jobmaroc.common.utils.ThemeHelper;
import com.centoria.jobmaroc.model.PageZones;
import com.google.gson.Gson;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.Writer;
import java.util.*;

@Slf4j
public abstract class BaseServlet extends HttpServlet {
    protected Gson gson = new Gson();
    private ZonePageApp zonePageApp = ZonePageApp.getInstance();
    protected MessageBundle messageBundle = MessageBundle.getInstance();
    
    /**
     * Gets a localized message using MessageBundle singleton
     */
    protected String getMessage(String key, Locale locale) {
        return messageBundle.getMessage(key, locale);
    }
    
    /**
     * Gets a localized message using default locale
     */
    protected String getMessage(String key) {
        return messageBundle.getMessage(key);
    }

    /**
     * Prépare la map (i18n, zones, utils) et fait un forward vers /template-processor.
     */
    protected void dispatch(HttpServletRequest req,
                            HttpServletResponse resp,
                            String templatePath,
                            Map<String, Object> model)
            throws ServletException, IOException {
        // 1) Préparation du model
        model = (model != null ? model : new HashMap<>());
        model.put("host", req.getRequestURL().toString());
        
        // Add theme-aware assets path to model
        String assetsFolder = ThemeHelper.getAssetsFolder();
        model.put("assetsPath", "/" + assetsFolder);

        // 2) Gestion du cookie de langue
        String localeCookie = Optional.ofNullable(req.getCookies())
                .flatMap(cookies -> Arrays.stream(cookies)
                        .filter(c -> "local".equals(c.getName()))
                        .map(Cookie::getValue)
                        .findFirst())
                .orElse("fr");  // valeur par défaut

        Locale locale = localeCookie.equals("ar")
                ? new Locale("ar")
                : Locale.FRENCH;

        // expose la langue et l'instance MessageBundle au template
        model.put("lang", locale.getLanguage());
        I18nModel i18nModel = new I18nModel(messageBundle, locale);
        model.put("i18n", i18nModel);
        model.put("utils", i18nModel);

        // 3) Zones de page
        String uri = req.getRequestURI();
        if (!uri.contains("/assets") && !uri.contains("/public")) {
            try {
                List<PageZones> zones = zonePageApp.search(
                        templatePath,
                        uri,
                        Collections.emptyMap(),
                        req.getParameterMap()
                );
                model.put("zones", zones);
            } catch (TechnicalException | BusinessException e) {
                log.error("Erreur génération zones", e);
                throw new ServletException(getMessage("error.zones.generation", locale), e);
            }
        }

        // 4) Rendu FreeMarker
        Configuration cfg = (Configuration)
                req.getServletContext().getAttribute("freemarkerConfig");
        resp.setContentType("text/html;charset=UTF-8");
        try (Writer out = resp.getWriter()) {
            // Resolve template path with theme suffix
            String themedTemplatePath = ThemeHelper.resolveTemplatePath(templatePath);
            Template tpl = cfg.getTemplate(themedTemplatePath);
            tpl.process(model, out);
        } catch (TemplateException e) {
            log.error("Erreur FreeMarker", e);
            throw new ServletException(getMessage("error.freemarker", locale), e);
        }
    }


    /**
     * Envoie une page d'erreur 404 ou 500, sans ModelAndView.
     */
    protected void showError(int status,
                             String message,
                             HttpServletRequest req,
                             HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setStatus(status);
        Map<String, Object> errorModel = new HashMap<>();
        errorModel.put("error", message);
        errorModel.put("code", String.valueOf(status));
        
        // Utiliser le template error-page pour toutes les erreurs
        dispatch(req, resp, "errors/error-page.ftl", errorModel);
    }
}
