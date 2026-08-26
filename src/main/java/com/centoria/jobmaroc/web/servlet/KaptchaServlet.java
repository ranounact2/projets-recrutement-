package com.centoria.jobmaroc.web.servlet;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Properties;

@Slf4j
public class KaptchaServlet extends HttpServlet {

    public static final String SESSION_KEY = "KAPTCHA_SESSION_KEY";

    private final DefaultKaptcha kaptcha;

    public KaptchaServlet() {
        Properties props = new Properties();
        props.setProperty("kaptcha.border", "no");
        props.setProperty("kaptcha.image.width", "150");
        props.setProperty("kaptcha.image.height", "50");
        props.setProperty("kaptcha.textproducer.font.size", "36");
        props.setProperty("kaptcha.textproducer.char.length", "5");
        props.setProperty("kaptcha.textproducer.char.string", "ABCDEFGHJKLMNPQRSTUVWXYZ23456789");
        props.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");
        props.setProperty("kaptcha.background.impl", "com.google.code.kaptcha.impl.DefaultBackground");
        props.setProperty("kaptcha.background.clear.from", "white");
        props.setProperty("kaptcha.background.clear.to", "white");

        Config config = new Config(props);
        kaptcha = new DefaultKaptcha();
        kaptcha.setConfig(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        resp.setHeader("Pragma", "no-cache");
        resp.setDateHeader("Expires", 0);
        resp.setContentType("image/jpeg");

        String text = kaptcha.createText();
        HttpSession session = req.getSession();
        session.setAttribute(SESSION_KEY, text);

        BufferedImage image = kaptcha.createImage(text);
        ImageIO.write(image, "jpg", resp.getOutputStream());
    }
}
