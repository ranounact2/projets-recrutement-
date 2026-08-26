package com.centoria.jobmaroc.web.servlet;

import com.centoria.jobmaroc.common.context.ApplicationContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.PrintWriter;

@Slf4j
public class RobotsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String robotsContent = getRobotsContent();

        try (PrintWriter writer = resp.getWriter()) {
            writer.print(robotsContent);
            writer.flush();
        }
    }

    private String getRobotsContent() throws IOException {
        ApplicationContext appContext = ApplicationContext.getInstance();
        String environment = appContext.getEnvVariable("EM_ENV", "local");
        
        // 1) Check external CONFIG_DIR first (for production)
        String configDir = appContext.getEnvVariable("CONFIG_DIR", null);
        if (configDir != null) {
            Path externalPath = Paths.get(configDir, "robots-" + environment + ".txt");
            if (Files.exists(externalPath)) {
                try {
                    return Files.readString(externalPath);
                } catch (IOException e) {
                    log.error("Failed to read from CONFIG_DIR: {}", externalPath);
                }
            }
        }
        
        // 2) Check classpath (bundled in JAR) - from config folder
        InputStream is = getClass().getResourceAsStream("/config/robots-" + environment + ".txt");
        if (is != null) {
            return new String(is.readAllBytes());
        }
        
        // 3) Fallback to generic robots.txt from classpath
        is = getClass().getResourceAsStream("/config/robots.txt");
        if (is != null) {
            return new String(is.readAllBytes());
        }
        
        // 4) Final fallback: default content
        return "User-agent: *\nDisallow: /\n";
    }
}
