# SparkJava to Servlet Migration Checklist

## Overview
This document tracks the migration progress from SparkJava to Servlet-based architecture with embedded Jetty.

---

## ✅ Completed Tasks

### Core Infrastructure
- [x] Embedded Jetty server setup in `Main.java`
- [x] FreeMarker template engine configuration
- [x] BaseServlet abstract class implementation
- [x] Static file serving (StaticFileServlet)
- [x] Robots.txt dynamic servlet

### Frontend Routes (FrontController)
- [x] `/` - IndexServlet
- [x] `/categorie` and `/categorie/` - CategoryServlet
- [x] `/categorie/*` - CategoryByDomainServlet
- [x] `/region/*` - RegionServlet (with delegation)
- [x] `/contact` (GET/POST) - ContactServlet
- [x] `/a-propos-emplois-maroc` - AboutServlet
- [x] `/mentions` - MentionsServlet
- [x] `/offre-emploi-maroc/*` - DetailOfferServlet
- [x] `/ajouter-offre-emploi` (GET/POST with multipart) - AddJobOfferServlet

### Middle Office Routes (MoController)
- [x] `/mes-annonces-emploi` - MyJobAdsServlet (partial - verify all routes)

---

## ⚠️ Partially Complete / Needs Verification

### Middle Office Routes
- [ ] `/m-office/mes-annonces/:secretCode` - Verify if covered by MyJobAdsServlet
- [ ] `/backoffice/mes-annonces/:secretCode/delete/:id` - Needs servlet
- [ ] `/m-office/mes-annonces/:secretCode/update/:id` - Needs servlet
- [ ] `/m-office/mes-annonces/:secretCode/updateState/:id` - Needs servlet
- [ ] `/verification/:code/:id` - Needs servlet

### Frontend Routes (Missing)
- [ ] `/keyword` - Search by keyword (showResultByKeyword)
- [ ] `/ville` and `/ville/` - All cities listing
- [ ] `/ville/:city` - Jobs by city
- [ ] `/postuler-emploi/:id` (GET/POST) - Apply for job
- [ ] `/categorie/:domain/:region` - Category by domain and region
- [ ] `/categorie/:domain/:region/:city` - Category by domain, region, city
- [ ] `/categorie/:domain/:city` - Category by domain and city

### Error Handling
- [ ] `/500` - Error page servlet
- [ ] `/404` - Not found handler
- [ ] Global exception handlers (BusinessException, TechnicalException, etc.)

---

## ❌ Not Started

### Back Office Routes (BoController)
- [ ] `/rtsystems_admin_8965_hXtwWUCzukqwPyEuWxcE` (GET) - Admin index
- [ ] `/rtsystems_admin_8965_hXtwWUCzukqwPyEuWxcE` (POST) - Admin actions (validate, delete, disable)

### Filters
- [ ] Breadcrumb generation filter (FilterController)
- [ ] Convert to Servlet Filter or implement in BaseServlet

### Configuration Cleanup
- [ ] Remove SparkFilter from `web.xml`
- [ ] Clean up commented SparkJava code (or archive)
- [ ] Remove duplicate FreeMarker dependency (2.3.31 vs 2.3.33)
- [ ] Remove commented `spark.Request` imports from DTOs

---

## 🔧 Technical Debt

### Code Cleanup
- [ ] Remove or archive old controller classes:
  - `FrontController.java` (all routes commented)
  - `BoController.java` (all routes commented)
  - `MoController.java` (all routes commented)
  - `FilterController.java` (all filters commented)
- [ ] Remove `BaseController.java` (marked for deletion)
- [ ] Clean up commented code in DTOs:
  - `UserDto.java` - commented `spark.Request` import
  - `MoDto.java` - commented `spark.Request` import

### Documentation
- [ ] Update README.md with new architecture
- [ ] Document servlet registration patterns
- [ ] Document error handling approach

### Testing
- [ ] Unit tests for new servlets
- [ ] Integration tests for routes
- [ ] Test multipart file uploads
- [ ] Test error scenarios (404, 500)
- [ ] Test i18n (French/Arabic switching)

---

## 📋 Implementation Guide

### Creating a New Servlet

1. **Create Servlet Class**
   ```java
   public class MyServlet extends BaseServlet {
       @Override
       protected void doGet(HttpServletRequest req, HttpServletResponse resp)
               throws ServletException, IOException {
           // Implementation
       }
   }
   ```

2. **Register in Main.java**
   ```java
   servletContext.addServlet(
       new ServletHolder(new MyServlet()),
       "/my-path/*"
   );
   ```

3. **Handle Path Parameters**
   ```java
   String pathInfo = req.getPathInfo();
   String[] parts = pathInfo.split("/");
   String param = parts[1]; // First path segment
   ```

4. **Render Template**
   ```java
   Map<String, Object> model = new HashMap<>();
   model.put("data", result);
   dispatch(req, resp, "front/template.ftl", model);
   ```

### Handling POST Requests

```java
@Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
    String param = req.getParameter("key");
    // Process and redirect or dispatch
}
```

### Multipart File Upload

```java
// In Main.java
MultipartConfigElement multipartConfig = new MultipartConfigElement(
    System.getProperty("java.io.tmpdir"),
    5 * 1024 * 1024,  // maxFileSize
    10 * 1024 * 1024,  // maxRequestSize
    0                  // fileSizeThreshold
);

ServletHolder holder = new ServletHolder(new MyServlet());
holder.getRegistration().setMultipartConfig(multipartConfig);
servletContext.addServlet(holder, "/upload");
```

### Error Handling

```java
// In servlet
showError(HttpServletResponse.SC_NOT_FOUND, "Page not found", req, resp);
```

---

## 🎯 Priority Order

### High Priority
1. Remove SparkFilter from `web.xml` (blocking deployment)
2. Complete missing frontend routes (user-facing)
3. Implement error handlers (404/500)

### Medium Priority
4. Complete Middle Office routes
5. Complete Back Office routes
6. Implement breadcrumb filter

### Low Priority
7. Code cleanup (remove commented code)
8. Documentation updates
9. Testing

---

## 📊 Migration Progress

**Overall Progress**: ~70% Complete

- **Core Infrastructure**: 100% ✅
- **Frontend Routes**: ~60% ⚠️
- **Middle Office**: ~30% ⚠️
- **Back Office**: 0% ❌
- **Filters**: 0% ❌
- **Error Handling**: ~20% ⚠️
- **Configuration Cleanup**: 0% ❌

---

## 🔍 Verification Steps

After completing migration:

1. **Start Server**
   ```bash
   java -jar target/emploismaroc.jar
   ```

2. **Test All Routes**
   - [ ] Homepage loads
   - [ ] Categories page works
   - [ ] Region pages work
   - [ ] Contact form works (GET and POST)
   - [ ] Job detail pages work
   - [ ] Job application form works
   - [ ] Admin routes work
   - [ ] Middle office routes work

3. **Test Error Scenarios**
   - [ ] 404 page displays correctly
   - [ ] 500 page displays correctly
   - [ ] Invalid routes handled properly

4. **Test File Uploads**
   - [ ] Job offer logo upload
   - [ ] CV upload for applications

5. **Test i18n**
   - [ ] Language switching works
   - [ ] Templates render in both languages

---

## 📝 Notes

- All SparkJava code is commented out (safe to remove after verification)
- `web.xml` still contains SparkFilter (needs removal)
- Application runs as embedded Jetty (standalone JAR)
- Configuration via `EM_ENV` environment variable
- Templates in `src/main/resources/`

---

**Last Updated**: Based on current codebase analysis
