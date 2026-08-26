package com.centoria.jobmaroc.web.handler;

import com.centoria.jobmaroc.common.utils.ThemeHelper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.handler.ErrorHandler;
import org.eclipse.jetty.util.Callback;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom Error Handler for embedded Jetty 12 server with EE10 servlets.
 * 
 * This handler:
 * - Catches all errors (404, 500, etc.) and servlet exceptions
 * - Renders custom error pages using FreeMarker templates
 * - Shows stack traces ONLY in development mode (EM_ENV=local or test)
 * - In production, shows a generic error message without technical details
 * 
 * @author Generated for emplois-maroc project
 */
@Slf4j
public class CustomErrorHandler extends ErrorHandler {

    // Jakarta Servlet error attributes (EE10)
    private static final String JAKARTA_ERROR_EXCEPTION = "jakarta.servlet.error.exception";
    private static final String JAKARTA_ERROR_MESSAGE = "jakarta.servlet.error.message";
    private static final String JAKARTA_ERROR_STATUS_CODE = "jakarta.servlet.error.status_code";
    private static final String JAKARTA_ERROR_REQUEST_URI = "jakarta.servlet.error.request_uri";
    
    // Legacy javax attributes (for compatibility)
    private static final String JAVAX_ERROR_EXCEPTION = "javax.servlet.error.exception";
    private static final String JAVAX_ERROR_MESSAGE = "javax.servlet.error.message";
    
    private Configuration freemarkerConfig;

    /**
     * Sets the FreeMarker configuration for template rendering.
     * @param config The FreeMarker configuration
     */
    public void setFreemarkerConfig(Configuration config) {
        this.freemarkerConfig = config;
    }

    /**
     * Handle error responses in Jetty 12.
     * This is the main entry point for error handling in Jetty 12.
     */
    @Override
    public boolean handle(Request request, Response response, Callback callback) throws Exception {
        // Get the error code - try multiple sources
        int code = response.getStatus();
        
        // Check Jakarta servlet error status code attribute
        Object statusCodeAttr = request.getAttribute(JAKARTA_ERROR_STATUS_CODE);
        if (statusCodeAttr instanceof Integer) {
            code = (Integer) statusCodeAttr;
        }
        
        // If status is still OK (200), default to 500 for exceptions or 404 otherwise
        if (code == 200 || code == 0) {
            Throwable exception = getException(request);
            code = (exception != null) ? 500 : 404;
        }
        
        // Get the exception from multiple possible sources
        Throwable throwable = getException(request);
        
        // Get error message - try multiple sources
        String message = getErrorMessage(request, code, throwable);
        
        // Get the request URI
        String requestUri = getRequestUri(request);
        
        // Log the error appropriately
        if (throwable != null) {
            log.error("Error {} on {}: {} - Exception: {}", 
                code, requestUri, message, throwable.getClass().getName(), throwable);
        } else if (code >= 500) {
            log.error("Error {} on {}: {}", code, requestUri, message);
        } else {
            log.debug("Error {} on {}: {}", code, requestUri, message);
        }
        
        // Set response headers
        response.setStatus(code);
        response.getHeaders().put("Content-Type", "text/html;charset=UTF-8");
        
        // Try to render FreeMarker template
        String htmlContent = renderErrorPage(request, code, message, requestUri, throwable);
        
        // Write the response
        ByteBuffer buffer = ByteBuffer.wrap(htmlContent.getBytes(StandardCharsets.UTF_8));
        response.write(true, buffer, callback);
        
        return true; // We handled the error
    }

    /**
     * Gets the exception from various possible request attributes.
     */
    private Throwable getException(Request request) {
        // Try Jetty 12 error attribute first
        Object exception = request.getAttribute(ErrorHandler.ERROR_EXCEPTION);
        if (exception instanceof Throwable) {
            return (Throwable) exception;
        }
        
        // Try Jakarta servlet attribute (EE10)
        exception = request.getAttribute(JAKARTA_ERROR_EXCEPTION);
        if (exception instanceof Throwable) {
            return (Throwable) exception;
        }
        
        // Try legacy javax attribute
        exception = request.getAttribute(JAVAX_ERROR_EXCEPTION);
        if (exception instanceof Throwable) {
            return (Throwable) exception;
        }
        
        return null;
    }
    
    /**
     * Gets the error message from various sources.
     */
    private String getErrorMessage(Request request, int code, Throwable throwable) {
        // Try Jakarta error message attribute
        Object messageAttr = request.getAttribute(JAKARTA_ERROR_MESSAGE);
        if (messageAttr instanceof String && !((String) messageAttr).isEmpty()) {
            return (String) messageAttr;
        }
        
        // Try javax error message attribute  
        messageAttr = request.getAttribute(JAVAX_ERROR_MESSAGE);
        if (messageAttr instanceof String && !((String) messageAttr).isEmpty()) {
            return (String) messageAttr;
        }
        
        // Try Jetty error message attribute
        messageAttr = request.getAttribute(ErrorHandler.ERROR_MESSAGE);
        if (messageAttr instanceof String && !((String) messageAttr).isEmpty()) {
            return (String) messageAttr;
        }
        
        // Use exception message if available
        if (throwable != null && throwable.getMessage() != null) {
            return throwable.getMessage();
        }
        
        // Default to HTTP status message
        return HttpStatus.getMessage(code);
    }
    
