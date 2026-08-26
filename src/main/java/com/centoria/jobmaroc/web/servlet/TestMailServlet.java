package com.centoria.jobmaroc.web.servlet;

import com.centoria.jobmaroc.common.context.ApplicationContext;
import com.centoria.jobmaroc.service.IMailService;
import com.centoria.jobmaroc.service.MailServiceFactory;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class TestMailServlet extends HttpServlet {

    private final IMailService mailService = MailServiceFactory.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String to = ApplicationContext.getInstance().getProps().getValue("mail.test.to");
        if (to == null || to.trim().isEmpty()) {
            resp.setContentType("text/html; charset=UTF-8");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("""
                    <!DOCTYPE html>
                    <html><head><meta charset="UTF-8"><title>Test Mail - Erreur</title></head>
                    <body style="font-family: Arial, sans-serif; max-width: 500px; margin: 50px auto; text-align: center;">
                      <div style="background: #fff3e0; border: 1px solid #ff9800; border-radius: 8px; padding: 30px;">
                        <h2 style="color: #e65100; margin-top: 0;">Configuration manquante</h2>
                        <p><code>mail.test.to</code> n'est pas defini dans le fichier de configuration.</p>
                      </div>
                    </body></html>
                    """);
            return;
        }
        String subject = "Test MailerSend - Emplois Maroc";
        String provider = MailServiceFactory.getProvider();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        String html = """
                <!DOCTYPE html>
                <html>
                <head><meta charset="UTF-8"></head>
                <body style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px;">
                  <div style="background: linear-gradient(135deg, #1a73e8, #0d47a1); padding: 30px; border-radius: 10px 10px 0 0; text-align: center;">
                    <h1 style="color: #fff; margin: 0;">Emplois Maroc</h1>
                    <p style="color: #bbdefb; margin: 5px 0 0;">Test d'envoi d'email</p>
                  </div>
                  <div style="background: #ffffff; padding: 30px; border: 1px solid #e0e0e0; border-top: none; border-radius: 0 0 10px 10px;">
                    <h2 style="color: #1a73e8;">Hello!</h2>
                    <p style="color: #333; line-height: 1.6;">
                      Ceci est un email de test envoye via <strong>%s</strong> depuis l'application Emplois Maroc.
                    </p>
                    <p style="color: #333; line-height: 1.6;">
                      Si vous recevez ce message, la configuration fonctionne correctement.
                    </p>
                    <div style="background: #e8f5e9; border-left: 4px solid #4caf50; padding: 15px; margin: 20px 0; border-radius: 4px;">
                      <strong style="color: #2e7d32;">Provider:</strong> %s<br>
                      <strong style="color: #2e7d32;">Date:</strong> %s
                    </div>
                    <hr style="border: none; border-top: 1px solid #e0e0e0; margin: 20px 0;">
                    <p style="color: #999; font-size: 12px; text-align: center;">
                      Emplois Maroc &mdash; Test automatique
                    </p>
                  </div>
                </body>
                </html>
                """.formatted(provider, provider, timestamp);

        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            mailService.sendMail(to, null, subject, html, null, null);
            log.info("Test email sent to {} via provider '{}'", to, provider);

            out.println("""
                    <!DOCTYPE html>
                    <html><head><meta charset="UTF-8"><title>Test Mail - OK</title></head>
                    <body style="font-family: Arial, sans-serif; max-width: 500px; margin: 50px auto; text-align: center;">
                      <div style="background: #e8f5e9; border: 1px solid #4caf50; border-radius: 8px; padding: 30px;">
                        <h2 style="color: #2e7d32; margin-top: 0;">Email accepte par %s</h2>
                        <p><strong>To:</strong> %s</p>
                        <p><strong>Provider:</strong> %s</p>
                        <p><strong>Date:</strong> %s</p>
                      </div>
                    </body></html>
                    """.formatted(provider, to, provider, timestamp));

        } catch (Exception e) {
            log.error("Test email failed", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            out.println("""
                    <!DOCTYPE html>
                    <html><head><meta charset="UTF-8"><title>Test Mail - Erreur</title></head>
                    <body style="font-family: Arial, sans-serif; max-width: 600px; margin: 50px auto; text-align: center;">
                      <div style="background: #ffebee; border: 1px solid #f44336; border-radius: 8px; padding: 30px;">
                        <h2 style="color: #c62828; margin-top: 0;">Erreur d'envoi</h2>
                        <p><strong>Provider:</strong> %s</p>
                        <p><strong>To:</strong> %s</p>
                        <div style="background: #fff; border: 1px solid #e0e0e0; border-radius: 4px; padding: 15px; margin: 15px 0; text-align: left;">
                          <strong>Erreur:</strong><br>
                          <code style="color: #c62828; word-break: break-all;">%s</code>
                        </div>
                      </div>
                    </body></html>
                    """.formatted(provider, to, escapeHtml(e.getMessage())));
        }
    }

    private static String escapeHtml(String s) {
        if (s == null) return "null";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
