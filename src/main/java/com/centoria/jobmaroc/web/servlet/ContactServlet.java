package com.centoria.jobmaroc.web.servlet;

import com.centoria.jobmaroc.app.MoApp;
import com.centoria.jobmaroc.model.exception.BusinessException;
import com.centoria.jobmaroc.model.exception.TechnicalException;
import com.centoria.jobmaroc.dto.ContactDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ContactServlet extends BaseServlet {

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

    private final MoApp moApp = MoApp.getInstance();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (req.getAttribute("processed") != null) {
            showError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Processing loop detected", req, resp);
            return;
        }
        req.setAttribute("processed", true);

        Map<String, Object> map = new HashMap<>();
        Map<String, Object> paramMap = new HashMap<>();
        map.put("param", paramMap);
        req.setAttribute("param", paramMap);
        dispatch(req, resp, "front/contact.ftl", map);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (req.getAttribute("processed") != null) {
            showError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Processing loop detected", req, resp);
            return;
        }
        req.setAttribute("processed", true);
        
        if (!verifyKaptcha(req)) {
            Map<String, Object> map = new HashMap<>();
            Map<String, Object> paramMap = new HashMap<>();
            
            paramMap.put("name", req.getParameter("name"));
            paramMap.put("email", req.getParameter("email"));
            paramMap.put("message", req.getParameter("message"));
            paramMap.put("objet", req.getParameter("objet"));
            paramMap.put("phone", req.getParameter("phone"));
            
            String errorMsg = getMessage("error.captcha.invalid");
            if (errorMsg == null || errorMsg.trim().isEmpty()) {
                errorMsg = "Captcha invalide. Veuillez réessayer.";
            }
            paramMap.put("error", errorMsg);
            map.put("param", paramMap);
            dispatch(req, resp, "front/contact.ftl", map);
            return;
        }

        try {
            String scheme = req.getScheme();
            String serverName = req.getServerName();
            int serverPort = req.getServerPort();
            String siteUrl = scheme + "://" + serverName
                    + ((scheme.equals("http") && serverPort != 80)
                    || (scheme.equals("https") && serverPort != 443)
                    ? ":" + serverPort
                    : "");

            Map<String, String> params = new HashMap<>();
            for (String name : new String[]{"name", "email", "phone", "objet", "message"}) {
                String v = req.getParameter(name);
                if (v != null) params.put(name, v);
            }

            ContactDto contactDto = new ContactDto(params);
            contactDto.setSiteUrl(siteUrl);
            
            if (contactDto.getErrors() != null && !contactDto.getErrors().isEmpty()) {
                Map<String, Object> map = new HashMap<>();
                Map<String, Object> paramMap = new HashMap<>();
                
                paramMap.put("name", contactDto.getName() != null ? contactDto.getName() : "");
                paramMap.put("email", contactDto.getEmail() != null ? contactDto.getEmail() : "");
                paramMap.put("phone", contactDto.getPhone() != null ? contactDto.getPhone() : "");
                paramMap.put("objet", contactDto.getObjet() != null ? contactDto.getObjet() : "");
                paramMap.put("message", contactDto.getMessage() != null ? contactDto.getMessage() : "");
                paramMap.put("errors", contactDto.getErrors());
                
                if (!contactDto.getErrors().isEmpty()) {
                    String firstError = contactDto.getErrors().values().iterator().next();
                    paramMap.put("error", firstError);
                }
                
                map.put("param", paramMap);
                dispatch(req, resp, "front/contact.ftl", map);
                return;
            }

            ContactDto resultDto = moApp.sendContactMail(contactDto);
            
            Map<String, Object> map = new HashMap<>();
            Map<String, Object> paramMap = new HashMap<>();
            
            if (resultDto != null && resultDto.getMessages() != null && !resultDto.getMessages().isEmpty()) {
                String successMsg = resultDto.getMessages().get("messages");
                if (successMsg != null && !successMsg.isEmpty()) {
                    paramMap.put("success", successMsg);
                }
            }
            
            if (resultDto != null && resultDto.getErrors() != null && !resultDto.getErrors().isEmpty()) {
                String firstError = resultDto.getErrors().values().iterator().next();
                paramMap.put("error", firstError);
            }
            
            map.put("param", paramMap);
            dispatch(req, resp, "front/contact.ftl", map);

        } catch (BusinessException | TechnicalException e) {
            log.error("Erreur lors du traitement du formulaire de contact", e);
            Map<String, Object> map = new HashMap<>();
            Map<String, Object> paramMap = new HashMap<>();
            String errorMessage = e.getMessage();
            if (errorMessage == null || errorMessage.trim().isEmpty()) {
                errorMessage = "Une erreur est survenue. Veuillez réessayer.";
            }
            if (errorMessage.contains("mail") || errorMessage.contains("email")) {
                errorMessage = "Erreur lors de l'envoi de l'email. Votre message a été sauvegardé. Veuillez réessayer plus tard.";
            }
            paramMap.put("error", errorMessage);
            map.put("param", paramMap);
            dispatch(req, resp, "front/contact.ftl", map);
        } catch (Exception e) {
            log.error("Erreur inattendue dans ContactServlet.doPost", e);
            try {
                Map<String, Object> map = new HashMap<>();
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("error", "Une erreur inattendue est survenue. Veuillez réessayer.");
                map.put("param", paramMap);
                dispatch(req, resp, "front/contact.ftl", map);
            } catch (Exception dispatchException) {
                log.error("Impossible d'afficher la page d'erreur", dispatchException);
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.setContentType("text/html;charset=UTF-8");
                resp.getWriter().println("<html><body><h1>Erreur</h1><p>Une erreur inattendue est survenue.</p></body></html>");
            }
        }
    }
}
