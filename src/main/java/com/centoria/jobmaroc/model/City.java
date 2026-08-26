package com.centoria.jobmaroc.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class City extends AbstractModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String name;
    private String pays;
    private String codePays;
    private String slug;
    private int population;
    private Region region;

    @Override
    public String getCollectionName() {
        return "city";
    }

}
