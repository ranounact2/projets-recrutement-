package com.centoria.jobmaroc.dto;

import com.centoria.jobmaroc.common.utils.MessageBundle;
import com.centoria.jobmaroc.model.Applicant;
import com.centoria.jobmaroc.model.Cv;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Part;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
public class ApplicantDto extends Applicant {

    private static final Logger logger = LoggerFactory.getLogger(ApplicantDto.class);
    private static final int MAX_FILE_SIZE_MB = 5;
    private static final int BYTES_PER_MB = 1024 * 1024;
    private static final long MAX_FILE_SIZE_BYTES = (long) MAX_FILE_SIZE_MB * BYTES_PER_MB;
    
    private String inputKey;
    private static final long serialVersionUID = 1L;
    private Boolean withOffers;
    private boolean recaptchaValid = false;
    private Map<String, String> errors = new HashMap<>();
    private Map<String, String> messages = new HashMap<>();
    private static final MessageBundle messageBundle = MessageBundle.getInstance();
    private String siteUrl; // used on email template
    private AdDto job;

    public ApplicantDto(Part file, Map<String, String> params) {
        fillFromParams(params, file);
        validate();
    }

    public void fillFromParams(Map<String, String> params, Part file) {
        if (params.get("nom") != null && !params.get("nom").isEmpty()) {
            setNom(params.get("nom"));
        }
        if (params.get("prenom") != null && !params.get("prenom").isEmpty()) {
            setPrenom(params.get("prenom"));
        }
        if (params.get("email") != null && !params.get("email").isEmpty()) {
            setEmail(params.get("email"));
        }
        if (params.get("phone") != null && !params.get("phone").isEmpty()) {
            setPhone(params.get("phone"));
        }
        if (params.get("motivation") != null && !params.get("motivation").isEmpty()) {
            setMotivation(params.get("motivation"));
        }
        if (params.get("experienceLevel") != null && !params.get("experienceLevel").isEmpty()) {
            setExperienceLevel(params.get("experienceLevel"));
        }
        if (params.get("formation") != null && !params.get("formation").isEmpty()) {
            setFormation(params.get("formation"));
        }

//		if (params.get("g-recaptcha-response") != null && !params.get("g-recaptcha-response").isEmpty()){
//			recaptchaValid = VerifyUtils.verify(params.get("g-recaptcha-response"));
//		}
        try {
            if (file != null) {
                byte[] cvFileBytes = fileTOByte(file);
                if (cvFileBytes != null && cvFileBytes.length > 0) {
                    Cv cv = new Cv();
                    cv.setCvFile(cvFileBytes);
                    setCv(cv);
                }
                // Si cvFileBytes est null, la validation a échoué et l'erreur est déjà dans errors
            }
        } catch (IOException | ServletException e) {
            logger.error("Erreur lors du traitement du fichier CV", e);
            errors.put("cv", getMessage("error.file.cv.processing"));
        }


    }

    /**
     * Gets a localized message using MessageBundle singleton
     */
    private String getMessage(String key) {
        return messageBundle.getMessage(key);
    }
    
    public void validate() {

        if (getNom() == null) {
            errors.put("nom", getMessage("validation.field.required"));
        } else if (getNom().length() < 2) {
            errors.put("nom", getMessage("validation.field.min2"));
        }

        if (getPrenom() == null) {
            errors.put("prenom", getMessage("validation.field.required"));
        } else if (getPrenom().length() < 2) {
            errors.put("prenom", getMessage("validation.field.min2"));
        }

        if (getEmail() == null) {
            errors.put("email", getMessage("validation.field.required"));
        } else {
            try {
                InternetAddress emailAddr = new InternetAddress(getEmail());
                emailAddr.validate();
            } catch (AddressException ex) {
                errors.put("email", getMessage("validation.email.invalid"));
            }
        }

        if (getPhone() == null) {
            errors.put("phone", getMessage("validation.field.required"));
        } else if (getPhone().isEmpty()) {
            errors.put("phone", getMessage("validation.field.required"));
        } else if (!getPhone().matches("^(?:(?:\\+|00)212|0)\\s*[1-9](?:[\\s.-]*\\d{2}){4}$")) {
            errors.put("phone", getMessage("validation.phone.invalid"));
        }

        if (getMotivation() == null || getMotivation().isEmpty()) {
            errors.put("motivation", getMessage("validation.field.required"));
        } else if (getMotivation().length() < 8) {
            errors.put("motivation", getMessage("validation.field.min8"));
        }

        if (getFormation() == null) {
            errors.put("formation", getMessage("validation.field.required"));
        }
        if (getExperienceLevel() == null) {
            errors.put("experience", getMessage("validation.field.required"));
        }

//		Il faut ajouter le recaptcha coté front
//		if (!recaptchaValid){
//			errors.put("recaptcha", getMessage("validation.captcha.required"));
//		}

    }

//	public boolean isRecaptchaValid() {return recaptchaValid;}

    public void setRecaptchaValid(boolean recaptchaValid) {
        this.recaptchaValid = recaptchaValid;
    }


    public byte[] fileTOByte(Part file) throws IOException, ServletException {

        Part uploadedFile = file;
        boolean valid = true;
        String[] File_MIME_TYPE = {"application/pdf",
                "application/vnd.oasis.opendocument.text",
                "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document"};


        try {
            // if null
            if (uploadedFile == null) {
                errors.put("cv", getMessage("validation.field.required"));
                valid = false;
            } else {
                // size
                if (uploadedFile.getSize() > MAX_FILE_SIZE_BYTES) {
                    errors.put("cv", getMessage("validation.cv.size.max"));
                    valid = false;
                }
                // size egal 0
                if (uploadedFile.getSize() == 0) {
                    errors.put("cv", getMessage("validation.cv.empty"));
                    valid = false;
                }

                // content type
                if (uploadedFile.getContentType() != null && 
                    !uploadedFile.getContentType().equals(File_MIME_TYPE[0]) &&
                    !uploadedFile.getContentType().equals(File_MIME_TYPE[1]) &&
                    !uploadedFile.getContentType().equals(File_MIME_TYPE[2]) &&
                    !uploadedFile.getContentType().equals(File_MIME_TYPE[3])) {
                    errors.put("cv", getMessage("validation.cv.format"));
                    valid = false;
                }
            }
        } catch (Exception e) {
            logger.error("Erreur lors du traitement du fichier", e);
            errors.put("cv", getMessage("error.file.processing"));
            valid = false;
        }


        if (valid) {
            try (final InputStream in = uploadedFile.getInputStream()) {
                byte[] bytes = IOUtils.toByteArray(in);
                return bytes;
            }

        }
        return null;
    }

}
