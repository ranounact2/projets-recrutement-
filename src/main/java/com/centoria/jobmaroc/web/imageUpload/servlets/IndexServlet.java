package com.centoria.jobmaroc.web.imageUpload.servlets;


import com.centoria.jobmaroc.app.IndexApp;
import com.centoria.jobmaroc.app.LinksApp;
import com.centoria.jobmaroc.dto.SearchResultAdDto;
import com.centoria.jobmaroc.model.Region;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class IndexServlet extends HttpServlet {
    private IndexApp indexApp;
    private LinksApp linksApp;
    private Configuration cfg;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        // Initialize FreeMarker configuration
        cfg = new Configuration(Configuration.VERSION_2_3_33);
        cfg.setServletContextForTemplateLoading(getServletContext(), "/templates");
        cfg.setDefaultEncoding("UTF-8");

        // Initialize dependencies
        indexApp = IndexApp.getInstance();
        linksApp = LinksApp.getInstance();
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");

        Map<String, Object> map = new HashMap<>();

        try {
            List<Region> regions = linksApp.getAllRegion();
            SearchResultAdDto result = indexApp.indexPage(1, 8);

            map.put("data", result);
            map.put("regions", regions);
        } catch (Exception e) {
            log.error("Error fetching data for index page", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching data for index page");
            return;
        }

        try (PrintWriter out = response.getWriter()) {
            Template template = cfg.getTemplate("front/index.ftl");
            template.process(map, out);

        } catch (TemplateException e) {
            log.error("Error processing FreeMarker template", e);
            throw new ServletException("Error processing FreeMarker template", e);
        }
    }
}
