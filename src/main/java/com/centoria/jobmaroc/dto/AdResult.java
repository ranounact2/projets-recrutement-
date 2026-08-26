package com.centoria.jobmaroc.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor @Builder
public class AdResult {
    private long totalAds;
    private List<AdDto> ads;
}
