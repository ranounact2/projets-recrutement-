package com.centoria.jobmaroc.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class PageZones extends AbstractModel {

    private String url;

    private String page;

    private String variante;
    private Map<String, ZoneContent> zones;

    @Override
    public String getCollectionName() {
        return "zone";
    }
}
