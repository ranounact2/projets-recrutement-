# AI Documentation Index

This directory contains AI-generated documentation files for the Emplois Maroc project. These documents provide context, guides, and analysis that help AI IDEs (Cursor, GitHub Copilot, etc.) understand the codebase structure, architecture, and development history.

## Documentation Files

### Architecture & Codebase Analysis

- **[CODEBASE_ANALYSIS.md](CODEBASE_ANALYSIS.md)**
  - Comprehensive analysis of the codebase structure
  - Documents the migration from SparkJava to Servlet-based architecture with embedded Jetty
  - Architecture layers, key components, and migration status

### Configuration & Setup

- **[CONFIGURATION_GUIDE.md](CONFIGURATION_GUIDE.md)**
  - Environment-aware configuration guide
  - Explains how to use `EM_ENV` environment variable
  - Configuration files for different environments (local, dev, staging, prod, test)

- **[SCSS_SETUP_GUIDE.md](SCSS_SETUP_GUIDE.md)**
  - Complete guide for SCSS/SASS setup with Maven
  - WRO4J Maven Plugin configuration with LibSass
  - Directory structure and compilation process

- **[SCSS_SETUP_SUMMARY.md](SCSS_SETUP_SUMMARY.md)**
  - Summary of SCSS setup completion
  - Build process flow and SCSS import chain
  - Usage examples for FreeMarker templates

### Error Handling

- **[ERROR_HANDLING_GUIDE.md](ERROR_HANDLING_GUIDE.md)**
  - Global exception handling system documentation
  - CustomErrorHandler architecture for embedded Jetty
  - Error page rendering with FreeMarker templates

### Migration Documentation

- **[HOMEPAGE_ANALYSIS.md](HOMEPAGE_ANALYSIS.md)**
  - Analysis of homepage structure and template migration plan
  - Backend controller and template analysis
  - Data model and migration steps

- **[HOMEPAGE_MIGRATION_GUIDE.md](HOMEPAGE_MIGRATION_GUIDE.md)**
  - Step-by-step guide for homepage migration
  - Template migration process
  - Testing and validation steps

- **[MIGRATION_CHECKLIST.md](MIGRATION_CHECKLIST.md)**
  - Tracks migration progress from SparkJava to Servlet architecture
  - Completed tasks and pending items
  - Migration status tracking

- **[MIGRATION_QUICK_REFERENCE.md](MIGRATION_QUICK_REFERENCE.md)**
  - Quick reference guide for migration tasks
  - Common patterns and conversions
  - Frequently used code snippets

### Refactoring Documentation

- **[REFACTORING_EXPLANATION.md](REFACTORING_EXPLANATION.md)**
  - Detailed explanation of refactoring work
  - Problem identification and solutions
  - Code improvements and optimizations

- **[REFACTORING_SUMMARY.md](REFACTORING_SUMMARY.md)**
  - Summary of refactoring work completed
  - Test results and build status
  - Changes and improvements made

### Testing & Development

- **[TESTING_GUIDE.md](TESTING_GUIDE.md)**
  - Guide for running tests in the project
  - Unit tests (JUnit 5), integration tests (Selenium), and BDD tests (Cucumber)
  - Test execution instructions

- **[NEXT_STEPS.md](NEXT_STEPS.md)**
  - Next steps and future work items
  - Completed tasks and pending improvements
  - Development roadmap

## Structured Documentation (in `docs/` subdirectory)

The `docs/` subdirectory contains structured documentation files that follow standard formats and can be enriched via prompts:

### Specifications & Requirements

- **[docs/SPECIFICATION.md](docs/SPECIFICATION.md)**
  - Functional specifications and roadmap
  - Requirements (functional and non-functional)
  - User stories and acceptance criteria
  - Technical specifications and dependencies
  - **Usage**: Template for documenting functional needs and roadmap. Can be enriched via prompts to add new features or update requirements.

### Use Case Documentation

- **[docs/CONCEPTION.md](docs/CONCEPTION.md)**
  - Use case oriented documentation
  - Standard UML use case structure for each use case
  - Use case diagrams (Mermaid format)
  - Collaboration diagrams showing object interactions
  - State diagrams for state transitions
  - Business rules and relationships between use cases
  - **Usage**: Comprehensive use case documentation that can be enriched via prompts or autodetection. Each use case includes diagrams and standard structure.

### Test Cases for BDD Integration Tests

- **[docs/TEST_CASES.md](docs/TEST_CASES.md)**
  - BDD-formatted test cases ready for integration testing
  - Gherkin scenario format (Given-When-Then)
  - CSV data structures for test data export
  - Test case metadata (priority, type, status, tags)
  - Expected results and test data variations
  - **Usage**: 
    - Generate prompts for creating BDD integration tests
    - Export test data to CSV format for testing utilities
    - Convert scenarios to Gherkin `.feature` files for Cucumber
    - Each test case is linked to a use case from CONCEPTION.md

## Usage

These documentation files are automatically discoverable by AI IDEs that support the `.ai/` folder convention. They provide context about:

- Project architecture and design decisions
- Migration history and patterns
- Configuration and setup procedures
- Testing strategies
- Refactoring explanations
- **Functional specifications and requirements** (SPECIFICATION.md)
- **Use case analysis and design** (CONCEPTION.md)
- **BDD test cases for integration testing** (TEST_CASES.md)

When working with AI assistants, reference these documents to provide better context about:
- The codebase structure and development history
- Functional requirements and user stories
- Use case scenarios and business rules
- Test scenarios and test data structures

## File Organization

### Root Level (`.ai/`)
All historical and analysis documentation files are located at the root level of `.ai/` for easy access:
- Codebase analysis
- Configuration guides
- Migration documentation
- Refactoring documentation
- Testing guides

### Documentation Subdirectory (`.ai/docs/`)
Structured documentation files that follow standard formats:
- **SPECIFICATION.md**: Functional specifications and roadmap (can be enriched via prompts)
- **CONCEPTION.md**: Use case documentation with diagrams (can be enriched via prompts or autodetection)
- **TEST_CASES.md**: BDD test cases with CSV export format (can be enriched via prompts)

This structure makes it easy to:
- Find specific documentation
- Maintain and update documentation
- Add new documentation files as needed
- Generate test data and BDD scenarios
- Keep the project root clean

## How to Use TEST_CASES.md for CSV Generation

The `TEST_CASES.md` file contains structured test data that can be exported to CSV format:

1. **Extract CSV Structures**: Each test case includes a CSV structure section showing column headers and example rows
2. **Export Test Data**: Use the CSV export format sections to generate CSV files for testing utilities
3. **Generate BDD Scenarios**: Copy the Gherkin scenarios directly into `.feature` files for Cucumber
4. **Create Test Prompts**: Use test case descriptions to generate prompts for integration test creation

Example CSV export from TEST_CASES.md:
```csv
test_case_id,scenario_name,title,email,domain,expected_result,test_type
TC-001,Create Job Offer,Développeur Java,recruiter@company.com,Informatique,Success,positive
TC-002,Validate Required Fields,Test Job,,,Error,negative
```

## Enriching Documentation via Prompts

All three structured documentation files (`SPECIFICATION.md`, `CONCEPTION.md`, `TEST_CASES.md`) are designed to be enriched via prompts:

- **SPECIFICATION.md**: Add new features, update roadmap, add user stories
- **CONCEPTION.md**: Add new use cases, detail scenarios, add diagrams, update business rules
- **TEST_CASES.md**: Add new test cases, expand test data variations, generate CSV exports
