package com.centoria.jobmaroc.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Region extends AbstractModel {
    private static final long serialVersionUID = 1L;
    private String name;

    private String slug;

    @Override
    public String getCollectionName() {
        return "region";
    }
}
