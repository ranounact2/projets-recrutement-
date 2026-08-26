# SCSS/SASS Setup Guide with Maven

## Overview

This project uses **WRO4J Maven Plugin** with **LibSass** for SCSS compilation. No Node.js or npm required!

## Directory Structure

```
src/main/
├── scss/
│   ├── base/
│   │   ├── _variables.scss    # SCSS variables (colors, fonts, spacing)
│   │   ├── _mixins.scss       # Reusable mixins
│   │   ├── _reset.scss        # CSS reset
│   │   └── _typography.scss   # Font definitions
│   ├── components/
│   │   ├── _header.scss       # Header component
│   │   ├── _footer.scss       # Footer component
│   │   ├── _buttons.scss      # Button styles
│   │   ├── _jobs.scss         # Job cards component
│   │   ├── _cities.scss       # Cities grid component
│   │   ├── _regions.scss      # Regions grid component
│   │   └── _domains.scss      # Domains grid component
│   ├── pages/
│   │   ├── common.scss        # Common styles (imports base + components)
│   │   ├── index.scss         # Index page styles
│   │   ├── region.scss        # Region page styles
│   │   ├── category.scss      # Category page styles
│   │   └── ...
│   └── common.scss            # Entry point for common.css
├── js/
│   ├── components/
│   │   ├── header.js          # Header functionality
│   │   └── forms.js           # Form handling
│   ├── utils/
│   │   └── helpers.js         # Utility functions
│   └── pages/
│       ├── common.js          # Common JavaScript
│       ├── index.js           # Index page JavaScript
│       └── ...
└── wro/
    └── wro.xml                # WRO4J configuration
```

## Build Process

### Compilation Flow

1. **SCSS Compilation** (during `generate-resources` phase):
   - WRO4J reads `wro.xml` configuration
   - Compiles SCSS files using LibSass (no Node.js)
   - Outputs CSS to `target/classes/public/assets/css/`

2. **JavaScript Copying** (during `process-resources` phase):
   - Maven Resources Plugin copies JS files
   - Outputs to `target/classes/public/assets/js/`

3. **Minification** (during `process-resources` phase):
   - Existing `minify-maven-plugin` minifies CSS/JS
   - Creates `.min.css` and `.min.js` files

### Output Structure

After `mvn clean install`:

```
target/classes/public/assets/
├── css/
│   ├── common.css
│   ├── index.css
│   ├── region.css
│   ├── category.css
│   └── ...
└── js/
    ├── common.js
    ├── index.js
    ├── region.js
    └── ...
```

## Usage in FreeMarker Templates

### Example: index.ftl

```html
<!DOCTYPE html>
<html>
<head>
    <!-- Common styles (shared across all pages) -->
    <link rel="stylesheet" href="/assets/css/common.css">
    <!-- Page-specific styles -->
    <link rel="stylesheet" href="/assets/css/index.css">
</head>
<body>
    <!-- content -->
    
    <!-- Common scripts (shared across all pages) -->
    <script src="/assets/js/utils/helpers.js"></script>
    <script src="/assets/js/components/header.js"></script>
    <script src="/assets/js/components/forms.js"></script>
    <script src="/assets/js/pages/common.js"></script>
    <!-- Page-specific scripts -->
    <script src="/assets/js/pages/index.js"></script>
</body>
</html>
```

## Adding a New Page

### Step 1: Create SCSS File

Create `src/main/scss/pages/newpage.scss`:

```scss
// Import common styles
@import '../pages/common';

// Import page-specific components
@import '../components/specific-component';

// Page-specific styles
.newpage {
  &__section {
    // styles
  }
}
```

### Step 2: Add to WRO Configuration

Edit `src/main/wro/wro.xml`:

```xml
<group name="newpage">
  <css>classpath:scss/pages/newpage.scss</css>
</group>
```

### Step 3: Create JavaScript File (if needed)

Create `src/main/js/pages/newpage.js`:

```javascript
document.addEventListener('DOMContentLoaded', function() {
    // Page-specific initialization
});
```

### Step 4: Update FreeMarker Template

In your template:

```html
<link rel="stylesheet" href="/assets/css/common.css">
<link rel="stylesheet" href="/assets/css/newpage.css">
<script src="/assets/js/pages/common.js"></script>
<script src="/assets/js/pages/newpage.js"></script>
```

## SCSS Best Practices

### 1. Use Variables

Always use variables from `_variables.scss`:

```scss
.button {
  background: $primary-color;  // ✅ Good
  background: #10195D;         // ❌ Bad
}
```

### 2. Use Mixins

Reuse common patterns:

```scss
.card {
  @include card;  // Includes card styles + hover effects
}
```

### 3. Component Organization

Keep components focused and reusable:

```scss
// _header.scss - Only header-related styles
// _footer.scss - Only footer-related styles
```

### 4. Page-Specific Styles

Page files should import components, not redefine them:

```scss
// index.scss
@import '../pages/common';
@import '../components/jobs';  // ✅ Import component

// Don't redefine job styles here ❌
```

## Building

### Development Build

```bash
mvn clean compile
```

### Production Build

```bash
mvn clean install
```

This will:
1. Compile SCSS to CSS
2. Copy JavaScript files
3. Minify CSS and JS
4. Package everything in the JAR

## Troubleshooting

### SCSS Not Compiling

1. Check `wro.xml` configuration
2. Verify SCSS file paths in `wro.xml`
3. Check Maven build logs for errors
4. Ensure LibSass dependency is included

### Styles Not Loading

1. Verify CSS files are in `target/classes/public/assets/css/`
2. Check FreeMarker template paths
3. Ensure Jetty is serving static files correctly
4. Hard refresh browser (Ctrl+F5)

### Import Errors

1. Check import paths (use relative paths from importing file)
2. Ensure partial files start with `_`
3. Verify file extensions (`.scss` not `.sass`)

## Maven Commands

```bash
# Clean and compile
mvn clean compile

# Full build with packaging
mvn clean install

# Skip tests
mvn clean install -DskipTests

# Debug WRO4J
mvn clean install -X
```

## Next Steps

1. ✅ Maven plugin configured
2. ✅ Directory structure created
3. ✅ Base SCSS files created
4. ✅ Example index.scss created
5. ⏳ Migrate remaining CSS files to SCSS
6. ⏳ Create remaining page SCSS files
7. ⏳ Update FreeMarker templates
8. ⏳ Test build process
