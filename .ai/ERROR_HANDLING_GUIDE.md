# Global Exception Handling Guide

This document explains the global exception handling system implemented for the embedded Jetty application.

## Overview

The application uses a **CustomErrorHandler** that catches all exceptions thrown by servlets and forwards them to an **ErrorPageServlet** that renders custom error pages using FreeMarker templates.

## Architecture

```
┌─────────────────────────────────────────────────────────┐
│  Servlet throws exception                               │
└──────────────────┬──────────────────────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────────────────────┐
│  CustomErrorHandler (Jetty ErrorHandler)               │
│  - Catches exception                                    │
│  - Logs error details                                   │
│  - Sets request attributes                              │
│  - Forwards to ErrorPageServlet                         │
└──────────────────┬──────────────────────────────────────┘
                   │
                   ▼
┌─────────────────────────────────────────────────────────┐
│  ErrorPageServlet                                       │
│  - Extracts error details from request attributes       │
│  - Builds FreeMarker model                              │
│  - Renders error-page.ftl template                     │
└─────────────────────────────────────────────────────────┘
```

## Components

### 1. CustomErrorHandler

**Location**: `src/main/java/com/centoria/jobmaroc/web/handler/CustomErrorHandler.java`

**Purpose**: 
- Extends Jetty's `ErrorHandler` class
- Intercepts all exceptions thrown by servlets
- Logs exception details with full stack traces
- Forwards to ErrorPageServlet for rendering

**Key Features**:
- Logs all exceptions with context (status code, URI, message, stack trace)
- Supports different error pages for different HTTP status codes
- Provides fallback HTML response if error page servlet fails
- Escapes HTML to prevent XSS attacks

**Request Attributes Set**:
- `errorCode`: HTTP status code (Integer)
- `errorMessage`: Error message (String)
- `errorException`: The exception object (Throwable)
- `errorRequestUri`: The URI that caused the error (String)

### 2. ErrorPageServlet

**Location**: `src/main/java/com/centoria/jobmaroc/web/servlet/ErrorPageServlet.java`

**Purpose**:
- Renders custom error pages using FreeMarker templates
- Handles different error codes (404, 500, 403, 401, 400)
- Shows exception details in development mode only

**Routes**:
- `/error/404` - Not Found
- `/error/500` - Internal Server Error
- `/error/403` - Forbidden
- `/error/401` - Unauthorized
- `/error/400` - Bad Request

**Template**: Uses `errors/error-page.ftl` (existing template)

**Model Variables Available in Template**:
- `${error}` - Error message
- `${code}` - HTTP status code (as String)
- `${requestUri}` - The URI that caused the error
- `${exception}` - Exception object (only in development mode)
- `${stackTrace}` - Stack trace as string (only in development mode)

### 3. Configuration in Main.java

The error handler is configured at two levels:

1. **ServletContextHandler level**: Handles exceptions from servlets
2. **Server level**: Handles unhandled exceptions at the server level

```java
// Create custom error handler
CustomErrorHandler customErrorHandler = new CustomErrorHandler();

// Set on ServletContextHandler
servletContext.setErrorHandler(customErrorHandler);

// Set on Server (for unhandled exceptions)
server.setErrorHandler(customErrorHandler);

// Register ErrorPageServlet
servletContext.addServlet(
    new ServletHolder(new ErrorPageServlet()),
    "/error/*"
);
```

## How It Works

### Exception Flow

1. **Servlet throws exception**:
   ```java
   throw new RuntimeException("Something went wrong");
   ```

2. **CustomErrorHandler intercepts**:
   - Jetty catches the exception
   - CustomErrorHandler.handle() is called
   - Exception details are logged
   - Request attributes are set

3. **Forward to ErrorPageServlet**:
   - CustomErrorHandler forwards to `/error/500`
   - ErrorPageServlet extracts error details
   - FreeMarker template is rendered
   - HTML response is sent to client

### Logging

All exceptions are logged with:
- HTTP status code
- Request URI
- Error message
- Full stack trace

Example log output:
```
ERROR CustomErrorHandler - Error 500 on /test-error/500: Test 500 error - This is a simulated server error
java.lang.RuntimeException: Test 500 error - This is a simulated server error
    at com.centoria.jobmaroc.web.test.TestErrorServlet.doGet(TestErrorServlet.java:45)
    ...
```

## Customization

### Custom Error Pages for Different Status Codes

To use different templates for different error codes, modify `ErrorPageServlet.determineTemplatePath()`:

```java
private String determineTemplatePath(int errorCode) {
    switch (errorCode) {
        case HttpServletResponse.SC_NOT_FOUND:
            return "errors/404.ftl";
        case HttpServletResponse.SC_INTERNAL_SERVER_ERROR:
            return "errors/500.ftl";
        case HttpServletResponse.SC_FORBIDDEN:
            return "errors/403.ftl";
        default:
            return "errors/error-page.ftl";
    }
}
```

