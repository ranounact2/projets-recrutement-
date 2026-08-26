# Codebase Structure Analysis - SparkJava to Servlet Migration

## Executive Summary

This codebase is in the process of migrating from **SparkJava** framework to **Servlet-based architecture with embedded Jetty**. The migration is partially complete - many routes have been converted to servlets, but some legacy SparkJava configurations remain.

---

## 1. Architecture Overview

### Current State
- **Framework**: Migrating from SparkJava → Servlet API with Embedded Jetty
- **Server**: Embedded Jetty 11.0.15
- **Templating**: FreeMarker 2.3.31/2.3.33
- **Database**: MongoDB 5.1.1
- **Java Version**: 17
- **Packaging**: JAR (with embedded server)

### Architecture Layers

```
┌─────────────────────────────────────┐
│   Web Layer (servlet/)              │  ← New Servlet-based controllers
├─────────────────────────────────────┤
│   Web Layer (web/)                  │  ← Old SparkJava controllers (commented)
├─────────────────────────────────────┤
│   Application Layer (app/)          │  ← Business logic
├─────────────────────────────────────┤
│   Service Layer (service/)          │  ← Services
├─────────────────────────────────────┤
│   DAO Layer (dao/)                  │  ← Data access
├─────────────────────────────────────┤
│   Model Layer (model/)              │  ← Domain models
├─────────────────────────────────────┤
│   DTO Layer (dto/)                  │  ← Data transfer objects
└─────────────────────────────────────┘
```

---

## 2. Key Components

### 2.1 Main Entry Point
**File**: `src/main/java/com/centoria/jobmaroc/web/Main.java`

- **Purpose**: Application bootstrap and server initialization
- **Current Implementation**: 
  - Configures FreeMarker template engine
  - Sets up embedded Jetty server on port 8080
  - Registers servlets with URL patterns
  - Handles static file serving
- **Status**: ✅ Migrated to Servlet-based approach

**Key Servlets Registered**:
- `IndexServlet` → `/*` (catch-all, handles root and index)
- `RobotsServlet` → `/robots.txt`
- `RegionServlet` → `/region/*`
- `CategoryServlet` → `/categorie` and `/categorie/`
- `CategoryByDomainServlet` → `/categorie/*`
- `AddJobOfferServlet` → `/ajouter-offre-emploi` (with multipart support)
- `MyJobAdsServlet` → `/mes-annonces-emploi`
- `ContactServlet` → `/contact`
- `AboutServlet` → `/a-propos-emplois-maroc`
- `MentionsServlet` → `/mentions`
- `DetailOfferServlet` → `/offre-emploi-maroc/*`

### 2.2 Base Classes

#### BaseServlet (New - Active)
**File**: `src/main/java/com/centoria/jobmaroc/web/servlet/BaseServlet.java`

- **Purpose**: Base class for all servlets
- **Key Features**:
  - Template rendering via `dispatch()` method
  - Internationalization (i18n) support
  - Zone page management
  - Error handling (`showError()`)
  - Cookie-based locale detection

#### BaseController (Old - Deprecated)
**File**: `src/main/java/com/centoria/jobmaroc/web/base/BaseController.java`

- **Status**: All methods commented out
- **Purpose**: Previously used by SparkJava controllers
- **Note**: Marked with `@TODO delete after keep now to move method in the new BaseServlet`

### 2.3 Old Controllers (Commented Out)

#### FrontController
**File**: `src/main/java/com/centoria/jobmaroc/web/FrontController.java`

- **Status**: All SparkJava routes commented out
- **Old Routes** (now migrated to servlets):
  - `/` → `IndexServlet`
  - `/categorie/*` → `CategoryServlet`, `CategoryByDomainServlet`
  - `/ville/*` → Various city-related servlets
  - `/contact` → `ContactServlet`
  - `/offre-emploi-maroc/:id` → `DetailOfferServlet`
  - `/ajouter-offre-emploi` → `AddJobOfferServlet`
  - `/region/*` → `RegionServlet`

#### BoController (Back Office)
**File**: `src/main/java/com/centoria/jobmaroc/web/BoController.java`

- **Status**: Routes commented out
- **Old Routes**:
  - `/rtsystems_admin_8965_hXtwWUCzukqwPyEuWxcE` (GET/POST)
- **Status**: ❌ Not yet migrated to servlets

#### MoController (Middle Office)
**File**: `src/main/java/com/centoria/jobmaroc/web/MoController.java`

