package com.centoria.jobmaroc.dto.Ad;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class InputSearchAd {
    private String domain;
    private String city;
    private String citySlug;
    private String domainSlug;
    private int page = 0;
    private int jobsNumber = 8;
    private String code;
    public String keyword;
}
