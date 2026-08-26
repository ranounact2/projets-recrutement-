package com.centoria.jobmaroc.common.context;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import jodd.props.Props;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * AppParameters store context and app parameters from many ways
 * 
 * From file 
 * From data
 * 
 * Manage uniq contexte or map context by domain
 * 
 * Environment-aware configuration loading:
 * - Reads EM_ENV environment variable
 * - Loads config.{ENV}.properties based on environment
 * - Falls back to "local" if EM_ENV is not set
 * - Validates environment values
 * 
 * @author pc
 *
 */
@Slf4j
public class ApplicationContext {

	private static final String ENV_VARIABLE_NAME = "EM_ENV";
	private static final String DEFAULT_ENVIRONMENT = "local";
	private static final Set<String> VALID_ENVIRONMENTS = new HashSet<>(
			Arrays.asList("local", "dev", "staging", "prod", "test")
	);
	
	private Props props = null;
	private String currentEnvironment = null;
	private final ThreadLocal<String> contextDomain = new ThreadLocal<>();
	@Getter
    private static final ApplicationContext instance = new ApplicationContext();

	private ApplicationContext() {
	}

	/**
	 * Gets the current environment name (local, dev, staging, prod, test)
	 * @return the current environment name
	 */
	public String getCurrentEnvironment() {
		if (currentEnvironment == null) {
			determineEnvironment();
		}

		return currentEnvironment;
	}

	/**
	 * Determines the environment from EM_ENV environment variable or uses default
	 */
	private void determineEnvironment() {
		String env = System.getenv(ENV_VARIABLE_NAME);
		
		if (env == null || env.trim().isEmpty()) {
			currentEnvironment = DEFAULT_ENVIRONMENT;
			log.info("Environment variable '{}' not set, using default environment: '{}'", 
					ENV_VARIABLE_NAME, currentEnvironment);
		} else {
			env = env.trim().toLowerCase();
			if (VALID_ENVIRONMENTS.contains(env)) {
				currentEnvironment = env;
				log.info("Environment determined from '{}': '{}'", ENV_VARIABLE_NAME, currentEnvironment);
			} else {
				log.warn("Invalid environment value '{}' in '{}'. Valid values are: {}. Falling back to '{}'", 
						env, ENV_VARIABLE_NAME, VALID_ENVIRONMENTS, DEFAULT_ENVIRONMENT);
				currentEnvironment = DEFAULT_ENVIRONMENT;
			}
		}
	}

	/**
	 * Gets the configuration file name based on the current environment
	 * @return the configuration file name (e.g., "config.local.properties")
	 */
	private String getConfigFileName() {
		if (currentEnvironment == null) {
			determineEnvironment();
		}
		return "config/config." + currentEnvironment + ".properties";
	}

    /** 
     * Charge la configuration depuis le fichier approprié basé sur l'environnement
     * Lecture depuis classpath à la première demande
     */
	public synchronized Props getProps() {
		if (props == null) {
			loadConfiguration();
		}
		return props;
	}

	/**
	 * Loads the configuration file based on the current environment
	 */
	private void loadConfiguration() {
		determineEnvironment();
		String configFileName = getConfigFileName();
		
		props = new Props();
		// Chemin relatif dans src/main/resources
		try (InputStream in = getClass()
				.getClassLoader()
				.getResourceAsStream(configFileName)) {
			if (in == null) {
				String errorMsg = String.format(
						"Impossible de trouver le fichier de configuration '%s' dans le classpath. " +
						"Vérifiez que le fichier existe dans src/main/resources/config/",
						configFileName
				);
				log.error(errorMsg);
				throw new RuntimeException(errorMsg);
			}
			props.load(in);
			log.info("Configuration chargée avec succès depuis: {}", configFileName);
		} catch (IOException e) {
			String errorMsg = String.format(
					"Erreur de chargement de la configuration depuis '%s'", 
					configFileName
			);
			log.error(errorMsg, e);
			throw new RuntimeException(errorMsg, e);
		}
	}

	/** 
	 * Si vous avez besoin de charger un autre flux explicitement
	 * Note: Cette méthode remplace la configuration actuelle si elle existe déjà
	 */
	public synchronized Props getProps(InputStream inputStream) throws IOException {
		props = new Props();
		props.load(inputStream);
		log.info("Configuration chargée depuis un InputStream personnalisé");
		return props;
	}

	/**
	 * Forces reload of configuration (useful for testing or environment changes)
	 */
	public synchronized void reloadConfiguration() {
		log.info("Rechargement de la configuration demandé");
		props = null;
		currentEnvironment = null;
		loadConfiguration();
	}

	public void setDomain(String domain) {
		contextDomain.set(domain);
	}

	public String getDomain() {
		return contextDomain.get();
	}

	/**
	 * Get environment variable with optional default value
	 * @param key the environment variable key
	 * @param defaultValue default value if not found
	 * @return the environment variable value or default
	 */
	public String getEnvVariable(String key, String defaultValue) {
		String value = System.getenv(key);
		if (value != null) {
			return value;
		}
		return defaultValue;
	}

}
