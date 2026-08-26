package com.centoria.jobmaroc.model.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BusinessException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String code;

    private String message;

    /**
     * Permet de stocker les erreurs par clés
     * Par exemple erreurs sur des champs de saisies
     */
    private Map<String, String> mapErrors;

    /**
     * Permet de stocker les erreurs en liste
     */
    private List<String> errors;

    private List<String> warnings;

    public BusinessException() {
        super();
    }

    public BusinessException(String code, String message) {
        super();
        this.code = code;
        addError(code);
        this.message = message;
    }

    public BusinessException(String code) {
        super();
        this.code = code;
        addError(code);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getMapErrors() {
        return mapErrors;
    }

    public void setMapErrors(Map<String, String> mapErrors) {
        this.mapErrors = mapErrors;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> listErrors) {
        this.errors = listErrors;
    }

    public void addError(String error) {
        if (errors == null) {
            errors = new ArrayList<String>();
        }
        this.errors.add(error);
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

}
