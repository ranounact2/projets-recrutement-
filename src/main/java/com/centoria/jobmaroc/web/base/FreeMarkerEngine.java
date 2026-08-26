package com.centoria.jobmaroc.web.base;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * FreeMarker configuration can be set with the {@link FreeMarkerEngine#setConfiguration(Configuration)} method.
 * If no configuration is set the default configuration will be used where ftl
 * files need to be put in directory resources/template/freemarker.
 *
 * @author Alex
 * @author Per Wendel
 */
@Slf4j
public class FreeMarkerEngine {

    /**
     * The FreeMarker configuration
     */
    private Configuration configuration;

    /**
     * Creates a FreeMarkerEngine
     */
    public FreeMarkerEngine() {
        this.configuration = createDefaultConfiguration();
    }

    /**
     * Creates a FreeMarkerEngine with the specified configuration
     *
     * @param configuration The Freemarker configuration
     */
    public FreeMarkerEngine(Configuration configuration) {
        this.configuration = configuration;
    }


    public String render(Map<String, Object> model, String templatePath) {
        try {
            StringWriter stringWriter = new StringWriter();
            Template template = configuration.getTemplate(templatePath);
            template.process(model, stringWriter);
            return stringWriter.toString();
        } catch (IOException | TemplateException e) {
            log.error("Error rendering FreeMarker template: {}", templatePath, e);
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Sets FreeMarker configuration.
     * Note: If configuration is not set the default configuration will be used.
     *
     * @param configuration the configuration to set
     */
    @Deprecated // use constructor
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    private Configuration createDefaultConfiguration() {
        Configuration configuration = new Configuration(new Version(2, 3, 23));
        configuration.setClassForTemplateLoading(FreeMarkerEngine.class, "/");
        return configuration;
    }

}