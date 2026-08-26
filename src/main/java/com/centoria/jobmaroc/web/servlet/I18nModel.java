package com.centoria.jobmaroc.web.servlet;

import com.centoria.jobmaroc.common.utils.MessageBundle;

import java.util.Locale;

/**
 * Exposed to FreeMarker as {@code i18n} / {@code utils}. A named class is required so
 * {@code ${i18n.get('key')}} is resolved by the BeansWrapper (anonymous subclasses of
 * {@code Object} are not handled the same way).
 */
public final class I18nModel {

    private final MessageBundle messageBundle;
    private final Locale locale;

    public I18nModel(MessageBundle messageBundle, Locale locale) {
        this.messageBundle = messageBundle;
        this.locale = locale;
    }

    public String get(String key) {
        return messageBundle.getMessage(key, locale);
    }
}
