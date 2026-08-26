# Homepage Analysis & Template Migration Plan

## Current Structure Analysis

### 1. Backend Controller
**File**: `src/main/java/com/centoria/jobmaroc/web/servlet/IndexServlet.java`
- **Template Used**: `front/temp/index.ftl`
- **Data Model**:
  - `data` (SearchResultAdDto):
    - `data.jobs` - List of job offers (AdDto)
    - `data.cities` - List of cities for dropdown
    - `data.domains` - List of domains/categories
  - `regions` - List of regions
  - `zones` - Dynamic content zones (meta tags, H1, paragraphs, etc.)

### 2. Current Template Structure
**File**: `src/main/resources/front/temp/index.ftl`

**Sections**:
1. **Hero Section** (`section.intro`):
   - H1 title (from zones or default: "Des offres d'emploi au Maroc.")
   - Paragraph (from zones or default: "Votre nouvel emploi vous attend")
   - Search form with:
     - Keyword input (`#keyword`)
     - City dropdown (`#city`) - populated from `data.cities`
     - Domain dropdown (`#domain`) - populated from `data.domains`
     - Search button

2. **Featured Jobs Carousel** (`section.offres`):
   - Title: "Les offres d'emploi du mois"
   - Displays first 6 jobs from `data.jobs`
   - Each job shows: image, title (link), domain, city, type badge, positions count

3. **Employer CTA** (`section.add-job`):
   - Title: "Vous êtes un <span>employeur</span>"
   - Call-to-action link to `/ajouter-offre-emploi`

4. **Job Listings** (`section.offres`):
   - Title: "Les offres d'emploi"
   - Description paragraph
   - Table/list of first 5 jobs from `data.jobs`

5. **Cities Section** (`section.city`):
   - Title from zones or default: "Les villes qui recrutent"
   - Description from zones
   - Grid of 8 city cards (hardcoded links):
     - Casablanca, Tanger, Fes, Meknes, Rabat, Agadir, Dakhla, Laâyoune
   - "Voir plus" link to `/region`

6. **Regions Section** (`section.regions`):
   - Title from zones or default: "Domaines populaires"
   - Grid of 12 region links (hardcoded)

7. **Domains Section** (`section.domaine`):
   - Title from zones or default: "Domaines populaires"
   - Grid of domains from `data.domains` (first 8 visible, rest hidden)
   - "Voir plus"/"Voir moins" toggle functionality

### 3. CSS Files
- `/assets/css/style.css` - Main stylesheet (base styles, fonts, header, buttons)
- `/assets/css/index.css` - Index-specific styles (hero, sections, cards, grids)

### 4. JavaScript Files
- `/assets/js/script.js` - Main JavaScript
- `/assets/js/temp/index.js` - Index-specific functionality:
  - `slide()` - Carousel navigation
  - `toggleDomains()` - Show/hide additional domains

### 5. Data Variables Used
**From `data` (SearchResultAdDto)**:
- `${data.jobs}` - Job listings
  - `${job.key}` - Job ID/slug
  - `${job.title}` - Job title
  - `${job.img}` - Job image URL
  - `${job.domain}` or `${job.type}` - Domain/type
  - `${job.city}` - City name
  - `${job.type}` - Job type (CDI, CDD, etc.)
  - `${job.positionsCount}` - Number of positions

- `${data.cities}` - Cities list
  - `${city.slug}` - City slug
  - `${city.name}` - City name
  - `${city.region.slug}` - Region slug

- `${data.domains}` - Domains list
  - `${domain.slug}` - Domain slug
  - `${domain.name}` - Domain name
  - `${domain.icon}` - Domain icon class

**From `zones` (PageZones)**:
- `${zones[].zones[].values.title}` - Page title
- `${zones[].zones[].values.meta_description}` - Meta description
- `${zones[].zones[].values.meta_keyword}` - Meta keywords
- `${zones[].zones[].values.h1}` - H1 content
- `${zones[].zones[].values.paragraph}` - Hero paragraph
- `${zones[].zones[].values.h2_block_ville}` - Cities section title
- `${zones[].zones[].values.text_block_ville}` - Cities section text
- `${zones[].zones[].values.h2_block_categorie}` - Categories section title
- `${zones[].zones[].values.text_block_categorie}` - Categories section text

**From `regions`**:
- `${regions}` - List of regions
  - `${region.slug}` - Region slug
  - `${region.name}` - Region name

**JavaScript Data**:
- `window.citiesData` - Array of city objects with slug and regionSlug
- `window.domainsData` - Array of domain objects with slug

### 6. Current Design Characteristics
- **Color Scheme**: 
  - Primary: Blue (#10195D / rgba(16, 25, 93))
  - Accent: Orange (#F9AB00)
  - Background: Light gray (#ECEDF2)
- **Typography**: Roboto font family (300, 400, 500, 700, 900)
- **Layout**: 
  - Sections with 10% horizontal padding
  - Grid-based layouts for cities, regions, domains
  - Card-based job listings
- **Responsive**: Mobile-first approach with burger menu

## Template Requirements

### Must Preserve:
1. All data bindings (${variables})
2. All functionality (search form, carousel, toggles)
3. All links and navigation
4. Header and footer includes
5. JavaScript data injection
6. Zone-based dynamic content

### Can Update:
1. Visual layout and styling
2. CSS structure (as long as classes match or are mapped)
3. HTML structure (as long as data bindings work)
4. Modern design patterns (CSS Grid, Flexbox improvements)

## Next Steps

1. ✅ Analyze current structure
2. ⏳ Find/create lightweight modern template
3. ⏳ Convert template to FreeMarker with all data bindings
4. ⏳ Create new CSS file
5. ⏳ Update JavaScript if needed
6. ⏳ Update IndexServlet.java
7. ⏳ Test all functionality
