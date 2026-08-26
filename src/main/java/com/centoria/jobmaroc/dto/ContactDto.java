package com.centoria.jobmaroc.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
public class ContactDto extends AbstractInputDTO {

    private String name;
    private String email;
    private String phone;
    private String objet;
    private String message;
    private String siteUrl;

    protected Map<String, String> mapErrors = new HashMap<>();
    protected Map<String, String> messages = null;
    private boolean recaptchaValid;

    public ContactDto(Map<String, String> params) {
        fillFromParams(params);
        validate();
    }

    public void fillFromParams(Map<String, String> params) {
        if (params == null) {
            return;
        }
        if (params.get("name") != null && !params.get("name").isEmpty()) {
            name = params.get("name");
        }
        if (params.get("email") != null && !params.get("email").isEmpty()) {
            email = params.get("email");
        }
        if (params.get("phone") != null && !params.get("phone").isEmpty()) {
            phone = params.get("phone");
        }
        if (params.get("objet") != null && !params.get("objet").isEmpty()) {
            objet = params.get("objet");
        }
        if (params.get("message") != null && !params.get("message").isEmpty()) {
            message = params.get("message");
        }
        if (params.get("g-recaptcha-response") != null && !params.get("g-recaptcha-response").isEmpty()) {
            if (params.get("g-recaptcha-response").equals("true")) {
                recaptchaValid = true;
            }
        }
    }

    @Override
    public Map<String, String> validate() {
        mapErrors.clear();
        
        if (name == null || name.isEmpty()) {
            mapErrors.put("name", "Ce champ est obligatoire.");
        } else if (name.length() < 3) {
            mapErrors.put("name", "Veuillez fournir au moins 3 caractères.");
        }
        
        if (phone == null || phone.isEmpty()) {
            mapErrors.put("phone", "Ce champ est obligatoire.");
        } else if (!phone.matches("^(?:(?:\\+|00)212|0)\\s*[1-9](?:[\\s.-]*\\d{2}){4}$")) {
            mapErrors.put("phone", "Veuillez fournir un numéro de téléphone valide.");
        }
        
        if (email == null || email.isEmpty()) {
            mapErrors.put("email", "Ce champ est obligatoire.");
        } else {
            try {
                InternetAddress emailAddr = new InternetAddress(getEmail());
                emailAddr.validate();
            } catch (AddressException ex) {
                mapErrors.put("email", "Veuillez fournir une adresse électronique valide.");
            }
        }
        
        if (objet == null || objet.isEmpty()) {
            mapErrors.put("objet", "Ce champ est obligatoire.");
        } else if (objet.length() < 3) {
            mapErrors.put("objet", "Veuillez fournir au moins 3 caractères.");
        }
        
        if (message == null || message.isEmpty()) {
            mapErrors.put("message", "Ce champ est obligatoire.");
        } else if (message.length() < 3) {
            mapErrors.put("message", "Veuillez fournir au moins 3 caractères.");
        }
        
        return mapErrors;
    }

    public void setRecaptchaValid(boolean recaptchaValid) {
        this.recaptchaValid = recaptchaValid;
    }

    public Map<String, String> getErrors() {
        return mapErrors;
    }

    @Override
    public String[] getFields() {
        return null;
    }

    @Override
    public String[] getSessionFields() {
        return null;
    }

    @Override
    public String[] getUrlFields() {
        return null;
    }

    @Override
    public String[] getHeaderFields() {
        return null;
    }

    @Override
    public String[] getAttributes() {
        return null;
    }

    @Override
    public String[] getSessionAttributes() {
        return null;
    }

    @Override
    public String[] getUrlAttributes() {
        return null;
    }

    @Override
    public String[] getHeaderAttributes() {
        return null;
    }
}