### Development vs Production Mode

The `ErrorPageServlet` shows exception details only in development mode:

```java
private boolean isDevelopmentMode() {
    String env = System.getenv("EM_ENV");
    return env == null || "local".equals(env) || "test".equals(env);
}
```

In production (when `EM_ENV=prod`), stack traces are hidden from users.

### Custom Error Messages

You can customize default error messages in `ErrorPageServlet.getDefaultErrorMessage()`:

```java
private String getDefaultErrorMessage(int errorCode) {
    switch (errorCode) {
        case HttpServletResponse.SC_NOT_FOUND:
            return "La page demandée n'existe pas";
        // ... other cases
    }
}
```

## Testing

### Using TestErrorServlet

A test servlet is provided to test error handling:

**Location**: `src/main/java/com/centoria/jobmaroc/web/test/TestErrorServlet.java`

**Add to Main.java** (for testing only):
```java
import com.centoria.jobmaroc.web.test.TestErrorServlet;

// ...

servletContext.addServlet(
    new ServletHolder(new TestErrorServlet()),
    "/test-error/*"
);
```

**Test URLs**:
- `http://localhost:8080/test-error/500` - Throws RuntimeException
- `http://localhost:8080/test-error/404` - Returns 404
- `http://localhost:8080/test-error/null` - Throws NullPointerException
- `http://localhost:8080/test-error/business` - Throws BusinessException
- `http://localhost:8080/test-error/technical` - Throws TechnicalException

### Manual Testing

1. **Test 500 error**: Access a servlet that throws an exception
2. **Test 404 error**: Access a non-existent URL
3. **Check logs**: Verify exceptions are logged correctly
4. **Check error page**: Verify custom error page is displayed

## Best Practices

### 1. Exception Handling in Servlets

**Good**: Let exceptions propagate to the error handler
```java
public class MyServlet extends BaseServlet {
    protected void doGet(...) throws ServletException, IOException {
        // Business logic that may throw exceptions
        someService.doSomething(); // May throw BusinessException
    }
}
```

**Bad**: Catching and swallowing exceptions
```java
try {
    someService.doSomething();
} catch (Exception e) {
    // Don't do this - error handler won't see it
}
```

### 2. Logging in Servlets

Use `@Slf4j` and log errors before rethrowing:
```java
@Slf4j
public class MyServlet extends BaseServlet {
    try {
        // ...
    } catch (BusinessException e) {
        log.error("Business error occurred", e);
        throw e; // Let error handler deal with it
    }
}
```

### 3. Error Page Templates

Keep error pages simple and user-friendly:
- Don't expose technical details in production
- Provide helpful messages
- Include a link back to homepage
- Use consistent styling

### 4. Monitoring

Monitor error logs for:
- Frequent 500 errors (indicates bugs)
- 404 errors (broken links)
- Exception patterns (common causes)

## Troubleshooting

### Error Page Not Displaying

1. **Check ErrorPageServlet is registered**: Verify in Main.java
2. **Check template exists**: Verify `errors/error-page.ftl` exists
3. **Check FreeMarker config**: Verify `freemarkerConfig` is in servlet context
4. **Check logs**: Look for errors in ErrorPageServlet

### Exceptions Not Caught

1. **Verify error handler is set**: Check both servletContext and server level
2. **Check exception type**: Some exceptions might need special handling
3. **Check servlet order**: Ensure ErrorPageServlet is registered before catch-all servlets

### Stack Traces in Production

1. **Check EM_ENV**: Ensure `EM_ENV=prod` is set
2. **Verify isDevelopmentMode()**: Check the logic in ErrorPageServlet

## Example: Complete Error Handling Flow

```java
// 1. Servlet throws exception
public class MyServlet extends BaseServlet {
    protected void doGet(...) {
        throw new BusinessException("400", "Invalid input");
    }
}

// 2. CustomErrorHandler catches it
// - Logs: "Error 500 on /my-servlet: Invalid input"
// - Sets request attributes
// - Forwards to /error/500

// 3. ErrorPageServlet renders
// - Extracts error details
// - Builds model: {error: "Invalid input", code: "500", ...}
// - Renders errors/error-page.ftl

// 4. User sees custom error page
```

## Summary

The global exception handling system provides:
- ✅ Automatic exception catching
- ✅ Comprehensive logging
- ✅ Custom error pages
- ✅ Development/production mode support
- ✅ Easy customization
- ✅ Production-ready implementation

All exceptions are now properly handled, logged, and displayed to users with appropriate error pages.
