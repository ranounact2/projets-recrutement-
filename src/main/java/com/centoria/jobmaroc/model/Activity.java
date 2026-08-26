package com.centoria.jobmaroc.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Activity extends AbstractModel {
    private static final long serialVersionUID = 1L;
    private String name;

    @Override
    public String getCollectionName() {
        return "activity";
    }
}