- **Status**: Routes commented out
- **Old Routes**:
  - `/m-office/mes-annonces/:secretCode`
  - `/backoffice/mes-annonces/:secretCode/delete/:id`
  - `/m-office/mes-annonces/:secretCode/update/:id`
  - `/m-office/mes-annonces/:secretCode/updateState/:id`
  - `/mes-annonces-emploi` (GET/POST)
  - `/verification/:code/:id`
- **Status**: ⚠️ Partially migrated (`MyJobAdsServlet` exists but may not cover all routes)

#### FilterController
**File**: `src/main/java/com/centoria/jobmaroc/web/filter/FilterController.java`

- **Status**: All SparkJava filters commented out
- **Purpose**: Previously handled breadcrumb generation
- **Status**: ❌ Not migrated to Servlet Filter

### 2.4 New Servlet Implementations

All servlets extend `BaseServlet` and implement `doGet()` and/or `doPost()`:

1. **IndexServlet** - Homepage
2. **RegionServlet** - Region routing (delegates to other servlets)
3. **CategoryServlet** - Category listing
4. **CategoryByDomainServlet** - Domain-specific categories
5. **CatgoryByDomainByCityServlet** - City + domain categories
6. **RegionByCityServlet** - Cities by region
7. **RegionByCityByDomainServlet** - Domains by region and city
8. **ContactServlet** - Contact form (GET/POST with reCAPTCHA)
9. **AboutServlet** - About page
10. **MentionsServlet** - Legal mentions
11. **DetailOfferServlet** - Job offer details
12. **AddJobOfferServlet** - Add/update job offers (multipart)
13. **MyJobAdsServlet** - User's job ads management
14. **RobotsServlet** - Dynamic robots.txt
15. **StaticFileServlet** - Static file serving

---

## 3. Configuration Files

### 3.1 pom.xml
**Status**: ✅ No SparkJava dependencies found
- **Jetty**: 11.0.15 (jetty-server, jetty-servlet)
- **Servlet API**: Jakarta Servlet API 6.1.0
- **FreeMarker**: 2.3.31 and 2.3.33 (duplicate - should be cleaned)
- **Packaging**: JAR with maven-shade-plugin

### 3.2 web.xml
**File**: `src/main/webapp/WEB-INF/web.xml`
**Status**: ⚠️ **NEEDS CLEANUP**

**Current Issues**:
- Still contains `SparkFilter` configuration (lines 5-26)
- References `spark.servlet.SparkFilter` class
- Has `IndexServlet` mapping that conflicts with Main.java registration

**Action Required**: Remove SparkFilter configuration or update for Servlet-only deployment

### 3.3 Application Context
**File**: `src/main/java/com/centoria/jobmaroc/common/context/ApplicationContext.java`

- Singleton pattern for configuration management
- Loads properties from `config/config.{env}.properties`
- Environment variable: `EM_ENV` (local, test, prod)

---

## 4. URL Routing Patterns

### Old SparkJava Pattern
```java
Spark.get("/path/:param", (request, response) -> {
    // handler
}, TEMPLATEENGINE);
```

### New Servlet Pattern
```java
// In Main.java
servletContext.addServlet(
    new ServletHolder(new MyServlet()),
    "/path/*"
);

// In servlet
String pathInfo = req.getPathInfo();
String[] parts = pathInfo.split("/");
```

### URL Constants
**File**: `src/main/java/com/centoria/jobmaroc/common/globalData/UrlConst.java`

```java
CATEGORY = "categorie"
VILLE = "ville"
CONTACT = "contact"
REGION = "region"
```

---

## 5. Template Rendering

### Old Approach (SparkJava)
```java
ModelAndView mv = getBasePage("front/index.ftl", map, request);
return TEMPLATEENGINE.render(mv);
```

### New Approach (Servlet)
```java
// In BaseServlet
dispatch(req, resp, "front/index.ftl", model);
```

**Key Differences**:
- Old: Returns `ModelAndView`, rendered by SparkJava
- New: Direct FreeMarker template processing in `BaseServlet.dispatch()`
- Both use FreeMarker templates from `src/main/resources/`

---

## 6. Request/Response Handling

### Old (SparkJava)
- `Request` and `Response` objects from SparkJava
- `request.params("id")` for path parameters
- `request.queryParams("key")` for query parameters
- `request.raw()` to access `HttpServletRequest`

