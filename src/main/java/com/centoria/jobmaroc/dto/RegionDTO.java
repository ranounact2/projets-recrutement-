package com.centoria.jobmaroc.dto;

import com.centoria.jobmaroc.model.Region;
import lombok.Getter;
import lombok.Setter;

public class RegionDTO extends Region {
    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private String link;
}
