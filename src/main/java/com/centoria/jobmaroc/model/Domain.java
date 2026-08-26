package com.centoria.jobmaroc.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Domain extends AbstractModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String name;
    private String slug;
    private int population;
    private String icon;

    @Override
    public String getCollectionName() {
        return "domain";
    }


}