### New (Servlet)
- Direct `HttpServletRequest` and `HttpServletResponse`
- Path parameters extracted from `req.getPathInfo()`
- Query parameters via `req.getParameter("key")`
- Multipart handled via `MultipartConfigElement`

---

## 7. Migration Status Summary

### ✅ Completed
- Main application bootstrap (Main.java)
- Core frontend routes (index, categories, regions, contact, about)
- Job offer detail pages
- Static file serving
- FreeMarker integration
- Base servlet infrastructure

### ⚠️ Partially Complete
- Middle Office routes (some servlets exist, may need verification)
- Error handling (404/500 pages configured but may need servlet implementation)

### ❌ Not Migrated
- Back Office (BoController) routes
- FilterController (breadcrumb generation)
- Some commented routes in FrontController may need servlets
- web.xml cleanup (SparkFilter removal)

---

## 8. Dependencies Analysis

### Current Dependencies
- ✅ **Jetty**: 11.0.15 (embedded server)
- ✅ **Jakarta Servlet API**: 6.1.0
- ✅ **FreeMarker**: 2.3.31/2.3.33 (duplicate)
- ✅ **MongoDB**: 5.1.1
- ✅ **Lombok**: 1.18.22
- ✅ **Gson**: 2.7
- ❌ **SparkJava**: Not found in pom.xml (good!)

### Potential Issues
1. **Duplicate FreeMarker versions** (2.3.31 and 2.3.33)
2. **web.xml** still references SparkFilter (may cause issues if deployed as WAR)

---

## 9. File Structure

```
src/main/java/com/centoria/jobmaroc/
├── app/              # Application layer (business logic)
├── common/           # Common utilities, context, IHM
├── dao/              # Data access objects
├── dto/              # Data transfer objects
├── model/            # Domain models
├── service/          # Service layer
├── test/             # Tests
└── web/              # Web layer
    ├── base/         # BaseController (old, deprecated)
    ├── filter/       # FilterController (old, commented)
    ├── imageUpload/  # Image upload utilities
    ├── servlet/      # NEW: Servlet implementations ✅
    ├── BoController.java      # Old (commented)
    ├── FrontController.java    # Old (commented)
    ├── Main.java              # ✅ Migrated
    └── MoController.java      # Old (commented)
```

---

## 10. Next Steps for Complete Migration

1. **Remove SparkJava remnants**:
   - Clean up `web.xml` (remove SparkFilter)
   - Remove commented SparkJava code or move to archive
   - Check DTOs for `spark.Request` imports

2. **Complete missing routes**:
   - Migrate BoController routes to servlets
   - Verify MoController routes are fully covered
   - Implement missing routes from FrontController

3. **Implement Servlet Filters**:
   - Convert FilterController to `@WebFilter` or register in Main.java
   - Breadcrumb generation

4. **Error Handling**:
   - Implement 404/500 servlets or error pages
   - Configure error handlers in Main.java

5. **Testing**:
   - Verify all routes work correctly
   - Test multipart file uploads
   - Test error scenarios

6. **Cleanup**:
   - Remove duplicate FreeMarker dependency
   - Remove old controller classes or archive them
   - Update documentation

---

## 11. Key Patterns to Follow

### Creating New Servlets
1. Extend `BaseServlet`
2. Override `doGet()` and/or `doPost()`
3. Use `dispatch(req, resp, "template.ftl", model)` for rendering
4. Register in `Main.java` with appropriate URL pattern
5. Handle path parameters via `req.getPathInfo()`

### Error Handling
```java
showError(HttpServletResponse.SC_NOT_FOUND, "Page not found", req, resp);
```

### Multipart File Upload
```java
MultipartConfigElement multipartConfig = new MultipartConfigElement(
    tmpDir, maxFileSize, maxRequestSize, fileSizeThreshold
);
ServletHolder holder = new ServletHolder(new MyServlet());
holder.getRegistration().setMultipartConfig(multipartConfig);
```

---

## 12. Notes

- The application uses **embedded Jetty**, so it runs as a standalone JAR
- FreeMarker templates are in `src/main/resources/`
- Static files are served via `StaticFileServlet`
- Configuration is environment-based (`EM_ENV` variable)
- MongoDB is used for persistence
- Internationalization (i18n) supports French and Arabic

---

**Last Updated**: Based on current codebase analysis
**Migration Status**: ~70% Complete
