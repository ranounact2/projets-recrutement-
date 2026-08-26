package com.centoria.jobmaroc.service.impl;

import com.centoria.jobmaroc.common.context.ApplicationContext;
import com.centoria.jobmaroc.model.exception.TechnicalException;
import com.centoria.jobmaroc.service.IMailService;
import com.centoria.jobmaroc.web.base.FreeMarkerEngine;
import lombok.extern.slf4j.Slf4j;

import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.util.Properties;

@Slf4j
public class MailService implements IMailService {

    private static IMailService instance = null;
    private Properties properties;
    FreeMarkerEngine template = new FreeMarkerEngine();

    private String from;
    private String pass;

    private MailService() {
    }

    public static IMailService getInstance() {
        if (instance == null) {
            instance = new MailService();
        }
        return instance;
    }

    @Override
    public void sendMail(String toMail, String mailCc, String object, String text, byte[] dataBytes, String cvName) {

        initMailParams();

        // Recipient's email ID needs to be mentioned.
        String to = toMail;
        String cc = mailCc;

        // Get the  Session object.
        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, pass);
                    }
                });

        try {

            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // send cc : mettre en copie cette adresse
            if (cc != null && !cc.trim().isEmpty()) {
                message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));
            }

            // Set Subject: header field
            message.setSubject(object);

            //the body of the email
            BodyPart messageBody = new MimeBodyPart();

            messageBody.setContent(text, "text/html; charset=UTF-8");


            //the attachements files
            Multipart multipart = new MimeMultipart();
            if (dataBytes != null) {
                MimeBodyPart messagett = new MimeBodyPart();
                ByteArrayDataSource bds = new ByteArrayDataSource(dataBytes, "application/pdf");// dataType example : "application/pdf"
                messagett.setDataHandler(new DataHandler(bds));
                messagett.setFileName(cvName); // here is the name of the Attachment
                multipart.addBodyPart(messagett); /* append attachment to message if exist */
            }

            multipart.addBodyPart(messageBody);

            message.setContent(multipart);

            // Send message
            Transport tr = session.getTransport("smtp");
            tr.connect(from, pass);
            message.saveChanges();

            tr.sendMessage(message, message.getAllRecipients());
            tr.close();

            log.info("Sent message successfully to {}", to);
        } catch (AuthenticationFailedException e) {
            log.error("Échec d'authentification SMTP pour l'adresse: {}. Vérifiez que vous utilisez un 'Mot de passe d'application' Gmail et non le mot de passe normal.", from, e);
            throw new TechnicalException("Échec d'authentification SMTP. Vérifiez que vous utilisez un 'Mot de passe d'application' Gmail. Consultez CONFIGURATION_GMAIL_SMTP.md", e.getMessage());
        } catch (MessagingException mex) {
            log.error("Failed to send email to {} from {}", to, from, mex);
            throw new TechnicalException("Failed to send email: " + mex.getMessage(), mex.getMessage());
        }
    }


    private void initMailParams() {
        properties = new Properties();

        from = ApplicationContext.getInstance().getProps().getValue("mail.from");
        pass = ApplicationContext.getInstance().getProps().getValue("mail.from.pass");

        // Vérifier que les paramètres essentiels sont configurés
        if (from == null || from.trim().isEmpty()) {
            throw new TechnicalException("Configuration manquante: mail.from n'est pas défini", "");
        }
        if (pass == null || pass.trim().isEmpty()) {
            throw new TechnicalException("Configuration manquante: mail.from.pass n'est pas défini", "");
        }

        String smtpAuth = ApplicationContext.getInstance().getProps().getValue("mail.smtp.auth");
        String smtpStartTls = ApplicationContext.getInstance().getProps().getValue("mail.smtp.starttls.enable");
        String smtpHost = ApplicationContext.getInstance().getProps().getValue("mail.smtp.host");
        String smtpPort = ApplicationContext.getInstance().getProps().getValue("mail.smtp.port");
        String debug = ApplicationContext.getInstance().getProps().getValue("mail.debug");

        // Setup mail server avec valeurs par défaut si non configurées
        properties.put("mail.smtp.auth", smtpAuth != null ? smtpAuth : "true");
        properties.put("mail.smtp.starttls.enable", smtpStartTls != null ? smtpStartTls : "true");
        properties.put("mail.smtp.host", smtpHost != null ? smtpHost : "smtp.gmail.com");
        properties.put("mail.smtp.port", smtpPort != null ? smtpPort : "587");
        // For seeing debugging in the console
        properties.put("mail.debug", debug != null ? debug : "false");
        
        log.debug("Mail configuration: from={}, host={}, port={}", from, 
                properties.get("mail.smtp.host"), properties.get("mail.smtp.port"));
    }

}
