package com.centoria.jobmaroc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class LinkDTO {

    private String link;
    private String icon;
    private String label;

}
