package com.centoria.jobmaroc.web.servlet;

import com.centoria.jobmaroc.app.MoApp;
import com.centoria.jobmaroc.dto.InputSearchAdDTO;
import com.centoria.jobmaroc.dto.SearchResultAdDto;
import com.centoria.jobmaroc.service.IAdService;
import com.centoria.jobmaroc.service.IMailService;
import com.centoria.jobmaroc.service.impl.AdService;
import com.centoria.jobmaroc.service.MailServiceFactory;
import freemarker.template.Configuration;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MyJobAdsServlet extends BaseServlet {

    private MoApp moApp = MoApp.getInstance();
    private final IAdService adService = AdService.getInstance();
    private final IMailService mailService = MailServiceFactory.getInstance();

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {
        if (req.getAttribute("processed") != null) {
            // Utilisation du message i18n
            showError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    getMessage("error.processingLoop"),
                    req, resp
            );
            return;
        }
        req.setAttribute("processed", true);

        // Pas de getMap, on part d’une map vide
        Map<String, Object> map = new HashMap<>();
        dispatch(req, resp, "m-office/secret-code.ftl", map);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // … gestion ‘processed’ …

        // 1) Récupération du DTO
        InputSearchAdDTO inputDto = new InputSearchAdDTO();
        String emailParam = req.getParameter("email");
        inputDto.setEmail(emailParam);

        // 2) Construction du host/baseUrl
        String scheme = req.getScheme();
        String hostHdr = req.getHeader("Host");
        String host = scheme + "://" + hostHdr;

        // 3) Récupérer la config FreeMarker du ServletContext
        Configuration cfg = (Configuration)
                req.getServletContext().getAttribute("freemarkerConfig");

        // 4) Appel du service
        SearchResultAdDto sDto = MoApp.getInstance()
                .sendEmailWithSecretCode(inputDto, host, cfg);

        // 5) Affichage du résultat
        Map<String, Object> viewModel = new HashMap<>();
        viewModel.put("data", sDto);
        viewModel.put("message", sDto.getMessages().get("message"));
        dispatch(req, resp, "front/message.ftl", viewModel);
    }
}