    /**
     * Gets the request URI from various sources.
     */
    private String getRequestUri(Request request) {
        // Try Jakarta error request URI attribute
        Object uriAttr = request.getAttribute(JAKARTA_ERROR_REQUEST_URI);
        if (uriAttr instanceof String) {
            return (String) uriAttr;
        }
        
        // Fallback to request path
        return request.getHttpURI().getPath();
    }

    /**
     * Renders the error page using FreeMarker template.
     */
    private String renderErrorPage(Request request, int code, String message, 
                                   String requestUri, Throwable throwable) {
        // Try to get FreeMarker config from request context or use cached one
        Configuration cfg = freemarkerConfig;
        if (cfg == null) {
            // Try to get from servlet context attribute
            Object configAttr = request.getContext().getAttribute("freemarkerConfig");
            if (configAttr instanceof Configuration) {
                cfg = (Configuration) configAttr;
            }
        }
        
        if (cfg != null) {
            try {
                // Build model for FreeMarker template
                Map<String, Object> model = new HashMap<>();
                model.put("code", String.valueOf(code));
                model.put("requestUri", requestUri);
                
                // Add theme-aware assets path to model
                String assetsFolder = ThemeHelper.getAssetsFolder();
                model.put("assetsPath", "/" + assetsFolder);
                
                // Determine if we should show technical details
                boolean showDetails = isDevelopmentMode();
                model.put("showDetails", showDetails);
                
                // Set error message based on mode
                if (showDetails) {
                    // In development: show detailed message
                    model.put("error", message);
                    if (throwable != null) {
                        model.put("exception", throwable);
                        model.put("stackTrace", getStackTrace(throwable));
                    }
                } else {
                    // In production: show generic message
                    if (code == 404) {
                        model.put("error", "Page introuvable");
                    } else if (code >= 500) {
                        model.put("error", "Une erreur interne s'est produite");
                    } else {
                        model.put("error", "Une erreur s'est produite");
                    }
                    // Don't expose exception details in production
                }
                
                // Render the error page using FreeMarker with theme-aware path
                String templatePath = ThemeHelper.resolveTemplatePath("errors/error-page.ftl");
                Template template = cfg.getTemplate(templatePath);
                StringWriter writer = new StringWriter();
                template.process(model, writer);
                return writer.toString();
                
            } catch (TemplateException | IOException e) {
                log.error("Error rendering error page template for code {}", code, e);
            }
        } else {
            log.warn("FreeMarker configuration not found for error page rendering");
        }
        
        // Fallback: return simple HTML
        return generateFallbackHtml(code, message, requestUri, throwable);
    }

    /**
     * Checks if the application is running in development mode.
     * Development mode: EM_ENV is null, "local", or "test"
     * Production mode: EM_ENV is "prod" or any other value
     */
    private boolean isDevelopmentMode() {
        String env = System.getenv("EM_ENV");
        return env == null || "local".equals(env) || "test".equals(env);
    }

    /**
     * Gets the stack trace as a string.
     */
    private String getStackTrace(Throwable throwable) {
        if (throwable == null) {
            return "";
        }
        StringWriter sw = new StringWriter();
        java.io.PrintWriter pw = new java.io.PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }

    /**
     * Generates fallback HTML if template rendering fails.
     */
    private String generateFallbackHtml(int code, String message, String requestUri, Throwable throwable) {
        boolean showDetails = isDevelopmentMode();
        
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n");
        sb.append("<html lang=\"fr\">\n");
        sb.append("<head>\n");
        sb.append("  <meta charset=\"UTF-8\">\n");
        sb.append("  <title>Erreur ").append(code).append("</title>\n");
        sb.append("  <style>\n");
        sb.append("    body { font-family: Arial, sans-serif; text-align: center; padding: 50px; }\n");
        sb.append("    h1 { color: #333; }\n");
        sb.append("    p { color: #666; }\n");
        sb.append("    a { color: #007bff; text-decoration: none; }\n");
        sb.append("    pre { text-align: left; background: #f5f5f5; padding: 15px; overflow: auto; max-height: 400px; }\n");
        sb.append("  </style>\n");
        sb.append("</head>\n");
        sb.append("<body>\n");
        sb.append("  <h1>Erreur ").append(code).append("</h1>\n");
        
        if (showDetails) {
            sb.append("  <p>").append(escapeHtml(message)).append("</p>\n");
            if (requestUri != null && !requestUri.isEmpty()) {
                sb.append("  <p>Page demandée: ").append(escapeHtml(requestUri)).append("</p>\n");
            }
            if (throwable != null) {
                sb.append("  <h3>Exception: ").append(escapeHtml(throwable.getClass().getName())).append("</h3>\n");
                sb.append("  <p>Message: ").append(escapeHtml(throwable.getMessage())).append("</p>\n");
                sb.append("  <h4>Stack Trace:</h4>\n");
                sb.append("  <pre>").append(escapeHtml(getStackTrace(throwable))).append("</pre>\n");
            }
        } else {
            // Production mode - generic message
            if (code == 404) {
                sb.append("  <p>La page demandée n'existe pas.</p>\n");
            } else {
                sb.append("  <p>Une erreur interne s'est produite. Veuillez réessayer plus tard.</p>\n");
            }
        }
        
        sb.append("  <p><a href=\"/\">Retour à la page d'accueil</a></p>\n");
        sb.append("</body>\n");
        sb.append("</html>\n");
        return sb.toString();
    }

    /**
     * Escapes HTML special characters.
     */
    private String escapeHtml(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }
}
