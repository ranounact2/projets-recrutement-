# Quick Reference: SparkJava → Servlet Migration

## Key Differences

| Aspect | SparkJava (Old) | Servlet (New) |
|--------|----------------|---------------|
| **Entry Point** | `Spark.get()`, `Spark.post()` | `doGet()`, `doPost()` methods |
| **Request Object** | `spark.Request` | `HttpServletRequest` |
| **Response Object** | `spark.Response` | `HttpServletResponse` |
| **Path Params** | `request.params("id")` | `req.getPathInfo()` then parse |
| **Query Params** | `request.queryParams("key")` | `req.getParameter("key")` |
| **Template Rendering** | `ModelAndView` + `TEMPLATEENGINE.render()` | `dispatch(req, resp, "template.ftl", model)` |
| **Registration** | In `defineRoutes()` method | In `Main.java` with `addServlet()` |
| **Base Class** | `BaseController` | `BaseServlet` |
| **Multipart** | `request.raw().getPart()` | `req.getPart()` (with MultipartConfig) |

---

## Common Patterns

### 1. Simple GET Route

**Old (SparkJava)**:
```java
Spark.get("/about", (request, response) -> {
    Map<String, Object> map = getMap(request);
    return getBasePage("front/about.ftl", map, request);
}, TEMPLATEENGINE);
```

**New (Servlet)**:
```java
public class AboutServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Map<String, Object> model = new HashMap<>();
        dispatch(req, resp, "front/about.ftl", model);
    }
}

// In Main.java:
servletContext.addServlet(
    new ServletHolder(new AboutServlet()),
    "/about"
);
```

---

### 2. Route with Path Parameter

**Old (SparkJava)**:
```java
Spark.get("/job/:id", (request, response) -> {
    String id = request.params("id");
    // ... use id
}, TEMPLATEENGINE);
```

**New (Servlet)**:
```java
public class JobServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String pathInfo = req.getPathInfo(); // "/123" or "/123/"
        String id = pathInfo.replaceAll("^/|/$", "");
        // ... use id
    }
}

// In Main.java:
servletContext.addServlet(
    new ServletHolder(new JobServlet()),
    "/job/*"
);
```

---

### 3. POST Route with Form Data

**Old (SparkJava)**:
```java
Spark.post("/contact", (request, response) -> {
    String name = request.queryParams("name");
    String email = request.queryParams("email");
    // ... process
}, TEMPLATEENGINE);
```

**New (Servlet)**:
```java
public class ContactServlet extends BaseServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        // ... process
        dispatch(req, resp, "front/contact.ftl", model);
    }
}
```

---

### 4. Multipart File Upload

**Old (SparkJava)**:
```java
MultipartConfigElement multipartConfig = new MultipartConfigElement(
    System.getProperty("java.io.tmpdir")
);
request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfig);
Part file = request.raw().getPart("logoImage");
```

**New (Servlet)**:
```java
// In Main.java:
MultipartConfigElement multipartConfig = new MultipartConfigElement(
    System.getProperty("java.io.tmpdir"),
    5 * 1024 * 1024,  // maxFileSize
    10 * 1024 * 1024,  // maxRequestSize
    0
);
ServletHolder holder = new ServletHolder(new UploadServlet());
holder.getRegistration().setMultipartConfig(multipartConfig);
servletContext.addServlet(holder, "/upload");

// In servlet:
Part file = req.getPart("logoImage");
```

---

### 5. Error Handling

**Old (SparkJava)**:
```java
Spark.exception(Exception.class, (e, request, response) -> {
    log.error("Exception occurred", e);
    log.error(e.getMessage());
});

Spark.notFound((request, response) -> {
    return show404(request, response);
});
```

**New (Servlet)**:
```java
// In servlet:
try {
    // ... code
} catch (BusinessException e) {
    showError(HttpServletResponse.SC_BAD_REQUEST, 
              "Business error: " + e.getMessage(), req, resp);
} catch (Exception e) {
    showError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
              "Server error", req, resp);
}

// For 404, use showError with SC_NOT_FOUND
showError(HttpServletResponse.SC_NOT_FOUND, "Page not found", req, resp);
```

---

### 6. Path-based Routing (Multiple Segments)

**Old (SparkJava)**:
```java
Spark.path("/region", () -> {
    Spark.get("", this::showAllRegions, TEMPLATEENGINE);
    Spark.get("/:region", this::showCities, TEMPLATEENGINE);
    Spark.get("/:region/:city", this::showDomains, TEMPLATEENGINE);
});
```

