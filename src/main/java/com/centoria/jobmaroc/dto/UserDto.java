package com.centoria.jobmaroc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.HashMap;
import java.util.Map;
//import spark.Request;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String email;

    private String password;

    private Map<String, String> errors = new HashMap<>();

//	public void fillFromParams(Map<String, String> params ,Request request, File uploadDir) {
//		if (params.get("email") != null && !params.get("email").isEmpty()){
//			setEmail(params.get("email"));
//		}
//		if (params.get("password") != null && !params.get("password").isEmpty()){
//			setPassword(params.get("password"));
//		}
//	}

    public void validate() {

        if (getEmail() == null) {
            errors.put("email", "Ce champ est obligatoire.");
        } else if (getEmail().isEmpty()) {
            errors.put("email", "Ce champ est obligatoire.");
        } else {
            try {
                InternetAddress emailAddr = new InternetAddress(getEmail());
                emailAddr.validate();
            } catch (AddressException ex) {
                errors.put("email", "Veuillez fournir une adresse électronique valide.");
            }
        }
        if (getPassword() == null) {
            errors.put("password", "Ce champ est obligatoire.");
        } else if (getPassword().isEmpty()) {
            errors.put("password", "Ce champ est obligatoire.");
        }

    }

}
