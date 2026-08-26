package com.centoria.jobmaroc.service.impl;

import com.centoria.jobmaroc.common.context.ApplicationContext;
import com.centoria.jobmaroc.model.exception.TechnicalException;
import com.centoria.jobmaroc.service.IMailService;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.resource.Emailv31;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.activation.DataHandler;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

@Slf4j
public class MailJetApi implements IMailService {
    private static IMailService instance = null;

    private MailJetApi() {
    }

    public static IMailService getInstance() {
        if (instance == null) {
            instance = new MailJetApi();
        }
        return instance;
    }

    @Override
    public void sendMail(String toMail, String mailCc, String object, String text, byte[] dataBytes, String cvName) {

        // Recipient's email ID needs to be mentioned.
        String to = toMail;
        String cc = mailCc;

        try {

            // send cc : mettre en copie cette adresse

            //the attachements files
            Multipart multipart = new MimeMultipart();
            if (dataBytes != null) {
                MimeBodyPart messagett = new MimeBodyPart();
                ByteArrayDataSource bds = new ByteArrayDataSource(dataBytes, "application/pdf");// dataType example : "application/pdf"
                messagett.setDataHandler(new DataHandler(bds));
                messagett.setFileName(cvName); // here is the name of the Attachment
                multipart.addBodyPart(messagett); /* append attachment to message if exist */
            }

            // Get parameters from config
            String from = ApplicationContext.getInstance().getProps().getValue("mail.from");
            String fromName = ApplicationContext.getInstance().getProps().getValue("mail.from.name");
            String toName = ApplicationContext.getInstance().getProps().getValue("mail.to.name");
            // Create a Mailjet request
            MailjetRequest request = new MailjetRequest(Emailv31.resource)
                    .property(Emailv31.MESSAGES, new JSONArray()
                            .put(new JSONObject()
                                    .put(Emailv31.Message.SENDER, new JSONObject().put("email", from).put("name", fromName))
                                    .put(Emailv31.Message.TO, new JSONArray()
                                            .put(new JSONObject().put("email", to).put("name", toName)))
                                    .put(Emailv31.Message.CC, new JSONArray()
                                            .put(new JSONObject().put("email", cc)))
                                    .put(Emailv31.Message.SUBJECT, object)
                                    .put(Emailv31.Message.TEXTPART, text)
                                    .put(Emailv31.Message.ATTACHMENTS, multipart)
                            )
                    );


            String apiKey = ApplicationContext.getInstance().getProps().getValue("mailjet.apiKey");
            String secretKey = ApplicationContext.getInstance().getProps().getValue("mailjet.secretKey");

            // Create Mailjet Client
            MailjetClient client = new MailjetClient(apiKey,secretKey);

            // Send the email
            MailjetResponse response = client.post(request);
            // System.out.println(response.getData());

        } catch (MessagingException mex) {
            log.error("Failed to send email due to messaging error", mex);
            throw new TechnicalException("404","Failed to send email due to messaging error.");
        } catch (MailjetException e) {
            log.error("Failed to send email due to Mailjet error", e);
            throw new TechnicalException("404","Failed to send email due to Mailjet error.");
        }
    }

}







