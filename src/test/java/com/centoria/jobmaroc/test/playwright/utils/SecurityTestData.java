package com.centoria.jobmaroc.test.playwright.utils;

import java.util.Arrays;
import java.util.List;

/**
 * Enterprise Security & Boundary Test Vector Repository.
 * Contains strings for Security Injection, Unicode, Internationalization, Edge Cases.
 */
public class SecurityTestData {

    // XSS Payloads
    public static final String XSS_SCRIPT_TAG = "<script>alert('XSS')</script>";
    public static final String XSS_SVG_PAYLOAD = "<svg onload=alert(1)>";
    public static final String XSS_IMG_ERROR = "<img src=x onerror=alert('XSS')>";
    public static final String XSS_BODY_ONLOAD = "<body onload=alert('XSS')>";

    public static final List<String> XSS_PAYLOADS = Arrays.asList(
            XSS_SCRIPT_TAG,
            XSS_SVG_PAYLOAD,
            XSS_IMG_ERROR,
            XSS_BODY_ONLOAD
    );

    // SQL Injection Payloads
    public static final String SQLI_OR_TRUE = "' OR '1'='1";
    public static final String SQLI_DROP_TABLE = "'; DROP TABLE jobs; --";
    public static final String SQLI_UNION_SELECT = "' UNION SELECT NULL, NULL, NULL --";

    public static final List<String> SQLI_PAYLOADS = Arrays.asList(
            SQLI_OR_TRUE,
            SQLI_DROP_TABLE,
            SQLI_UNION_SELECT
    );

    // Internationalization & Accents & Unicode
    public static final String FRENCH_ACCENTS = "Ingénieur Développeur d'Applications";
    public static final String ARABIC_QUERY = "مطور برامج بالرباط";
    public static final String UNICODE_SPECIAL_CHARS = "C++ / C# & Node.js (Full-Stack)";
    public static final String EMOJI_QUERY = "💼 Développeur Java 🚀";

    // Edge Cases & Boundaries
    public static final String WHITESPACE_ONLY = "     ";
    public static final String EXTREMELY_LONG_INPUT = "A".repeat(1024);
    public static final String NUMERIC_QUERY = "123456789";
    public static final String NON_EXISTENT_KEYWORD = "ZzXyWq987UnobtainableSearchKeyword";
}
