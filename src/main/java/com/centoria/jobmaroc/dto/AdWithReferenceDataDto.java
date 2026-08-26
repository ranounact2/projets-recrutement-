package com.centoria.jobmaroc.dto;

import com.centoria.jobmaroc.common.context.ApplicationContext;
import com.centoria.jobmaroc.model.City;
import com.centoria.jobmaroc.web.imageUpload.ImageUpload;
import com.github.slugify.Slugify;
import google.recaptcha.VerifyUtils;
import jakarta.servlet.http.Part;
import lombok.*;
import org.apache.commons.codec.digest.DigestUtils;
import lombok.extern.slf4j.Slf4j;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdWithReferenceDataDto extends AdDisplayDto {

    private static final long serialVersionUID = 1L;

    @Builder.Default
    AdDto job = new AdDto();
    private String siteUrl; // used on email template

    @Builder.Default
    private boolean recaptchaValid = false;
    @Builder.Default
    private List<DomainDto> domains = null;
    @Builder.Default
    private List<City> cities = null;

    @Builder.Default
    protected Map<String, String> errors = new HashMap<>();

    @Builder.Default
    protected Map<String, String> messages = new HashMap<>();

    public AdWithReferenceDataDto(List<City> cities, List<DomainDto> domains) {
        super();
        this.cities = cities;
        this.domains = domains;
    }

    public void fillFromParams(Part file, Map<String, String> params) {
        if (params.get("key") != null && !params.get("key").isEmpty()) {
            job.setKey(params.get("key"));
            Slugify slg = new Slugify();
            job.setSlug(slg.slugify(params.get("title") + "-" + params.get("key")));
        }
        if (params.get("city") != null && !params.get("city").isEmpty()) {
            // Store the value as-is. Conversion from name to slug will be done in the service layer.
            job.setCity(params.get("city"));
        }
        if (params.get("title") != null && !params.get("title").isEmpty()) {
            job.setTitle(params.get("title"));
        }
        if (params.get("content") != null && !params.get("content").isEmpty()) {
            job.setContent(params.get("content"));
        }
        if (params.get("tel") != null && !params.get("tel").isEmpty()) {
            job.setPhone(params.get("tel"));
        }
        if (params.get("type") != null && !params.get("type").isEmpty()) {
            job.setType(params.get("type"));
        }
        if (params.get("companyName") != null && !params.get("companyName").isEmpty()) {
            job.setCompanyName(params.get("companyName"));
        }
        if (params.get("companyCode") != null && !params.get("companyCode").isEmpty()) {
            job.setCompanyCode(params.get("companyCode"));
        }
        if (params.get("domain") != null && !params.get("domain").isEmpty()) {
            // Store the value as-is. Conversion from name to slug will be done in the service layer.
            job.setDomain(params.get("domain"));
        }
        if (params.get("confidentialite") != null && !params.get("confidentialite").isEmpty()) {
            job.setConfidentiality(params.get("confidentialite"));
        }
        if (params.get("nbrDePostes") != null && !params.get("nbrDePostes").isEmpty()) {
            int nbrpost = Integer.parseInt(params.get("nbrDePostes"));
            job.setNbrDePostes(nbrpost);
        }
        if (params.get("companyCode") != null && !params.get("companyCode").isEmpty()) {
            job.setCompanyCode(params.get("companyCode"));
        }
        if (params.get("companyName") != null && !params.get("companyName").isEmpty()) {
            job.setCompanyName(params.get("companyName"));
        }
        if (params.get("formation") != null && !params.get("formation").isEmpty()) {
            job.setFormation(params.get("formation"));
        }
        if (params.get("experienceLevel") != null && !params.get("experienceLevel").isEmpty()) {
            job.setExperienceLevel(params.get("experienceLevel"));
        }
        if (params.get("email") != null && !params.get("email").isEmpty()) {
            job.setEmail(params.get("email"));
            job.setSecretCode(DigestUtils.md5Hex(job.getEmail().toLowerCase().replaceAll(" ", "")));
            job.setCode(DigestUtils.md5Hex((job.getEmail() + job.getCreationDate()).toLowerCase().replaceAll(" ", ""))); // 2éme
            //identifiant
        }
        if (params.get("g-recaptcha-response") != null && !params.get("g-recaptcha-response").isEmpty()) {
            recaptchaValid = VerifyUtils.verify(params.get("g-recaptcha-response"));
        }
        if (params.get("adtype") != null && !params.get("adtype").isEmpty()) {
            job.setAnnouncetype(Integer.parseInt(params.get("adtype")));
        }
        if (params.get("facebook") != null && !params.get("facebook").isEmpty()) {
            job.setFacebook(params.get("facebook"));
        }
        if (params.get("twitter") != null && !params.get("twitter").isEmpty()) {
            job.setTwitter(params.get("twitter"));
        }
        if (params.get("linkedin") != null && !params.get("linkedin").isEmpty()) {
            job.setLinkedin(params.get("linkedin"));
        }
        if (file != null) {
            try {
                ImageUpload
                        imageUpload = new ImageUpload();
                imageUpload.uploadFile(file,
                        ApplicationContext.getInstance().getProps().getValue("repository.images") + File.separator + "img");
                job.setImg(imageUpload.getImageName());
            } catch (IOException e) {
                log.error("Error uploading image file", e);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }

        }
        // call validate function
        validate();
    }

    public Map<String, String> validate() {
        //Map<String, String> mapErrors = new HashMap<String, String>();
        if (job.getTitle() == null) {
            errors.put("title", "Ce champ est obligatoire.");
        } else if (job.getTitle().length() < 3) {
            if (job.getTitle().isEmpty()) {
                errors.put("title", "Ce champ est obligatoire.");
            } else {
                errors.put("title", "Veuillez fournir au moins 3 caractères.");
            }
        }
        if (job.getDomain() == null) {
            errors.put("domain", "Ce champ est obligatoire.");
        } else if (job.getDomain().isEmpty() || job.getDomain().equals("Choisir un secteur")) {
            errors.put("domain", "Ce champ est obligatoire.");
        }
        if (job.getCity() == null) {
            errors.put("city", "Ce champ est obligatoire.");
        } else if (job.getCity().isEmpty() || job.getCity().equals("Choisir une ville")) {
            errors.put("city", "Ce champ est obligatoire.");
        }
        if (job.getType() == null) {
            errors.put("type", "Ce champ est obligatoire.");
        } else if (job.getType().isEmpty()) {
            errors.put("type", "Ce champ est obligatoire.");
        }

        Integer nbrDePoste = job.getNbrDePostes();
        if (nbrDePoste <= 0) {
            errors.put("nbrDePostes", "Le nombre ne doit pas être zéro ou inférieur à zéro. ");
        }

        if (job.getContent() == null) {
            errors.put("content", "Ce champ est obligatoire.");
        } else if (job.getContent().length() < 8) {
            if (job.getContent().isEmpty()) {
                errors.put("content", "Ce champ est obligatoire.");
            } else {
                errors.put("content", "Veuillez fournir au moins 8 caractères.");
            }
        }

        if (job.getPhone() == null) {
            errors.put("phone", "Ce champ est obligatoire.");
        } else if (job.getPhone().isEmpty()) {
            errors.put("phone", "Ce champ est obligatoire.");
        } else if (!job.getPhone().matches("^(?:(?:\\+|00)212|0)\\s*[1-9](?:[\\s.-]*\\d{2}){4}$")) {
            errors.put("phone", "Veuillez fournir un numéro de téléphone valide.");
        }
        if (job.getEmail() == null) {
            errors.put("email", "Ce champ est obligatoire.");
        } else if (job.getEmail().isEmpty()) {
            errors.put("email", "Ce champ est obligatoire.");
        } else {
            try {
                InternetAddress emailAddr = new InternetAddress(job.getEmail());
                emailAddr.validate();
            } catch (AddressException ex) {
                errors.put("email", "Veuillez fournir une adresse électronique valide.");
            }
        }
        if (job.getCompanyName() == null) {
            errors.put("companyName", "Ce champ est obligatoire.");
        } else if (job.getCompanyName().length() < 2) {
            if (job.getCompanyName().isEmpty()) {
                errors.put("companyName", "Ce champ est obligatoire.");
            } else {
                errors.put("companyName", "Veuillez fournir au moins 2 caractères.");
            }
        }
        if (job.getCompanyCode() == null) {
            errors.put("companyCode", "Ce champ est obligatoire.");
        } else if (job.getCompanyCode().length() < 2) {
            if (job.getCompanyCode().isEmpty()) {
                errors.put("companyCode", "Ce champ est obligatoire.");
            } else {
                errors.put("companyCode", "Veuillez fournir au moins 2 caractères.");
            }
        }
        if (job.getConfidentiality() == null) {
            errors.put("confidentiality", "Ce champ est obligatoire.");
        } else if (job.getConfidentiality().isEmpty()) {
            errors.put("confidentiality", "Ce champ est obligatoire.");
        }
/**
 *  utiliser ce code pour plus tad
 if (!recaptchaValid) {
 errors.put("recaptcha", "veuillez cocher cette captcha.");
 }
 **/
        return errors;
    }

    @Override
    public String[] getFields() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String[] getSessionFields() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String[] getUrlFields() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String[] getHeaderFields() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String[] getAttributes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String[] getSessionAttributes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String[] getUrlAttributes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String[] getHeaderAttributes() {
        // TODO Auto-generated method stub
        return null;
    }

}
