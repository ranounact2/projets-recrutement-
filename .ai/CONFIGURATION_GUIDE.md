# Environment-Aware Configuration Guide

## Overview

The application now supports environment-aware configuration loading using the `EM_ENV` environment variable. This allows you to use different configuration files for different environments (local, dev, staging, prod, test) without code changes.

## How It Works

### Environment Variable

The application reads the `EM_ENV` environment variable to determine which configuration file to load:

- **Environment Variable**: `EM_ENV`
- **Valid Values**: `local`, `dev`, `staging`, `prod`, `test`
- **Default**: `local` (if `EM_ENV` is not set or invalid)

### Configuration Files

Configuration files are located in `src/main/resources/config/`:

- `config.local.properties` - Default/local development (fallback, **not committed to Git**)
- `config.dev.properties` - Development environment (**not committed to Git**, create locally if needed)
- `config.staging.properties` - Staging environment (**not committed to Git**, create locally if needed)
- `config.prod.properties` - Production environment (committed to Git)
- `config.test.properties` - Test environment (committed to Git)
- `config.sample.properties` - Template file (committed to Git, use as base for local configs)

### Usage Examples

#### Setting Environment Variable

**Linux/macOS:**
```bash
export EM_ENV=prod
java -jar emploismaroc.jar
```

**Windows (Command Prompt):**
```cmd
set EM_ENV=prod
java -jar emploismaroc.jar
```

**Windows (PowerShell):**
```powershell
$env:EM_ENV="prod"
java -jar emploismaroc.jar
```

**Docker:**
```dockerfile
ENV EM_ENV=prod
```

**Docker Compose:**
```yaml
services:
  app:
    environment:
      - EM_ENV=prod
```

**Systemd Service:**
```ini
[Service]
Environment="EM_ENV=prod"
```

### ApplicationContext API

The `ApplicationContext` class automatically loads the appropriate configuration:

```java
// Automatically loads config.{ENV}.properties based on EM_ENV
ApplicationContext context = ApplicationContext.getInstance();
Props props = context.getProps();

// Get current environment
String env = context.getCurrentEnvironment(); // Returns: "local", "dev", "staging", "prod", or "test"

// Get environment variable with default
String value = context.getEnvVariable("SOME_VAR", "defaultValue");

// Force reload configuration (useful for testing)
context.reloadConfiguration();
```

### Logging

The configuration system logs important information:

- **Info**: Environment determination and successful configuration loading
- **Warn**: Invalid environment values (falls back to default)
- **Error**: Configuration file not found or loading errors

Example log output:
```
INFO  - Environment variable 'EM_ENV' not set, using default environment: 'local'
INFO  - Configuration chargée avec succès depuis: config/config.local.properties
```

Or when `EM_ENV=prod`:
```
INFO  - Environment determined from 'EM_ENV': 'prod'
INFO  - Configuration chargée avec succès depuis: config/config.prod.properties
```

## Migration Notes

### Backward Compatibility

- If `EM_ENV` is not set, the application defaults to `config.local.properties`
- Existing code using `ApplicationContext.getInstance().getProps()` continues to work
- Tests can still explicitly load configuration files using `getProps(InputStream)`

### Configuration File Structure

All configuration files should follow the same structure. Use `config.sample.properties` as a template when creating new environment-specific files.

**Important**: Replace placeholder values (like `YOUR_EMAIL_PASSWORD`, `YOUR_DB_PASSWORD`) with actual values for each environment.

## Troubleshooting

### Configuration File Not Found

**Error**: `Impossible de trouver le fichier de configuration 'config/config.{ENV}.properties'`

**Solution**: 
1. Ensure the configuration file exists in `src/main/resources/config/`
2. Verify the `EM_ENV` value is one of: `local`, `dev`, `staging`, `prod`, `test`
3. Check that the file is included in the JAR build (check `pom.xml`)

### Invalid Environment Value

**Warning**: `Invalid environment value 'xyz' in 'EM_ENV'. Valid values are: [local, dev, staging, prod, test]. Falling back to 'local'`

**Solution**: Set `EM_ENV` to one of the valid values listed in the warning message.

### Configuration Not Loading

If configuration doesn't seem to be loading correctly:

1. Check application logs for configuration loading messages
2. Verify `EM_ENV` is set correctly: `echo $EM_ENV` (Linux/macOS) or `echo %EM_ENV%` (Windows)
3. Ensure the configuration file exists and is readable
4. Use `context.getCurrentEnvironment()` to verify which environment is active

## Best Practices

1. **Never commit sensitive data**: Use `config.sample.properties` as a template with placeholder values
2. **Local Development Files**: 
   - `config.local.properties`, `config.dev.properties`, and `config.staging.properties` are **not committed to Git**
   - Each developer should copy `config.sample.properties` to create their own local config files
   - Example: `cp config.sample.properties config.local.properties` then fill in your local values
3. **Environment-specific values**: Keep environment-specific values (like database hosts, API keys) in separate config files
4. **CI/CD Integration**: Set `EM_ENV` in your CI/CD pipeline based on the deployment target
5. **Testing**: Use `config.test.properties` for automated tests, or set `EM_ENV=test` in test environments
6. **Creating Local Configs**: 
   ```bash
   # For local development
   cp config.sample.properties config.local.properties
   # Edit config.local.properties with your local values
   
   # For dev environment (if needed)
   cp config.sample.properties config.dev.properties
   # Edit config.dev.properties with dev-specific values
   ```

## File Status

- ❌ `config.local.properties` - Local development (**ignored by Git**, create locally from `config.sample.properties`)
- ❌ `config.dev.properties` - Development environment (**ignored by Git**, create locally if needed)
- ❌ `config.staging.properties` - Staging environment (**ignored by Git**, create locally if needed)
- ✅ `config.prod.properties` - Production environment (committed to Git)
- ✅ `config.test.properties` - Test environment (committed to Git)
- ✅ `config.sample.properties` - Template (committed to Git, use as base for local configs)
