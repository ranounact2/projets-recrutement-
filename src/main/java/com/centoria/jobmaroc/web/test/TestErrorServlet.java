package com.centoria.jobmaroc.web.test;

import com.centoria.jobmaroc.model.exception.BusinessException;
import com.centoria.jobmaroc.model.exception.TechnicalException;
import com.centoria.jobmaroc.web.servlet.BaseServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Test servlet to demonstrate exception handling.
 * 
 * This servlet can be used to test the global error handling mechanism:
 * - /test-error/500 - Throws a RuntimeException (500 error)
 * - /test-error/404 - Returns 404 status
 * - /test-error/null - Throws NullPointerException
 * - /test-error/business - Throws BusinessException
 * 
 * Usage:
 * - Add this servlet to Main.java for testing
 * - Access /test-error/500 to see error handling in action
 * - Remove or comment out in production
 * 
 * @author Generated for testing error handling
 */
@Slf4j
public class TestErrorServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Usage: /test-error/{type} where type is: 500, 404, null, business");
            return;
        }
        
        String errorType = pathInfo.substring(1); // Remove leading "/"
        
        log.info("Test error servlet called with type: {}", errorType);
        
        switch (errorType) {
            case "500":
                // Simulate a 500 error with RuntimeException
                throw new RuntimeException("Test 500 error - This is a simulated server error");
                
            case "404":
                // Simulate a 404 error
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                showError(HttpServletResponse.SC_NOT_FOUND, "Test 404 - Page not found", req, resp);
                break;
                
            case "null":
                // Simulate NullPointerException
                throw new NullPointerException("Test NullPointerException - Simulated null pointer access");
                
            case "business":
                // Simulate BusinessException
                throw new BusinessException("400", "Test business exception");
                
            case "technical":
                // Simulate TechnicalException
                throw new TechnicalException("500", "Test technical exception");
                
            default:
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Unknown error type: " + errorType + 
                    ". Valid types: 500, 404, null, business, technical");
        }
    }
}
