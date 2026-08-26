package com.centoria.jobmaroc.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Contact extends AbstractModel {

    private static final long serialVersionUID = 1L;

    private String name;
    private String email;
    private String phone;
    private String objet;
    private String message;

    @Override
    public String getCollectionName() {
        return "contact";
    }
}
