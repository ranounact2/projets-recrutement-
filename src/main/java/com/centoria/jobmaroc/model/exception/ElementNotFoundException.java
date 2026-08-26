package com.centoria.jobmaroc.model.exception;

public class ElementNotFoundException extends RuntimeException {
    private int code;

    private String message;

    public ElementNotFoundException() {
        super();
    }

    public ElementNotFoundException(int code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
