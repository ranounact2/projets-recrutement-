/**
 *
 */
package com.centoria.jobmaroc.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.HashMap;
import java.util.Map;
//import spark.Request;

/**
 * @author Omar
 */
@Setter
@Getter
@NoArgsConstructor
public class MoDto extends AbstractInputDTO {
    private String email;
    private boolean recaptchaValid = false;

    /*
     *
     * Zid lia les params nécessaire au Midlle Office
     */


//	public MoDto(Map<String, String> params) {
//		fillFromParams(params, null, null);
//	}
//
//	public void fillFromParams(Map<String, String> params, Request request, File uploadDir) {
//		if (params.get("email") != null && !params.get("email").isEmpty()){
//			email = params.get("email");
//		}
//		if (params.get("g-recaptcha-response") != null && !params.get("g-recaptcha-response").isEmpty()){
//			recaptchaValid = VerifyUtils.verify(params.get("g-recaptcha-response"));
//		}
//	}

    public Map<String, String> validate() {
        Map<String, String> mapErrors = new HashMap<String, String>();
        if (email == null) {
            mapErrors.put("email", "Ce champ est obligatoire.");
        } else if (email.isEmpty()) {
            mapErrors.put("email", "Ce champ est obligatoire.");
        } else {
            try {
                InternetAddress emailAddr = new InternetAddress(getEmail());
                emailAddr.validate();
            } catch (AddressException ex) {
                mapErrors.put("email", "Veuillez fournir une adresse électronique valide.");
            }
        }

        if (!recaptchaValid) {
            mapErrors.put("recaptcha", "veuillez cocher cette captcha.");
        }
        return mapErrors;
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
