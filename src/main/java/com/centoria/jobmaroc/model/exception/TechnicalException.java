package com.centoria.jobmaroc.model.exception;

public class TechnicalException extends RuntimeException {

    private String code;

    private String message;

    public TechnicalException() {
        super();
    }

    public TechnicalException(String code, String message) {
        super();
        this.code = code;
        this.message = message;
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
}
