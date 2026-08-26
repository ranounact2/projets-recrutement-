package com.centoria.jobmaroc.common.utils;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Singleton utility class for internationalization (i18n) message retrieval.
 * Provides centralized access to localized messages using ResourceBundle.
 */
public class MessageBundle {
    
    private static MessageBundle instance;
    private static final String BUNDLE_NAME = "i18n/messages";
    private static final Locale DEFAULT_LOCALE = Locale.FRENCH;
    
    private MessageBundle() {
        // Private constructor for singleton
    }
    
    /**
     * Gets the singleton instance of MessageBundle.
     * @return The MessageBundle instance
     */
    public static synchronized MessageBundle getInstance() {
        if (instance == null) {
            instance = new MessageBundle();
        }
        return instance;
    }
    
    /**
     * Gets a localized message using the default locale (French).
     * @param key The message key
     * @return The localized string, or "???key???" if not found
     */
    public String getMessage(String key) {
        return getMessage(key, DEFAULT_LOCALE);
    }
    
    /**
     * Gets a localized message using a specific locale.
     * @param key The message key
     * @param locale The locale to use
     * @return The localized string, or "???key???" if not found
     */
    public String getMessage(String key, Locale locale) {
        try {
            Locale localeToUse = locale != null ? locale : DEFAULT_LOCALE;
            ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME, localeToUse);
            return bundle.getString(key);
        } catch (Exception e) {
            // Return the key with markers if message not found (useful for debugging)
            return "???" + key + "???";
        }
    }
    
    /**
     * Gets a ResourceBundle for a specific locale.
     * Useful for FreeMarker templates that need direct bundle access.
     * @param locale The locale to use
     * @return The ResourceBundle for the locale
     */
    public ResourceBundle getBundle(Locale locale) {
        Locale localeToUse = locale != null ? locale : DEFAULT_LOCALE;
        return ResourceBundle.getBundle(BUNDLE_NAME, localeToUse);
    }
    
    /**
     * Gets the default locale.
     * @return The default locale
     */
    public Locale getDefaultLocale() {
        return DEFAULT_LOCALE;
    }
}
