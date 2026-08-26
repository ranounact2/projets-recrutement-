package com.centoria.jobmaroc.service;

import com.centoria.jobmaroc.common.context.ApplicationContext;
import com.centoria.jobmaroc.service.impl.MailJetApi;
import com.centoria.jobmaroc.service.impl.MailService;
import com.centoria.jobmaroc.service.impl.MailerSendService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MailServiceFactory {

    private static IMailService instance;
    private static String resolvedProvider;

    private MailServiceFactory() {
    }

    public static IMailService getInstance() {
        if (instance == null) {
            instance = createFromConfig();
        }
        return instance;
    }

    public static String getProvider() {
        if (resolvedProvider == null) {
            getInstance();
        }
        return resolvedProvider;
    }

    private static IMailService createFromConfig() {
        String provider = ApplicationContext.getInstance()
                .getProps().getValue("mail.provider");

        if (provider == null || provider.trim().isEmpty()) {
            provider = "smtp";
        }

        resolvedProvider = provider.trim().toLowerCase();

        log.info("Mail provider configured: '{}'", resolvedProvider);

        return switch (resolvedProvider) {
            case "mailjet" -> {
                log.info("Using MailJet API for email sending");
                yield MailJetApi.getInstance();
            }
            case "mailersend" -> {
                log.info("Using MailerSend API for email sending (async)");
                yield MailerSendService.getInstance();
            }
            default -> {
                log.info("Using SMTP for email sending");
                yield MailService.getInstance();
            }
        };
    }
}