**New (Servlet)**:
```java
public class RegionServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String pathInfo = req.getPathInfo(); // null, "/", "/casablanca", "/casablanca/rabat"
        String clean = (pathInfo == null ? "" : pathInfo.replaceAll("^/|/$", ""));
        String[] parts = clean.isEmpty() ? new String[0] : clean.split("/");
        
        if (parts.length == 0) {
            // /region → show all regions
            showAllRegions(req, resp);
        } else if (parts.length == 1) {
            // /region/casablanca → show cities
            showCities(req, resp, parts[0]);
        } else if (parts.length == 2) {
            // /region/casablanca/rabat → show domains
            showDomains(req, resp, parts[0], parts[1]);
        } else {
            showError(404, "Invalid path", req, resp);
        }
    }
}

// In Main.java:
servletContext.addServlet(
    new ServletHolder(new RegionServlet()),
    "/region/*"
);
```

---

### 7. Building Site URL

**Old (SparkJava)**:
```java
String host = request.scheme() + "://" + request.host();
```

**New (Servlet)**:
```java
String scheme = req.getScheme();
String serverName = req.getServerName();
int serverPort = req.getServerPort();
String siteUrl = scheme + "://" + serverName
    + ((scheme.equals("http") && serverPort != 80)
    || (scheme.equals("https") && serverPort != 443)
    ? ":" + serverPort : "");
```

---

### 8. Cookie Access

**Old (SparkJava)**:
```java
String locale = request.cookies().get("local");
```

**New (Servlet)**:
```java
String locale = Optional.ofNullable(req.getCookies())
    .flatMap(cookies -> Arrays.stream(cookies)
        .filter(c -> "local".equals(c.getName()))
        .map(Cookie::getValue)
        .findFirst())
    .orElse("fr");
```

---

## File Locations

| Component | Location |
|-----------|----------|
| **Servlets** | `src/main/java/com/centoria/jobmaroc/web/servlet/` |
| **Base Servlet** | `src/main/java/com/centoria/jobmaroc/web/servlet/BaseServlet.java` |
| **Main Entry** | `src/main/java/com/centoria/jobmaroc/web/Main.java` |
| **Templates** | `src/main/resources/front/`, `src/main/resources/back-office/`, etc. |
| **Static Files** | `src/main/resources/public/` |
| **Config** | `src/main/resources/config/` |

---

## URL Patterns in Main.java

Current servlet registrations (as of analysis):

```java
// Root/catch-all
servletContext.addServlet(new ServletHolder(new IndexServlet()), "/*");

// Specific routes (order matters - more specific first)
servletContext.addServlet(new ServletHolder(new RobotsServlet()), "/robots.txt");
servletContext.addServlet(new ServletHolder(new RegionServlet()), "/region/*");
servletContext.addServlet(new ServletHolder(new CategoryServlet()), "/categorie");
servletContext.addServlet(new ServletHolder(new CategoryServlet()), "/categorie/");
servletContext.addServlet(new ServletHolder(new CategoryByDomainServlet()), "/categorie/*");
servletContext.addServlet(new ServletHolder(new ContactServlet()), "/contact");
servletContext.addServlet(new ServletHolder(new AboutServlet()), "/a-propos-emplois-maroc");
servletContext.addServlet(new ServletHolder(new MentionsServlet()), "/mentions");
servletContext.addServlet(new ServletHolder(new DetailOfferServlet()), "/offre-emploi-maroc/*");
servletContext.addServlet(new ServletHolder(new AddJobOfferServlet()), "/ajouter-offre-emploi");
servletContext.addServlet(new ServletHolder(new MyJobAdsServlet()), "/mes-annonces-emploi");
```

**Note**: Order matters! More specific patterns should be registered before catch-all patterns.

---

## Common Pitfalls

1. **Path Parameter Extraction**: Use `req.getPathInfo()` not `req.getRequestURI()` for path parameters
2. **Servlet Order**: Register specific patterns before catch-all (`/*`)
3. **Multipart Config**: Must be set on ServletHolder, not servlet class
4. **Template Path**: Use relative paths from `src/main/resources/`
5. **Error Handling**: Always use `showError()` from BaseServlet, don't set status manually
6. **Loop Prevention**: Some servlets check `req.getAttribute("processed")` to prevent loops

---

## Testing Checklist

- [ ] Servlet responds to GET requests
- [ ] Servlet responds to POST requests (if applicable)
- [ ] Path parameters extracted correctly
- [ ] Query parameters read correctly
- [ ] Template renders without errors
- [ ] Error cases handled (404, 500)
- [ ] File uploads work (if applicable)
- [ ] Cookies read/written correctly
- [ ] i18n works (language switching)

---

**Last Updated**: Based on current codebase analysis
