package com.centoria.jobmaroc.service.impl;

import com.centoria.jobmaroc.common.context.ApplicationContext;
import com.centoria.jobmaroc.model.exception.TechnicalException;
import com.centoria.jobmaroc.service.IMailService;
import com.mailersend.sdk.MailerSend;
import com.mailersend.sdk.MailerSendResponse;
import com.mailersend.sdk.emails.Attachment;
import com.mailersend.sdk.emails.Email;
import com.mailersend.sdk.exceptions.MailerSendException;
import lombok.extern.slf4j.Slf4j;

import java.util.Base64;

@Slf4j
public class MailerSendService implements IMailService {

    private static IMailService instance = null;

    private MailerSendService() {
    }

    public static IMailService getInstance() {
        if (instance == null) {
            instance = new MailerSendService();
        }
        return instance;
    }

    @Override
    public void sendMail(String toMail, String mailCc, String object, String text, byte[] dataBytes, String dataType) {
        String token = ApplicationContext.getInstance().getProps().getValue("mailersend.token");
        String fromEmail = ApplicationContext.getInstance().getProps().getValue("mail.from");
        String fromName = ApplicationContext.getInstance().getProps().getValue("mail.from.name");

        if (token == null || token.trim().isEmpty()) {
            throw new TechnicalException("Configuration manquante: mailersend.token n'est pas defini", "");
        }
        if (fromEmail == null || fromEmail.trim().isEmpty()) {
            throw new TechnicalException("Configuration manquante: mail.from n'est pas defini", "");
        }

        log.info("Sending email via MailerSend to={}, subject={}", toMail, object);

        try {
            MailerSend ms = new MailerSend();
            ms.setToken(token);

            Email email = new Email();
            email.setFrom(fromName != null ? fromName : "emploismaroc", fromEmail);
            email.addRecipient("", toMail);

            if (mailCc != null && !mailCc.trim().isEmpty()) {
                email.AddCc("", mailCc);
            }

            email.setSubject(object);
            email.setHtml(text);

            if (dataBytes != null && dataBytes.length > 0) {
                String base64Content = Base64.getEncoder().encodeToString(dataBytes);
                String fileName = (dataType != null && !dataType.trim().isEmpty())
                        ? dataType : "attachment.pdf";
                Attachment attachment = new Attachment();
                attachment.setAttachment(base64Content, fileName);
                email.attachments.add(attachment);
            }

            MailerSendResponse response = ms.emails().send(email);
            log.info("MailerSend email accepted: to={}, messageId={}, status={}",
                    toMail, response.messageId, response.responseStatusCode);

        } catch (MailerSendException e) {
            log.error("MailerSend API error: to={}, code={}, message={}", toMail, e.code, e.message, e);
            throw new TechnicalException(
                    "MailerSend error (code " + e.code + "): " + e.message,
                    e.responseBody != null ? e.responseBody : e.getMessage());
        }
    }
}
