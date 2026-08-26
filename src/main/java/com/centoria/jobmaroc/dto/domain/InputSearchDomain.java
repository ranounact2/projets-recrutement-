package com.centoria.jobmaroc.dto.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InputSearchDomain {
    private String slug;
    private String keyword;
    private int page;
    private int numberOfDomains;
    public InputSearchDomain() {
        this.numberOfDomains = -1;
        this.page = -1;
        this.slug = null;
        this.keyword = null;
    }
}
