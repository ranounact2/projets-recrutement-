package com.centoria.jobmaroc.web.servlet;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.ResourceFactory;

import java.io.File;
import java.net.URL;


public class StaticFileServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Construit et configure le handler statique pour un dossier d'assets spécifique
     * @param assetFolder nom du dossier d'assets (e.g., "assets" ou "assets-theme")
     * @return ContextHandler configuré pour servir les fichiers statiques
     */
    public static ContextHandler createStaticHandler(String assetFolder) {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirAllowed(false);

        // 1. On tente d'abord le dossier de dev
        String devAssetsPath = "src/main/resources/public/" + assetFolder;
        File devDir = new File(devAssetsPath);
        String resourceBase;
        if (devDir.exists()) {
            resourceBase = devDir.getAbsolutePath();
        } else {
            // 2. Fallback sur le classpath
            URL cpUrl = StaticFileServlet.class.getResource("/public/" + assetFolder);
            if (cpUrl == null) {
                resourceBase = "";
            } else {
                resourceBase = cpUrl.toExternalForm();
            }
        }
        resourceHandler.setBaseResource(ResourceFactory.of(resourceHandler)
                .newResource(resourceBase));
        resourceHandler.setCacheControl("max-age=3600,public");
        // on monte ce handler sous /{assetFolder}
        ContextHandler staticContext = new ContextHandler("/" + assetFolder);
        staticContext.setHandler(resourceHandler);
        return staticContext;
    }

    /**
     * Construit et configure le handler statique pour /assets/* (backward compatibility)
     * @return ContextHandler configuré pour servir les fichiers statiques depuis /assets
     */
    public static ContextHandler createStaticHandler() {
        return createStaticHandler("assets");
    }

    /**
     * Crée les handlers statiques pour les deux dossiers d'assets (/assets et /assets-theme)
     * @return Handler.Sequence contenant les handlers pour les deux dossiers d'assets
     */
    public static Handler.Sequence createStaticHandlers() {
        Handler.Sequence handlers = new Handler.Sequence();
        handlers.addHandler(createStaticHandler("assets"));
        handlers.addHandler(createStaticHandler("assets-theme"));
        return handlers;
    }
}