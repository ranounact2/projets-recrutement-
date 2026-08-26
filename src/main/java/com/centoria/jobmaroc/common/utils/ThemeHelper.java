package com.centoria.jobmaroc.common.utils;

import com.centoria.jobmaroc.common.constants.ThemeConstants;
import com.centoria.jobmaroc.common.context.ApplicationContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThemeHelper {
    private static String currentTheme = null;
    
    /**
     * Gets the current theme suffix from configuration
     * @return theme suffix (e.g., "" for default, "-new" for new theme)
     */
    public static String getThemeSuffix() {
        if (currentTheme == null) {
            String themeConfig = ApplicationContext.getInstance()
                .getProps()
                .getValue("theme.name", ThemeConstants.THEME_DEFAULT);
            
            // Map config value "new" to suffix "-new"
            currentTheme = "new".equals(themeConfig) 
                ? ThemeConstants.THEME_NEW 
                : ThemeConstants.THEME_DEFAULT;
            
            log.info("Theme config value: '{}', Theme suffix: '{}'", 
                    themeConfig, currentTheme.isEmpty() ? "default" : currentTheme);
        }
        return currentTheme;
    }
    
    /**
     * Resolves template path with theme suffix
     * @param templatePath original template path (e.g., "front/offre.ftl")
     * @return themed template path (e.g., "front-new/offre.ftl" if theme is "new")
     */
    public static String resolveTemplatePath(String templatePath) {
        String themeSuffix = getThemeSuffix();
        if (themeSuffix.isEmpty()) {
            log.debug("No theme suffix, returning original path: {}", templatePath);
            return templatePath;
        }
        
        // Check if path already has theme suffix to prevent double replacement
        if (templatePath.contains(themeSuffix + "/")) {
            log.debug("Path already contains theme suffix, returning as-is: {}", templatePath);
            return templatePath;
        }
        
        // Replace folder names with themed versions
        String resolved = templatePath;
        
        // Handle paths starting with folder name (e.g., "front/index.ftl")
        resolved = resolved.replaceFirst("^front/", "front" + themeSuffix + "/");
        resolved = resolved.replaceFirst("^common/", "common" + themeSuffix + "/");
        resolved = resolved.replaceFirst("^m-office/", "m-office" + themeSuffix + "/");
        resolved = resolved.replaceFirst("^back-office/", "back-office" + themeSuffix + "/");
        resolved = resolved.replaceFirst("^errors/", "errors" + themeSuffix + "/");
        resolved = resolved.replaceFirst("^emails/", "emails" + themeSuffix + "/");
        
        // Handle paths with subdirectories (e.g., "front/temp/index.ftl")
        resolved = resolved.replace("/front/", "/front" + themeSuffix + "/");
        resolved = resolved.replace("/common/", "/common" + themeSuffix + "/");
        resolved = resolved.replace("/m-office/", "/m-office" + themeSuffix + "/");
        resolved = resolved.replace("/back-office/", "/back-office" + themeSuffix + "/");
        resolved = resolved.replace("/errors/", "/errors" + themeSuffix + "/");
        resolved = resolved.replace("/emails/", "/emails" + themeSuffix + "/");
        
        log.debug("Resolved template path: {} -> {}", templatePath, resolved);
        return resolved;
    }
    
    /**
     * Gets the assets folder name based on current theme
     * @return "assets" for default theme, "assets-theme" for new theme
     */
    public static String getAssetsFolder() {
        String themeSuffix = getThemeSuffix();
        return themeSuffix.isEmpty() 
            ? ThemeConstants.ASSETS_DEFAULT 
            : ThemeConstants.ASSETS_THEME;
    }
    
    /**
     * Resets cached theme (useful for testing or config reload)
     */
    public static void resetTheme() {
        currentTheme = null;
    }
}
