package com.centoria.jobmaroc.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ZoneContent {

    private String zoneCode;
    private Map<String, ZoneValue> values;

}
